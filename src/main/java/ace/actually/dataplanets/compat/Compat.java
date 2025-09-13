package ace.actually.dataplanets.compat;

import ace.actually.dataplanets.DPPackets;
import ace.actually.dataplanets.compat.gcyr.GCYRCompat;
import ace.actually.dataplanets.compat.gtceu.GTCEUCompat;
import ace.actually.dataplanets.space.S2PTranslationPacket;
import ace.actually.dataplanets.space.StarSystemCreator;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.PacketDistributor;

public class Compat {

    public static  String[] SURFACE_BLOCKS;
    public static String COMPAT_MOD = "";
    public static ResourceKey<DimensionType> SPACE_DIMENSION_TYPE;
    public static ResourceKey<Biome> SPACE_BIOME;

    /**
     * This method is called when Dataplanets is initialized
     * @param bus
     */
    public static void modEventBusLoad(IEventBus bus)
    {
        if(COMPAT_MOD.equals("gcyr"))
        {
            GTCEUCompat.doRegister(bus);
        }
    }

    /**
     * this method is called after a planet's level stem has been re/created
     * @param planetData
     */
    public static void postLoadPlanet(CompoundTag planetData)
    {
        if(COMPAT_MOD.equals("gcyr"))
        {
            GCYRCompat.postLoadPlanet(planetData);
        }
    }

    public static void loadCompat(String compatmod)
    {
        if(compatmod.equals("gcyr"))
        {
            GCYRCompat.loadCompat();
        }
    }

    /**
     * this method is called when building an orbit of a planet
     * @param biomeHolder
     * @return
     */
    public static ChunkGenerator spaceGenerator(Holder.Reference<Biome> biomeHolder)
    {
        if(COMPAT_MOD.equals("gcyr"))
        {
            return GCYRCompat.chunkGenerator(biomeHolder);
        }
        return null;
    }

    /**
     * this method is called immediately after first discovering a system, when all chunks are saved, and whenever a player logs in
     */
    public static void postLoadWorld()
    {
        if(COMPAT_MOD.equals("gcyr"))
        {
            GCYRCompat.postLoadWorld();
        }
        DPPackets.INSTANCE.send(PacketDistributor.ALL.noArg(),new S2PTranslationPacket(StarSystemCreator.getDynamicDataOrNew()));
    }
}
