package lekavar.lma.drinkbeer.effects;

import lekavar.lma.drinkbeer.registries.MobEffectRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class DrunkStatusEffect extends MobEffect {
    public final static int MAX_DRUNK_AMPLIFIER = 4;
    public final static int MIN_DRUNK_AMPLIFIER = 0;
    private final static int BASE_DURATION = 1200;
    private final static boolean visible = false;
    private static final int[] drunkDurations = {3600, 3000, 2400, 1800, 1200};
    private static final int[] nauseaDurations = {160, 160, 200, 300, 600};
    private static final int[] slownessDurations = {0, 80, 160, 200, 600};
    private static final int[] harmulStatusEffectsIntervals = {200, 160, 200, 300, 20};

    public DrunkStatusEffect() {
        super(MobEffectCategory.HARMFUL, new Color(255, 222, 173, 255).getRGB());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    //Value: > 0:Increase drunk amplifier 0 <:Decrease drunk amplifier
    public static void addStatusEffect(LivingEntity user, int value) {
        if (value == 0) {
            return;
        }

        MobEffectInstance statusEffectInstance = user.getEffect(MobEffectRegistry.DRUNK.get());
        int currentDrunkAmplifier = statusEffectInstance == null ? -1 : statusEffectInstance.getAmplifier();
        int newDrunkAmplifier = currentDrunkAmplifier + value;
        newDrunkAmplifier = Math.min(newDrunkAmplifier, MAX_DRUNK_AMPLIFIER);

        if (currentDrunkAmplifier < MIN_DRUNK_AMPLIFIER && newDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            return;
        } else if (currentDrunkAmplifier >= MIN_DRUNK_AMPLIFIER && newDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            user.removeEffect(MobEffectRegistry.DRUNK.get());
        } else if (currentDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            user.addEffect(new MobEffectInstance(MobEffectRegistry.DRUNK.get(), DrunkStatusEffect.getDrunkDuratioin(newDrunkAmplifier), newDrunkAmplifier));
        } else {
            if (newDrunkAmplifier > currentDrunkAmplifier) {
                user.addEffect(new MobEffectInstance(MobEffectRegistry.DRUNK.get(), DrunkStatusEffect.getDrunkDuratioin(newDrunkAmplifier), newDrunkAmplifier));
            } else if (newDrunkAmplifier < currentDrunkAmplifier) {
                int tempDrunkAmplifier = currentDrunkAmplifier - newDrunkAmplifier;
                while (tempDrunkAmplifier > 0) {
                    decreaseDrunkStatusEffefct(user, currentDrunkAmplifier);
                    currentDrunkAmplifier--;
                    tempDrunkAmplifier--;
                }
            }
        }
    }

    public static void addStatusEffect(LivingEntity user) {
        addStatusEffect(user, 1);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        int time = entity.getEffect(MobEffectRegistry.DRUNK.get()).getDuration();
        //Always give harmful status effects
        giveHarmfulStatusEffects(entity, amplifier, time);
        //Give next lower Drunk status effect when duration's out
        if (time == 1) {
            decreaseDrunkStatusEffefct(entity, amplifier);
        }
    }

    private void giveHarmfulStatusEffects(LivingEntity entity, int amplifier, int time) {
        if (amplifier >= MAX_DRUNK_AMPLIFIER) {
            int duration = entity.getEffect(MobEffectRegistry.DRUNK.get()).getDuration();
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, duration, 0, false, visible));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, MAX_DRUNK_AMPLIFIER - 1, false, visible));
        } else if (time % harmulStatusEffectsIntervals[amplifier] == 0) {
            int nauseaDuration = nauseaDurations[amplifier];
            int slownessDuration = slownessDurations[amplifier];
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, nauseaDuration, 0, false, visible));
            if (amplifier > 0) {
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slownessDuration, amplifier - 1, false, visible));
            }
        }
    }

    private static void decreaseDrunkStatusEffefct(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            entity.removeEffect(MobEffectRegistry.DRUNK.get());
            MobEffectInstance nextDrunkStatusEffect = getDecreasedDrunkStatusEffect(amplifier);
            if (nextDrunkStatusEffect != null) {
                entity.addEffect(nextDrunkStatusEffect);
            }
        }
    }

    private static MobEffectInstance getDecreasedDrunkStatusEffect(int currentAmplifier) {
        int nextDrunkAmplifier = currentAmplifier - 1;
        if (nextDrunkAmplifier < MIN_DRUNK_AMPLIFIER) {
            return null;
        } else {
            return new MobEffectInstance(MobEffectRegistry.DRUNK.get(), getDrunkDuratioin(nextDrunkAmplifier), nextDrunkAmplifier);
        }
    }

    public static int getNextDrunkAmplifier(LivingEntity user) {
        MobEffectInstance statusEffectInstance = user.getEffect(MobEffectRegistry.DRUNK.get());
        int drunkAmplifier = statusEffectInstance == null ? -1 : statusEffectInstance.getAmplifier();
        return drunkAmplifier < MAX_DRUNK_AMPLIFIER ? drunkAmplifier + 1 : drunkAmplifier;
    }

    public static int getDrunkDuratioin(int amplifier) {
        try {
            return drunkDurations[amplifier];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Amplifier is out of range");
            return BASE_DURATION;
        }
    }
}
