package com.mcjty.hazards.datagen;

import com.mcjty.hazards.Hazards;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Hazards.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
