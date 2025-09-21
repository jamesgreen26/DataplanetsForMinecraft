package ace.actually.dataplanets.space;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.awt.*;
import java.util.function.Supplier;

public class S2PSyncPacket {

    CompoundTag data;
    public S2PSyncPacket(CompoundTag data)
    {
        this.data=data;
    }

    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeNbt(data);
    }

    public static S2PSyncPacket decoder(FriendlyByteBuf buffer) {
        return new S2PSyncPacket(buffer.readAnySizeNbt());
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

                            ListTag biomes = planetData.getList("biomes", ListTag.TAG_COMPOUND);
                            for (int i = 0; i < biomes.size(); i++) {
                                CompoundTag tag = biomes.getCompound(i);
                                int waterColour = tag.getInt("waterColour");
                                Color c = new Color(waterColour);
                                DynamicSystems.RAIN_COLOUR.put(tag.getString("name"),new float[]{c.getRed()/255f,c.getGreen()/255f,c.getBlue()/255f,c.getAlpha()/255f});
                            }
                        }
                    }
                }

            }




        });
        ctx.get().setPacketHandled(true);
    }
}
