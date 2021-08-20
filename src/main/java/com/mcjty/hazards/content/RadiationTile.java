package com.mcjty.hazards.content;

import com.mcjty.hazards.DamageHelpers;
import com.mcjty.hazards.setup.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class RadiationTile extends TileEntity implements ITickableTileEntity {

    public static final int MAX_TIERS = 3;

    private int counter = 0;
    private static final EntityPredicate DEFAULT = new EntityPredicate();

    private final int tier;
    private final double damage;
    private final double radius;
    private AxisAlignedBB box = null;

    public RadiationTile(TileEntityType<?> type, int tier) {
        super(type);
        tier = tier % MAX_TIERS;
        this.tier = tier;
        damage = Config.DAMAGE_BLOCK[tier].get();
        radius = Config.RADIUS_BLOCK[tier].get();
    }

    public AxisAlignedBB getBox() {
        if (box == null) {
            BlockPos p = getBlockPos();
            box = new AxisAlignedBB(p.getX() - radius, p.getY() - radius, p.getZ() - radius, p.getX() + radius, p.getY() + radius, p.getZ() + radius);
        }
        return box;
    }

    @Override
    public void tick() {
        if (!level.isClientSide()) {
            counter--;
            if (counter <= 0) {
                counter = Config.RADIATION_TICKS.get();
                double sqradius = radius * radius;
                level.getEntities((Entity) null, getBox(), e -> isEntityAffected(sqradius, e)).forEach(p -> {
                    LivingEntity entity = (LivingEntity) p;
                    double factor = calculateRadiation(p.blockPosition());
                    float d = (float) (damage * factor);
                    float protectionFactor = DamageHelpers.doDamage(entity, d, Config.getRadiationHelpers(tier));
                    DamageHelpers.applyPotionEffects(entity, protectionFactor, (float) factor, Config.getRadiationBlockEffects(tier));
                });
            }
        } else {
            // Client side we also keep radiation data
            ClientRadiationData.mark(level, getBlockPos());
        }
    }

    private boolean isEntityAffected(double sqradius, Entity e) {
        return (e instanceof PlayerEntity
                || (Config.AFFECT_VILLAGERS.get() && e instanceof VillagerEntity)
                || (Config.AFFECT_PASSIVE_CREATURES.get() && e instanceof AnimalEntity))
                && e.blockPosition().distSqr(getBlockPos()) < sqradius;
    }

    public double calculateRadiation(BlockPos p) {
        double dist = Math.sqrt(p.distSqr(getBlockPos()));
        double factor = 1.0 - dist / radius;
        if (factor < 0) {
            factor = 0;
        }
        return factor;
    }
}
