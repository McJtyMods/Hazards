package com.mcjty.hazards;

import com.mcjty.hazards.setup.Config;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onEntityTick(LivingEvent.LivingUpdateEvent event) {
        if (Config.BAD_RAIN.get() || Config.BAD_SUN.get()) {
            if (event.getEntity() instanceof AnimalEntity) {
                if (Config.AFFECT_PASSIVE_CREATURES.get()) {
                    doRadiationDamage(event.getEntityLiving());
                }
            } else if (event.getEntity() instanceof VillagerEntity) {
                if (Config.AFFECT_VILLAGERS.get()) {
                    doRadiationDamage(event.getEntityLiving());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (Config.BAD_RAIN.get() || Config.BAD_SUN.get()) {
            doRadiationDamage(event.player);
        }
    }

    private void doRadiationDamage(LivingEntity entity) {
        World world = entity.getCommandSenderWorld();
        int ticks = Config.DAMAGE_TICKS.get();
        if (ticks <= 1 || world.getGameTime() % ticks == 0) {
            if (Config.BAD_RAIN.get() && Config.getRainDimensions().contains(world.dimension().location())) {
                if (world.isRainingAt(entity.blockPosition())) {
                    float damage = (float) (double) Config.RAIN_DAMAGE.get();
                    float protectionFactor = DamageHelpers.doDamage(entity, damage, Config.getRainDamageHelpers());
                    DamageHelpers.applyPotionEffects(entity, protectionFactor, 1.0f, Config.getRainDamageEffects());
                }
            }
            if (Config.BAD_SUN.get() && Config.getSunDimensions().contains(world.dimension().location())) {
                if (world.canSeeSky(entity.blockPosition())) {
                    long time = world.getDayTime() % 24000;
                    long dist = Math.abs(time - 6000);
                    if (dist < 5500) {
                        float factor = (6000f-dist) / 6000f;
                        float damage = factor * (float) (double) Config.SUN_DAMAGE.get();
                        float protectionFactor = DamageHelpers.doDamage(entity, damage, Config.getSunDamageHelpers());
                        DamageHelpers.applyPotionEffects(entity, protectionFactor, 1.0f, Config.getSunDamageEffects());
                    }
                }
            }
        }
    }

}