package com.mycompany.mymod.satoshiminex.block;

import com.mycompany.mymod.satoshiminex.init.ModItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class SatoshiOreBlock extends DropExperienceBlock {
    public SatoshiOreBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .strength(3.0f)
                .requiresCorrectToolForDrops()
                .explosionResistance(6.0f));
    }

    // Можно переопределить drop, чтобы гарантировать сатоши
    // @Override
    // public void appendDrops(BlockState state, LootContext.Builder builder) {
    //     builder.withParameter(LootContextParams.TOOL, ItemStack.EMPTY);
    //     super.appendDrops(state, builder);
    //     builder.addDrop(ModItems.SATOSHI_ITEM.get());
    // }
}