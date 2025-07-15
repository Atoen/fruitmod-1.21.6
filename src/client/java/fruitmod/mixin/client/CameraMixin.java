package fruitmod.mixin.client;

import fruitmod.JamSubmersionState;
import fruitmod.block.ModBlocks;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    private boolean ready;

    @Shadow
    private BlockView area;

    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    private void checkJamSubmersion(CallbackInfoReturnable<CameraSubmersionType> cir) {
        if (!ready) {
            cir.setReturnValue(CameraSubmersionType.NONE);
        } else {
            var cameraPos = ((Camera) (Object) this).getPos();
            var blockPos = BlockPos.ofFloored(cameraPos);
            var blockState = area.getBlockState(blockPos);

            if (blockState.isOf(ModBlocks.INSTANCE.getJAM_BLOCK())) {
                JamSubmersionState.isInJam = true;
                cir.setReturnValue(CameraSubmersionType.POWDER_SNOW);
            } else {
                JamSubmersionState.isInJam = false;
            }
        }
    }
}
