package tfar.antfarm;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class AntFarmChunkGenerators {

    public static MapCodec<? extends ChunkGenerator> bootstrap(Registry<MapCodec<? extends ChunkGenerator>> registry) {
        return Registry.register(registry, AntFarm.id("antfarm"), AntFarmChunkGenerator.CODEC);
    }
}
