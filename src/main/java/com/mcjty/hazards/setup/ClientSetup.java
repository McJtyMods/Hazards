package com.mcjty.hazards.setup;

import com.mcjty.hazards.content.ClientRadiationData;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void initClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRadiationData.initOverrides(Registration.RADIATION_MONITOR.get());
        });
    }

}
