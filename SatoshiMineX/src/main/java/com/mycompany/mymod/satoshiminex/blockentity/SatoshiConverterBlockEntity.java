package com.mycompany.mymod.satoshiminex.blockentity;

import com.mycompany.mymod.satoshiminex.capability.Wallet;
import com.mycompany.mymod.satoshiminex.capability.WalletCapability;
import com.mycompany.mymod.satoshiminex.init.ModItems;
import com.mycompany.mymod.satoshiminex.menu.SatoshiConverterMenu;
import com.mycompany.mymod.satoshiminex.network.ExchangeMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SatoshiConverterBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() == ModItems.SATOSHI_ITEM.get();
        }
    };

    public SatoshiConverterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SATOSHI_CONVERTER_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("items")) {
            itemHandler.deserializeNBT(nbt.getCompound("items"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("items", itemHandler.serializeNBT());
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable net.minecraft.core.Direction side) {
        return super.getCapability(cap, side); // Кошелек привязан к игроку
    }

    public void convertSatoshi(Player player) {
        if (level != null && !level.isClientSide) {
            ItemStack stack = itemHandler.getStackInSlot(0);
            if (!stack.isEmpty() && stack.getItem() == ModItems.SATOSHI_ITEM.get()) {
                int satoshiCount = stack.getCount();
                if (satoshiCount > 1000) {
                    player.sendSystemMessage(Component.literal("Max 1000 Satoshi per conversion!"));
                    return;
                }
                WalletCapability.getWallet(player).ifPresent(wallet -> {
                    wallet.addBtcBalance(satoshiCount * 1500L); // 1 сатоши = 1500 реальных сатоши
                    itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                    setChanged();
                    if (player instanceof ServerPlayer) {
                        ExchangeMessage.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), satoshiCount * 1500L);
                    }
                });
            }
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SatoshiConverterMenu(containerId, playerInventory, this);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state, SatoshiConverterBlockEntity blockEntity) {
        // Логика тика не требуется, обработка происходит при взаимодействии
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}
