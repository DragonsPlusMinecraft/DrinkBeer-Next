package lekavar.lma.drinkbeer;

import lekavar.lma.drinkbeer.networking.NetWorking;
import lekavar.lma.drinkbeer.registries.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(DrinkBeer.MOD_ID)
public class DrinkBeer {

    public static final String MOD_ID = "drinkbeer";

    public DrinkBeer(IEventBus modEventBus) {;

        MobEffectRegistry.STATUS_EFFECTS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        BlockEntityRegistry.BLOKC_ENTITIES.register(modEventBus);
        SoundEventRegistry.SOUNDS.register(modEventBus);
        MenuTypeRegistry.MENUS.register(modEventBus);
        RecipeRegistry.RECIPE_TYPES.register(modEventBus);
        RecipeRegistry.RECIPE_SERIALIZERS.register(modEventBus);
        ParticleTypeRegistry.PARTICLES.register(modEventBus);
        CreativeTabRegistry.TABS.register(modEventBus);

        modEventBus.addListener(CapabilityRegistry::registerCapabilities);

        modEventBus.addListener(NetWorking::init);
    }

}
