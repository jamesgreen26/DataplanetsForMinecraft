package ace.actually.dataplanets;

import ace.actually.dataplanets.interfaces.IUnfreezableRegistry;
import argent_matter.gcyr.common.data.GCYRBlocks;
import com.google.common.collect.ImmutableList;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.data.worldgen.modifier.BiomePlacement;
import com.gregtechceu.gtceu.common.data.GTConfiguredFeatures;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.mojang.serialization.Lifecycle;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.level.LevelEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

//Big props to https://github.com/iPortalTeam/DimLib/blob/1.21/src/main/java/qouteall/dimlib/DynamicDimensionsImpl.java
public class DynamicSystems {
    public static Registry<Biome> BIOMES = null;
    public static Registry<ConfiguredWorldCarver<?>> CONFIGURED_CARVERS = null;
    public static Registry<PlacedFeature> PLACED_FEATURES = null;
    public static Registry<DimensionType> DIMENSION_TYPE = null;
    public static Registry<ConfiguredFeature<?,?>> CONFIGURED_FEATURES = null;
    public static Registry<LevelStem> LEVEL_STEMS = null;
    public static Registry<NormalNoise.NoiseParameters> NOISE = null;
    public static int frozeTimes = 0;

    public static void loadDynamicResources()
    {
        //TODO: 20 is a somewhat arbitrary number, check this works on a big modpack
        //I would have thought this would have more problems on a smaller modpack actually...
        if(DynamicSystems.allRegistriesFrozen() && frozeTimes>20)
        {
            //System.out.println("BUILDING PLANETS!");

            CompoundTag tag = StarSystemCreator.getDynamicDataOrNew();
            for(String system: tag.getAllKeys())
            {
                if(tag.getTagType(system)== Tag.TAG_COMPOUND)
                {
                    CompoundTag specificSystem = tag.getCompound(system);

                    for(String planet: specificSystem.getAllKeys())
                    {
                        if(specificSystem.getTagType(planet)== Tag.TAG_COMPOUND)
                        {
                            CompoundTag specificPlanet = specificSystem.getCompound(planet);

                            DynamicSystems.makeLevelStem(specificPlanet);

                        }
                    }

                }
            }

        }
    }

    public static boolean allRegistriesFrozen()
    {
        return BIOMES!=null
                && CONFIGURED_CARVERS !=null
                && PLACED_FEATURES!=null
                && DIMENSION_TYPE!=null
                && CONFIGURED_FEATURES!=null
                && LEVEL_STEMS!=null
                && NOISE!=null;
    }

    public static ResourceKey<Biome> planetDataToBiome(CompoundTag planetData)
    {
        ResourceKey<Biome> biomeKey = ResourceKey.create(BIOMES.key(), ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_terrain"));

        if(!BIOMES.containsKey(biomeKey))
        {
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
                    .generationSettings(builder(new BiomeGenerationSettingsBuilder(BiomeGenerationSettings.EMPTY),planetData).build()).build();


            ((IUnfreezableRegistry) BIOMES).setRegFrozen(false);
            ((MappedRegistry<Biome>) BIOMES).register(
                    biomeKey,
                    biome,
                    Lifecycle.stable() // use built-in registration info for now
            );
            ((IUnfreezableRegistry) BIOMES).setRegFrozen(true);
        }
        return biomeKey;
    }

    private static BiomeGenerationSettings.PlainBuilder builder(BiomeGenerationSettingsBuilder builder,CompoundTag planetData)
    {
        Holder.Reference<ConfiguredWorldCarver<?>> canyon = CONFIGURED_CARVERS.getHolderOrThrow(Carvers.CANYON);
        Holder.Reference<ConfiguredWorldCarver<?>> cave = CONFIGURED_CARVERS.getHolderOrThrow(Carvers.CAVE);
        Holder.Reference<ConfiguredWorldCarver<?>> cave_extra = CONFIGURED_CARVERS.getHolderOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND);


        Holder.Reference<PlacedFeature> dripstone = PLACED_FEATURES.getHolder(ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryParse("large_dripstone"))).get();
        Holder.Reference<PlacedFeature> dripstone_cluster = PLACED_FEATURES.getHolder(ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.tryParse("dripstone_cluster"))).get();
        Holder.Reference<PlacedFeature> pointed_dripstone = PLACED_FEATURES.getHolder(ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.tryParse("pointed_dripstone"))).get();

        builder.addCarver(GenerationStep.Carving.AIR,canyon)
                .addCarver(GenerationStep.Carving.AIR,cave)
                .addCarver(GenerationStep.Carving.AIR,cave_extra)
                .addFeature(0,dripstone)
                .addFeature(0,dripstone_cluster)
                .addFeature(0,pointed_dripstone);


        List<ResourceKey<PlacedFeature>> features = makeOres(planetData);
        for(ResourceKey<PlacedFeature> feature: features)
        {
            builder.addFeature(0,PLACED_FEATURES.getHolder(feature).get());
        }
        features = makeLakes(planetData);
        for(ResourceKey<PlacedFeature> feature: features)
        {
            builder.addFeature(0,PLACED_FEATURES.getHolder(feature).get());
        }


        return builder;
    }

    public static ResourceKey<DimensionType> makeDynamicDimType(CompoundTag planetData)
    {
        ResourceKey<DimensionType> dimKey = ResourceKey.create(DIMENSION_TYPE.key(),ResourceLocation.tryBuild("dataplanets",planetData.getString("name")));

        if(!DIMENSION_TYPE.containsKey(dimKey))
        {
            ResourceLocation effects;
            if(planetData.getString("hasAtmosphere").equals("true"))
            {
                effects=ResourceLocation.fromNamespaceAndPath("minecraft","overworld");
            }
            else
            {
                effects=ResourceLocation.fromNamespaceAndPath("minecraft","the_end");
            }

            DimensionType dimensionType = new DimensionType(
                    OptionalLong.empty(),
                    planetData.getInt("solarPower")>5,
                    false,
                    planetData.getInt("temperature")>400,
                    true,
                    1,
                    true,
                    false,
                    -64,
                    384,
                    284,
                    BlockTags.INFINIBURN_OVERWORLD,
                    effects,
                    0,
                    new DimensionType.MonsterSettings(planetData.getInt("temperature")>400,false, UniformInt.of(0,7),0)
            );



            ((IUnfreezableRegistry) DIMENSION_TYPE).setRegFrozen(false);
            ((MappedRegistry<DimensionType>) DIMENSION_TYPE).register(
                    dimKey,
                    dimensionType,
                    Lifecycle.stable() // use built-in registration info for now
            );
            ((IUnfreezableRegistry) DIMENSION_TYPE).setRegFrozen(true);
        }

        return dimKey;
    }

    public static List<ResourceKey<PlacedFeature>> makeOres(CompoundTag planetData)
    {
        List<ResourceKey<PlacedFeature>> features = new ArrayList<>();
        if(planetData.contains("planet_ores"))
        {
            ListTag ores = (ListTag) planetData.get("planet_ores");
            for (int i = 0; i < ores.size(); i++) {
                ResourceKey<ConfiguredFeature<?,?>> configuredKey = ResourceKey.create(CONFIGURED_FEATURES.key(),ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_ore_"+i));
                ResourceKey<PlacedFeature> placedKey = ResourceKey.create(PLACED_FEATURES.key(),ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_ore_"+i));
                if(!PLACED_FEATURES.containsKey(placedKey))
                {
                    BlockState oreState = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(ores.getString(i))).defaultBlockState();
                    OreConfiguration configuration = new OreConfiguration(List.of(OreConfiguration.target(new TagMatchTest(BlockTags.DIRT),oreState)),10);
                    ConfiguredFeature<?,?> feature = new ConfiguredFeature<>(OreFeature.ORE,configuration);



                    ((IUnfreezableRegistry) CONFIGURED_FEATURES).setRegFrozen(false);
                    ((MappedRegistry<ConfiguredFeature<?,?>>) CONFIGURED_FEATURES).register(
                            configuredKey,
                            feature,
                            Lifecycle.stable() // use built-in registration info for now
                    );
                    ((IUnfreezableRegistry) CONFIGURED_FEATURES).setRegFrozen(true);

                    List<PlacementModifier> modifiers = new ArrayList<>();
                    modifiers.add(CountPlacement.of(16));
                    modifiers.add(InSquarePlacement.spread());
                    modifiers.add(HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-11),VerticalAnchor.belowTop(112)));
                    PlacedFeature placedFeature = new PlacedFeature(CONFIGURED_FEATURES.getHolderOrThrow(configuredKey),modifiers);



                    ((IUnfreezableRegistry) PLACED_FEATURES).setRegFrozen(false);
                    ((MappedRegistry<PlacedFeature>) PLACED_FEATURES).register(
                            placedKey,
                            placedFeature,
                            Lifecycle.stable() // use built-in registration info for now
                    );
                    ((IUnfreezableRegistry) PLACED_FEATURES).setRegFrozen(true);
                    features.add(placedKey);
                }



            }
        }
        return features;
    }
    public static List<ResourceKey<PlacedFeature>> makeLakes(CompoundTag planetData)
    {
        List<ResourceKey<PlacedFeature>> features = new ArrayList<>();
        if(planetData.contains("lakeFluids"))
        {
            ListTag lakes = (ListTag) planetData.get("lakeFluids");
            BlockState barrier = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(planetData.getString("generalBlock"))).defaultBlockState();
            for (int i = 0; i < lakes.size(); i++) {
                ResourceKey<ConfiguredFeature<?,?>> configuredKey = ResourceKey.create(CONFIGURED_FEATURES.key(),ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_lake_"+i));
                ResourceKey<PlacedFeature> placedKey = ResourceKey.create(PLACED_FEATURES.key(),ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_lake_"+i));
                if(!PLACED_FEATURES.containsKey(placedKey))
                {
                    BlockState fluid = BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(lakes.getString(i))).defaultBlockState();

                    LakeFeature.Configuration lakeConfig = new LakeFeature.Configuration(BlockStateProvider.simple(fluid),BlockStateProvider.simple(barrier));

                    ConfiguredFeature<?,?> feature = new ConfiguredFeature<>(Feature.LAKE,lakeConfig);



                    ((IUnfreezableRegistry) CONFIGURED_FEATURES).setRegFrozen(false);
                    ((MappedRegistry<ConfiguredFeature<?,?>>) CONFIGURED_FEATURES).register(
                            configuredKey,
                            feature,
                            Lifecycle.stable() // use built-in registration info for now
                    );
                    ((IUnfreezableRegistry) CONFIGURED_FEATURES).setRegFrozen(true);

                    List<PlacementModifier> modifiers = new ArrayList<>();
                    modifiers.add(RarityFilter.onAverageOnceEvery(200));
                    modifiers.add(InSquarePlacement.spread());
                    modifiers.add(HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG));
                    PlacedFeature placedFeature = new PlacedFeature(CONFIGURED_FEATURES.getHolderOrThrow(configuredKey),modifiers);



                    ((IUnfreezableRegistry) PLACED_FEATURES).setRegFrozen(false);
                    ((MappedRegistry<PlacedFeature>) PLACED_FEATURES).register(
                            placedKey,
                            placedFeature,
                            Lifecycle.stable() // use built-in registration info for now
                    );
                    ((IUnfreezableRegistry) PLACED_FEATURES).setRegFrozen(true);
                    features.add(placedKey);
                }



            }
        }
        return features;
    }



    public static LevelStem makeLevelStem(CompoundTag planetData)
    {
        ResourceKey<LevelStem> resourcekey = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.tryBuild("dataplanets",planetData.getString("name")));
        Holder.Reference<Biome> biomeHolder = BIOMES.getHolderOrThrow(planetDataToBiome(planetData));
        Holder.Reference<DimensionType> holder = DIMENSION_TYPE.getHolderOrThrow(makeDynamicDimType(planetData));
        if(!LEVEL_STEMS.containsKey(resourcekey))
        {
            NoiseGeneratorSettings settings = new NoiseGeneratorSettings(
                    NoiseSettings.create(-64, 384, 2, 2),
                    BuiltInRegistries.BLOCK.get(ResourceLocation.tryParse(planetData.getString("generalBlock"))).defaultBlockState(),
                    Blocks.AIR.defaultBlockState(),
                    new NoiseRouter(
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.add(
                                    DensityFunctions.yClampedGradient(-64,320,1,-1),
                                    DensityFunctions.noise(NOISE.getHolderOrThrow(Noises.GRAVEL),planetData.getFloat("nr1"),planetData.getFloat("nr2"))),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0),
                            DensityFunctions.constant(0)),
                    SurfaceRuleData.overworld(),
                    new OverworldBiomeBuilder().spawnTarget(),
                    63,
                    false,
                    true,
                    true,
                    false
            );
            NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(new FixedBiomeSource(biomeHolder), Holder.direct(settings));
            LevelStem stem = new LevelStem(holder,noiseBasedChunkGenerator);

            ((IUnfreezableRegistry) LEVEL_STEMS).setRegFrozen(false);
            ((MappedRegistry<LevelStem>) LEVEL_STEMS).register(
                    resourcekey,
                    stem,
                    Lifecycle.stable() // use built-in registration info for now
            );
            ((IUnfreezableRegistry) LEVEL_STEMS).setRegFrozen(true);
        }
        return LEVEL_STEMS.get(resourcekey);
    }

    public static void makeDynamicWorld(MinecraftServer server, CompoundTag planetData)
    {

        ChunkProgressListener listener = server.progressListenerFactory.create(server.getWorldData().getGameRules().getInt(GameRules.RULE_SPAWN_RADIUS));

        ServerLevelData serverleveldata = server.getWorldData().overworldData();


        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.tryBuild("dataplanets",planetData.getString("name")));
        DerivedLevelData derivedleveldata = new DerivedLevelData(server.getWorldData(), serverleveldata);
        LevelStem stem = makeLevelStem(planetData);
        ServerLevel serverlevel1 = new ServerLevel(server, Util.backgroundExecutor(), server.storageSource, derivedleveldata, dimensionKey, stem, listener, server.getWorldData().isDebugWorld(), BiomeManager.obfuscateSeed(server.getWorldData().worldGenOptions().seed()), ImmutableList.of(), false, server.overworld().getRandomSequences());
        server.overworld().getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(serverlevel1.getWorldBorder()));

        server.levels.put(dimensionKey, serverlevel1);
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(server.levels.get(dimensionKey)));
    }


}
