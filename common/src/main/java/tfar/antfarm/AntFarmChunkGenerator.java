package tfar.antfarm;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.CompletableFuture;

public abstract class AntFarmChunkGenerator extends NoiseBasedChunkGenerator {

    public AntFarmChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }

   /* @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
        FillType fillType = getFillType(chunk);

        switch (fillType) {
            case NORMAL -> {
                super.applyBiomeDecoration(level, chunk, structureManager);
            }
            case BARRIER -> {
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
                for (int y = chunk.getMinBuildHeight(); y < chunk.getMaxBuildHeight(); y++) {
                    for (int z = 0; z < 16; z++) {
                        for (int x = 0; x < 16; x++) {
                            mutableBlockPos.set(x, y, z);
                            chunk.setBlockState(mutableBlockPos, Blocks.BARRIER.defaultBlockState(), false);
                        }
                    }
                }
            }
            case NOTHING -> {}
        }
    }*/



    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
        FillType fillType = getFillType(chunk);
        return switch (fillType) {
            case NORMAL -> super.fillFromNoise(blender, randomState, structureManager, chunk);
            case BARRIER -> fillBarrierBlocks(chunk);
            case NOTHING -> CompletableFuture.completedFuture(chunk);
        };
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

    protected enum FillType{
        NORMAL,BARRIER,NOTHING
    }

    protected abstract FillType getFillType(ChunkAccess chunkAccess);

    public abstract float getOutOfBoundsDamage(Vec3 pos);

}
