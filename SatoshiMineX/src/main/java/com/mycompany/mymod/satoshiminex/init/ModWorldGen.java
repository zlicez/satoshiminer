package com.mycompany.mymod.satoshiminex.init;

import com.mycompany.mymod.satoshiminex.block.SatoshiOreBlock;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.biome.BiomeFilter;
import net.minecraft.world.level.biome.BiomeLoadingEvent;
import net.minecraft.world.level.biome.GenerationStep;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModWorldGen {
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(BuiltinRegistries.CONFIGURED_FEATURE, "satoshiminex");

    public static final RegistryObject<ConfiguredFeature<?, ?>> SATOSHI_ORE = CONFIGURED_FEATURES.register("satoshi_ore",
            () -> new ConfiguredFeature<>(Feature.ORE,
                    new OreConfiguration(List.of(
                            OreConfiguration.target(new TagMatchTest(net.minecraft.tags.BlockTags.STONE_ORE_REPLACEABLES),
                                    ModBlocks.SATOSHI_ORE.get().defaultBlockState()),
                            OreConfiguration.target(new TagMatchTest(net.minecraft.tags.BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                                    ModBlocks.SATOSHI_ORE.get().defaultBlockState())),
                            5))); // Размер жилы

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }

    public static void registerConfiguredFeatures() {
        // Метод заглушка, так как регистрация уже выполняется через DeferredRegister
    }

    public static void addOreGeneration() {
        List<PlacementModifier> modifiers = List.of(
                CountPlacement.of(10), // Количество попыток генерации
                InSquarePlacement.spread(),
                HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)),
                BiomeFilter.biome()
        );
        PlacedFeature placedFeature = new PlacedFeature(SATOSHI_ORE.getHolder().get(), modifiers);
        BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, new ResourceLocation("satoshiminex", "satoshi_ore_placed"), placedFeature);
    }

    public static void addFeaturesToBiomes() {
        BiomeLoadingEvent.BIOME_MODIFICATION_EVENT.register((biomeLoadingContext, generateData) -> {
            generateData.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES,
                    () -> PlacedFeature.REGISTRY.getHolder(new ResourceLocation("satoshiminex", "satoshi_ore_placed")).get());
        });
    }
}
