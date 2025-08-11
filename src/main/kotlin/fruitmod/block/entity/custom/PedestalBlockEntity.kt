package fruitmod.block.entity.custom

import fruitmod.block.entity.ImplementedInventory
import fruitmod.block.entity.ModBlockEntities
import fruitmod.screen.custom.PedestalScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.RegistryWrapper
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.storage.ReadView
import net.minecraft.storage.WriteView
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

class PedestalBlockEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(
    ModBlockEntities.PEDESTAL_BLOCK_ENTITY,
    pos,
    state
), ExtendedScreenHandlerFactory<BlockPos>, ImplementedInventory {

    private val inventory = DefaultedList.ofSize(1, ItemStack.EMPTY)

    override fun getItems(): DefaultedList<ItemStack> = inventory

    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }

    override fun writeData(view: WriteView) {
        Inventories.writeData(view, inventory)
    }

    override fun readData(view: ReadView) {
        Inventories.readData(view, inventory)
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> {
        return BlockEntityUpdateS2CPacket.create(this)
    }

    override fun toInitialChunkDataNbt(registries: RegistryWrapper.WrapperLookup): NbtCompound {
        return createNbt(registries)
    }

    override fun getScreenOpeningData(player: ServerPlayerEntity): BlockPos = this.pos

    override fun getDisplayName(): Text = Text.literal("Pedestal")

    override fun createMenu(
        syncId: Int,
        playerInventory: PlayerInventory,
        player: PlayerEntity,
    ): ScreenHandler {
        return PedestalScreenHandler(syncId, playerInventory, pos)
    }
}