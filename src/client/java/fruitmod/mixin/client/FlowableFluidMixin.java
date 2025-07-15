package fruitmod.mixin.client;

import fruitmod.block.custom.JamBlock;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowableFluid.class)
public class FlowableFluidMixin {

    @Inject(method = "canFill(Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    private static void preventFillingJamBlocks(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        var block = state.getBlock();
        if (block instanceof JamBlock) {
            cir.setReturnValue(false);
        }
    }
}
