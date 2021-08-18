package com.mcjty.hazards.setup;

import com.mcjty.hazards.ForgeEventHandlers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {

    public void init(FMLCommonSetupEvent e) {
        Messages.registerMessages("hazards");
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
    }
}
