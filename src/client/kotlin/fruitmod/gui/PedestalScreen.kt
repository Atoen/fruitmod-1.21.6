package fruitmod.gui

import fruitmod.screen.custom.PedestalScreenHandler
import fruitmod.util.modIdentifier
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class PedestalScreen(
    handler: PedestalScreenHandler,
    inventory: PlayerInventory,
    title: Text
) : HandledScreen<PedestalScreenHandler>(handler, inventory, title) {

    override fun drawBackground(
        context: DrawContext,
        deltaTicks: Float,
        mouseX: Int,
        mouseY: Int,
    ) {
        val x = (width - backgroundWidth) / 2
        val y = (height - backgroundHeight) / 2

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            GUI_TEXTURE,
            x, y,
            0f, 0f,
            backgroundWidth, backgroundHeight,
            256, 256
        )
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, deltaTicks: Float) {
        super.render(context, mouseX, mouseY, deltaTicks)
        drawMouseoverTooltip(context, mouseX, mouseY)
    }

    companion object {
        val GUI_TEXTURE = modIdentifier("textures/gui/pedestal/pedestal_gui.png")
    }
}