package tfar.antfarm.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.antfarm.AntFarm;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ChunkPos;<init>(Lnet/minecraft/core/BlockPos;)V"), method = "setInitialSpawn",cancellable = true)
    private static void fixSpawn(ServerLevel level, ServerLevelData levelData, boolean generateBonusChest, boolean debug, CallbackInfo ci) {
        AntFarm.forceDefaultSpawn(level,levelData, ci::cancel);
    }
}