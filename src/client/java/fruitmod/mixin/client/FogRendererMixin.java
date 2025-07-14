package fruitmod.mixin.client;

import fruitmod.JamFogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addJamFogModifier(CallbackInfo ci) {
        var modifiers = FogRendererAccessor.getFogModifiers();
        modifiers.add(new JamFogModifier());
    }
}
