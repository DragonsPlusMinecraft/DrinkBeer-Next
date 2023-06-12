package lekavar.lma.drinkbeer.compat.jade.provider;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blockentities.MixedBeerBlockEntity;
import lekavar.lma.drinkbeer.blocks.MixedBeerBlock;
import lekavar.lma.drinkbeer.items.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.managers.MixedBeerManager;
import lekavar.lma.drinkbeer.managers.SpiceAndFlavorManager;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.utils.beer.Beers;
import lekavar.lma.drinkbeer.utils.mixedbeer.Flavors;
import lekavar.lma.drinkbeer.utils.mixedbeer.Spices;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ItemStackElement;
import snownee.jade.impl.ui.TextElement;

import java.util.List;

public class MixedBeerComponentProvider implements IBlockComponentProvider {
    public static MixedBeerComponentProvider INSTANCE = new MixedBeerComponentProvider();
    public static final float TEXT_HEIGHT = new TextElement(Component.nullToEmpty("")).getSize().y;

    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        if (accessor.getBlockEntity() instanceof MixedBeerBlockEntity mixedBeer) {
            return ItemStackElement.of(Beers.byId(mixedBeer.getBeerId()).getBeerItem().getDefaultInstance());
        }
        return IBlockComponentProvider.super.getIcon(accessor, config, currentIcon);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!(accessor.getBlock() instanceof MixedBeerBlock &&
                accessor.getBlockEntity() instanceof MixedBeerBlockEntity mxBeer))
            return;
        MixedBeerBlockItem MIXED_BEER = (MixedBeerBlockItem) ItemRegistry.MIXED_BEER.get();
        Component name = MIXED_BEER.getMixedBeerName(mxBeer.getPickStack());
        if (name != null) {
            @SuppressWarnings("all")
            IWailaConfig wailaConfig = IWailaConfig.get();
            tooltip.clear();
            tooltip.add(name.copy().setStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)));
        }

        if (!(accessor.getBlock() instanceof MixedBeerBlock &&
                accessor.getBlockEntity() instanceof MixedBeerBlockEntity mixedBeer))
            return;

        IElementHelper helper = tooltip.getElementHelper();
        ItemStack stack = mixedBeer.getPickStack();

        java.util.List<Integer> spiceList = MixedBeerManager.getSpiceList(stack);
        if (!spiceList.isEmpty()) {
            for (int spiceId : spiceList) {
                Spices spice = Spices.byId(spiceId);
                Item spiceItem = spice.getSpiceItem();
                Flavors flavor = spice.getFlavor();

                // [Flavor icon] Flavor name
                tooltip.add(List.of(
                        JadeHelper.translateDelta(JadeHelper.createCenteredItem(helper, spiceItem.getDefaultInstance(), 0.8F, TEXT_HEIGHT), 0, -2),
                        helper.text(Component.translatable(SpiceAndFlavorManager.getFlavorTranslationKey(flavor.getId())).withStyle(ChatFormatting.RED))
                ));
            }

            //Flavor combination(if exists)
            Flavors combinedFlavor = SpiceAndFlavorManager.getCombinedFlavor(spiceList);
            if (combinedFlavor != null) {
                tooltip.add(Component.literal("")
                        .append("\"")
                        .append(Component.translatable(SpiceAndFlavorManager.getFlavorTranslationKey(combinedFlavor.getId())))
                        .append("\"")
                        .withStyle(ChatFormatting.DARK_RED));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(DrinkBeer.MOD_ID, "mixed_beer");
    }
}