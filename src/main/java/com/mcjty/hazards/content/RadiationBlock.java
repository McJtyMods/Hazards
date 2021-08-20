package com.mcjty.hazards.content;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class RadiationBlock extends Block {

    private final Supplier<TileEntity> tileEntitySupplier;

    public RadiationBlock(Properties properties, Supplier<TileEntity> tileEntitySupplier) {
        super(properties);
        this.tileEntitySupplier = tileEntitySupplier;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return this.tileEntitySupplier != null;
    }

    @Override
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return this.tileEntitySupplier == null ? null : this.tileEntitySupplier.get();
    }
}