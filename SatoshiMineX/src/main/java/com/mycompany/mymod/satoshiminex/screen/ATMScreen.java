package com.mycompany.mymod.satoshiminex.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mycompany.mymod.satoshiminex.blockentity.ATMBlockEntity;
import com.mycompany.mymod.satoshiminex.capability.Wallet;
import com.mycompany.mymod.satoshiminex.capability.WalletCapability;
import com.mycompany.mymod.satoshiminex.menu.ATMMenu;
import com.mycompany.mymod.satoshiminex.network.ConvertMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.network.PacketDistributor;

public class ATMScreen extends AbstractContainerScreen<ATMMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("satoshiminex", "textures/gui/atm_gui.png");
    private EditBox btcInput;
    private final ATMBlockEntity blockEntity;

    public ATMScreen(ATMMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.blockEntity = menu.getBlockEntity();
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        btcInput = new EditBox(this.font, this.leftPos + 70, this.topPos + 40, 50, 20, Component.literal("0.0"));
        addRenderableWidget(btcInput);
        addRenderableWidget(new Button(this.leftPos + 130, this.topPos + 40, 40, 20, Component.literal("Convert"), button -> {
            try {
                double btcAmount = Double.parseDouble(btcInput.getValue());
                if (btcAmount > 0) {
                    menu.convertBtcToRub(minecraft.player, btcAmount);
                }
            } catch (NumberFormatException e) {
                // Игнорируем некорректный ввод
            }
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
        WalletCapability.getWallet(minecraft.player).ifPresent(wallet -> {
            font.draw(poseStack, "BTC: " + String.format("%.8f", wallet.getBtc()), leftPos + 10, topPos + 20, 0xFFFFFF);
            font.draw(poseStack, "RUB: " + String.format("%.2f", wallet.getRub()), leftPos + 10, topPos + 30, 0xFFFFFF);
        });
        font.draw(poseStack, "Rate: " + String.format("%.2f", menu.getBtcToRubRate()) + " RUB/BTC", leftPos + 10, topPos + 50, 0xFFFFFF);
    }

    private Wallet getWallet() {
        return WalletCapability.getWallet(minecraft.player).orElse(null);
    }
}