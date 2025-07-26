package com.mycompany.mymod.satoshiminex.init;

import com.mycompany.mymod.satoshiminex.item.SatoshiItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "satoshiminex");

    public static final RegistryObject<Item> SATOSHI_ITEM = ITEMS.register("satoshi", SatoshiItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
