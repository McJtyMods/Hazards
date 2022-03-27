package com.mcjty.hazards;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.Set;

public class DamageHelpers {

    public static void applyPotionEffects(LivingEntity entity, float protectionFactor, float damageFactor, Set<Triple<MobEffect, Integer, Integer>> effects) {
        for (Triple<MobEffect, Integer, Integer> effect : effects) {
            int strength = effect.getRight();
            if (damageFactor < .7f) {
                strength--;
            }
            if (damageFactor < .5f) {
                strength--;
            }
            if (damageFactor < .2f) {
                strength--;
            }

            if (protectionFactor < .6) {
                strength--;
            }
            if (protectionFactor < .3) {
                strength--;
            }

            if (strength >= 0) {
                entity.addEffect(new MobEffectInstance(effect.getLeft(), effect.getMiddle(), strength));
            }
        }
    }

    public static float calculateProtectionFactor(LivingEntity entity, Map<ResourceLocation, Float> helperItems) {
        if (helperItems.isEmpty()) {
            return 1.0f;
        }
        float factor = 0.0f;
        for (ItemStack stack : entity.getArmorSlots()) {
            if (!stack.isEmpty()) {
                Float f = helperItems.get(stack.getItem().getRegistryName());
                if (f != null) {
                    factor += f;
                }
            }
        }
        if (factor >= 1) {
            return 0;
        }
        return 1 - factor;
    }

    public static float doDamage(LivingEntity entity, float damage, Map<ResourceLocation, Float> damageHelpers) {
        float protectionFactor = calculateProtectionFactor(entity, damageHelpers);
        damage *= protectionFactor;
        if (damage > 0.01) {
            entity.hurt(DamageSource.GENERIC, damage);
        }
        return protectionFactor;
    }
}
