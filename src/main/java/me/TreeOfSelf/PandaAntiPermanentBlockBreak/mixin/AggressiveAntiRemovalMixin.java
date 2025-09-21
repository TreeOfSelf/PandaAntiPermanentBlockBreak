package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import me.TreeOfSelf.PandaAntiPermanentBlockBreak.PandaAntiPermanentBlockBreakConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionTypes;
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
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved, CallbackInfo ci) {
        if (!PandaAntiPermanentBlockBreakConfig.isFeatureEnabled("strictBreakCheck")) {
            return;
        }
        if (world.getDimensionEntry().getKey().get().getRegistry() != DimensionTypes.THE_END.getRegistry()) {
            if (state.getBlock() == Blocks.BEDROCK || state.getBlock() == Blocks.END_PORTAL_FRAME) {
                if (state.getBlock() != world.getBlockState(pos).getBlock()) {
                    world.setBlockState(pos, state);
                    ci.cancel();
                }
            }
        } else if (state.getBlock() == Blocks.BEDROCK) {
            if (state.getBlock() != world.getBlockState(pos).getBlock()) {
                List<EndSpikeFeature.Spike> spikes = EndSpikeFeature.getSpikes(world);
                for (EndSpikeFeature.Spike spike : spikes) {
                    int x = spike.getCenterX();
                    int z = spike.getCenterZ();
                    if (pos.getX() == x && pos.getZ() == z)
                        return;
                }
            }
            world.setBlockState(pos, state);
            ci.cancel();
        }



    }
}