package com.mycompany.mymod.satoshiminex.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import com.mycompany.mymod.satoshiminex.screen.SatoshiConverterScreen;
import com.mycompany.mymod.satoshiminex.network.NetworkHandler;

import java.util.function.Supplier;

public class ExchangeMessage {
    private final long btcAmount;

    public ExchangeMessage(long btcAmount) {
        this.btcAmount = btcAmount;
    }

    public static void encode(ExchangeMessage message, FriendlyByteBuf buffer) {
        buffer.writeLong(message.btcAmount);
    }

    public static ExchangeMessage decode(FriendlyByteBuf buffer) {
        return new ExchangeMessage(buffer.readLong());
    }

    public static void handle(ExchangeMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof SatoshiConverterScreen) {
                SatoshiConverterScreen screen = (SatoshiConverterScreen) Minecraft.getInstance().screen;
                // Обновляем отображение (например, перерендеринг)
                screen.init(Minecraft.getInstance(), screen.width, screen.height);
            }
        });
        ctx.setPacketHandled(true);
    }

    public static void send(PacketDistributor.PacketTarget target, long btcAmount) {
        NetworkHandler.INSTANCE.send(target, new ExchangeMessage(btcAmount));
    }
}
