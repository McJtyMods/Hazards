package com.mcjty.hazards.datagen;

import com.mcjty.hazards.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Registration.RADIATION_MONITOR.get())
                .define('r', Tags.Items.DUSTS_REDSTONE)
                .define('C', Items.CLOCK)
                .define('q', Tags.Items.GEMS_QUARTZ)
                .unlockedBy("clock", has(Items.CLOCK))
                .pattern("rrr").pattern("qCq").pattern("rrr")
                .group("monitor").save(consumer);
    }
}
