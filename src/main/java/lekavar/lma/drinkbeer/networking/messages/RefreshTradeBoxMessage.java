package lekavar.lma.drinkbeer.networking.messages;

import lekavar.lma.drinkbeer.utils.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class RefreshTradeBoxMessage {
    private BlockPos pos;

    public RefreshTradeBoxMessage(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public static RefreshTradeBoxMessage fromBytes(FriendlyByteBuf packetBuffer) {
        return new RefreshTradeBoxMessage(packetBuffer.readBlockPos());
    }

    public static void toBytes(RefreshTradeBoxMessage message,FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(message.pos);
    }
}
