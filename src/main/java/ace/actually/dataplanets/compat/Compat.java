package ace.actually.dataplanets.compat;

import ace.actually.dataplanets.DPPackets;
import ace.actually.dataplanets.space.S2PTranslationPacket;
import ace.actually.dataplanets.space.StarSystemCreator;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
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

    }

    /**
     * this method is called after a planet's level stem has been re/created
     * @param planetData
     */
    public static void postLoadPlanet(CompoundTag planetData)
    {

    }

    public static void loadCompat(String compatmod)
    {
        SURFACE_BLOCKS = new String[]{"minecraft:stone","minecraft:cobblestone","minecraft:end_stone","minecraft:netherrack","minecraft:blackstone","minecraft:sandstone","minecraft:red_sandstone","minecraft:basalt"};
        SPACE_BIOME = Biomes.END_BARRENS;
        SPACE_DIMENSION_TYPE = BuiltinDimensionTypes.END;
    }

    /**
     * this method is called when building an orbit of a planet
     * @param biomeHolder
     * @return
     */
    public static ChunkGenerator spaceGenerator(Holder.Reference<Biome> biomeHolder)
    {

        return new NoiseBasedChunkGenerator(new FixedBiomeSource(biomeHolder),Holder.direct(NoiseGeneratorSettings.dummy()));
    }

    /**
     * this method is called immediately after first discovering a system, when all chunks are saved, and whenever a player logs in
     */
    public static void postLoadWorld()
    {
        DPPackets.INSTANCE.send(PacketDistributor.ALL.noArg(),new S2PTranslationPacket(StarSystemCreator.getDynamicDataOrNew()));
    }
}
