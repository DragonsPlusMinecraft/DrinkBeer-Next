package lekavar.lma.drinkbeer.effects;

import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Random;


public class NightHowlStatusEffect {
    private final static int BASE_NIGHT_VISION_TIME = 2400;

    public static void addStatusEffect(ItemStack stack, Level world, LivingEntity user) {
        if (stack.getItem() == ItemRegistry.BEER_MUG_NIGHT_HOWL_KVASS.get()) {
            //Duration will be longest when the moon is full, shortest when new
            user.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, getNightVisionTime(getMoonPhase(world))));
            //Play random howl sound
            playRandomHowlSound(world, user);
        }
    }

    public static Pair<MobEffect, Integer> getStatusEffectPair(Level world) {
        return Pair.of(MobEffects.NIGHT_VISION.value(), getNightVisionTime(getMoonPhase(world)));
    }

    public static void playRandomHowlSound(Level world, LivingEntity user) {
        if (!world.isClientSide) {
            world.playSound(null, user.blockPosition(), SoundEventRegistry.NIGHT_HOWL[new Random().nextInt(4)].get(), SoundSource.PLAYERS, 1.2f, 1f);
        }
    }

    private static int getNightVisionTime(int moonPhase) {
        return BASE_NIGHT_VISION_TIME + (moonPhase == 0 ? Math.abs(moonPhase - 1 - 4) * 1200 : Math.abs(moonPhase - 4) * 1200);
    }

    private static int getMoonPhase(Level world) {
        try {
            long timeOfDay = world.getDayTime();
            return (int) (timeOfDay / 24000L % 8L + 8L) % 8;
        } catch (Exception e) {
            return 0;
        }
    }
}
