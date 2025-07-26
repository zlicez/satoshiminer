package com.mycompany.mymod.satoshiminex.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class WalletCapability {
    public static final Capability<Wallet> WALLET_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    private static class WalletStorage implements Capability.IStorage<Wallet> {
        @Override
        @Nullable
        public CompoundTag writeNBT(Capability<Wallet> capability, Wallet instance, Direction side) {
            CompoundTag nbt = new CompoundTag();
            instance.saveNBTData(nbt);
            return nbt;
        }

        @Override
        public void readNBT(Capability<Wallet> capability, Wallet instance, Direction side, INBTSerializable<CompoundTag> nbt) {
            instance.loadNBTData(nbt.serializeNBT());
        }
    }

    public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
        private final Wallet wallet = new Wallet();
        private final LazyOptional<Wallet> optional = LazyOptional.of(() -> wallet);

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return WALLET_CAPABILITY.orEmpty(cap, optional);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            wallet.saveNBTData(nbt);
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            wallet.loadNBTData(nbt);
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(Wallet.class, new WalletStorage(), Wallet::new);
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(Wallet.class);
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation("satoshiminex", "wallet"), new Provider());
        }
    }

    public static LazyOptional<Wallet> getWallet(Player player) {
        return player.getCapability(WALLET_CAPABILITY);
    }
}