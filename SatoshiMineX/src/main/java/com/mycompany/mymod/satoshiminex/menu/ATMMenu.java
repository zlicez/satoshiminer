package com.mycompany.mymod.satoshiminex.menu;

import com.mycompany.mymod.satoshiminex.blockentity.ATMBlockEntity;
import com.mycompany.mymod.satoshiminex.init.ModMenus;
import com.mycompany.mymod.satoshiminex.init.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class ATMMenu extends AbstractContainerMenu {
    private final ATMBlockEntity blockEntity;
    private final ContainerData data;

    public ATMMenu(int containerId, Inventory playerInventory, ATMBlockEntity blockEntity) {
        super(ModMenus.ATM_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = new SimpleContainerData(1); // Для хранения курса
        this.data.set(0, (int) (blockEntity.getBtcToRubRate() * 100)); // Курс в копейках

        // Слоты инвентаря игрока
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public ATMMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, (ATMBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.ATM.get());
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 36) { // Слоты игрока
                if (!this.moveItemStackTo(itemstack1, 0, 0, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    public double getBtcToRubRate() {
        return data.get(0) / 100.0;
    }

    public void convertBtcToRub(Player player, double btcAmount) {
        if (blockEntity != null) {
            blockEntity.convertBtcToRub(player, btcAmount);
        }
    }

    public ATMBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
