package lekavar.lma.drinkbeer.entities.damages;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class AlcoholDamage extends DamageSource {
    public static DamageType ALCOHOL = new DamageType("drinkbeer.alcohol", 0);

    public AlcoholDamage() {
        super(Holder.direct(ALCOHOL));
    }
}
