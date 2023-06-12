package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DrinkBeer.MOD_ID);
    public static final RegistryObject<ParticleType<SimpleParticleType>> MIXED_BEER_DEFAULT = PARTICLES.register("mixed_beer_default", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<SimpleParticleType>> CALL_BELL_TINKLE_PAW = PARTICLES.register("call_bell_tinkle_paw", () -> new SimpleParticleType(true));

    public static void registerParticleProvider(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.MIXED_BEER_DEFAULT.get(), FlameParticle.Provider::new);
        Minecraft.getInstance().particleEngine.register(ParticleTypeRegistry.CALL_BELL_TINKLE_PAW.get(), HeartParticle.AngryVillagerProvider::new);
    }
}
