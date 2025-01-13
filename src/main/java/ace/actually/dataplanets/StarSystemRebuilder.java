package ace.actually.dataplanets;

import ace.actually.dataplanets.interfaces.IUnfreezableRegistry;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class StarSystemRebuilder {
    private static ResourceKey<Biome> planetDataToBiome(MinecraftServer server, CompoundTag planetData)
    {
        RegistryAccess.Frozen registryAccess = server.registryAccess();

        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);

        Registry<ConfiguredWorldCarver<?>> carversRegistry = server.registryAccess().registryOrThrow(Registries.CONFIGURED_CARVER);
        Holder.Reference<ConfiguredWorldCarver<?>> canyon = carversRegistry.getHolderOrThrow(Carvers.CANYON);
        Holder.Reference<ConfiguredWorldCarver<?>> cave = carversRegistry.getHolderOrThrow(Carvers.CAVE);
        Holder.Reference<ConfiguredWorldCarver<?>> cave_extra = carversRegistry.getHolderOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND);

        Registry<PlacedFeature> placedFeatureRegistry = server.registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
        Holder.Reference<PlacedFeature> dripstone = placedFeatureRegistry.getHolder(ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryParse("large_dripstone"))).get();
        Holder.Reference<PlacedFeature> dripstone_cluster = placedFeatureRegistry.getHolder(ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.tryParse("dripstone_cluster"))).get();
        Holder.Reference<PlacedFeature> pointed_dripstone = placedFeatureRegistry.getHolder(ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.tryParse("pointed_dripstone"))).get();

        Biome biome = new Biome.BiomeBuilder()
                .downfall(0)
                .temperature(0.93f)
                .hasPrecipitation(false)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .skyColor(planetData.getInt("skyColour"))
                        .fogColor(planetData.getInt("fogColour"))
                        .waterColor(planetData.getInt("waterColour"))
                        .waterFogColor(planetData.getInt("waterFogColour"))
                        .grassColorOverride(planetData.getInt("grassColour"))
                        .foliageColorOverride(planetData.getInt("foliage")).build())
                .mobSpawnSettings(new MobSpawnSettings.Builder().build())
                .generationSettings(new BiomeGenerationSettingsBuilder(BiomeGenerationSettings.EMPTY)
                        .addCarver(GenerationStep.Carving.AIR,canyon)
                        .addCarver(GenerationStep.Carving.AIR,cave)
                        .addCarver(GenerationStep.Carving.AIR,cave_extra)
                        .addFeature(0,dripstone)
                        .addFeature(0,dripstone_cluster)
                        .addFeature(0,pointed_dripstone).build()).build();



        ResourceKey<Biome> biomeKey = ResourceKey.create(biomeRegistry.key(), ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_terrain"));


        ((IUnfreezableRegistry) biomeRegistry).setRegFrozen(false);
        ((MappedRegistry<Biome>) biomeRegistry).register(
                biomeKey,
                biome,
                Lifecycle.experimental() // use built-in registration info for now
        );
        ((IUnfreezableRegistry) biomeRegistry).setRegFrozen(true);

        return biomeKey;
    }

    public static void buildRPFromData(CompoundTag tagIn)
    {
        String LOCLOC = Path.of("./").toAbsolutePath()+"\\resourcepacks\\";

        for(String system: tagIn.getAllKeys())
        {
            CompoundTag tag = tagIn.getCompound(system);

            String uuid = UUID.randomUUID().toString();
            String systemName = tag.getString("systemName");
            List<String> solar_system = List.of("{\n" +
                    "  \"galaxy\": \"gcyr:milky_way\",\n" +
                    "  \"solar_system\": \"dataplanets:SW\",\n".replace("SW",systemName) +
                    "  \"sun\": \"gcyr:textures/sky/sunmenu.png\",\n" +
                    "  \"sun_scale\": 60,\n" +
                    "  \"button_color\": 4284861158,\n" +
                    "  \"ring_color\": 3357815419\n" +
                    "}");



            try {
                FileUtils.writeLines(new File(LOCLOC+"pack"+uuid+"\\assets\\dataplanets\\gcyr\\planet_assets\\solar_systems\\"+systemName+".json"),solar_system);
                FileUtils.writeLines(new File(LOCLOC+"pack"+uuid+"\\pack.mcmeta"), List.of("""
                    {
                    \t"pack": {
                    \t\t"description": "Generated by Dataplanets",
                    \t\t"pack_format": 34
                    \t}
                    }"""));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            StringBuilder langFile = new StringBuilder("{\"dataplanets.").append(systemName).append("\":\"");
            String rebuild = systemName.toUpperCase();
            rebuild = rebuild.substring(0,2)+" "+rebuild.substring(2);
            langFile.append(rebuild).append("\",");
            for(String key: tag.getAllKeys())
            {
                if(!tag.getCompound(key).isEmpty())
                {
                    CompoundTag planetData = tag.getCompound(key);
                    String planetName = planetData.getString("name");
                    List<String> planet_rings = List.of("{\n" +
                            "  \"galaxy\": \"gcyr:milky_way\",\n" +
                            "  \"solar_system\": \"dataplanets:SOMEWHERE\",\n".replace("SOMEWHERE",systemName) +
                            "  \"texture\": \"gcyr:textures/sky/deimos.png\",\n" +
                            "  \"speed\": S,\n".replace("S",""+planetData.getInt("yearDays")) +
                            "  \"scale\": S,\n".replace("S",""+planetData.getInt("scaleClient")) +
                            "  \"radius\": X\n".replace("X",""+planetData.getFloat("radiusClient")) +
                            "}");

                    List<String> sky_renderer = List.of("{\n" +
                            "  \"world\": \"dataplanets:SW\",\n".replace("SW",planetName) +
                            "  \"stars\": {\n" +
                            "    \"fancy_count\": 13000,\n" +
                            "    \"fast_count\": 6000,\n" +
                            "    \"colored_stars\": true,\n" +
                            "    \"daylight_visible\": true\n" +
                            "  },\n" +
                            "  \"sunset_color\": \"none\",\n" +
                            "  \"dimension_effects\": {\n" +
                            "    \"type\": \"none\"\n" +
                            "  },\n" +
                            "  \"cloud_effects\": \"none\",\n" +
                            "  \"weather_effects\": \"none\",\n" +
                            "  \"horizon_angle\": 0,\n" +
                            "  \"sky_objects\": [\n" +
                            "    {\n" +
                            "      \"texture\": \"gcyr:textures/sky/sun.png\",\n" +
                            "      \"blending\": true,\n" +
                            "      \"render_type\": \"dynamic\",\n" +
                            "      \"scale\": SZ,\n".replace("SZ",""+(planetData.getInt("solarPower")+1)) +
                            "      \"rotation\": [\n" +
                            "        0.0,\n" +
                            "        -90.0,\n" +
                            "        0.0\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}");

                    langFile.append("\"level.").append(planetName).append("\":\"");
                    rebuild = planetName.toUpperCase();
                    rebuild = rebuild.substring(0,2)+" "+rebuild.substring(2,rebuild.length()-1)+" "+rebuild.toLowerCase().charAt(rebuild.length()-1);
                    langFile.append(rebuild).append("\",");

                    try {
                        FileUtils.writeLines(new File(LOCLOC+"pack"+uuid+"\\assets\\dataplanets\\gcyr\\planet_assets\\planet_rings\\"+systemName+"\\"+planetName+".json"),planet_rings);
                        FileUtils.writeLines(new File(LOCLOC+"pack"+uuid+"\\assets\\dataplanets\\gcyr\\planet_assets\\sky_renderers\\"+planetName+".json"),sky_renderer);
                        FileUtils.writeLines(new File(LOCLOC+"pack"+uuid+"\\assets\\dataplanets\\lang\\en_us.json"),List.of(langFile.substring(0,langFile.length()-1)+"}"));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                FileUtils.writeLines(new File(LOCLOC+"pack"+uuid+"\\assets\\dataplanets\\lang\\en_us.json"),List.of(langFile.substring(0,langFile.length()-1)+"}"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
