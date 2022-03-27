package com.mcjty.hazards.content;

import com.mcjty.hazards.DamageHelpers;
import com.mcjty.hazards.setup.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;

public class RadiationTile extends BlockEntity {

    public static final int MAX_TIERS = 3;

    private int counter = 0;
    private static final TargetingConditions DEFAULT = TargetingConditions.DEFAULT;

    private final int tier;
    private final double damage;
    private final double radius;
    private AABB box = null;

    public RadiationTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int tier) {
        super(type, pos, state);
        tier = tier % MAX_TIERS;
        this.tier = tier;
        damage = Config.DAMAGE_BLOCK[tier].get();
        radius = Config.RADIUS_BLOCK[tier].get();
    }

    public AABB getBox() {
        if (box == null) {
            BlockPos p = getBlockPos();
            box = new AABB(p.getX() - radius, p.getY() - radius, p.getZ() - radius, p.getX() + radius, p.getY() + radius, p.getZ() + radius);
        }
        return box;
    }

    public void tickServer() {
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
    }

    public void tickClient() {
        // Client side we also keep radiation data
        ClientRadiationData.mark(level, getBlockPos());
    }

    private boolean isEntityAffected(double sqradius, Entity e) {
        return (e instanceof Player
                || (Config.AFFECT_VILLAGERS.get() && e instanceof Villager)
                || (Config.AFFECT_PASSIVE_CREATURES.get() && e instanceof Animal))
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
