package ace.actually.dataplanets.compat;

import ace.actually.dataplanets.compat.gcyr.GCYRCompat;
import argent_matter.gcyr.common.data.GCYRBiomes;
import argent_matter.gcyr.common.data.GCYRDimensionTypes;
import argent_matter.gcyr.common.worldgen.SpaceLevelSource;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;

public class Compat {

    public static  String[] SURFACE_BLOCKS;
    public static String COMPAT_MOD = "";
    public static ResourceKey<DimensionType> SPACE_DIMENSION_TYPE;
    public static ResourceKey<Biome> SPACE_BIOME;

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
            COMPAT_MOD="gcyr";
            SPACE_DIMENSION_TYPE = GCYRDimensionTypes.SPACE_TYPE;
            SPACE_BIOME = GCYRBiomes.SPACE;
            SURFACE_BLOCKS = new String[]{"gcyr:moon_stone","gcyr:moon_cobblestone","gcyr:venusian_regolith","gcyr:martian_rock"};
        }
    }

    public static ChunkGenerator spaceGenerator(Holder.Reference<Biome> biomeHolder)
    {
        if(COMPAT_MOD.equals("gcyr"))
        {
            return GCYRCompat.chunkGenerator(biomeHolder);
        }
        return null;
    }

    public static void postLoadGame()
    {
        if(COMPAT_MOD.equals("gcyr"))
        {
            GCYRCompat.postLoadGame();
        }
    }
}
