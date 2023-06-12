package lekavar.lma.drinkbeer.networking;

import lekavar.lma.drinkbeer.blockentities.TradeBoxBlockEntity;
import lekavar.lma.drinkbeer.gui.TradeBoxMenu;
import lekavar.lma.drinkbeer.networking.messages.RefreshTradeBoxMessage;
import lekavar.lma.drinkbeer.registries.NetworkingRegistry;
import lekavar.lma.drinkbeer.utils.ChannelHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NetWorking {

    public static void init(FMLCommonSetupEvent event) {
        ChannelHelper.RegisterMessage(
                NetworkingRegistry.SEND_REFRESH_TRADEBOX,
                RefreshTradeBoxMessage.class,
                (RefreshTradeBoxMessage msg, Supplier<NetworkEvent.Context> ctx) -> {
                    NetworkEvent.Context context = ctx.get();
                    ServerPlayer player = context.getSender();
                    if (player == null) {
                        return;
                    }
                    AbstractContainerMenu screenContainer = player.containerMenu;
                    if (screenContainer instanceof TradeBoxMenu) {
                        BlockPos pos = msg.getPos();
                        context.enqueueWork(() -> {
                            TradeBoxBlockEntity tradeboxEntity = (TradeBoxBlockEntity) player.level().getBlockEntity(pos);
                            tradeboxEntity.screenHandler.setTradeboxCooling();
                        });
                    }
                });
    }

    public static void sendRefreshTradebox(BlockPos pos) {
        ChannelHelper.DEFAULT_CHANNEL.sendToServer(
                new RefreshTradeBoxMessage(pos)
        );
    }
}
