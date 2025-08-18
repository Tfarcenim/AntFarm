package tfar.antfarm;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import tfar.antfarm.datagen.ModDatagen;

@Mod(AntFarm.MOD_ID)
public class AntFarmNeoForge {

    public AntFarmNeoForge(IEventBus eventBus) {

        eventBus.addListener(ModDatagen::gather);
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.
        eventBus.addListener(this::register);
        NeoForge.EVENT_BUS.addListener(this::forceDefaultSpawn);
        // Use NeoForge to bootstrap the Common mod.
        AntFarm.init();

    }

    void forceDefaultSpawn(LevelEvent.CreateSpawnPosition event) {
        ServerLevelData settings = event.getSettings();
        ServerLevel level = (ServerLevel) event.getLevel();
        AntFarm.forceDefaultSpawn(level,settings,() -> event.setCanceled(true));
    }

    void register(RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.BLOCK) {
            AntFarmChunkGenerators.bootstrap(BuiltInRegistries.CHUNK_GENERATOR);
        }
    }
}