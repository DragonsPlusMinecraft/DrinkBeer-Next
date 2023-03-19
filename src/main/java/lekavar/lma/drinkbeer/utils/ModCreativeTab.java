package lekavar.lma.drinkbeer.utils;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;


public class ModCreativeTab {

    public static void register(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(DrinkBeer.MOD_ID, "general"), builder ->
                // Set name of tab to display
                builder.title(Component.translatable("itemGroup.drinkbeer.general"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(BlockRegistry.BEER_BARREL.get()))
                        // Add default items to tab
                        .displayItems((parameters, output) -> {
                            output.accept(ItemRegistry.BEER_BARREL.get());
                            output.accept(ItemRegistry.BARTENDING_TABLE.get());
                            output.accept(ItemRegistry.TRADE_BOX.get());
                            output.accept(ItemRegistry.EMPTY_BEER_MUG.get());
                            output.accept(ItemRegistry.IRON_CALL_BELL.get());
                            output.accept(ItemRegistry.GOLDEN_CALL_BELL.get());
                            output.accept(ItemRegistry.LEKAS_CALL_BELL.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_PACKAGE.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_BLAZE_STOUT.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_BLAZE_MILK_STOUT.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_APPLE_LAMBIC.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_SWEET_BERRY_KRIEK.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_HAARS_ICEY_PALE_LAGER.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_PUMPKIN_KVASS.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_NIGHT_HOWL_KVASS.get());
                            output.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_FROTHY_PINK_EGGNOG.get());
                            output.accept(ItemRegistry.SPICE_BLAZE_PAPRIKA.get());
                            output.accept(ItemRegistry.SPICE_DRIED_EGLIA_BUD.get());
                            output.accept(ItemRegistry.SPICE_SMOKED_EGLIA_BUD.get());
                            output.accept(ItemRegistry.SPICE_AMETHYST_NIGELLA_SEEDS.get());
                            output.accept(ItemRegistry.SPICE_CITRINE_NIGELLA_SEEDS.get());
                            output.accept(ItemRegistry.SPICE_ICE_MINT.get());
                            output.accept(ItemRegistry.SPICE_ICE_PATCHOULI.get());
                            output.accept(ItemRegistry.SPICE_STORM_SHARDS.get());
                            output.accept(ItemRegistry.SPICE_ROASTED_RED_PINE_NUTS.get());
                            output.accept(ItemRegistry.SPICE_GLACE_GOJI_BERRIES.get());
                            output.accept(ItemRegistry.SPICE_FROZEN_PERSIMMON.get());
                            output.accept(ItemRegistry.SPICE_ROASTED_PECANS.get());
                            output.accept(ItemRegistry.SPICE_SILVER_NEEDLE_WHITE_TEA.get());
                            output.accept(ItemRegistry.SPICE_GOLDEN_CINNAMON_POWDER.get());
                            output.accept(ItemRegistry.SPICE_DRIED_SELAGINELLA.get());
                        })
        );
        event.registerCreativeModeTab(new ResourceLocation(DrinkBeer.MOD_ID, "beer"), builder ->
                // Set name of tab to display
                builder.title(Component.translatable("itemGroup.drinkbeer.beer"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(BlockRegistry.BEER_BARREL.get()))
                        // Add default items to tab
                        .displayItems((parameters, output) -> {
                            output.accept(ItemRegistry.BEER_MUG.get());
                            output.accept(ItemRegistry.BEER_MUG_BLAZE_STOUT.get());
                            output.accept(ItemRegistry.BEER_MUG_BLAZE_MILK_STOUT.get());
                            output.accept(ItemRegistry.BEER_MUG_APPLE_LAMBIC.get());
                            output.accept(ItemRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get());
                            output.accept(ItemRegistry.BEER_MUG_HAARS_ICEY_PALE_LAGER.get());
                            output.accept(ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get());
                            output.accept(ItemRegistry.BEER_MUG_NIGHT_HOWL_KVASS.get());
                            output.accept(ItemRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get());
                        })
        );
    }
}
