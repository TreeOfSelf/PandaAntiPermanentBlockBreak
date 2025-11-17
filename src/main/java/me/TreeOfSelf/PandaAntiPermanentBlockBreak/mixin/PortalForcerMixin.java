package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import me.TreeOfSelf.PandaAntiPermanentBlockBreak.PandaAntiPermanentBlockBreakConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
    private void antiPermanentBlockBreak_expandedSafetyCheck(BlockPos pos, BlockPos.Mutable temp, Direction portalDirection, int distanceOrthogonalToPortal, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        if (isFallbackAreaUnsafe(pos, portalDirection)) {
            cir.setReturnValue(false);
        }
    }


    @Unique
    private boolean isFallbackAreaUnsafe(BlockPos portalBasePos, Direction primaryDirection) {
        Direction perpendicularDirection = primaryDirection.rotateYClockwise();
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int l = -1; l < 2; ++l) {
            for (int m = 0; m < 2; ++m) {
                for (int n = -1; n < 3; ++n) {
                    mutable.set(portalBasePos,
                            m * primaryDirection.getOffsetX() + l * perpendicularDirection.getOffsetX(),
                            n,
                            m * primaryDirection.getOffsetZ() + l * perpendicularDirection.getOffsetZ());
                    if (isForbidden(this.world.getBlockState(mutable))) return true;
                }
            }
        }

        for (int o = -1; o < 3; ++o) {
            for (int p = -1; p < 4; ++p) {
                mutable.set(portalBasePos, o * primaryDirection.getOffsetX(), p, o * primaryDirection.getOffsetZ());
                if (isForbidden(this.world.getBlockState(mutable))) return true;
            }
        }

        for (int p = 0; p < 2; ++p) {
            for (int k = 0; k < 3; ++k) {
                mutable.set(portalBasePos, p * primaryDirection.getOffsetX(), k, p * primaryDirection.getOffsetZ());
                if (isForbidden(this.world.getBlockState(mutable))) return true;
            }
        }

        return false;
    }

    @Unique
    private boolean isForbidden(BlockState blockState) {
        if (blockState.isOf(Blocks.BEDROCK) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectBedrock")) {
            return true;
        }
        if (blockState.isOf(Blocks.END_PORTAL_FRAME) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndPortalFrame")) {
            return true;
        }
        if (blockState.isOf(Blocks.END_PORTAL) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndPortal")) {
            return true;
        }
        if (blockState.isOf(Blocks.END_GATEWAY) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndGateway")) {
            return true;
        }
        return false;
    }
}