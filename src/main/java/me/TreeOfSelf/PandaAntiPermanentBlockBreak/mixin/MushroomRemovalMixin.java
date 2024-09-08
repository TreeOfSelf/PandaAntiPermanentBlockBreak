package me.TreeOfSelf.PandaAntiPermanentBlockBreak.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MushroomPlantBlock.class)
public class MushroomRemovalMixin {
     @Inject(
                method = "trySpawningBigMushroom",
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/server/world/ServerWorld;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
                ),
                cancellable = true
        )
     public void trySpawningBigMushroom(ServerWorld world, BlockPos pos, BlockState state, Random random, CallbackInfoReturnable<Boolean> cir) {
         if (world.getBlockState(pos).getBlock() == Blocks.BEDROCK ||
                 world.getBlockState(pos).getBlock() == Blocks.END_PORTAL_FRAME ||
                 world.getBlockState(pos).getBlock() == Blocks.END_PORTAL) {
             cir.setReturnValue(false);
             cir.cancel();
         }

     }

}
