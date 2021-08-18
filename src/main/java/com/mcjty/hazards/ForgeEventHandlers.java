package com.mcjty.hazards;

import com.mcjty.hazards.setup.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.Set;

public class ForgeEventHandlers {

    private float calculateProtectionFactor(PlayerEntity player, Map<ResourceLocation, Float> helperItems) {
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

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (Config.BAD_RAIN.get() || Config.BAD_SUN.get()) {
            World world = event.player.getCommandSenderWorld();
            int ticks = Config.DAMAGE_TICKS.get();
            if (ticks <= 1 || world.getGameTime() % ticks == 0) {
                if (Config.BAD_RAIN.get()) {
                    if (world.isRainingAt(event.player.blockPosition())) {
                        float damage = (float) (double) Config.RAIN_DAMAGE.get();
                        float protectionFactor = doDamage(event.player, damage, Config.getRainDamageHelpers());
                        applyPotionEffects(event.player, protectionFactor, Config.getRainDamageEffects());
                    }
                }
                if (Config.BAD_SUN.get()) {
                    if (world.canSeeSky(event.player.blockPosition())) {
                        long time = world.getDayTime() % 24000;
                        long dist = Math.abs(time - 6000);
                        if (dist < 5500) {
                            float factor = (6000f-dist) / 6000f;
                            float damage = factor * (float) (double) Config.SUN_DAMAGE.get();
                            float protectionFactor = doDamage(event.player, damage, Config.getSunDamageHelpers());
                            applyPotionEffects(event.player, protectionFactor, Config.getSunDamageEffects());
                        }
                    }
                }
            }
        }
    }

    private float doDamage(PlayerEntity player, float damage, Map<ResourceLocation, Float> rainDamageHelpers) {
        float protectionFactor = calculateProtectionFactor(player, rainDamageHelpers);
        damage *= protectionFactor;
        if (damage > 0.01) {
            player.hurt(DamageSource.GENERIC, damage);
        }
        return protectionFactor;
    }

    private void applyPotionEffects(PlayerEntity player, float protectionFactor, Set<Triple<Effect, Integer, Integer>> effects) {
        for (Triple<Effect, Integer, Integer> effect : effects) {
            int strength = effect.getRight();
            if (protectionFactor < .5) {
                strength = strength < 1 ? 0 : strength - 1;
            }
            if (protectionFactor > .1) {
                player.addEffect(new EffectInstance(effect.getLeft(), effect.getMiddle(), strength));
            }
        }
    }

}