package com.mcjty.hazards;

import com.mcjty.hazards.setup.ClientSetup;
import com.mcjty.hazards.setup.Config;
import com.mcjty.hazards.setup.ModSetup;
import com.mcjty.hazards.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Hazards.MODID)
public class Hazards {

    public static final String MODID = "hazards";

    @SuppressWarnings("PublicField")
    public static ModSetup setup = new ModSetup();

    public Hazards() {
        Config.register();
        Registration.register();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(setup::init);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            bus.addListener(ClientSetup::initClient);
        });
    }
}
