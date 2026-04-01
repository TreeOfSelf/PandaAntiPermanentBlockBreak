package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import me.TreeOfSelf.PandaAntiPermanentBlockBreak.PandaAntiPermanentBlockBreakConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalForcer;
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
    private ServerLevel level;

    @Inject(
            method = "canHostFrame",
            at = @At("RETURN"),
            cancellable = true
    )
    private void antiPermanentBlockBreak_expandedSafetyCheck(BlockPos origin, BlockPos.MutableBlockPos mutable, Direction direction, int offset, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        if (isFallbackAreaUnsafe(origin, direction)) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean isFallbackAreaUnsafe(BlockPos portalBasePos, Direction primaryDirection) {
        Direction perpendicularDirection = primaryDirection.getClockWise();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int l = -1; l < 2; ++l) {
            for (int m = 0; m < 2; ++m) {
                for (int n = -1; n < 3; ++n) {
                    mutable.setWithOffset(
                            portalBasePos,
                            m * primaryDirection.getStepX() + l * perpendicularDirection.getStepX(),
                            n,
                            m * primaryDirection.getStepZ() + l * perpendicularDirection.getStepZ());
                    if (isForbidden(this.level.getBlockState(mutable))) return true;
                }
            }
        }

        for (int o = -1; o < 3; ++o) {
            for (int p = -1; p < 4; ++p) {
                mutable.setWithOffset(portalBasePos, o * primaryDirection.getStepX(), p, o * primaryDirection.getStepZ());
                if (isForbidden(this.level.getBlockState(mutable))) return true;
            }
        }

        for (int p = 0; p < 2; ++p) {
            for (int k = 0; k < 3; ++k) {
                mutable.setWithOffset(portalBasePos, p * primaryDirection.getStepX(), k, p * primaryDirection.getStepZ());
                if (isForbidden(this.level.getBlockState(mutable))) return true;
            }
        }

        return false;
    }

    @Unique
    private boolean isForbidden(BlockState blockState) {
        if (blockState.is(Blocks.BEDROCK) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectBedrock")) {
            return true;
        }
        if (blockState.is(Blocks.END_PORTAL_FRAME) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndPortalFrame")) {
            return true;
        }
        if (blockState.is(Blocks.END_PORTAL) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndPortal")) {
            return true;
        }
        if (blockState.is(Blocks.END_GATEWAY) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndGateway")) {
            return true;
        }
        return false;
    }
}
