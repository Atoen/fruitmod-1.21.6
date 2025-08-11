package fruitmod.gui

import fruitmod.screen.custom.JamStationScreenHandler
import fruitmod.util.modIdentifier
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class JamStationScreen(
    handler: JamStationScreenHandler, inventory: PlayerInventory, title: Text,
) : HandledScreen<JamStationScreenHandler>(handler, inventory, title) {

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)
        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    override fun drawBackground(context: DrawContext, deltaTicks: Float, mouseX: Int, mouseY: Int) {
        val centerX = (width - backgroundWidth) / 2
        val centerY = (height - backgroundHeight) / 2

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            GUI_TEXTURE,
            centerX, centerY,
            0f, 0f,
            backgroundWidth, backgroundHeight,
            256, 256
        )

        if (handler.isHeated) {
            context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                HEATED_TEXTURE,
                centerX + 84, centerY + 47,
                0f, 0f,
                14, 14,
                14, 14
            )
        }

        val arrowWidth = MathHelper.ceil(handler.jammingProgress * 24)

        context.drawGuiTexture(
            RenderPipelines.GUI_TEXTURED,
            PROGRESS_TEXTURE,
            24, 16,
            0, 0,
            x + 106, y + 34,
            arrowWidth, 16
        )
    }

    companion object {
        private val HEATED_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/container/furnace/lit_progress.png")
        private val PROGRESS_TEXTURE = Identifier.ofVanilla("container/furnace/burn_progress")
        private val GUI_TEXTURE = modIdentifier("textures/gui/jam_station/jam_station_gui.png")
    }
}
