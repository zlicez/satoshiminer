package com.mycompany.mymod.satoshiminex.menu;

import com.mycompany.mymod.satoshiminex.blockentity.SatoshiConverterBlockEntity;
import com.mycompany.mymod.satoshiminex.init.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SatoshiConverterMenu extends AbstractContainerMenu {
    private final SatoshiConverterBlockEntity blockEntity;
    private final Slot inputSlot;

    public SatoshiConverterMenu(int containerId, Inventory playerInventory, SatoshiConverterBlockEntity blockEntity) {
        super(ModMenus.SATOSHI_CONVERTER_MENU.get(), containerId);
        this.blockEntity = blockEntity;

        // Слот для предметов сатоши
        inputSlot = new SlotItemHandler(blockEntity.getItemHandler(), 0, 80, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() == ModItems.SATOSHI_ITEM.get();
            }
        };
        addSlot(inputSlot);

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

    public SatoshiConverterMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, (SatoshiConverterBlockEntity) playerInventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.SATOSHI_CONVERTER.get());
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    public void convertSatoshi(Player player) {
        if (blockEntity != null) {
            blockEntity.convertSatoshi(player);
        }
    }

    public SatoshiConverterBlockEntity getBlockEntity() {
        return blockEntity;
    }
}