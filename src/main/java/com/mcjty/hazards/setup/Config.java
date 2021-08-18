package com.mcjty.hazards.setup;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class Config {

    public static final Builder SERVER_BUILDER = new Builder();

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue BAD_RAIN;
    public static ForgeConfigSpec.IntValue RAIN_DAMAGE;
    public static ForgeConfigSpec.BooleanValue BAD_SUN;
    public static ForgeConfigSpec.IntValue SUN_DAMAGE;
    public static ForgeConfigSpec.IntValue DAMAGE_TICKS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> SUN_DAMAGE_EFFECTS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> RAIN_DAMAGE_EFFECTS;


    public static void register() {
        SERVER_BUILDER.comment("General settings").push("general");

        BAD_RAIN = SERVER_BUILDER
                .comment("If true the rain will hurt")
                .define("badRain", true);
        RAIN_DAMAGE = SERVER_BUILDER
                .comment("If bad rain is enabled then this is the amount of damage done to the player")
                .defineInRange("rainDamage", 1, 0, 10000);
        BAD_SUN = SERVER_BUILDER
                .comment("If true the sun will hurt")
                .define("badSun", false);
        SUN_DAMAGE = SERVER_BUILDER
                .comment("If bad sun is enabled then this is the amount of damage done to the player")
                .defineInRange("sunDamage", 1, 0, 10000);
        DAMAGE_TICKS = SERVER_BUILDER
                .comment("If bad rain or bad sun is enabled then this is the amount of ticks to wait between every damage")
                .defineInRange("damageTicks", 10, 0, 10000);

        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }

}
