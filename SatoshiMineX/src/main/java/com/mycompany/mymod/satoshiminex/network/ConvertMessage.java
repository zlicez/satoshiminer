package com.mycompany.mymod.satoshiminex.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import com.mycompany.mymod.satoshiminex.screen.ATMScreen;
import com.mycompany.mymod.satoshiminex.network.NetworkHandler;

import java.util.function.Supplier;

public class ConvertMessage {
    private final double btcAmount;
    private final long rubAmount;

    public ConvertMessage(double btcAmount, long rubAmount) {
        this.btcAmount = btcAmount;
        this.rubAmount = rubAmount;
    }

    public static void encode(ConvertMessage message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.btcAmount);
        buffer.writeLong(message.rubAmount);
    }

    public static ConvertMessage decode(FriendlyByteBuf buffer) {
        return new ConvertMessage(buffer.readDouble(), buffer.readLong());
    }

    public static void handle(ConvertMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof ATMScreen) {
                ATMScreen screen = (ATMScreen) Minecraft.getInstance().screen;
                // Обновляем отображение (например, перерендеринг)
                screen.init(Minecraft.getInstance(), screen.width, screen.height);
            }
        });
        ctx.setPacketHandled(true);
    }

    public static void send(PacketDistributor.PacketTarget target, double btcAmount, long rubAmount) {
        NetworkHandler.INSTANCE.send(target, new ConvertMessage(btcAmount, rubAmount));
    }
}
