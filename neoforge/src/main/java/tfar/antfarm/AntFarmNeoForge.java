package tfar.antfarm;


import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
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
        // Use NeoForge to bootstrap the Common mod.
        AntFarm.init();

    }

    void register(RegisterEvent event) {
        if (event.getRegistry() == BuiltInRegistries.BLOCK) {
            AntFarmChunkGenerators.bootstrap(BuiltInRegistries.CHUNK_GENERATOR);
        }
    }
}