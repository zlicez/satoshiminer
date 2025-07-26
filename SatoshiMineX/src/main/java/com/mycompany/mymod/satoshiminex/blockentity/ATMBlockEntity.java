package com.mycompany.mymod.satoshiminex.blockentity;

import com.mycompany.mymod.satoshiminex.capability.Wallet;
import com.mycompany.mymod.satoshiminex.capability.WalletCapability;
import com.mycompany.mymod.satoshiminex.menu.ATMMenu;
import com.mycompany.mymod.satoshiminex.network.ConvertMessage;
import com.mycompany.mymod.satoshiminex.util.BtcRateFetcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ATMBlockEntity extends BlockEntity {
    private double btcToRubRate = 0.0;

    public ATMBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ATM_BLOCK_ENTITY.get(), pos, state);
        updateRate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide) {
            updateRate();
        }
    }

    public void updateRate() {
        btcToRubRate = BtcRateFetcher.getCurrentBtcToRubRate();
        setChanged();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("btcToRubRate")) {
            btcToRubRate = nbt.getDouble("btcToRubRate");
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putDouble("btcToRubRate", btcToRubRate);
    }

    public double getBtcToRubRate() {
        return btcToRubRate;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        return super.getCapability(cap, side); // Не используем локальный wallet, так как он привязан к игроку
    }

    public void convertBtcToRub(Player player, double btcAmount) {
        if (level != null && !level.isClientSide) {
            WalletCapability.getWallet(player).ifPresent(wallet -> {
                if (wallet.getBtcBalance() >= btcAmount * 100_000_000) {
                    long rubKopecks = (long) (btcAmount * btcToRubRate * 100);
                    wallet.removeBtcBalance((long) (btcAmount * 100_000_000));
                    wallet.addRubBalance(rubKopecks);
                    setChanged();
                    if (player instanceof ServerPlayer) {
                        ConvertMessage.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), btcAmount, rubKopecks);
                    }
                } else {
                    player.sendSystemMessage(Component.literal("Insufficient BTC balance!"));
                }
            });
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ATMMenu(containerId, playerInventory, this);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state, ATMBlockEntity blockEntity) {
        if (level.getGameTime() % 6000 == 0) { // Обновление каждые 5 минут (6000 тиков)
            updateRate();
        }
    }
}
