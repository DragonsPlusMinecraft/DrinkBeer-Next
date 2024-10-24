package lekavar.lma.drinkbeer.networking.client;

import lekavar.lma.drinkbeer.blockentities.TradeBoxBlockEntity;
import lekavar.lma.drinkbeer.gui.TradeBoxMenu;
import lekavar.lma.drinkbeer.networking.RefreshTradeBoxPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ServerPayloadHandler {

    public static void handlePayload(final RefreshTradeBoxPayload data, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
                    ServerPlayer player = (ServerPlayer) ctx.player();
                    if (player == null) {
                        return;
                    }
                    AbstractContainerMenu screenContainer = player.containerMenu;
                    if (screenContainer instanceof TradeBoxMenu) {
                        BlockPos pos = data.pos();
                        if(player.level().hasChunkAt(pos)){
                            TradeBoxBlockEntity tradeboxEntity = (TradeBoxBlockEntity) player.level().getBlockEntity(pos);
                            tradeboxEntity.screenHandler.setTradeboxCooling();
                            tradeboxEntity.setChanged();
                        }
                    }
                })
                .exceptionally(e -> {
                    // Handle exception
                    ctx.disconnect(Component.literal("Refresh Packet of DrinkBeer Refill produced critical error! Please Report!" + e.getMessage()));
                    return null;
                });
    }
}
