package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class SoundEventRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, DrinkBeer.MOD_ID);
    public static final Supplier<SoundEvent> DRINKING_BEER = register("drinking_beer");
    public static final Supplier<SoundEvent> POURING = register("pouring");
    public static final Supplier<SoundEvent> POURING_CHRISTMAS = register("pouring_christmas");
    public static final Supplier<SoundEvent> IRON_CALL_BELL_TINKLING = register("iron_call_bell_tinkle");
    public static final Supplier<SoundEvent> GOLDEN_CALL_BELL_TINKLING = register("golden_call_bell_tinkle");
    public static final Supplier<SoundEvent> LEKAS_CALL_BELL_TINKLE = register("lekas_call_bell_tinkle");
    public static final Supplier<SoundEvent>[] NIGHT_HOWL = new Supplier[]{register("night_howl0"), register("night_howl1"), register("night_howl2"), register("night_howl3")};

    public static final Supplier<SoundEvent> UNPACKING = register("unpacking");
    public static final Supplier<SoundEvent> BARTENDING_TABLE_OPEN = register("bartending_table_open");
    public static final Supplier<SoundEvent> BARTENDING_TABLE_CLOSE = register("bartending_table_close");
    public static final Supplier<SoundEvent> TRADEBOX_OPEN = register("tradebox_open");
    public static final Supplier<SoundEvent> TRADEBOX_CLOSE = register("tradebox_close");


    private static Supplier<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(DrinkBeer.MOD_ID, name)));
    }
}
