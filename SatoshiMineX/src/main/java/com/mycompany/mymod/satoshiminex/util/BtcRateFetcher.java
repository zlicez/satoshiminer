package com.mycompany.mymod.satoshiminex.util;

import com.google.gson.Gson;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class BtcRateFetcher {
    private static double currentRate = 5000.0; // Резервный курс
    private static long lastFetchTime = 0;
    /** Interval between rate fetches in milliseconds. Configurable via the common config. */
    public static long FETCH_INTERVAL_MS = 300_000; // 5 минут

    static {
        MinecraftForge.EVENT_BUS.register(BtcRateFetcher.class);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFetchTime >= FETCH_INTERVAL_MS) {
                fetchRateAsync().thenAccept(rate -> {
                    if (rate > 0) {
                        currentRate = rate;
                        lastFetchTime = currentTime;
                    }
                });
            }
        }
    }

    public static double getCurrentBtcToRubRate() {
        return currentRate;
    }

    private static CompletableFuture<Double> fetchRateAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=rub");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                String content = new java.util.Scanner(con.getInputStream()).useDelimiter("\\A").next();
                con.disconnect();
                Gson gson = new Gson();
                PriceResponse response = gson.fromJson(content, PriceResponse.class);
                return response.bitcoin.rub;
            } catch (Exception e) {
                System.err.println("Failed to fetch BTC/RUB rate: " + e.getMessage());
                return -1.0;
            }
        });
    }

    private static class PriceResponse {
        public Bitcoin bitcoin;

        private static class Bitcoin {
            public double rub;
        }
    }
}
