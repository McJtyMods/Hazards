package com.mcjty.hazards.content;

import com.mcjty.hazards.Hazards;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

public class ClientRadiationData {

    private static final Set<Pair<RegistryKey<World>, BlockPos>> radiationCenters = new HashSet<>();
    public static final ResourceLocation RAD_PROPERTY = new ResourceLocation(Hazards.MODID, "rad");

    public static void mark(World world, BlockPos pos) {
        radiationCenters.add(Pair.of(world.dimension(), pos));
    }

    public static double checkRadiation(World world, BlockPos pos) {
        Set<Pair<RegistryKey<World>, BlockPos>> toRemove = new HashSet<>();
        double radiation = 0.0;
        for (Pair<RegistryKey<World>, BlockPos> pair : radiationCenters) {
            if (pair.getLeft().equals(world.dimension())) {
                if (world.isLoaded(pair.getRight())) {
                    TileEntity blockEntity = world.getBlockEntity(pair.getRight());
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
        ItemModelsProperties.register(item, RAD_PROPERTY, (stack, world, livingEntity) -> {
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
