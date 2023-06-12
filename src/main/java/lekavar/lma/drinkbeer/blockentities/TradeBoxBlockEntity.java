package lekavar.lma.drinkbeer.blockentities;

import lekavar.lma.drinkbeer.gui.TradeBoxMenu;
import lekavar.lma.drinkbeer.managers.TradeBoxManager;
import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import lekavar.lma.drinkbeer.utils.tradebox.Locations;
import lekavar.lma.drinkbeer.utils.tradebox.Residents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TradeBoxBlockEntity extends BaseContainerBlockEntity {
    private NonNullList<ItemStack> goodInventory = NonNullList.withSize(8, ItemStack.EMPTY);
    private int coolingTime;
    private int locationId;
    private int residentId;
    private int process;
    public TradeBoxMenu screenHandler;

    public static final int PROCESS_COOLING = 0;
    public static final int PROCESS_TRADING = 1;

    public TradeBoxBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TRADE_BOX_TILEENTITY.get(), pos, state);
    }

    public TradeBoxBlockEntity(int coolingTime, BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.TRADE_BOX_TILEENTITY.get(), pos, state);

        this.coolingTime = TradeBoxManager.COOLING_TIME_ON_PLACE;
        this.locationId = Locations.EMPTY_LOCATION.getId();
        this.residentId = Residents.EMPTY_RESIDENT.getId();
        this.process = PROCESS_COOLING;

        syncData.set(0, coolingTime);
        syncData.set(1, locationId);
        syncData.set(2, residentId);
        syncData.set(3, process);
    }

    public NonNullList<ItemStack> getItems() {
        return goodInventory;
    }


    public final ContainerData syncData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return coolingTime;
                case 1:
                    return locationId;
                case 2:
                    return residentId;
                case 3:
                    return process;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    coolingTime = value;
                    break;
                case 1:
                    locationId = value;
                    break;
                case 2:
                    residentId = value;
                    break;
                case 3:
                    process = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.drinkbeer.trade_box_normal");
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.drinkbeer.trade_box_normal");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        this.screenHandler = new TradeBoxMenu(id, this, syncData, inventory, this);
        return this.screenHandler;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return null;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        ContainerHelper.saveAllItems(tag, this.goodInventory);
        tag.putShort("CoolingTime", (short) this.coolingTime);
        tag.putShort("LocationId", (short) this.locationId);
        tag.putShort("ResidentId", (short) this.residentId);
        tag.putShort("Process", (short) this.process);
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);

        this.goodInventory.clear(); // = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.goodInventory);

        this.coolingTime = tag.getShort("CoolingTime");
        this.locationId = tag.getShort("LocationId");
        this.residentId = tag.getShort("ResidentId");
        this.process = tag.getShort("Process");
    }

    public static void tick(Level world, BlockPos pos, BlockState state, TradeBoxBlockEntity tradeboxEntity) {
        if (!world.isClientSide()) {
            tradeboxEntity.coolingTime = tradeboxEntity.coolingTime > 0 ? --tradeboxEntity.coolingTime : 0;
            if (tradeboxEntity.coolingTime == 0 && tradeboxEntity.syncData.get(3) == PROCESS_COOLING) {
                if (tradeboxEntity.screenHandler != null) {
                    tradeboxEntity.screenHandler.setTradeboxTrading();
                }
            }
        }
    }

    @Override
    public int getContainerSize() {
        return goodInventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.goodInventory) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int p_70301_1_) {
        return p_70301_1_ >= 0 && p_70301_1_ < this.goodInventory.size() ? this.goodInventory.get(p_70301_1_) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return ContainerHelper.removeItem(this.goodInventory, p_18942_, p_18943_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return ContainerHelper.takeItem(this.goodInventory, p_18951_);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        if (p_18944_ >= 0 && p_18944_ < this.goodInventory.size()) {
            this.goodInventory.set(p_18944_, p_18945_);
        }
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(p_18946_.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        this.goodInventory.clear();
    }
}
