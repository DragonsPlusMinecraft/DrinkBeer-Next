package lekavar.lma.drinkbeer.compat.jade;

import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.blockentities.TradeBoxBlockEntity;
import lekavar.lma.drinkbeer.blocks.BeerBarrelBlock;
import lekavar.lma.drinkbeer.blocks.MixedBeerBlock;
import lekavar.lma.drinkbeer.blocks.TradeboxBlock;
import lekavar.lma.drinkbeer.compat.jade.provider.BeerBarrelComponentProvider;
import lekavar.lma.drinkbeer.compat.jade.provider.MixedBeerComponentProvider;
import lekavar.lma.drinkbeer.compat.jade.provider.TradeBoxComponentProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(BeerBarrelComponentProvider.INSTANCE, BeerBarrelBlock.class);
        registration.registerBlockComponent(TradeBoxComponentProvider.INSTANCE, TradeboxBlock.class);
        registration.registerBlockComponent(MixedBeerComponentProvider.INSTANCE, MixedBeerBlock.class);

        registration.registerBlockIcon(MixedBeerComponentProvider.INSTANCE, MixedBeerBlock.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(BeerBarrelComponentProvider.INSTANCE, BeerBarrelBlockEntity.class);
        registration.registerBlockDataProvider(TradeBoxComponentProvider.INSTANCE, TradeBoxBlockEntity.class);
    }
}
