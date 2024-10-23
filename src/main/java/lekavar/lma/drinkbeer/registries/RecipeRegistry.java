package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "drinkbeer");

    public static final RegistryObject<RecipeType<BrewingRecipe>> RECIPE_TYPE_BREWING = RECIPE_TYPES.register("brewing", () -> new RecipeType<>() {
        public String toString() {
            return "brewing";
        }
    });

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "drinkbeer");
    public static final RegistryObject<RecipeSerializer<BrewingRecipe>> RECIPE_SERIALIZER_BREWING = RECIPE_SERIALIZERS.register("brewing", BrewingRecipe.Serializer::new);
}
