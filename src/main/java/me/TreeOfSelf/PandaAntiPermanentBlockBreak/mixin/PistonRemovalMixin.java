package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBaseBlock.class)
public class PistonRemovalMixin {
	@Inject(
			method = "triggerEvent",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
					ordinal = 0
			),
			cancellable = true
	)
	protected void triggerEvent(BlockState state, Level level, BlockPos pos, int b0, int b1, CallbackInfoReturnable<Boolean> cir) {
			Direction direction = state.getValue(DirectionalBlock.FACING);
			if (level.getBlockState(pos.relative(direction)).getBlock() != Blocks.PISTON_HEAD) {
				cir.cancel();
			}
	}

	@Inject(
			method = "triggerEvent",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z",
					ordinal = 1
			),
			cancellable = true
	)
	protected void triggerEventTwo(BlockState state, Level level, BlockPos pos, int b0, int b1, CallbackInfoReturnable<Boolean> cir) {
		Direction direction = state.getValue(DirectionalBlock.FACING);
		if (level.getBlockState(pos.relative(direction)).getBlock() != Blocks.PISTON_HEAD) {
			cir.cancel();
		}
	}
}
