package lekavar.lma.drinkbeer.blockentities;

import lekavar.lma.drinkbeer.managers.MixedBeerManager;
import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MixedBeerBlockEntity extends BlockEntity {
    private int beerId;
    private List<Integer> spiceList = new ArrayList<>();

    public MixedBeerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.MIXED_BEER_TILEENTITY.get(), pos, state);
    }

    public MixedBeerBlockEntity(BlockPos pos, BlockState state, int beerId, List<Integer> spiceList) {
        super(BlockEntityRegistry.MIXED_BEER_TILEENTITY.get(), pos, state);
        this.beerId = beerId;
        this.spiceList.clear();
        this.spiceList.addAll(spiceList);
    }

    /**
     * @see MixedBeerManager#genMixedBeerItemStack(int, List)
     */
    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries);

        CompoundTag descriptorTag = new CompoundTag();
        descriptorTag.putInt("beerId", getBeerId());
        descriptorTag.putIntArray("spiceList", getSpiceList());

        tag.put("MixedBeer", descriptorTag);
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag,registries);

        CompoundTag descriptorTag = tag.getCompound("MixedBeer");
        this.beerId = descriptorTag.getShort("beerId");
        this.spiceList.clear();
        for (int spice : descriptorTag.getIntArray("spiceList")) {
            this.spiceList.add(spice);
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        super.handleUpdateTag(tag,registries); // will directly call load()
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag,registries);

        return tag;
    }

    public ItemStack getPickStack() {
        //Generate mixed beer item stack for dropping
        ItemStack resultStack = MixedBeerManager.genMixedBeerItemStack(this.beerId, this.spiceList);
        return resultStack;
    }

    public List<Integer> getSpiceList() {
        return spiceList;
    }

    public int getBeerId() {
        return beerId;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider registries) {
        handleUpdateTag(pkt.getTag(),registries);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}