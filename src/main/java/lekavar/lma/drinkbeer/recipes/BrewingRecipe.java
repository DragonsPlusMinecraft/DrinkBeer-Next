package lekavar.lma.drinkbeer.recipes;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class BrewingRecipe implements Recipe<IBrewingInventory> {
    public static final int INPUT_SIZE = 4;

    private final ResourceLocation id;
    private final NonNullList<Ingredient> input;
    private final ItemStack cup;
    private final int brewingTime;
    private final ItemStack result;

    public BrewingRecipe(ResourceLocation id, NonNullList<Ingredient> input, ItemStack cup, int brewingTime, ItemStack result) {
        this.id = id;
        this.input = input;
        this.cup = cup;
        this.brewingTime = brewingTime;
        this.result = result;
    }

    @Deprecated
    public NonNullList<Ingredient> getIngredient() {
        NonNullList<Ingredient> result = NonNullList.create();
        result.addAll(input);
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> result = NonNullList.create();
        result.addAll(input);
        return result;
    }

    @Deprecated
    public ItemStack geBeerCup() {
        return cup.copy();
    }

    public ItemStack getBeerCup() {
        return cup.copy();
    }

    @Override
    public boolean matches(IBrewingInventory pContainer, Level pLevel) {
        List<Ingredient> testTarget = Lists.newArrayList(input);
        List<ItemStack> tested = pContainer.getIngredients();
        if (tested.size() < 4) return false;
        for (ItemStack itemStack : tested) {
            int i = getLatestMatched(testTarget, itemStack);
            if (i == -1) return false;
            else testTarget.remove(i);
        }
        return testTarget.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    @Override
    public ItemStack assemble(@NotNull IBrewingInventory brewingInventory, @NotNull RegistryAccess registryAccess) {
        return result.copy();
    }

    private int getLatestMatched(List<Ingredient> testTarget, ItemStack tested) {
        for (int i = 0; i < testTarget.size(); i++) {
            if (testTarget.get(i).test(tested)) return i;
        }
        return -1;
    }

    // Can Craft at any dimension
    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book).
     * If your recipe has more than one possible result (e.g. it's dynamic and depends on its inputs),
     * then return an empty stack.
     */
    @Override
    public ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        //For Safety, I use #copy
        return result.copy();
    }

    // For JEI Addon.
    // See JEIBrewingRecipe#setRecipe
    public ItemStack getResultItemNoRegistryAccess() {
        //For Safety, I use #copy
        return result.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.RECIPE_SERIALIZER_BREWING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.RECIPE_TYPE_BREWING.get();
    }

    public int getRequiredCupCount() {
        return cup.getCount();
    }

    public boolean isCupQualified(IBrewingInventory inventory) {
        return inventory.getCup().getItem() == cup.getItem() && inventory.getCup().getCount() >= cup.getCount();
    }

    public int getBrewingTime() {
        return brewingTime;
    }

    public static class Serializer implements RecipeSerializer<BrewingRecipe> {

        @Override
        public BrewingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredients"));
            ItemStack cup = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "cup"), true);
            int brewing_time = GsonHelper.getAsInt(jsonObject, "brewing_time");
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(jsonObject, "result"), true);
            return new BrewingRecipe(resourceLocation, ingredients, cup, brewing_time, result);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                ingredients.add(ingredient);
            }
            return ingredients;
        }

        @Nullable
        @Override
        public BrewingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf packetBuffer) {
            int i = packetBuffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);
            for (int j = 0; j < ingredients.size(); ++j) {
                ingredients.set(j, Ingredient.fromNetwork(packetBuffer));
            }
            ItemStack cup = packetBuffer.readItem();
            int brewingTime = packetBuffer.readVarInt();
            ItemStack result = packetBuffer.readItem();
            return new BrewingRecipe(resourceLocation, ingredients, cup, brewingTime, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf packetBuffer, BrewingRecipe brewingRecipe) {
            packetBuffer.writeVarInt(brewingRecipe.input.size());
            for (Ingredient ingredient : brewingRecipe.input) {
                ingredient.toNetwork(packetBuffer);
            }
            packetBuffer.writeItem(brewingRecipe.cup);
            packetBuffer.writeVarInt(brewingRecipe.brewingTime);
            packetBuffer.writeItem(brewingRecipe.result);

        }
    }
}
