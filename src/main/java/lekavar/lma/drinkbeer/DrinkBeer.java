package lekavar.lma.drinkbeer;

import com.mojang.logging.LogUtils;
import lekavar.lma.drinkbeer.compat.ModCompat;
import lekavar.lma.drinkbeer.networking.NetWorking;
import lekavar.lma.drinkbeer.registries.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DrinkBeer.MOD_ID)
public class DrinkBeer {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "drinkbeer";

    public DrinkBeer(IEventBus modEventBus, ModContainer container) {;

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

        modEventBus.addListener(NetWorking::init);

        var gameEventBus = NeoForge.EVENT_BUS;

        gameEventBus.addListener(ModCompat::injectRecipes);
    }

}
