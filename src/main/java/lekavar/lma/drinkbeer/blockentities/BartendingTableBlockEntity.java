package lekavar.lma.drinkbeer.blockentities;

import lekavar.lma.drinkbeer.items.BeerMugItem;
import lekavar.lma.drinkbeer.items.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.items.SpiceBlockItem;
import lekavar.lma.drinkbeer.managers.MixedBeerManager;
import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import lekavar.lma.drinkbeer.utils.beer.Beers;
import lekavar.lma.drinkbeer.utils.mixedbeer.Spices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class BartendingTableBlockEntity extends BlockEntity {
    private final SimpleContainer inv = new OneItemContainer(2);
    public final IItemHandler itemHandler = new BartendingTableInvWrapper(this);

    public BartendingTableBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BARTENDING_TABLE_TILEENTITY.get(), pos, state);
    }

    public boolean placeBeer(ItemStack itemStack) {
        if (!inv.isEmpty())
            return false;
        Item beerItem = itemStack.getItem();
        if (!(beerItem instanceof MixedBeerBlockItem) && !(beerItem instanceof BeerMugItem))
            return false;
        var spiceList = MixedBeerManager.getSpiceList(itemStack);
        if (spiceList.size() >= 3)
            return false;
        inv.setItem(0, itemStack);
        markDirty();
        return true;
    }

    public boolean putSpice(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof SpiceBlockItem))
            return false;
        if (inv.isEmpty())
            return false;
        if (!inv.getItem(1).isEmpty()) {
            var spiceList = MixedBeerManager.getSpiceList(inv.getItem(1));
            if (spiceList.size() >= 3)
                return false;
        }
        ItemStack beerItem = inv.getItem(0);
        if (beerItem.isEmpty())
            beerItem = inv.getItem(1);
        var beerId = beerItem.getItem() instanceof MixedBeerBlockItem ? MixedBeerBlockItem.getBeerId(beerItem) : Beers.byItem(beerItem.getItem()).getId();
        var spiceList = MixedBeerManager.getSpiceList(beerItem);
        spiceList.add(Spices.byItem(itemStack.getItem()).getId());
        ItemStack flavoredBeer = MixedBeerManager.genMixedBeerItemStack(beerId, spiceList);
        inv.setItem(0, ItemStack.EMPTY);
        inv.setItem(1, flavoredBeer);
        markDirty();
        return true;
    }

    public ItemStack takeBeer(boolean simulate) {
        var ret = inv.getItem(0).copy();
        if (ret.isEmpty())
            ret = inv.getItem(1).copy();
        if (!simulate && !ret.isEmpty()) {
            inv.clearContent();
            markDirty();
        }
        return ret;
    }


    public void markDirty() {
        var pos = getBlockPos();
        var bs = level.getBlockState(pos);
        level.sendBlockUpdated(pos, bs, bs, Block.UPDATE_CLIENTS);
        setChanged();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        handleUpdateTag(pkt.getTag(),registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // The packet uses the CompoundTag returned by #getUpdateTag. An alternative overload of #create exists
        // that allows you to specify a custom update tag, including the ability to omit data the client might not need.
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        ContainerHelper.saveAllItems(tag, this.inv.getItems(), true, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        ContainerHelper.loadAllItems(tag, this.inv.getItems(), registries);
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries);
        ContainerHelper.saveAllItems(tag, this.inv.getItems(), true, registries);
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag,registries);
        ContainerHelper.loadAllItems(tag, this.inv.getItems(), registries);
    }

    static class OneItemContainer extends SimpleContainer {
        public OneItemContainer(int pSize) {
            super(pSize);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    static class BartendingTableInvWrapper extends InvWrapper {
        Container inv;
        BartendingTableBlockEntity be;

        public BartendingTableInvWrapper(BartendingTableBlockEntity be) {
            super(be.inv);
            this.inv = be.inv;
            this.be = be;
        }

        @Override
        public int getSlots() {
            // It's important to add a virtual slot for spice!
            // since there are forge and some other mods will determine if item inserting and existing can be stacked.
            return 3;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot == 2) return ItemStack.EMPTY;
            return super.getStackInSlot(slot);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (slot == 0 && stack.getItem() instanceof MixedBeerBlockItem || stack.getItem() instanceof BeerMugItem) {
                if (!inv.isEmpty())
                    return stack;
                var spiceList = MixedBeerManager.getSpiceList(stack);
                if (spiceList.size() >= 3)
                    return stack;
                var ret = stack.copy();
                if (!simulate) {
                    be.placeBeer(stack);
                }
                ret.shrink(1);
                return ret;
            }
            // Do not
            if (slot == 2 && stack.getItem() instanceof SpiceBlockItem) {
                if (inv.isEmpty())
                    return stack;
                if (!inv.getItem(1).isEmpty()) {
                    var spiceList = MixedBeerManager.getSpiceList(inv.getItem(1));
                    if (spiceList.size() >= 3)
                        return stack;
                }
                var ret = stack.copy();
                if (!simulate) {
                    be.putSpice(stack);
                }
                ret.shrink(1);
                return ret;
            }
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 0 || slot == 2)
                return ItemStack.EMPTY;
            if (!inv.getItem(1).isEmpty())
                return be.takeBeer(simulate);
            return ItemStack.EMPTY;
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            if (slot == 2) return;
            super.setStackInSlot(slot, stack);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0 && stack.getItem() instanceof MixedBeerBlockItem || stack.getItem() instanceof BeerMugItem) {
                if (!inv.isEmpty())
                    return false;
                var spiceList = MixedBeerManager.getSpiceList(stack);
                if (spiceList.size() >= 3)
                    return false;
                return true;
            }
            if (slot == 2 && stack.getItem() instanceof SpiceBlockItem) {
                if (inv.isEmpty())
                    return false;
                if (!inv.getItem(1).isEmpty()) {
                    var spiceList = MixedBeerManager.getSpiceList(inv.getItem(1));
                    if (spiceList.size() >= 3)
                        return false;
                }
                return true;
            }
            return false;
        }
    }

}
