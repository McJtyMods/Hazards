package com.mcjty.hazards.content;

import com.mcjty.hazards.Hazards;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

public class ClientRadiationData {

    private static final Set<Pair<ResourceKey<Level>, BlockPos>> radiationCenters = new HashSet<>();
    public static final ResourceLocation RAD_PROPERTY = new ResourceLocation(Hazards.MODID, "rad");

    public static void mark(Level world, BlockPos pos) {
        radiationCenters.add(Pair.of(world.dimension(), pos));
    }

    public static double checkRadiation(Level world, BlockPos pos) {
        Set<Pair<ResourceKey<Level>, BlockPos>> toRemove = new HashSet<>();
        double radiation = 0.0;
        for (Pair<ResourceKey<Level>, BlockPos> pair : radiationCenters) {
            if (pair.getLeft().equals(world.dimension())) {
                if (world.isLoaded(pair.getRight())) {
                    BlockEntity blockEntity = world.getBlockEntity(pair.getRight());
                    if (blockEntity instanceof RadiationTile) {
                        double rad = ((RadiationTile) blockEntity).calculateRadiation(pos);
                        radiation += rad;
                    } else {
                        toRemove.add(pair);
                    }
                }
            }
        }
        radiationCenters.removeAll(toRemove);
        return radiation;
    }

    public static void initOverrides(RadiationMonitorItem item) {
        ItemProperties.register(item, RAD_PROPERTY, (stack, world, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0;
            }
            if (world == null) {
                world = Minecraft.getInstance().level;
            }
            double radiation = checkRadiation(world, livingEntity.blockPosition());
            int rad = (int) (radiation * 6.5f);
            if (rad < 0) {
                rad = 0;
            } else if (rad > 6) {
                rad = 6;
            }
            return rad;
        });
    }


}
