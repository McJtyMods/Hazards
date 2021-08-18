package com.mcjty.hazards;

import com.mcjty.hazards.setup.Config;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (Config.BAD_RAIN.get()) {
            World world = event.player.getCommandSenderWorld();
            int ticks = Config.DAMAGE_TICKS.get();
            if (ticks <= 1 || world.getGameTime() % ticks == 0) {
                boolean rainingAt = world.isRainingAt(event.player.blockPosition());
            }
        }
    }

}