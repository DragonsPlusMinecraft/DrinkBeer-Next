package lekavar.lma.drinkbeer;

import com.mojang.logging.LogUtils;
import lekavar.lma.drinkbeer.compat.ModCompat;
import lekavar.lma.drinkbeer.networking.NetWorking;
import lekavar.lma.drinkbeer.registries.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("drinkbeer")
public class DrinkBeer {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "drinkbeer";

    public DrinkBeer() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

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

        forgeEventBus.addListener(ModCompat::injectRecipes);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DrinkBeerClient::new);
    }

}
