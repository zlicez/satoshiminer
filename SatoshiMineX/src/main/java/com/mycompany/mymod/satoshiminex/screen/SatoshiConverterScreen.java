package com.mycompany.mymod.satoshiminex.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mycompany.mymod.satoshiminex.blockentity.SatoshiConverterBlockEntity;
import com.mycompany.mymod.satoshiminex.capability.Wallet;
import com.mycompany.mymod.satoshiminex.capability.WalletCapability;
import com.mycompany.mymod.satoshiminex.menu.SatoshiConverterMenu;
import com.mycompany.mymod.satoshiminex.network.ExchangeMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.network.PacketDistributor;

public class SatoshiConverterScreen extends AbstractContainerScreen<SatoshiConverterMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("satoshiminex", "textures/gui/converter_gui.png");
    private final SatoshiConverterBlockEntity blockEntity;

    public SatoshiConverterScreen(SatoshiConverterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.blockEntity = menu.getBlockEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(new Button(this.leftPos + 70, this.topPos + 70, 60, 20, Component.literal("Convert"), button -> {
            menu.convertSatoshi(minecraft.player);
        }));
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        drawCenteredString(poseStack, font, title, leftPos + imageWidth / 2, topPos + 6, 0xFFFFFF);
        ItemStack stack = blockEntity.getItemHandler().getStackInSlot(0);
        font.draw(poseStack, "Satoshi: " + (stack.isEmpty() ? 0 : stack.getCount()), leftPos + 10, topPos + 20, 0xFFFFFF);
        WalletCapability.getWallet(minecraft.player).ifPresent(wallet -> {
            font.draw(poseStack, "BTC: " + String.format("%.8f", wallet.getBtc()), leftPos + 10, topPos + 30, 0xFFFFFF);
        });
    }

    private Wallet getWallet() {
        return WalletCapability.getWallet(minecraft.player).orElse(null);
    }
}