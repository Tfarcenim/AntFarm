package tfar.antfarm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRespawnLogic.class)
public class PlayerRespawnLogicMixin {
    @Inject(method = "getOverworldRespawnPos",at = @At("RETURN"), cancellable = true)
    private static void fixSpawn(ServerLevel level, int x, int z, CallbackInfoReturnable<BlockPos> cir) {
        if (z<0 || z > 15) {
            cir.setReturnValue(null);
        }
    }
}
