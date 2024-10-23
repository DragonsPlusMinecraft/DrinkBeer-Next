package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.gui.BeerBarrelMenu;
import lekavar.lma.drinkbeer.gui.BeerBarrelScreen;
import lekavar.lma.drinkbeer.gui.TradeBoxMenu;
import lekavar.lma.drinkbeer.gui.TradeBoxScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, DrinkBeer.MOD_ID);
    public static final Supplier<MenuType<BeerBarrelMenu>> beerBarrelContainer = MENUS.register("beer_barrel_container", () -> IMenuTypeExtension.create(BeerBarrelMenu::new));
    public static final Supplier<MenuType<TradeBoxMenu>> tradeBoxContainer = MENUS.register("trade_box_normal_container", () -> IMenuTypeExtension.create(TradeBoxMenu::new));

    public static void registerContainerScreen(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.beerBarrelContainer.get(), BeerBarrelScreen::new);
        event.register(MenuTypeRegistry.tradeBoxContainer.get(), TradeBoxScreen::new);
    }
}