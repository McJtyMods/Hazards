package com.mcjty.hazards;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.Set;

public class DamageHelpers {

    public static void applyPotionEffects(PlayerEntity player, float protectionFactor, float damageFactor, Set<Triple<Effect, Integer, Integer>> effects) {
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
                player.addEffect(new EffectInstance(effect.getLeft(), effect.getMiddle(), strength));
            }
        }
    }

    public static float calculateProtectionFactor(PlayerEntity player, Map<ResourceLocation, Float> helperItems) {
        if (helperItems.isEmpty()) {
            return 1.0f;
        }
        float factor = 0.0f;
        for (ItemStack stack : player.getArmorSlots()) {
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

    public static float doDamage(PlayerEntity player, float damage, Map<ResourceLocation, Float> damageHelpers) {
        float protectionFactor = calculateProtectionFactor(player, damageHelpers);
        damage *= protectionFactor;
        if (damage > 0.01) {
            player.hurt(DamageSource.GENERIC, damage);
        }
        return protectionFactor;
    }
}
