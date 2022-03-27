package com.mcjty.hazards.setup;

import com.mcjty.hazards.Hazards;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class Messages {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void registerMessages(String name) {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Hazards.MODID, name))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

//        net.messageBuilder(PacketUpdateSignData.class, id())
//                .encoder(PacketUpdateSignData::toBytes)
//                .decoder(PacketUpdateSignData::new)
//                .consumer(PacketUpdateSignData::handle)
//                .add();
//
//        PacketHandler.registerStandardMessages(id(), net);
    }
}
