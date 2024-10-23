package lekavar.lma.drinkbeer;

import lekavar.lma.drinkbeer.managers.MixedBeerManager;
import lekavar.lma.drinkbeer.registries.BlockEntityRegistry;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.MenuTypeRegistry;
import lekavar.lma.drinkbeer.registries.ParticleTypeRegistry;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = DrinkBeer.MOD_ID, dist = Dist.CLIENT)
public class DrinkBeerClient {

    public DrinkBeerClient(IEventBus modEventBus) {
        modEventBus.addListener(BlockEntityRegistry::registerRenderer);
        modEventBus.addListener(ParticleTypeRegistry::registerParticleProvider);
        modEventBus.addListener(MenuTypeRegistry::registerContainerScreen);
        modEventBus.addListener(DrinkBeerClient::registerItemPropertiesOverride);
    }

    private static void registerItemPropertiesOverride(FMLClientSetupEvent event){
        event.enqueueWork(()->{
            ItemProperties.register(ItemRegistry.MIXED_BEER.get(), ResourceLocation.withDefaultNamespace("beer_id"), (stack, level, living, id)
                    -> MixedBeerManager.getBeerId(stack) / 100.0f);
        });
    }

}