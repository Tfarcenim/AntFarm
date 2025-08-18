package tfar.antfarm;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class AntFarm {

    public static final String MOD_ID = "antfarm";
    public static final String MOD_NAME = "AntFarm";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.

    }

    public static void forceDefaultSpawn(ServerLevel level, ServerLevelData data, Runnable cancel) {
        ServerChunkCache chunkCache = level.getChunkSource();
        ChunkGenerator chunkGenerator = chunkCache.getGenerator();
        if (chunkGenerator instanceof AntFarmStripChunkGenerator antFarmStripChunkGenerator) {
            BlockPos spawn = data.getSpawnPos();

            int minz = antFarmStripChunkGenerator.minZ();
            int maxz = antFarmStripChunkGenerator.maxZ();
            int randomZ = 16 * minz +level.random.nextInt((maxz-minz) * 16);
            BlockPos newSpawn = new BlockPos(spawn.getX(), spawn.getY(), randomZ);
            data.setSpawn(newSpawn, 0);
            cancel.run();
        }
    }

    public static void playerTick(ServerPlayer player) {
        if (!player.getAbilities().instabuild  && player.serverLevel().getGameTime() % 10 == 0) {
            ServerLevel level = player.serverLevel();
            if (level.getChunkSource().getGenerator() instanceof AntFarmChunkGenerator antFarmChunkGenerator) {
                float damage = antFarmChunkGenerator.getOutOfBoundsDamage(player.position());
                if (damage > 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
                    player.hurt(player.damageSources().outOfBorder(), damage);
                }
            }
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}