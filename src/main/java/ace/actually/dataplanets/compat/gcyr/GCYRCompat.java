package ace.actually.dataplanets.compat.gcyr;

import ace.actually.dataplanets.DPPackets;
import ace.actually.dataplanets.space.DynamicSystems;
import ace.actually.dataplanets.space.StarSystemCreator;
import argent_matter.gcyr.api.space.planet.Planet;
import argent_matter.gcyr.common.worldgen.SpaceLevelSource;
import argent_matter.gcyr.data.loader.PlanetData;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GCYRCompat {

    public static void postLoadPlanet(CompoundTag planetData)
    {
        ResourceKey<Level> dimkey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryBuild("dataplanets",planetData.getString("name")));
        ResourceKey<Level> orbitKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_orbit"));

        Planet planet = new Planet(
                "level."+planetData.getString("name"),
                ResourceLocation.tryParse("gcyr:milky_way"),
                ResourceLocation.fromNamespaceAndPath("dataplanets", DynamicSystems.planetStarName(planetData)),
                dimkey,
                orbitKey,
                Optional.empty(),
                2,
                planetData.getInt("gravity"),
                planetData.getBoolean("hasAtmosphere"),
                planetData.getInt("yearDays"),
                planetData.getInt("temperature"),
                planetData.getInt("solarPower"),
                planetData.getBoolean("hasOxygen"),
                893717);
        Map<ResourceLocation,Planet> planets = new HashMap<>(Map.copyOf(PlanetData.planets()));
        planets.put(ResourceLocation.tryBuild("dataplanets",planetData.getString("name")),planet);
        PlanetData.updatePlanets(planets);
    }

    public static ChunkGenerator chunkGenerator(Holder.Reference<Biome> biomeHolder)
    {
        return new SpaceLevelSource(biomeHolder);
    }
    public static void postLoadWorld()
    {
        DPPackets.INSTANCE.send(PacketDistributor.ALL.noArg(),new GCYRPacket(StarSystemCreator.getDynamicDataOrNew()));
    }
}
