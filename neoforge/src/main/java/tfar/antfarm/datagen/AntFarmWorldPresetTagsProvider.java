package tfar.antfarm.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.WorldPresetTagsProvider;
import net.minecraft.tags.WorldPresetTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.antfarm.AntFarm;
import tfar.antfarm.AntFarmWorldPresets;

import java.util.concurrent.CompletableFuture;

public class AntFarmWorldPresetTagsProvider extends WorldPresetTagsProvider {
    public AntFarmWorldPresetTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider,@Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, AntFarm.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(WorldPresetTags.NORMAL).add(AntFarmWorldPresets.CLASSIC);
    }
}
