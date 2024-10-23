package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.resources.ResourceLocation;

public class NetworkingRegistry {
    private static final String PROTOCOL_VERSION = "1";

    public static final int SEND_REFRESH_TRADEBOX = 0;

    public static final SimpleChannel MAIN = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(DrinkBeer.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
}
