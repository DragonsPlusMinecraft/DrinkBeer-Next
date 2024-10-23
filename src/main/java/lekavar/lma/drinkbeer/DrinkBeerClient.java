package lekavar.lma.drinkbeer;

import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import lekavar.lma.drinkbeer.registries.MenuTypeRegistry;
import lekavar.lma.drinkbeer.registries.ParticleTypeRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = "drinkbeer", dist = Dist.CLIENT)
public class DrinkBeerClient {

    public DrinkBeerClient(IEventBus modEventBus) {
        modEventBus.addListener(BlockEntityRegistry::registerRenderer);
        modEventBus.addListener(ParticleTypeRegistry::registerParticleProvider);
        modEventBus.addListener(MenuTypeRegistry::registerContainerScreen);
    }

}