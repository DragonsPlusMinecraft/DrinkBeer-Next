package lekavar.lma.drinkbeer.utils.beer;

// import lekavar.lma.drinkbeer.recipes.BeerRecipe;
import lekavar.lma.drinkbeer.items.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Optional;

public enum Beers {
    BEER_MUG(1, ItemRegistry.BEER_MUG.get(), true/*,  new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(24000)
            .addMaterial(3, Items.WHEAT).addMaterial(Items.WATER_BUCKET)*/),
    BEER_MUG_BLAZE_STOUT(2, ItemRegistry.BEER_MUG_BLAZE_STOUT.get(), true/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(12000)
            .addMaterial(2, Items.WHEAT).addMaterial(Items.WATER_BUCKET).addMaterial(Items.BLAZE_POWDER)*/),
    BEER_MUG_BLAZE_MILK_STOUT(3, ItemRegistry.BEER_MUG_BLAZE_MILK_STOUT.get(), true/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(18000)
            .addMaterial(Items.WHEAT).addMaterial(Items.BLAZE_POWDER).addMaterial(Items.SUGAR).addMaterial(Items.WATER_BUCKET)*/),
    BEER_MUG_APPLE_LAMBIC(4, ItemRegistry.BEER_MUG_APPLE_LAMBIC.get(), true/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(24000)
            .addMaterial(2, Items.WHEAT).addMaterial(Items.APPLE).addMaterial(Items.WATER_BUCKET)*/),
    BEER_MUG_SWEET_BERRY_KRIEK(5, ItemRegistry.BEER_MUG_SWEET_BERRY_KRIEK.get(), true/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(24000)
            .addMaterial(2, Items.WHEAT).addMaterial(Items.SWEET_BERRIES).addMaterial(Items.WATER_BUCKET)*/),
    BEER_MUG_HAARS_ICEY_PALE_LAGER(6, ItemRegistry.BEER_MUG_HAARS_ICEY_PALE_LAGER.get(), true/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(24000)
            .addMaterial(3, Items.WHEAT).addMaterial(Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE)*/),
    BEER_MUG_PUMPKIN_KVASS(7, ItemRegistry.BEER_MUG_PUMPKIN_KVASS.get(), false/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(12000)
            .addMaterial(2, Items.BREAD).addMaterial(Items.PUMPKIN).addMaterial(Items.WATER_BUCKET)*/),
    BEER_MUG_FROTHY_PINK_EGGNOG(8, ItemRegistry.BEER_MUG_FROTHY_PINK_EGGNOG.get(), true/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(12000)
            .addMaterial(Items.WHEAT).addMaterial(Items.BEETROOT).addMaterial(Items.EGG).addMaterial(Items.MILK_BUCKET)*/),
    BEER_MUG_NIGHT_HOWL_KVASS(9, ItemRegistry.BEER_MUG_NIGHT_HOWL_KVASS.get(), true/*, new BeerRecipe()
            .setBeerResultNum(4).setBrewingTime(18000)
            .addMaterial(2, Items.BREAD).addMaterial(Items.BONE).addMaterial(Items.WATER_BUCKET)*/);

    public static final int DEFAULT_BEER_ID = 1;
    public static final Beers DEFAULT_BEER = Beers.BEER_MUG;
    public static final int EMPTY_BEER_ID = 0;

    private final int id;
    private final Item beerItem;
    // private final BeerRecipe beerRecipe;
    private final boolean hasStatusEffectTooltip;

    Beers(int id, Item beerItem, boolean hasStatusEffectTooltip /*, BeerRecipe beerRecipe */) {
        this.id = id;
        this.beerItem = beerItem;
        this.hasStatusEffectTooltip = hasStatusEffectTooltip;
        // this.beerRecipe = beerRecipe;
        // this.beerRecipe.setBeerResult(this.beerItem);
    }

    public int getId() {
        return this.id;
    }

    public Item getBeerItem() {
        return this.beerItem;
    }

    public boolean getHasStatusEffectTooltip() {
        return this.hasStatusEffectTooltip;
    }

   /*
    public BeerRecipe getBeerRecipe() {
        return this.beerRecipe;
    }

    */

    public static Beers byId(int id) {
        Beers[] beers = values();
        for (Beers beer : beers) {
            if (beer.id == id) {
                return beer;
            }
        }
        return DEFAULT_BEER;
    }

    public static Beers byItem(Item beerItem) {
        Beers[] beers = values();
        for (Beers beer : beers) {
            if (beer.beerItem.equals(beerItem)) {
                return beer;
            }
        }
        return DEFAULT_BEER;
    }

    public static int size() {
        return values().length;
    }

    public static Beers byRecipeBoardBlock(Block recipeBoardBlock) {
        String beerName = recipeBoardBlock.getName().getString().replace("block.drinkbeer.recipe_board_", "");
        Optional<Beers> matchedBeer = Arrays.stream(values())
                .filter(beer -> beer.beerItem.getName(beer.beerItem.getDefaultInstance()).equals("block.drinkbeer." + beerName))
                .findFirst();
        return matchedBeer.isPresent() ? matchedBeer.get() : null;
    }

}
