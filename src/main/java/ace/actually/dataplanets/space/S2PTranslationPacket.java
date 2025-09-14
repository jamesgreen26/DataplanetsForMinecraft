package ace.actually.dataplanets.space;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2PTranslationPacket {

    CompoundTag data;
    public S2PTranslationPacket(CompoundTag data)
    {
        this.data=data;
    }

    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeNbt(data);
    }

    public static S2PTranslationPacket decoder(FriendlyByteBuf buffer) {
        return new S2PTranslationPacket(buffer.readAnySizeNbt());
    }

    public void messageConsumer(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            for(String sys: data.getAllKeys())
            {
                if(data.getTagType(sys)== Tag.TAG_COMPOUND)
                {
                    //System.out.println(sys);
                    CompoundTag systemCompound = data.getCompound(sys);

                    for(String key: systemCompound.getAllKeys())
                    {
                        if(systemCompound.getTagType(key)== Tag.TAG_COMPOUND)
                        {
                            CompoundTag planetData = systemCompound.getCompound(key);

                            DynamicSystems.TRANSLATIONS.put("level."+planetData.getString("name"), Planets.fancyName(planetData.getString("name")));
                            DynamicSystems.TRANSLATIONS.put("dataplanets."+systemCompound.getString("systemName"), Planets.fancyName(systemCompound.getString("systemName")));
                        }
                    }
                }

            }




        });
        ctx.get().setPacketHandled(true);
    }
}
