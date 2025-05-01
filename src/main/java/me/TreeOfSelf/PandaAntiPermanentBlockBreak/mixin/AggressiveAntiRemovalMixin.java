package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.class)
public class AggressiveAntiRemovalMixin {
    @Inject(
            method = "onStateReplaced",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, boolean moved, CallbackInfo ci) {
       if (state.getBlock() == Blocks.BEDROCK || state.getBlock() == Blocks.END_PORTAL_FRAME || state.getBlock() == Blocks.END_PORTAL) {
           if (state.getBlock() != world.getBlockState(pos).getBlock()) {
               world.setBlockState(pos, state);
               ci.cancel();
           }
       }
    }
}
