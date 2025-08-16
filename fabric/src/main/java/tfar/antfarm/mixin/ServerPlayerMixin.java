package tfar.antfarm.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.antfarm.AntFarm;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "tick",at = @At("RETURN"))
    private void handleOutofBounds(CallbackInfo ci) {
        AntFarm.playerTick((ServerPlayer)(Object) this);
    }
}
