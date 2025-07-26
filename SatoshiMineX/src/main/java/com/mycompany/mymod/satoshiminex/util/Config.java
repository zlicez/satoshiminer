package com.mycompany.mymod.satoshiminex.util;

import com.mycompany.mymod.satoshiminex.capability.WalletCapability;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber
public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue FETCH_INTERVAL;
    public static final ForgeConfigSpec.DoubleValue STARTING_BTC;
    public static final ForgeConfigSpec.DoubleValue STARTING_RUB;

    static {
        BUILDER.push("General");
        FETCH_INTERVAL = BUILDER.comment("Interval for fetching BTC/RUB rate in seconds (minimum 300)")
                .defineInRange("fetchInterval", 300, 300, Integer.MAX_VALUE);
        STARTING_BTC = BUILDER.comment("Starting BTC balance for new players in BTC")
                .defineInRange("startingBtc", 0.0, 0.0, Double.MAX_VALUE);
        STARTING_RUB = BUILDER.comment("Starting RUB balance for new players in RUB")
                .defineInRange("startingRub", 0.0, 0.0, Double.MAX_VALUE);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public Config() {
        net.minecraftforge.fml.ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, SPEC, "satoshiminex-common.toml");
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        applyConfig();
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {
        applyConfig();
    }

    private static void applyConfig() {
        BtcRateFetcher.FETCH_INTERVAL_MS = FETCH_INTERVAL.get() * 1000L;
        WalletCapability.STARTING_BTC = STARTING_BTC.get();
        WalletCapability.STARTING_RUB = STARTING_RUB.get();
    }
}
