package fruitmod;

import fruitmod.block.JamBlockProperties;
import fruitmod.block.custom.JamBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.fog.PowderSnowFogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

public class JamFogModifier extends PowderSnowFogModifier {

    private static final int fallbackFogColor = 0xFFFF00FF; // Magenta
    private static final int channelScaleFactor = (int) (17 * 0.5);

    @Override
    public boolean shouldApply(@Nullable CameraSubmersionType submersionType, Entity cameraEntity) {
        if (submersionType == CameraSubmersionType.POWDER_SNOW) {
            return JamSubmersionState.isInJam;
        }

        return false;
    }

    @Override
    public int getFogColor(ClientWorld world, Camera camera, int viewDistance, float skyDarkness) {
        BlockPos camPos = BlockPos.ofFloored(camera.getPos());
        BlockState state = world.getBlockState(camPos);

        if (state.getBlock() instanceof JamBlock) {
            int red = state.get(JamBlockProperties.INSTANCE.getRED()) * channelScaleFactor;
            int green = state.get(JamBlockProperties.INSTANCE.getGREEN()) * channelScaleFactor;
            int blue = state.get(JamBlockProperties.INSTANCE.getBLUE()) * channelScaleFactor;

            return ColorHelper.getArgb(red, green, blue);
        }

        return fallbackFogColor;
    }
}
