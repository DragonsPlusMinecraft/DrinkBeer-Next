package lekavar.lma.drinkbeer.compat.jei;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.gui.BeerBarrelMenu;
import lekavar.lma.drinkbeer.gui.BeerBarrelScreen;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class Plugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(DrinkBeer.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIBrewingRecipe(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(JEIBrewingRecipe.TYPE, Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeRegistry.RECIPE_TYPE_BREWING.get()));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(BeerBarrelMenu.class, null, JEIBrewingRecipe.TYPE, 36, 4, 0, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BlockRegistry.BEER_BARREL.get()), JEIBrewingRecipe.TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(BeerBarrelScreen.class, 90, 31, 37, 22, JEIBrewingRecipe.TYPE);
    }
}
