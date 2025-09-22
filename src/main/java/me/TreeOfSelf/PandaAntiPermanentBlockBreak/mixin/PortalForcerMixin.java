package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalForcer.class)
public class PortalForcerMixin {

    @Shadow @Final
    private ServerWorld world;

    @Inject(method = "isValidPortalPos",
            at = @At("RETURN"),
            cancellable = true)
    private void checkGroundPortalSafety(BlockPos pos, BlockPos.Mutable temp, Direction portalDirection, int distanceOrthogonalToPortal, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue())
            return;

        Direction direction = portalDirection.rotateYClockwise();

        for(int l = -1; l < 2; ++l) {
            for(int m = 0; m < 2; ++m) {
                for(int n = -1; n < 3; ++n) {
                    temp.set(pos,
                            m * portalDirection.getOffsetX() + l * direction.getOffsetX(),
                            n,
                            m * portalDirection.getOffsetZ() + l * direction.getOffsetZ());

                    BlockState existing = world.getBlockState(temp);

                    if (existing.isOf(Blocks.BEDROCK) ||
                            existing.isOf(Blocks.END_PORTAL) ||
                            existing.isOf(Blocks.END_PORTAL_FRAME)) {
                        cir.setReturnValue(false);
                        return;
                    }
                }
            }
        }
    }
}