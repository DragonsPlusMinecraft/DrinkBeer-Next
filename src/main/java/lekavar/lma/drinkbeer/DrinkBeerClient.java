package lekavar.lma.drinkbeer;

import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import lekavar.lma.drinkbeer.registries.ParticleTypeRegistry;
import lekavar.lma.drinkbeer.utils.ModCreativeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class DrinkBeerClient {

    public DrinkBeerClient() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(BlockEntityRegistry::registerRenderer);
        modEventBus.addListener(ParticleTypeRegistry::registerParticleProvider);
        modEventBus.addListener(ModCreativeTab::register);
    }

}