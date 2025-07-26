package com.mycompany.mymod.satoshiminex.init;

import com.mycompany.mymod.satoshiminex.menu.ATMMenu;
import com.mycompany.mymod.satoshiminex.menu.SatoshiConverterMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "satoshiminex");

    public static final RegistryObject<MenuType<ATMMenu>> ATM_MENU = MENUS.register("atm",
            () -> IForgeMenuType.create(ATMMenu::new));
    public static final RegistryObject<MenuType<SatoshiConverterMenu>> SATOSHI_CONVERTER_MENU = MENUS.register("satoshi_converter",
            () -> IForgeMenuType.create(SatoshiConverterMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
