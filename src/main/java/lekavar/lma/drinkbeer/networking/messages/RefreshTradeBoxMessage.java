package lekavar.lma.drinkbeer.networking.messages;

import lekavar.lma.drinkbeer.utils.Message;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class RefreshTradeBoxMessage extends Message {
    private BlockPos pos;

    public RefreshTradeBoxMessage(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public void fromBytes(FriendlyByteBuf packetBuffer) {
        pos = packetBuffer.readBlockPos();
    }

    @Override
    public void toBytes(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeBlockPos(pos);
    }
}
