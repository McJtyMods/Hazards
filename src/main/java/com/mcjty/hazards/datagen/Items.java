package com.mcjty.hazards.datagen;

import com.mcjty.hazards.Hazards;
import com.mcjty.hazards.content.RadiationBlock;
import com.mcjty.hazards.content.RadiationTile;
import com.mcjty.hazards.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.mcjty.hazards.content.ClientRadiationData.RAD_PROPERTY;

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

        getBuilder(Registration.RADIATION_MONITOR.get().getRegistryName().getPath())
                .parent(getExistingFile(mcLoc("item/handheld")))
                .texture("layer0", "item/rad0")
                .override().predicate(RAD_PROPERTY, 0).model(createMonitorModel(0)).end()
                .override().predicate(RAD_PROPERTY, 1).model(createMonitorModel(1)).end()
                .override().predicate(RAD_PROPERTY, 2).model(createMonitorModel(2)).end()
                .override().predicate(RAD_PROPERTY, 3).model(createMonitorModel(3)).end()
                .override().predicate(RAD_PROPERTY, 4).model(createMonitorModel(4)).end()
                .override().predicate(RAD_PROPERTY, 5).model(createMonitorModel(5)).end()
                .override().predicate(RAD_PROPERTY, 6).model(createMonitorModel(6)).end()
        ;
    }

    private ItemModelBuilder createMonitorModel(int suffix) {
        return getBuilder("radiation_monitor" + suffix).parent(getExistingFile(mcLoc("item/handheld")))
                .texture("layer0", "item/rad" + suffix);
    }

    @Override
    public String getName() {
        return "Hazards Item Models";
    }
}
