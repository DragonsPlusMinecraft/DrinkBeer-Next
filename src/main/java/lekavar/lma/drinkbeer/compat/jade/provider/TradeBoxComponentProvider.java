package lekavar.lma.drinkbeer.compat.jade.provider;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blockentities.TradeBoxBlockEntity;
import lekavar.lma.drinkbeer.managers.TradeBoxManager;
import lekavar.lma.drinkbeer.utils.Convert;
import lekavar.lma.drinkbeer.utils.ItemStackHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ProgressArrowElement;

import java.util.ArrayList;
import java.util.List;

public class TradeBoxComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static TradeBoxComponentProvider INSTANCE = new TradeBoxComponentProvider();
    public static final String KEY_COOLING_TIME = "coolingTime";
    public static final String KEY_GOODS_FROM = "goodsFrom";
    public static final String KEY_GOODS_TO = "goodsTo";
    public static final int MaxGoodCount = 4;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig pluginConfig) {
        IElementHelper helper = tooltip.getElementHelper();
        CompoundTag data = accessor.getServerData();
        int coolingTime = data.getInt(KEY_COOLING_TIME); //tradebox.syncData.get(0);

        if (coolingTime == 0 && tooltip.size() > 0) {
            int goodsFromNum = data.getInt(KEY_GOODS_FROM);
            int goodsToNum = data.getInt(KEY_GOODS_TO);
            if (goodsFromNum == 0) {
                return;
            }

            List<IElement> itemElements = getInventoryTooltip(tooltip, accessor);
            if (itemElements == null) {
                return;
            }

            IElement[][] inputs = JadeHelper.layoutItems(itemElements, goodsFromNum);
            IElement[][] outputs = JadeHelper.layoutItems(itemElements, goodsToNum);

            int ih = inputs.length;
            int oh = outputs.length;

            int maxRow = Math.max(ih, oh);

            List<List<IElement>> grids = JadeHelper.createRows(maxRow);

            JadeHelper.placeItems(grids, inputs, true);
            JadeHelper.placeArrow(grids);
            JadeHelper.placeItems(grids, outputs, false);

            JadeHelper.addGridsToTooltip(tooltip, grids);
        } else {
            int maxCoolingTime = coolingTime > TradeBoxManager.COOLING_TIME_ON_REFRESH ?
                    TradeBoxManager.COOLING_TIME_ON_PLACE :
                    TradeBoxManager.COOLING_TIME_ON_REFRESH;

            tooltip.add(List.of(
                    new ProgressArrowElement(1 - (float) coolingTime / maxCoolingTime),
                    helper.text(Component.literal(Convert.tickToTime(coolingTime)))
            ));
        }
    }

    /**
     * NOTE: This relies on Jade's default InventoryProvider
     * which will automatically extract trade box's inventory
     * and pass it to client through ServerData["jadeHandler"].
     * <p>
     * However, the number of items InventoryProvider will show
     * is limited to @see JadeCommonConfig#inventoryNormalShowAmount.
     *
     * @see snownee.jade.addon.universal.ItemStorageProvider#append(ITooltip, Accessor, IPluginConfig)
     */
    private List<IElement> getInventoryTooltip(ITooltip tooltip, BlockAccessor accessor) {
        List<IElement> itemElements = null;

        if (accessor.getServerData().contains("jadeHandler")) {
            ItemStackHandler itemHandler = new ItemStackHandler();
            itemHandler.deserializeNBT(accessor.getServerData().getCompound("jadeHandler"));

            IElementHelper helper = tooltip.getElementHelper();
            itemElements = new ArrayList<>();

            for (int i = 0; i < itemHandler.getSlots(); ++i) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (stack.isEmpty()) {
                    break;
                }
                itemElements.add(helper.item(stack));
            }
        }

        if (itemElements != null) {
            tooltip.remove(Identifiers.UNIVERSAL_ITEM_STORAGE);
        }
        return itemElements;
    }

    private int getGoodCount(TradeBoxBlockEntity be, int start, int size) {
        int count = 0;
        outer:
        for (int i = 0; i < size; ++i) {
            if (be.getItem(start + i).isEmpty()) {
                break;
            }
            // jade's inv handler will automatically stack same items
            // it should be taken into consider
            for (int j = 0; j < i; ++j) {
                if (ItemStackHelper.isSameItem(be.getItem(start + i), be.getItem(start + j))) {
                    continue outer;
                }
            }
            ++count;
        }

        return count;
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(DrinkBeer.MOD_ID, "trade_box");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        TradeBoxBlockEntity be = (TradeBoxBlockEntity) blockAccessor.getBlockEntity();
        compoundTag.putInt(KEY_COOLING_TIME, be.syncData.get(0));

        if (be.syncData.get(3) == TradeBoxBlockEntity.PROCESS_TRADING) {
            int goodsFromLocNum = getGoodCount(be, 0, MaxGoodCount);
            int goodsToLocNum = getGoodCount(be, MaxGoodCount, MaxGoodCount);

            compoundTag.putInt(KEY_GOODS_FROM, goodsFromLocNum);
            compoundTag.putInt(KEY_GOODS_TO, goodsToLocNum);
        }
    }
}