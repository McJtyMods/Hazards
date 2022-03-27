package com.mcjty.hazards.datagen;

import com.mcjty.hazards.Hazards;
import com.mcjty.hazards.content.RadiationBlock;
import com.mcjty.hazards.content.RadiationTile;
import com.mcjty.hazards.setup.Registration;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Hazards.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (int i = 0; i < RadiationTile.MAX_TIERS * 2; i++) {
            RadiationBlock block = Registration.RADIATION_BLOCKS[i].get();
            ResourceLocation texture = blockTexture(Blocks.STONE);
            simpleBlock(block, models().cubeAll(block.getRegistryName().getPath(), texture));
        }
    }
}
