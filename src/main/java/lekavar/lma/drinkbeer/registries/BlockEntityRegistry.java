package lekavar.lma.drinkbeer.registries;

import lekavar.lma.drinkbeer.DrinkBeer;
import lekavar.lma.drinkbeer.blockentities.BartendingTableBlockEntity;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.blockentities.MixedBeerBlockEntity;
import lekavar.lma.drinkbeer.blockentities.TradeBoxBlockEntity;
import lekavar.lma.drinkbeer.client.renderers.BartendingTableBlockEntityRenderer;
import lekavar.lma.drinkbeer.client.renderers.MixedBeerBlockEntityRenderer;
import lekavar.lma.drinkbeer.managers.MixedBeerManager;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOKC_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DrinkBeer.MOD_ID);
    public static final RegistryObject<BlockEntityType<BeerBarrelBlockEntity>> BEER_BARREL_TILEENTITY = BLOKC_ENTITIES.register("beer_barrel_blockentity", () -> BlockEntityType.Builder.of(BeerBarrelBlockEntity::new, BlockRegistry.BEER_BARREL.get()).build(null));
    public static final RegistryObject<BlockEntityType<BartendingTableBlockEntity>> BARTENDING_TABLE_TILEENTITY = BLOKC_ENTITIES.register("bartending_table_normal_blockentity", () -> BlockEntityType.Builder.of(BartendingTableBlockEntity::new, BlockRegistry.BARTENDING_TABLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<TradeBoxBlockEntity>> TRADE_BOX_TILEENTITY = BLOKC_ENTITIES.register("trade_box_normal_blockentity", () -> BlockEntityType.Builder.of(TradeBoxBlockEntity::new, BlockRegistry.TRADE_BOX.get()).build(null));
    public static final RegistryObject<BlockEntityType<MixedBeerBlockEntity>> MIXED_BEER_TILEENTITY = BLOKC_ENTITIES.register("mixed_beer_blockentity", () -> BlockEntityType.Builder.of(MixedBeerBlockEntity::new, BlockRegistry.MIXED_BEER.get()).build(null));

    public static void registerRenderer(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(BlockEntityRegistry.MIXED_BEER_TILEENTITY.get(), MixedBeerBlockEntityRenderer::new);
            BlockEntityRenderers.register(BlockEntityRegistry.BARTENDING_TABLE_TILEENTITY.get(), BartendingTableBlockEntityRenderer::new);
            ItemProperties.register(ItemRegistry.MIXED_BEER.get(), new ResourceLocation("beer_id"), (stack, level, living, id)
                    -> MixedBeerManager.getBeerId(stack) / 100.0f);
        });
    }
}
