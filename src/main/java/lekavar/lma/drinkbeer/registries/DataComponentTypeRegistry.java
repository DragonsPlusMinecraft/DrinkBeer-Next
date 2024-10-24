package lekavar.lma.drinkbeer.registries;

import com.mojang.serialization.Codec;
import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DataComponentTypeRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, DrinkBeer.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> BEER_ID_COMPONENT = DATA_COMPONENTS.registerComponentType(
            "beer_id", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
    );
}
