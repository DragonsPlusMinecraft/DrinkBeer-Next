package lekavar.lma.drinkbeer.networking;

import lekavar.lma.drinkbeer.blockentities.TradeBoxBlockEntity;
import lekavar.lma.drinkbeer.gui.TradeBoxMenu;
import lekavar.lma.drinkbeer.networking.messages.RefreshTradeBoxMessage;
import lekavar.lma.drinkbeer.registries.NetworkingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NetWorking {

    public static void init(FMLCommonSetupEvent event) {
        NetworkingRegistry.MAIN.registerMessage(
                NetworkingRegistry.SEND_REFRESH_TRADEBOX,
                RefreshTradeBoxMessage.class,
                RefreshTradeBoxMessage::toBytes,
                RefreshTradeBoxMessage::fromBytes,
                (RefreshTradeBoxMessage msg, Supplier<NetworkEvent.Context> ctx) -> {
                    ctx.get().enqueueWork(()->{
                        ServerPlayer player = ctx.get().getSender();
                        if (player == null) {
                            return;
                        }
                        AbstractContainerMenu screenContainer = player.containerMenu;
                        if (screenContainer instanceof TradeBoxMenu) {
                            BlockPos pos = msg.getPos();
                            if(player.level().hasChunkAt(pos)){
                                TradeBoxBlockEntity tradeboxEntity = (TradeBoxBlockEntity) player.level().getBlockEntity(pos);
                                tradeboxEntity.screenHandler.setTradeboxCooling();
                                tradeboxEntity.setChanged();
                            }
                        }
                    });
                    ctx.get().setPacketHandled(true);
                });
    }

    public static void sendRefreshTradebox(BlockPos pos) {
        NetworkingRegistry.MAIN.sendToServer(
                new RefreshTradeBoxMessage(pos)
        );
    }
}
