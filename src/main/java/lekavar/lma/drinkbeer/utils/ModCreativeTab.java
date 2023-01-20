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
                        .displayItems((enabledFlags, populator, hasPermissions) -> {
                            populator.accept(ItemRegistry.BEER_BARREL.get());
                            populator.accept(ItemRegistry.BARTENDING_TABLE.get());
                            populator.accept(ItemRegistry.TRADE_BOX.get());
                            populator.accept(ItemRegistry.EMPTY_BEER_MUG.get());
                            populator.accept(ItemRegistry.IRON_CALL_BELL.get());
                            populator.accept(ItemRegistry.GOLDEN_CALL_BELL.get());
                            populator.accept(ItemRegistry.LEKAS_CALL_BELL.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_PACKAGE.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_BLAZE_STOUT.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_BLAZE_MILK_STOUT.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_APPLE_LAMBIC.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_SWEET_BERRY_KRIEK.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_HAARS_ICEY_PALE_LAGER.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_PUMPKIN_KVASS.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_NIGHT_HOWL_KVASS.get());
                            populator.accept(ItemRegistry.RECIPE_BOARD_BEER_MUG_FROTHY_PINK_EGGNOG.get());
                            populator.accept(ItemRegistry.SPICE_BLAZE_PAPRIKA.get());
                            populator.accept(ItemRegistry.SPICE_DRIED_EGLIA_BUD.get());
                            populator.accept(ItemRegistry.SPICE_SMOKED_EGLIA_BUD.get());
                            populator.accept(ItemRegistry.SPICE_AMETHYST_NIGELLA_SEEDS.get());
                            populator.accept(ItemRegistry.SPICE_CITRINE_NIGELLA_SEEDS.get());
                            populator.accept(ItemRegistry.SPICE_ICE_MINT.get());
                            populator.accept(ItemRegistry.SPICE_ICE_PATCHOULI.get());
                            populator.accept(ItemRegistry.SPICE_STORM_SHARDS.get());
                            populator.accept(ItemRegistry.SPICE_ROASTED_RED_PINE_NUTS.get());
                            populator.accept(ItemRegistry.SPICE_GLACE_GOJI_BERRIES.get());
                            populator.accept(ItemRegistry.SPICE_FROZEN_PERSIMMON.get());
                            populator.accept(ItemRegistry.SPICE_ROASTED_PECANS.get());
                            populator.accept(ItemRegistry.SPICE_SILVER_NEEDLE_WHITE_TEA.get());
                            populator.accept(ItemRegistry.SPICE_GOLDEN_CINNAMON_POWDER.get());
                            populator.accept(ItemRegistry.SPICE_DRIED_SELAGINELLA.get());
                        })
        );
        event.registerCreativeModeTab(new ResourceLocation(DrinkBeer.MOD_ID, "beer"), builder ->
                // Set name of tab to display
                builder.title(Component.translatable("item_group.drinkbeer.beer"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(BlockRegistry.BEER_BARREL.get()))
                        // Add default items to tab
                        .displayItems((enabledFlags, populator, hasPermissions) -> {
                            populator.accept(ItemRegistry.BEER_MUG.get());
                            populator.accept(ItemRegistry.BEER_MUG_BLAZE_STOUT.get());
                            populator.accept(ItemRegistry.BEER_MUG_BLAZE_MILK_STOUT.get());
                            populator.accept(ItemRegistry.BEER_MUG_APPLE_LAMBIC.get());
                            populator.accept(ItemRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get());
                            populator.accept(ItemRegistry.BEER_MUG_HAARS_ICEY_PALE_LAGER.get());
                            populator.accept(ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get());
                            populator.accept(ItemRegistry.BEER_MUG_NIGHT_HOWL_KVASS.get());
                            populator.accept(ItemRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get());
                        })
        );
    }
}
