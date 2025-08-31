package fruitmod.render

import fruitmod.block.entity.custom.EnchantedGoldenAppleBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Vec3d

class EnchantedGoldenAppleRenderer(
    ctx: BlockEntityRendererFactory.Context
) : BlockEntityRenderer<EnchantedGoldenAppleBlockEntity> {

    override fun render(
        entity: EnchantedGoldenAppleBlockEntity,
        tickProgress: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int,
        cameraPos: Vec3d,
    ) {

        val minecraftClient = MinecraftClient.getInstance()
        val glintConsumer = vertexConsumers.getBuffer(RenderLayer.getGlintTranslucent())

        minecraftClient.blockRenderManager.renderBlockAsEntity(
            entity.cachedState,
            matrices,
            { glintConsumer },
            light,
            overlay
        )
    }
}