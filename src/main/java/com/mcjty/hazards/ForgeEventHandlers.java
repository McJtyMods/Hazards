package com.mcjty.hazards;

import com.mcjty.hazards.setup.Config;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (Config.BAD_RAIN.get() || Config.BAD_SUN.get()) {
            World world = event.player.getCommandSenderWorld();
            int ticks = Config.DAMAGE_TICKS.get();
            if (ticks <= 1 || world.getGameTime() % ticks == 0) {
                if (Config.BAD_RAIN.get() && Config.getRainDimensions().contains(world.dimension().location())) {
                    if (world.isRainingAt(event.player.blockPosition())) {
                        float damage = (float) (double) Config.RAIN_DAMAGE.get();
                        float protectionFactor = DamageHelpers.doDamage(event.player, damage, Config.getRainDamageHelpers());
                        DamageHelpers.applyPotionEffects(event.player, protectionFactor, 1.0f, Config.getRainDamageEffects());
                    }
                }
                if (Config.BAD_SUN.get() && Config.getSunDimensions().contains(world.dimension().location())) {
                    if (world.canSeeSky(event.player.blockPosition())) {
                        long time = world.getDayTime() % 24000;
                        long dist = Math.abs(time - 6000);
                        if (dist < 5500) {
                            float factor = (6000f-dist) / 6000f;
                            float damage = factor * (float) (double) Config.SUN_DAMAGE.get();
                            float protectionFactor = DamageHelpers.doDamage(event.player, damage, Config.getSunDamageHelpers());
                            DamageHelpers.applyPotionEffects(event.player, protectionFactor, 1.0f, Config.getSunDamageEffects());
                        }
                    }
                }
            }
        }
    }

}