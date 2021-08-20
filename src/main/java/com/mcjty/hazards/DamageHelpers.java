package com.mcjty.hazards;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.Set;

public class DamageHelpers {

    public static void applyPotionEffects(LivingEntity entity, float protectionFactor, float damageFactor, Set<Triple<Effect, Integer, Integer>> effects) {
        for (Triple<Effect, Integer, Integer> effect : effects) {
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
                entity.addEffect(new EffectInstance(effect.getLeft(), effect.getMiddle(), strength));
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
