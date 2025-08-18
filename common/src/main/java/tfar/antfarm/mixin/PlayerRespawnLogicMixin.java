package tfar.antfarm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.antfarm.AntFarmChunkGenerator;

@Mixin(PlayerRespawnLogic.class)
public class PlayerRespawnLogicMixin {
    @Inject(method = "getOverworldRespawnPos",at = @At("RETURN"), cancellable = true)
    private static void fixSpawn(ServerLevel level, int x, int z, CallbackInfoReturnable<BlockPos> cir) {
        ChunkGenerator chunkGenerator = level.getChunkSource().getGenerator();
        if (chunkGenerator instanceof AntFarmChunkGenerator antFarmChunkGenerator) {
            if (antFarmChunkGenerator.getOutOfBoundsDamage(new Vec3(x,0,z))>0) {
                cir.setReturnValue(null);
            }
        }
    }
}
