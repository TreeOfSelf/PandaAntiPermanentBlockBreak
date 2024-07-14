package me.sebastian420.PandaAntiPermanentBlockBreak.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBlock.class)
public class PistonRemovalMixin {
	@Inject(
			method = "onSyncedBlockEvent",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z",
					ordinal = 0
			),
			cancellable = true
	)
	protected void onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
			Direction direction = state.get(PistonBlock.FACING);
			if (world.getBlockState(pos.offset(direction)).getBlock() != Blocks.PISTON_HEAD) {
				cir.cancel();
			}
	}

	@Inject(
			method = "onSyncedBlockEvent",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z",
					ordinal = 1
			),
			cancellable = true
	)
	protected void onSyncedBlockEventTwo(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
		Direction direction = state.get(PistonBlock.FACING);
		if (world.getBlockState(pos.offset(direction)).getBlock() != Blocks.PISTON_HEAD) {
			cir.cancel();
		}
	}
}