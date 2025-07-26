package com.mycompany.mymod.satoshiminex;

import com.mycompany.mymod.satoshiminex.capability.WalletCapability;
import com.mycompany.mymod.satoshiminex.init.ModBlockEntities;
import com.mycompany.mymod.satoshiminex.init.ModBlocks;
import com.mycompany.mymod.satoshiminex.init.ModItems;
import com.mycompany.mymod.satoshiminex.init.ModMenus;
import com.mycompany.mymod.satoshiminex.init.ModWorldGen;
import com.mycompany.mymod.satoshiminex.util.Config;
import com.mycompany.mymod.satoshiminex.network.NetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SatoshiMinerMod.MOD_ID)
public class SatoshiMinerMod {
    public static final String MOD_ID = "satoshiminex";
    public static final Logger LOGGER = LogManager.getLogger();

    public SatoshiMinerMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        ModMenus.MENUS.register(bus);
        ModWorldGen.CONFIGURED_FEATURES.register(bus);

        // Инициализация дополнительных компонентов
        new Config();
        NetworkHandler.register();

        // Регистрация событий для Capability
        bus.addListener(WalletCapability::registerCapabilities);
        MinecraftForge.EVENT_BUS.register(WalletCapability.class);

        bus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModWorldGen.addOreGeneration();
        });
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        // Копирование кошелька при смерти игрока
        WalletCapability.getWallet(event.getOriginal()).ifPresent(oldWallet -> {
            WalletCapability.getWallet(event.getEntity()).ifPresent(newWallet -> {
                newWallet.loadNBTData(oldWallet.saveNBTData(new CompoundTag()));
            });
        });
    }
}
