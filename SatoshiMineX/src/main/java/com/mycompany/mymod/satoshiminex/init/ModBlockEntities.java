package com.mycompany.mymod.satoshiminex.init;

import com.mycompany.mymod.satoshiminex.blockentity.ATMBlockEntity;
import com.mycompany.mymod.satoshiminex.blockentity.SatoshiConverterBlockEntity;
import com.mycompany.mymod.satoshiminex.block.ATMBlock;
import com.mycompany.mymod.satoshiminex.block.SatoshiConverterBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "satoshiminex");

    public static final RegistryObject<BlockEntityType<ATMBlockEntity>> ATM_BLOCK_ENTITY = BLOCK_ENTITIES.register("atm",
            () -> BlockEntityType.Builder.of(ATMBlockEntity::new, ModBlocks.ATM.get()).build());
    public static final RegistryObject<BlockEntityType<SatoshiConverterBlockEntity>> SATOSHI_CONVERTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("satoshi_converter",
            () -> BlockEntityType.Builder.of(SatoshiConverterBlockEntity::new, ModBlocks.SATOSHI_CONVERTER.get()).build());

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
