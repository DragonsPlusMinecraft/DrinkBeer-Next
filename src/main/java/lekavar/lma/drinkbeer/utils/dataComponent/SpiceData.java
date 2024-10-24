package lekavar.lma.drinkbeer.utils.dataComponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record SpiceData(int spiceA, int spiceB, int spiceC) {
    public static final Codec<SpiceData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("spiceA").forGetter(SpiceData::spiceA),
                    Codec.INT.fieldOf("spiceB").forGetter(SpiceData::spiceB),
                    Codec.INT.fieldOf("spiceC").forGetter(SpiceData::spiceC)
            ).apply(instance, SpiceData::new)
    );
    public static final StreamCodec<ByteBuf, SpiceData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SpiceData::spiceA,
            ByteBufCodecs.INT, SpiceData::spiceB,
            ByteBufCodecs.INT, SpiceData::spiceC,
            SpiceData::new
    );

    public static SpiceData fromSpiceList(List<Integer> spiceList){
        int a = 0, b = 0, c = 0;
        if(spiceList.get(0)>0){
            a = spiceList.get(0);
            if(spiceList.get(1)>0){
                b = spiceList.get(1);
                if(spiceList.get(2)>0){
                    c = spiceList.get(2);
                }
            }
        }
        return new SpiceData(a,b,c);
    }

}
