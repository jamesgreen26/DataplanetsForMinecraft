package ace.actually.dataplanets;

import ace.actually.dataplanets.interfaces.IUnfreezableRegistry;
import argent_matter.gcyr.common.data.GCYRBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.level.LevelEvent;

import java.util.OptionalLong;

public class DynamicSystems {
    public static Registry<Biome> BIOMES = null;
    public static Registry<ConfiguredWorldCarver<?>> CONFIGURED_CARVERS = null;
    public static Registry<PlacedFeature> PLACED_FEATURES = null;
    public static boolean frozenOnce = false;

    public static boolean allRegistriesFrozen()
    {
        return BIOMES!=null && CONFIGURED_CARVERS !=null && PLACED_FEATURES!=null;
    }


    public static ResourceKey<Biome> planetDataToBiome(RegistryAccess.Frozen registryAccess, CompoundTag planetData)
    {
        BIOMES = registryAccess.registryOrThrow(Registries.BIOME);
        CONFIGURED_CARVERS = registryAccess.registryOrThrow(Registries.CONFIGURED_CARVER);
        PLACED_FEATURES = registryAccess.registryOrThrow(Registries.PLACED_FEATURE);
        return  planetDataToBiome(planetData);
    }

    public static ResourceKey<Biome> planetDataToBiome(CompoundTag planetData)
    {

        Holder.Reference<ConfiguredWorldCarver<?>> canyon = CONFIGURED_CARVERS.getHolderOrThrow(Carvers.CANYON);
        Holder.Reference<ConfiguredWorldCarver<?>> cave = CONFIGURED_CARVERS.getHolderOrThrow(Carvers.CAVE);
        Holder.Reference<ConfiguredWorldCarver<?>> cave_extra = CONFIGURED_CARVERS.getHolderOrThrow(Carvers.CAVE_EXTRA_UNDERGROUND);


        Holder.Reference<PlacedFeature> dripstone = PLACED_FEATURES.getHolder(ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryParse("large_dripstone"))).get();
        Holder.Reference<PlacedFeature> dripstone_cluster = PLACED_FEATURES.getHolder(ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.tryParse("dripstone_cluster"))).get();
        Holder.Reference<PlacedFeature> pointed_dripstone = PLACED_FEATURES.getHolder(ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.tryParse("pointed_dripstone"))).get();

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

        ResourceKey<Biome> biomeKey = ResourceKey.create(BIOMES.key(), ResourceLocation.tryBuild("dataplanets",planetData.getString("name")+"_terrain"));


        ((IUnfreezableRegistry) BIOMES).setRegFrozen(false);
        ((MappedRegistry<Biome>) BIOMES).register(
                biomeKey,
                biome,
                Lifecycle.experimental() // use built-in registration info for now
        );
        ((IUnfreezableRegistry) BIOMES).setRegFrozen(true);
        System.out.println("created biome"+planetData.getString("name"));
        return biomeKey;
    }

    public static ResourceKey<DimensionType> makeDynamicDimType(RegistryAccess.Frozen registryAccess, CompoundTag planetData)
    {
        Registry<DimensionType> dimTypeRegistry = registryAccess.registryOrThrow(Registries.DIMENSION_TYPE);


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


        return registerDimensionType(planetData,dimTypeRegistry,dimensionType);
    }

    public static ResourceKey<DimensionType> registerDimensionType(CompoundTag planetData,Registry<DimensionType> dimTypeRegistry,DimensionType dimensionType)
    {
        ResourceKey<DimensionType> dimKey = ResourceKey.create(dimTypeRegistry.key(),ResourceLocation.tryBuild("dataplanets",planetData.getString("name")));
        ((IUnfreezableRegistry) dimTypeRegistry).setRegFrozen(false);
        ((MappedRegistry<DimensionType>) dimTypeRegistry).register(
                dimKey,
                dimensionType,
                Lifecycle.experimental() // use built-in registration info for now
        );
        ((IUnfreezableRegistry) dimTypeRegistry).setRegFrozen(true);
        return dimKey;
    }

    public static void makeDynamicWorld(MinecraftServer server, CompoundTag planetData)
    {
        RegistryAccess.Frozen registryAccess = server.registryAccess();
        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);
        Registry<NormalNoise.NoiseParameters> noise = registryAccess.registryOrThrow(Registries.NOISE);

        Holder.Reference<Biome> biomeHolder = biomeRegistry.getHolderOrThrow(planetDataToBiome(registryAccess,planetData));

        NoiseGeneratorSettings settings = new NoiseGeneratorSettings(
                NoiseSettings.create(-64, 384, 2, 2),
                GCYRBlocks.MOON_COBBLESTONE.getDefaultState(),
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
                                DensityFunctions.noise(noise.getHolderOrThrow(Noises.GRAVEL),planetData.getFloat("nr1"),planetData.getFloat("nr2"))),
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



        //DimensionType type = new DimensionType(OptionalLong.empty(),true,false,false,true,1.0,true,false,-64,384,384, BlockTags.INFINIBURN_OVERWORLD,ResourceLocation.fromNamespaceAndPath("gcyr","luna"),0,new DimensionType.MonsterSettings(true,false, UniformInt.of(0,7),0));
        //ResourceKey<DimensionType> dimensionTypeId = ResourceKey.create(Registries.DIMENSION_TYPE,ResourceLocation.fromNamespaceAndPath("dataplanets","coolworld"));
        ChunkProgressListener listener = server.progressListenerFactory.create(server.getWorldData().getGameRules().getInt(GameRules.RULE_SPAWN_RADIUS));

        Registry<DimensionType> dimensionTypes = server.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE);
        Holder.Reference<DimensionType> holder = dimensionTypes.getHolderOrThrow(makeDynamicDimType(registryAccess,planetData));

        ResourceKey<LevelStem> resourcekey = ResourceKey.create(Registries.LEVEL_STEM, ResourceLocation.tryBuild("dataplanets","coolworld"));
        ServerLevelData serverleveldata = server.getWorldData().overworldData();

        LevelStem stem = new LevelStem(holder,noiseBasedChunkGenerator);

        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, resourcekey.location());
        DerivedLevelData derivedleveldata = new DerivedLevelData(server.getWorldData(), serverleveldata);
        ServerLevel serverlevel1 = new ServerLevel(server, Util.backgroundExecutor(), server.storageSource, derivedleveldata, dimensionKey, stem, listener, server.getWorldData().isDebugWorld(), BiomeManager.obfuscateSeed(server.getWorldData().worldGenOptions().seed()), ImmutableList.of(), false, server.overworld().getRandomSequences());
        server.overworld().getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(serverlevel1.getWorldBorder()));

        server.levels.put(dimensionKey, serverlevel1);
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(server.levels.get(resourcekey)));
        //https://github.com/iPortalTeam/DimLib/blob/1.21/src/main/java/qouteall/dimlib/DynamicDimensionsImpl.java

        Registry<LevelStem> levelStemRegistry = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
        ((IUnfreezableRegistry) levelStemRegistry).setRegFrozen(false);
        ((MappedRegistry<LevelStem>) levelStemRegistry).register(
                resourcekey,
                stem,
                Lifecycle.experimental() // use built-in registration info for now
        );
        ((IUnfreezableRegistry) levelStemRegistry).setRegFrozen(true);

    }
}
