package lekavar.lma.drinkbeer.gui;

import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.MenuTypeRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BeerBarrelMenu extends AbstractContainerMenu {
    private static final int STATUS_CODE = 1;
    private static final int BREWING_REMAINING_TIME = 0;
    private final Container brewingSpace;
    private final ContainerData syncData;

    public BeerBarrelMenu(int id, Container brewingSpace, ContainerData syncData, Inventory playerInventory, BeerBarrelBlockEntity beerBarrelBlockEntity) {
        super(MenuTypeRegistry.beerBarrelContainer.get(), id);
        this.brewingSpace = brewingSpace;
        this.syncData = syncData;

        // Layout Slot
        // Player Inventory
        layoutPlayerInventorySlots(8, 84, new InvWrapper(playerInventory));
        // Input Ingredients
        addSlot(new Slot(brewingSpace, 0, 28, 26));
        addSlot(new Slot(brewingSpace, 1, 46, 26));
        addSlot(new Slot(brewingSpace, 2, 28, 44));
        addSlot(new Slot(brewingSpace, 3, 46, 44));
        // Empty Cup
        addSlot(new Slot(brewingSpace, 4, 73, 50));
        // Output
        addSlot(new OutputSlot(brewingSpace, 5, 128, 34, syncData, beerBarrelBlockEntity));

        //Tracking Data
        addDataSlots(syncData);
    }

    public BeerBarrelMenu(int id, Inventory playerInventory, FriendlyByteBuf data) {
        this(id, playerInventory, data.readBlockPos());
    }

    public BeerBarrelMenu(int id, Inventory playerInventory, BlockPos pos) {
        this(id, ((BeerBarrelBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos)).getBrewingInventory(), ((BeerBarrelBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos)).syncData, playerInventory, ((BeerBarrelBlockEntity) Minecraft.getInstance().level.getBlockEntity(pos)));
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow, IItemHandler playerInventory) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Try quick-pickup output
            if (pIndex == 41) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // Try quick-move item in player inv.
            else if (pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 41, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Try quick-move item to player inv.
            else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            // Detect weather the quick-move is successful or not
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            // Detect weather the quick-move is successful or not
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.brewingSpace.stillValid(pPlayer);
    }

    public boolean getIsBrewing() {
        return syncData.get(STATUS_CODE) == 1;
    }

    public int getStandardBrewingTime() {
        return syncData.get(BREWING_REMAINING_TIME);
    }

    public int getRemainingBrewingTime() {
        return syncData.get(BREWING_REMAINING_TIME);
    }

    @Override
    public void removed(Player player) {
        if (!player.level().isClientSide()) {
            // Play Closing Barrel Sound
            player.level().playSound(player, player.blockPosition(), SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 1f, 1f);
        }
        super.removed(player);
    }

    static class OutputSlot extends Slot {
        private final ContainerData syncData;
        private final BeerBarrelBlockEntity beerBarrelBlockEntity;

        public OutputSlot(Container container, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_, ContainerData syncData, BeerBarrelBlockEntity beerBarrelBlockEntity) {
            super(container, p_i1824_2_, p_i1824_3_, p_i1824_4_);
            this.syncData = syncData;
            this.beerBarrelBlockEntity = beerBarrelBlockEntity;
        }

        // After player picking up product, play pour sound effect
        // statusCode reset is handled by TileEntity#tick
        @Override
        public void onTake(Player player, ItemStack pStack) {
            if (pStack.getItem() == ItemRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get()) {
                player.level().playSound(null, beerBarrelBlockEntity.getBlockPos(), SoundEventRegistry.POURING_CHRISTMAS.get(), SoundSource.BLOCKS, 1.0F, 1.0F);

            } else {
                player.level().playSound(null, beerBarrelBlockEntity.getBlockPos(), SoundEventRegistry.POURING.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        // Placing item on output slot is prohibited.
        @Override
        public boolean mayPlace(ItemStack pStack) {
            return false;
        }

        // Only when the statusCode is 2 (waiting for pickup), pickup is allowed.
        @Override
        public boolean mayPickup(Player pPlayer) {
            return syncData.get(STATUS_CODE) == 2;
        }
    }
}
