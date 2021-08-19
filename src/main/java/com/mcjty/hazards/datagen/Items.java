package com.mcjty.hazards.datagen;

import com.mcjty.hazards.Hazards;
import com.mcjty.hazards.setup.RadiationBlock;
import com.mcjty.hazards.setup.RadiationTile;
import com.mcjty.hazards.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Hazards.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (int i = 0; i < RadiationTile.MAX_TIERS * 2; i++) {
            RadiationBlock block = Registration.RADIATION_BLOCKS[i].get();
            getBuilder(block.getRegistryName().getPath())
                    .parent(new ModelFile.UncheckedModelFile(modLoc("block/radiation_block" + i)));
        }
    }

    @Override
    public String getName() {
        return "Hazards Item Models";
    }
}
