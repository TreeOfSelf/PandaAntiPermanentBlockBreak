package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import me.TreeOfSelf.PandaAntiPermanentBlockBreak.PandaAntiPermanentBlockBreakConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractBlock.class)
public class AggressiveAntiRemovalMixin {
    @Inject(
            method = "onStateReplaced",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved, CallbackInfo ci) {
        if (!PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("strictBreakCheck")) {
            return;
        }

        if (!world.getDimensionEntry().matchesKey(DimensionTypes.THE_END)) {
            if (state.getBlock() == Blocks.BEDROCK || state.getBlock() == Blocks.END_PORTAL_FRAME) {
                if (state.getBlock() != world.getBlockState(pos).getBlock()) {
                    world.setBlockState(pos, state);
                    ci.cancel();
                }
            }
        } else {
            if (state.getBlock() == Blocks.BEDROCK) {
                BlockState newState = world.getBlockState(pos);

                if (newState.getBlock() == Blocks.END_STONE) {
                    return;
                }

                if (state.getBlock() != newState.getBlock()) {
                    List<EndSpikeFeature.Spike> spikes = EndSpikeFeature.getSpikes(world);
                    for (EndSpikeFeature.Spike spike : spikes) {
                        int x = spike.getCenterX();
                        int z = spike.getCenterZ();
                        if (pos.getX() == x && pos.getZ() == z) {
                            return;
                        }
                    }

                    BlockPos origin = BlockPos.ORIGIN;
                    BlockPos exitPortalCenter = EndPortalFeature.offsetOrigin(origin);
                    int distance = Math.max(Math.abs(pos.getX() - exitPortalCenter.getX()),
                            Math.abs(pos.getZ() - exitPortalCenter.getZ()));

                    if (distance <= 8 && pos.getY() >= 63 && pos.getY() <= 80) {
                        return;
                    }

                    world.setBlockState(pos, state);
                    ci.cancel();
                }
            }
        }
    }
}