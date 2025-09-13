package ace.actually.dataplanets.compat.gcyr;

import ace.actually.dataplanets.space.DynamicSystems;
import ace.actually.dataplanets.space.Planets;
import argent_matter.gcyr.GCYRClient;
import argent_matter.gcyr.api.space.planet.PlanetRing;
import argent_matter.gcyr.api.space.planet.PlanetSkyRenderer;
import argent_matter.gcyr.api.space.planet.SolarSystem;
import argent_matter.gcyr.client.dimension.ClientModSkies;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class GCYRPacket {

    CompoundTag data;
    public GCYRPacket(CompoundTag data)
    {
        this.data=data;
    }

    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeNbt(data);
    }

    public static GCYRPacket decoder(FriendlyByteBuf buffer) {
        return new GCYRPacket(buffer.readAnySizeNbt());
    }

    public void messageConsumer(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            for(String sys: data.getAllKeys())
            {
                if(data.getTagType(sys)== Tag.TAG_COMPOUND)
                {
                    //System.out.println(sys);
                    CompoundTag systemCompound = data.getCompound(sys);
                    SolarSystem system = new SolarSystem(
                            ResourceLocation.tryParse("gcyr:milky_way"),
                            ResourceLocation.tryParse("dataplanets:"+systemCompound.getString("systemName")),
                            ResourceLocation.tryParse("gcyr:textures/sky/sunmenu.png"),
                            60,
                            428486115,
                            335781541);
                    GCYRClient.solarSystems.add(system);
                    GCYRClient.solarSystems.sort(Comparator.comparing(SolarSystem::solarSystem));

                    for(String key: systemCompound.getAllKeys())
                    {
                        if(systemCompound.getTagType(key)== Tag.TAG_COMPOUND)
                        {
                            CompoundTag planetData = systemCompound.getCompound(key);
                            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION,
                                    ResourceLocation.fromNamespaceAndPath("dataplanets",planetData.getString("name")));
                            ResourceKey<Level> orbitDimension = ResourceKey.create(Registries.DIMENSION,
                                    ResourceLocation.fromNamespaceAndPath("dataplanets",planetData.getString("name")+"_orbit"));
                            PlanetSkyRenderer.StarsRenderer starsRenderer = new PlanetSkyRenderer.StarsRenderer(13000,6000,true,true);
                            PlanetSkyRenderer.DimensionEffects dimensionEffects = new PlanetSkyRenderer.DimensionEffects(PlanetSkyRenderer.DimensionEffectType.NONE,0);
                            PlanetSkyRenderer.CloudEffects cloudEffects = PlanetSkyRenderer.CloudEffects.NONE;
                            PlanetSkyRenderer.WeatherEffects weatherEffects = PlanetSkyRenderer.WeatherEffects.NONE;

                            PlanetRing ring = new PlanetRing(
                                    ResourceLocation.tryParse("gcyr:milky_way"),
                                    ResourceLocation.tryParse("dataplanets:"+systemCompound.getString("systemName")),
                                    ResourceLocation.tryParse("gcyr:textures/sky/luna.png"),
                                    planetData.getInt("yearsDays"),
                                    planetData.getInt("scaleClient"),
                                    planetData.getFloat("radiusClient"));

                            PlanetSkyRenderer.SkyObject sun = new PlanetSkyRenderer.SkyObject(
                                    ResourceLocation.tryParse("gcyr:textures/sky/sun.png"),
                                    true,
                                    PlanetSkyRenderer.RenderType.DYNAMIC,
                                    planetData.getInt("solarPower")+1,
                                    16777164,
                                    new Vector3f(0,-90,0));

                            PlanetSkyRenderer skyRenderer = new PlanetSkyRenderer(
                                    dimension,
                                    Optional.empty(),
                                    starsRenderer,
                                    dimensionEffects,
                                    cloudEffects,
                                    weatherEffects,
                                    1,
                                    true,
                                    List.of(sun));

                            PlanetSkyRenderer.SkyObject orbitPlanet = new PlanetSkyRenderer.SkyObject(
                                    ResourceLocation.tryParse("gcyr:textures/sky/mercury.png"),
                                    true,
                                    PlanetSkyRenderer.RenderType.DYNAMIC,
                                    planetData.getInt("solarPower")+2,
                                    16777164,
                                    new Vector3f(0,0,0));

                            PlanetSkyRenderer.SkyObject orbitSun = new PlanetSkyRenderer.SkyObject(
                                    ResourceLocation.tryParse("gcyr:textures/sky/sun.png"),
                                    true,
                                    PlanetSkyRenderer.RenderType.STATIC,
                                    15,
                                    16777164,
                                    new Vector3f(0,0,0));
                            PlanetSkyRenderer.SkyObject orbitLight = new PlanetSkyRenderer.SkyObject(
                                    ResourceLocation.tryParse("gcyr:textures/sky/light.png"),
                                    true,
                                    PlanetSkyRenderer.RenderType.STATIC,
                                    20,
                                    16777164,
                                    new Vector3f(0,0,0));

                            PlanetSkyRenderer orbitRenderer = new PlanetSkyRenderer(
                                    orbitDimension,
                                    Optional.empty(),
                                    starsRenderer,
                                    dimensionEffects,
                                    cloudEffects,
                                    weatherEffects,
                                    0,
                                    true,
                                    List.of(orbitPlanet,orbitSun,orbitLight));

                            GCYRClient.skyRenderers.add(skyRenderer);
                            GCYRClient.skyRenderers.add(orbitRenderer);
                            GCYRClient.planetRings.add(ring);
                            GCYRClient.hasUpdatedPlanets=true;
                            ClientModSkies.register();
                        }
                    }
                }

            }




        });
        ctx.get().setPacketHandled(true);
    }


}
