package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import me.TreeOfSelf.PandaAntiPermanentBlockBreak.PandaAntiPermanentBlockBreakConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlockBehaviour.class)
public class AggressiveAntiRemovalMixin {
    @Inject(
            method = "affectNeighborsAfterRemoval",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston, CallbackInfo ci) {
        if (!PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("strictBreakCheck")) {
            return;
        }

        if (level.dimension() != Level.END) {
            boolean shouldProtect = false;

            if (state.is(Blocks.BEDROCK) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectBedrock")) {
                shouldProtect = true;
            } else if (state.is(Blocks.END_PORTAL_FRAME) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndPortalFrame")) {
                shouldProtect = true;
            } else if (state.is(Blocks.END_PORTAL) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndPortal")) {
                shouldProtect = true;
            } else if (state.is(Blocks.END_GATEWAY) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectEndGateway")) {
                shouldProtect = true;
            }

            if (shouldProtect && state.getBlock() != level.getBlockState(pos).getBlock()) {
                level.setBlock(pos, state, 3);
                ci.cancel();
            }
        } else {
            if (state.is(Blocks.BEDROCK) && PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("protectBedrock")) {
                BlockState newState = level.getBlockState(pos);

                if (newState.is(Blocks.END_STONE)) {
                    return;
                }

                if (state.getBlock() != newState.getBlock()) {
                    List<EndSpikeFeature.EndSpike> spikes = EndSpikeFeature.getSpikesForLevel(level);
                    for (EndSpikeFeature.EndSpike spike : spikes) {
                        int x = spike.getCenterX();
                        int z = spike.getCenterZ();
                        if (pos.getX() == x && pos.getZ() == z) {
                            return;
                        }
                    }

                    BlockPos exitPortalCenter = EndPodiumFeature.getLocation(BlockPos.ZERO);
                    int distance = Math.max(Math.abs(pos.getX() - exitPortalCenter.getX()),
                            Math.abs(pos.getZ() - exitPortalCenter.getZ()));

                    if (distance <= 8 && pos.getY() >= 63 && pos.getY() <= 80) {
                        return;
                    }

                    level.setBlock(pos, state, 3);
                    ci.cancel();
                }
            }
        }
    }
}
