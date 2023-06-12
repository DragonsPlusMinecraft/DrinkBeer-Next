package lekavar.lma.drinkbeer.utils;

import net.minecraft.network.FriendlyByteBuf;

public abstract class Message {
    public abstract void fromBytes(FriendlyByteBuf packetBuffer);

    public abstract void toBytes(FriendlyByteBuf packetBuffer);
}
