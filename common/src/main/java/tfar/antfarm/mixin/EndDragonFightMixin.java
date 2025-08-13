package tfar.antfarm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.antfarm.AntFarmEndChunkGenerator;

@Mixin(EndDragonFight.class)
public class EndDragonFightMixin {

    @Shadow @Final private ServerLevel level;

    @Inject(method = "spawnNewGateway(Lnet/minecraft/core/BlockPos;)V",at = @At("HEAD"),cancellable = true)
    private void blockGateWay(BlockPos pos, CallbackInfo ci) {
        if (level.getChunkSource().getGenerator() instanceof AntFarmEndChunkGenerator) {
            ci.cancel();
        }
    }
}
