package lekavar.lma.drinkbeer.compat;

import com.google.common.collect.ImmutableMap;
import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.mixin.RecipeManagerAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.server.ServerStartingEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModCompat {
    // TODO, How to trigger it?
    public static final String ALCOCRAFT = "alcocraft";
    private static final List<Supplier<Supplier<IRecipeInjector>>> RECIPE_INJECTS = new ArrayList<>();

    public static void injectRecipes(ServerStartingEvent event) {
        RecipeManagerAccessor accessor = (RecipeManagerAccessor) event.getServer().getRecipeManager();
        // Alcocraft has crashing issue. See issue https://github.com/hadrus/Alcocraft/issues/10.
        // if(ModList.get().isLoaded(ALCOCRAFT)) RECIPE_INJECTS.add(()->AlcocraftCompat::new);

        if (!RECIPE_INJECTS.isEmpty()) {
            var byNameBuilder = ImmutableMap.<ResourceLocation, Recipe<?>>builder();
            byNameBuilder.putAll(accessor.getByName());

            Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>> recipesBuilder = new HashMap<>();

            DrinkBeer.LOGGER.debug("Recipes before: {}", accessor.getByName().size());

            for (var recipeInjector : RECIPE_INJECTS) {
                recipeInjector.get().get().injectRecipes(accessor.getByName(), byNameBuilder::put);
            }

            ImmutableMap<ResourceLocation, Recipe<?>> newByName = byNameBuilder.build();
            newByName.forEach((key, value) -> {
                RecipeType<?> type = value.getType();
                recipesBuilder.computeIfAbsent(type, (t) -> ImmutableMap.builder()).put(key, value);
            });

            DrinkBeer.LOGGER.debug("Recipes after: {}", accessor.getByName().size());

            accessor.setByName(newByName);
            accessor.setRecipes(recipesBuilder.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, (e) -> e.getValue().build())));

            RECIPE_INJECTS.clear();
        }

    }
}
