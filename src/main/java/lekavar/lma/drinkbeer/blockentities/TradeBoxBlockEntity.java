package lekavar.lma.drinkbeer.blockentities;

import lekavar.lma.drinkbeer.gui.TradeBoxMenu;
import lekavar.lma.drinkbeer.managers.TradeBoxManager;
import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import lekavar.lma.drinkbeer.utils.tradebox.Locations;
import lekavar.lma.drinkbeer.utils.tradebox.Residents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TradeBoxBlockEntity extends BlockEntity implements MenuProvider {
    private SimpleContainer goodInventory = new SimpleContainer(8);
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
            setChanged();
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.drinkbeer.trade_box_normal");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        this.screenHandler = new TradeBoxMenu(id, this.goodInventory, syncData, inventory, this);
        return this.screenHandler;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries);
        ContainerHelper.saveAllItems(tag, this.goodInventory.getItems(), registries);
        tag.putShort("CoolingTime", (short) this.coolingTime);
        tag.putShort("LocationId", (short) this.locationId);
        tag.putShort("ResidentId", (short) this.residentId);
        tag.putShort("Process", (short) this.process);
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag,registries);
        ContainerHelper.loadAllItems(tag, this.goodInventory.getItems(), registries);
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
}
