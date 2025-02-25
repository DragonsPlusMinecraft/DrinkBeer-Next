package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.effects.DrunkFrostWalkerStatusEffect;
import lekavar.lma.drinkbeer.effects.DrunkStatusEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;


public class MobEffectRegistry {
    public static final DeferredRegister<MobEffect> STATUS_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, DrinkBeer.MOD_ID);
    public static final Holder<MobEffect> DRUNK_FROST_WALKER = STATUS_EFFECTS.register("drunk_frost_walker", DrunkFrostWalkerStatusEffect::new);
    public static final Holder<MobEffect> DRUNK = STATUS_EFFECTS.register("drunk", DrunkStatusEffect::new);
}

