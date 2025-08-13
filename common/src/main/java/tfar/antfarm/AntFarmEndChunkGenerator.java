package tfar.antfarm;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.concurrent.CompletableFuture;

public class AntFarmEndChunkGenerator extends NoiseBasedChunkGenerator {

    public static final MapCodec<AntFarmEndChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            p_255585_ -> p_255585_.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(p_255584_ -> p_255584_.biomeSource),
                            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings)
                    )
                    .apply(p_255585_, p_255585_.stable(AntFarmEndChunkGenerator::new))
    );

    public AntFarmEndChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;

        if (z < -16 || z > 15 || x < -16 || x > 15) {
            return CompletableFuture.completedFuture(chunk);
        }

        if (z == -16 || z == 15 || x == -16 || x == 15) {
            return fillBarrierBlocks(chunk);
        }
        return super.fillFromNoise(blender, randomState, structureManager, chunk);

    }

    CompletableFuture<ChunkAccess> fillBarrierBlocks(ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(() -> {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                for (int z = 0; z < 16; z++) {
                    for (int x = 0; x < 16; x++) {
                        mutableBlockPos.set(x, y, z);
                        chunk.setBlockState(mutableBlockPos, Blocks.BARRIER.defaultBlockState(), false);
                    }
                }
            }
            return chunk;
        });
    }
}
