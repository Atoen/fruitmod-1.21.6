package fruitmod.mixin.client;

import fruitmod.block.ModBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin{

    @Shadow
    private World world;

    @Inject(method = "isSubmergedIn", at = @At("HEAD"), cancellable = true)
    private void isSubmergedInJam(TagKey<Fluid> fluidTag, CallbackInfoReturnable<Boolean> cir) {

        if (!fluidTag.equals(FluidTags.WATER)) return;

        var eyePos = ((Entity) (Object) this).getEyePos();
        var blockEyePos = BlockPos.ofFloored(eyePos);

        if (world.getBlockState(blockEyePos).isOf(ModBlocks.INSTANCE.getJAM_BLOCK())) {
            cir.setReturnValue(true);
        }
    }
}
