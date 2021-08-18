package com.mcjty.hazards.datagen;

import com.mcjty.hazards.Hazards;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Hazards.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }

    @Override
    public String getName() {
        return "Hazards Item Models";
    }
}
