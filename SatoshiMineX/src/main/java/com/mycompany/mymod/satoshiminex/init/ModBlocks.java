package com.mycompany.mymod.satoshiminex.init;

import com.mycompany.mymod.satoshiminex.block.ATMBlock;
import com.mycompany.mymod.satoshiminex.block.SatoshiConverterBlock;
import com.mycompany.mymod.satoshiminex.block.SatoshiOreBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "satoshiminex");

    public static final RegistryObject<Block> SATOSHI_ORE = BLOCKS.register("satoshi_ore", SatoshiOreBlock::new);
    public static final RegistryObject<Block> SATOSHI_CONVERTER = BLOCKS.register("satoshi_converter", SatoshiConverterBlock::new);
    public static final RegistryObject<Block> ATM = BLOCKS.register("atm", ATMBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}