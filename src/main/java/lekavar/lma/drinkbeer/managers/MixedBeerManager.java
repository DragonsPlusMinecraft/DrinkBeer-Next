package lekavar.lma.drinkbeer.managers;

import lekavar.lma.drinkbeer.effects.DrunkStatusEffect;
import lekavar.lma.drinkbeer.effects.NightHowlStatusEffect;
import lekavar.lma.drinkbeer.entities.damages.AlcoholDamage;
import lekavar.lma.drinkbeer.items.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.registries.DataComponentTypeRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.utils.beer.Beers;
import lekavar.lma.drinkbeer.utils.dataComponent.SpiceData;
import lekavar.lma.drinkbeer.utils.mixedbeer.Flavors;
import lekavar.lma.drinkbeer.utils.mixedbeer.MixedBeerOnUsing;
import lekavar.lma.drinkbeer.utils.mixedbeer.Spices;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MixedBeerManager {
    public static ItemStack genMixedBeerItemStack(int beerId, int... spiceIds) {
        List<Integer> spiceList = new ArrayList<>();
        for (int spiceId : spiceIds) {
            spiceList.add(spiceId);
        }
        return genMixedBeerItemStack(beerId, spiceList);
    }

    public static ItemStack genMixedBeerItemStack(int beerId, List<Integer> spiceList) {
        ItemStack resultStack = new ItemStack(ItemRegistry.MIXED_BEER.get(), 1);
        resultStack.set(DataComponentTypeRegistry.BEER_ID_COMPONENT,beerId);
        spiceList = removeIllegalSpiceId(spiceList);
        resultStack.set(DataComponentTypeRegistry.SPICE_COMPONENT,SpiceData.fromSpiceList(spiceList));
        return resultStack;
    }

    private static List<Integer> removeIllegalSpiceId(List<Integer> spiceList) {
        return spiceList.stream()
                .filter(value -> (value.compareTo(Spices.EMPTY_SPICE_ID) > 0) && (value.compareTo(Spices.size()) <= 0))
                .collect(Collectors.toList());
    }

    public static int getBeerId(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MixedBeerBlockItem) {
            return itemStack.get(DataComponentTypeRegistry.BEER_ID_COMPONENT);
        }
        return Beers.EMPTY_BEER_ID;
    }

    public static List<Integer> getSpiceList(ItemStack itemStack) {
        List<Integer> spiceList = new ArrayList<>();
        if (itemStack.getItem() instanceof MixedBeerBlockItem) {
            var data = itemStack.get(DataComponentTypeRegistry.SPICE_COMPONENT);
            if(data.spiceA()>0){
                spiceList.add(data.spiceA());
                if(data.spiceB()>0){
                    spiceList.add(data.spiceB());
                    if(data.spiceC()>0){
                        spiceList.add(data.spiceC());
                    }
                }
            }
        }
        return spiceList;
    }

    public static String getMixedBeerTranslationKey() {
        return ItemRegistry.MIXED_BEER.get().asItem().toString();
    }

    public static String getBaseBeerToolTipTranslationKey() {
        return "item.drinkbeer.mixed_beer.tooltip_base";
    }

    public static String getUnmixedToolTipTranslationKey() {
        return "item.drinkbeer.mixed_beer.tooltip_unmixed";
    }

    public static void useMixedBeer(ItemStack stack, Level world, LivingEntity user) {
        /*Initialize properties!*/
        /*------------------------------------------------------------------------------------------------------------------*/
        MixedBeerOnUsing mixedBeerOnUsing = new MixedBeerOnUsing();
        //Initialize beer
        mixedBeerOnUsing.setBeer(Beers.byId(getBeerId(stack)));
        //Initialize food level
        mixedBeerOnUsing.addHunger(Objects.requireNonNull(mixedBeerOnUsing.getBeerItem().getFoodProperties(stack,null).nutrition()));
        //Initialize spices and flavors
        List<Integer> spiceList = getSpiceList(stack);
        mixedBeerOnUsing.setSpiceList(spiceList);
        Flavors combinedFlavor = SpiceAndFlavorManager.getCombinedFlavor(spiceList);
        if (combinedFlavor != null) {
            mixedBeerOnUsing.addFlavor(combinedFlavor);
        }

        /*Deal with properties!*/
        /*------------------------------------------------------------------------------------------------------------------*/
        //Add base beer status effect
        mixedBeerOnUsing.addStatusEffect(getBeerStatusEffectList(mixedBeerOnUsing.getBeerItem().getDefaultInstance(), world));
        //Deal with flavors
        SpiceAndFlavorManager.applyFlavorValue(mixedBeerOnUsing);

        /*Apply properties!*/
        /*------------------------------------------------------------------------------------------------------------------*/
        //Apply Drunk status effect
        DrunkStatusEffect.addStatusEffect(user, mixedBeerOnUsing.getDrunkValue());
        //Apply beer's special actions
        if (mixedBeerOnUsing.getBeer().equals(Beers.BEER_MUG_NIGHT_HOWL_KVASS)) {
            NightHowlStatusEffect.playRandomHowlSound(world, user);
        }
        //Apply food level
        if (user instanceof Player && !((Player) user).isCreative()) {
            ((Player) user).getFoodData().eat(mixedBeerOnUsing.getHunger(), 0f);
        }
        //Apply health
        if (user instanceof Player) {
            if (!((Player) user).isCreative()) {
                if (mixedBeerOnUsing.getHealth() < 0) {
                    user.hurt(new AlcoholDamage(), Math.abs(mixedBeerOnUsing.getHealth()));
                } else {
                    user.heal(mixedBeerOnUsing.getHealth());
                }
            }
        } else {
            user.setHealth(user.getHealth() + mixedBeerOnUsing.getHealth());
        }
        //Apply status effects
        for (Pair<MobEffect, Integer> statusEffectPair : mixedBeerOnUsing.getStatusEffectList()) {
            user.addEffect(new MobEffectInstance(Holder.direct(statusEffectPair.getKey()), statusEffectPair.getValue()));
        }
        //Apply flavor actions
        SpiceAndFlavorManager.applyFlavorAction(mixedBeerOnUsing, world, user);
    }

    private static List<Pair<MobEffect, Integer>> getBeerStatusEffectList(ItemStack stack, Level world) {
        List<Pair<MobEffect, Integer>> resultStatusEffectList = new ArrayList<>();
        List<FoodProperties.PossibleEffect> possibleEffects = stack.getFoodProperties(null).effects();
        if (possibleEffects != null) {
            if (!possibleEffects.isEmpty()) {
                for (FoodProperties.PossibleEffect possibleEffect : possibleEffects)
                    resultStatusEffectList.add(Pair.of(possibleEffect.effect().getEffect().value(),possibleEffect.effect().getDuration()));
            }
        }
        if (stack.getItem().equals(Beers.BEER_MUG_NIGHT_HOWL_KVASS.getBeerItem())) {
            Pair<MobEffect, Integer> nightHowlStatusEffectPair = NightHowlStatusEffect.getStatusEffectPair(world);
            resultStatusEffectList.add(nightHowlStatusEffectPair);
        }
        return resultStatusEffectList;
    }

    /**
     * Get the number of the target action occurrences before current action.
     *
     * @param index        Current action's index in actionList
     * @param targetAction Which action to find
     * @param actionList   Current mixed beer's actionList
     * @return Number of the target action occurrences before current action
     */
    public static int getActionedTimes(int index, Flavors targetAction, List<Flavors> actionList) {
        if (index == 0)
            return 0;
        int actionTime = 0;
        for (int i = 0; i < index; i++) {
            if (actionList.get(i).equals(targetAction)) {
                actionTime++;
            }
        }
        return actionTime;
    }

    /**
     * Whether the target action exists before the current action.
     *
     * @param index        Current action's index in actionList
     * @param targetAction Which action to find
     * @param actionList   Current mixed beer's actionList
     * @return
     */
    public static boolean hasActionedBefore(int index, Flavors targetAction, List<Flavors> actionList) {
        return getActionedTimes(index, targetAction, actionList) != 0;
    }

    /**
     * Whether the target action exists after current action.
     *
     * @param index        Current action's index in actionList
     * @param targetAction Which action to find
     * @param actionList   Current mixed beer's actionList
     * @return
     */
    public static boolean hasActionAfter(int index, Flavors targetAction, List<Flavors> actionList) {
        if (actionList.size() - 1 == index) {
            return false;
        } else {
            for (int i = index + 1; i < actionList.size(); i++) {
                if (actionList.get(i).equals(targetAction)) {
                    return true;
                }
            }
        }
        return false;
    }
}
