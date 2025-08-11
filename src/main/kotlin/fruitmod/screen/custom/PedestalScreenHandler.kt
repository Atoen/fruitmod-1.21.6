package fruitmod.screen.custom

import fruitmod.screen.ModScreenHandlers
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.util.math.BlockPos

class PedestalScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    blockEntity: BlockEntity,
) : ScreenHandler(ModScreenHandlers.PEDESTAL_SCREEN_HANDLER, syncId) {

    private val inventory = blockEntity as Inventory

    constructor(syncId: Int, playerInventory: PlayerInventory, pos: BlockPos) : this(
        syncId,
        playerInventory,
        playerInventory.player.world.getBlockEntity(pos)!!
    )

    init {
        addSlot(object : Slot(inventory, 0, 80, 35) {
            override fun getMaxItemCount() = 1
        })

        addPlayerSlots(playerInventory, 8, 84)
    }

    override fun quickMove(
        player: PlayerEntity,
        slotIndex: Int,
    ): ItemStack {

        var newStack = ItemStack.EMPTY
        val slot = this.slots[slotIndex]

        if (slot.hasStack()) {
            val originalStack = slot.stack
            newStack = originalStack.copy()

            if (slotIndex < inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size, true)) {
                    return ItemStack.EMPTY
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY
            }

            if (originalStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
        }

        return newStack
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return inventory.canPlayerUse(player)
    }
}