package tfar.antfarm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.phys.Vec3;

public class AntFarmSquareChunkGenerator extends AntFarmChunkGenerator{

    public static final MapCodec<AntFarmSquareChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            p_255585_ -> p_255585_.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(p_255584_ -> p_255584_.biomeSource),
                            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings),
                    Codec.INT.fieldOf("size").forGetter(AntFarmSquareChunkGenerator::size)
                    )
                    .apply(p_255585_, p_255585_.stable(AntFarmSquareChunkGenerator::new))
    );
    private final int size;

    public AntFarmSquareChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings,int size) {
        super(biomeSource, settings);
        this.size = size;
    }

    public int size() {
        return size;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    protected FillType getFillType(ChunkAccess chunk) {
        int x = chunk.getPos().x;
        int z = chunk.getPos().z;

        if (z < -size || z > size-1 || x < -size || x > size-1) {
            return FillType.NORMAL;
        }

        if (z == -size || z == size-1 || x == -size || x == size-1) {
            return FillType.BARRIER;
        }
        return FillType.NOTHING;
    }

    @Override
    public float getOutOfBoundsDamage(Vec3 pos) {
        double outOfBoundX = Math.max(0,Math.abs(pos.x) - 16 * size);
        double outOfBoundZ = Math.max(0,Math.abs(pos.z) - 16 * size);
        return (float) (outOfBoundX+outOfBoundZ);
    }
}
