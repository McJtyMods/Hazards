package com.mcjty.hazards.content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import org.jetbrains.annotations.Nullable;

public class RadiationBlock extends Block implements EntityBlock {

    private final BlockEntityType.BlockEntitySupplier<RadiationTile> tileEntitySupplier;

    public RadiationBlock(Properties properties, BlockEntityType.BlockEntitySupplier<RadiationTile> tileEntitySupplier) {
        super(properties);
        this.tileEntitySupplier = tileEntitySupplier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return tileEntitySupplier.create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        if (level.isClientSide) {
           return (lev, pos, st, be) -> {
               if (be instanceof RadiationTile radiationTile) {
                   radiationTile.tickClient();
               }
           };
        } else {
            return (lev, pos, st, be) -> {
                if (be instanceof RadiationTile radiationTile) {
                    radiationTile.tickServer();
                }
            };
        }
    }
}