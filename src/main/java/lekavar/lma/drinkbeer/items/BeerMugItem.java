package lekavar.lma.drinkbeer.items;

import lekavar.lma.drinkbeer.effects.DrunkStatusEffect;
import lekavar.lma.drinkbeer.effects.NightHowlStatusEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BeerMugItem extends BeerBlockItem {
    private final static double MAX_PLACE_DISTANCE = 2.0D;
    private final boolean hasExtraTooltip;

    public BeerMugItem(Block block, int nutrition, boolean hasExtraTooltip) {
        super(block, new Item.Properties().stacksTo(16)
                .food(new FoodProperties.Builder().nutrition(nutrition).alwaysEdible().build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    public BeerMugItem(Block block, @Nullable MobEffectInstance statusEffectInstance, int nutrition, boolean hasExtraTooltip) {
        super(block, new Item.Properties().stacksTo(16)
                .food(statusEffectInstance != null
                        ? new FoodProperties.Builder().nutrition(nutrition).effect(statusEffectInstance, 1).alwaysEdible().build()
                        : new FoodProperties.Builder().nutrition(nutrition).alwaysEdible().build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    public BeerMugItem(Block block, Supplier<MobEffectInstance> statusEffectInstance, int nutrition, boolean hasExtraTooltip) {
        super(block, new Item.Properties().stacksTo(16)
                .food(statusEffectInstance != null
                        ? new FoodProperties.Builder().nutrition(nutrition).effect(statusEffectInstance, 1).alwaysEdible().build()
                        : new FoodProperties.Builder().nutrition(nutrition).alwaysEdible().build()));
        this.hasExtraTooltip = hasExtraTooltip;
    }

    @Override
    protected boolean canPlace(BlockPlaceContext p_195944_1_, BlockState p_195944_2_) {
        if ((p_195944_1_.getClickLocation().distanceTo(p_195944_1_.getPlayer().position()) > MAX_PLACE_DISTANCE))
            return false;
        else {
            return super.canPlace(p_195944_1_, p_195944_2_);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        String name = this.asItem().toString();
        if (hasEffectNoticeTooltip()) {
            tooltipComponents.add(Component.translatable("item.drinkbeer." + name + ".tooltip").setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE)));
        }
        String hunger = String.valueOf(stack.getItem().getFoodProperties(stack,null).nutrition());
        tooltipComponents.add(Component.translatable("drinkbeer.restores_hunger").setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE)).append(hunger));
    }

    private boolean hasEffectNoticeTooltip() {
        return this.hasExtraTooltip;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        //Give Drunk status effect
        DrunkStatusEffect.addStatusEffect(user);
        //Give Night Vision status effect if drank Night Howl Kvass
        NightHowlStatusEffect.addStatusEffect(stack, world, user);
        //Give empty mug back
        giveEmptyMugBack(user);

        return super.finishUsingItem(stack, world, user);
    }
}
