package tfar.antfarm;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.concurrent.CompletableFuture;

public class AntFarmChunkGenerator extends NoiseBasedChunkGenerator {

    public static final MapCodec<AntFarmChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            p_255585_ -> p_255585_.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(p_255584_ -> p_255584_.biomeSource),
                            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings)
                    )
                    .apply(p_255585_, p_255585_.stable(AntFarmChunkGenerator::new))
    );

    public AntFarmChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }

    @Override
    public void buildSurface(ChunkAccess chunk, WorldGenerationContext context, RandomState random, StructureManager structureManager,
                             BiomeManager biomeManager, Registry<Biome> biomes, Blender blender) {
        if (antFarmed(chunk)) {
            super.buildSurface(chunk, context, random, structureManager, biomeManager, biomes, blender);
        }
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        if (antFarmed(chunk)) {
            super.applyBiomeDecoration(level, chunk, structureManager);
        }
    }

    protected boolean antFarmed(ChunkAccess chunkAccess) {
        return chunkAccess.getPos().z == 0;
    }

    //-1 and 16

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        int z = chunk.getPos().z;
        switch (z) {
            case -1 -> {
                return placeBarrierBlocks(chunk,-1);
                }
            case 0 ->{
                return super.fillFromNoise(blender, randomState, structureManager, chunk);
            }
            case 1 ->{
                return placeBarrierBlocks(chunk,16);
            }
        }
        return CompletableFuture.completedFuture(chunk);
    }

    CompletableFuture<ChunkAccess> placeBarrierBlocks(ChunkAccess chunk,int z) {
        return CompletableFuture.supplyAsync(() -> {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                for (int x = 0; x < 16; x++) {
                    mutableBlockPos.set(x, y, z);
                    chunk.setBlockState(mutableBlockPos, Blocks.BARRIER.defaultBlockState(), false);
                }
            }
            return chunk;
        });
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }
}
