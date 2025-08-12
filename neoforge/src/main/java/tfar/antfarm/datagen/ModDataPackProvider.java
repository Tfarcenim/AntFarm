package tfar.antfarm.datagen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import tfar.antfarm.AntFarm;
import tfar.antfarm.AntFarmWorldPresets;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDataPackProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.WORLD_PRESET, ModDataPackProvider::dimensionType)
            //.add(Registries.DAMAGE_TYPE,ModDataPackProvider::damageType)
            ;


    public ModDataPackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(AntFarm.MOD_ID));
    }

    public static void damageType(BootstrapContext<DamageType> context) {
       // context.register(ModDamageTypes.ACID,new DamageType("acid", 0.1F, DamageEffects.HURT));
    }

    public static void dimensionType(BootstrapContext<WorldPreset> context) {
        AntFarmWorldPresets.bootstrap(context);
    }

}
