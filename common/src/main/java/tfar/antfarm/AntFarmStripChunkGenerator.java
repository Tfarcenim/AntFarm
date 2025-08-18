package tfar.antfarm;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.phys.Vec3;

public class AntFarmStripChunkGenerator extends AntFarmChunkGenerator {

    public static final MapCodec<AntFarmStripChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            p_255585_ -> p_255585_.group(
                            BiomeSource.CODEC.fieldOf("biome_source").forGetter(p_255584_ -> p_255584_.biomeSource),
                            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(NoiseBasedChunkGenerator::generatorSettings),
                            Codec.INT.fieldOf("min_z").forGetter(AntFarmStripChunkGenerator::minZ),
                            Codec.INT.fieldOf("max_z").forGetter(AntFarmStripChunkGenerator::maxZ)
                    )
                    .apply(p_255585_, p_255585_.stable(AntFarmStripChunkGenerator::new))
    );


    private final int minZ;
    private final int maxZ;

    public AntFarmStripChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings, int minZ, int maxZ) {
        super(biomeSource, settings);
        this.minZ = minZ;
        this.maxZ = maxZ;
    }


    public int minZ() {
        return minZ;
    }

    public int maxZ() {
        return maxZ;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    protected FillType getFillType(ChunkAccess chunkAccess) {
        int z = chunkAccess.getPos().z;
        if (z == minZ - 1 || z == maxZ) return FillType.BARRIER;
        else if (z >= minZ && z < maxZ) {
            return FillType.NORMAL;
        }
        return FillType.NOTHING;
    }

    @Override
    public float getOutOfBoundsDamage(Vec3 pos) {
        double z = pos.z;
        double zOutOFBounds = Math.max(0, Math.abs(z) - 16 * (maxZ + minZ));
        return (float) zOutOFBounds;
    }
}
