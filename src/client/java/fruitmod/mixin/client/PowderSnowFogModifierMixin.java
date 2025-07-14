package fruitmod.mixin.client;

import fruitmod.JamSubmersionState;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.fog.PowderSnowFogModifier;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowFogModifier.class)
public class PowderSnowFogModifierMixin {

    @Inject(method = "shouldApply", at = @At("HEAD"), cancellable = true)
    private void preventApplying(
        CameraSubmersionType submersionType,
        Entity cameraEntity,
        CallbackInfoReturnable<Boolean> cir
    ) {
        var shouldApply = submersionType == CameraSubmersionType.POWDER_SNOW
                && !JamSubmersionState.isInJam;

        cir.setReturnValue(shouldApply);
    }
}
