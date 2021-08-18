package com.mcjty.hazards.setup;


import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

public class Config {

    public static final Builder SERVER_BUILDER = new Builder();

    public static ForgeConfigSpec SERVER_CONFIG;

    public static ForgeConfigSpec.BooleanValue BAD_RAIN;
    public static ForgeConfigSpec.DoubleValue RAIN_DAMAGE;
    public static ForgeConfigSpec.BooleanValue BAD_SUN;
    public static ForgeConfigSpec.DoubleValue SUN_DAMAGE;
    public static ForgeConfigSpec.IntValue DAMAGE_TICKS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> SUN_DAMAGE_EFFECTS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> RAIN_DAMAGE_EFFECTS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> SUN_DAMAGE_HELPERS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> RAIN_DAMAGE_HELPERS;

    private static Set<Triple<Effect, Integer, Integer>> sunDamageEffects = null;
    private static Set<Triple<Effect, Integer, Integer>> rainDamageEffects = null;
    private static Map<ResourceLocation, Float> sunDamageHelpers = null;
    private static Map<ResourceLocation, Float> rainDamageHelpers = null;


    public static void register() {
        SERVER_BUILDER.comment("General settings").push("general");

        BAD_RAIN = SERVER_BUILDER
                .comment("If true the rain will hurt")
                .define("badRain", true);
        RAIN_DAMAGE = SERVER_BUILDER
                .comment("If bad rain is enabled then this is the amount of damage done to the player")
                .defineInRange("rainDamage", 0.5, 0, 100000);
        BAD_SUN = SERVER_BUILDER
                .comment("If true the sun will hurt")
                .define("badSun", false);
        SUN_DAMAGE = SERVER_BUILDER
                .comment("If bad sun is enabled then this is the amount of damage done to the player (note that this damage is scaled based on how high the sun is in the sky, the value here represents the maximum value that you get when the sun is at its peak)")
                .defineInRange("sunDamage", 2.0, 0, 10000);
        DAMAGE_TICKS = SERVER_BUILDER
                .comment("If bad rain or bad sun is enabled then this is the amount of ticks to wait between every damage")
                .defineInRange("damageTicks", 10, 0, 10000);
        RAIN_DAMAGE_EFFECTS = SERVER_BUILDER
                .comment("A list of potion effects that are applied when bad rain is enabled and the player is in the rain. Format is <effectname>/<duration>/<strength>")
                .defineList("rainDamageEffects", new ArrayList<>(),
                        s -> s instanceof String);
        SUN_DAMAGE_EFFECTS = SERVER_BUILDER
                .comment("A list of potion effects that are applied when bad sun is enabled and the player is unprotected. Format is <effectname>/<duration>/<strength>")
                .defineList("sunDamageEffects", new ArrayList<>(),
                        s -> s instanceof String);
        RAIN_DAMAGE_HELPERS = SERVER_BUILDER
                .comment("A list of items that can help reduce rain damage. Format is <itemname>/<factor> where a factor 1 means that it will reduce all damage and 0 means no damage is reduced")
                .defineList("rainDamageHelpers", new ArrayList<>(),
                        s -> s instanceof String);
        SUN_DAMAGE_HELPERS = SERVER_BUILDER
                .comment("A list of items that can help reduce sun damage. Format is <itemname>/<factor> where a factor 1 means that it will reduce all damage and 0 means no damage is reduced")
                .defineList("sunDamageHelpers", new ArrayList<>(),
                        s -> s instanceof String);

        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }

    public static Set<Triple<Effect, Integer, Integer>> getSunDamageEffects() {
        if (sunDamageEffects == null) {
            sunDamageEffects = new HashSet<>();
            for (String s : SUN_DAMAGE_EFFECTS.get()) {
                String[] splitted = StringUtils.split(s, "/");
                Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(splitted[0]));
                if (effect == null) {
                    throw new IllegalStateException("Can't find effect '" + splitted[0] + "'!");
                }
                int duration = Integer.parseInt(splitted[1]);
                int strength = Integer.parseInt(splitted[2]);
                sunDamageEffects.add(Triple.of(effect, duration, strength));
            }
        }
        return sunDamageEffects;
    }

    public static Set<Triple<Effect, Integer, Integer>> getRainDamageEffects() {
        if (rainDamageEffects == null) {
            rainDamageEffects = new HashSet<>();
            for (String s : RAIN_DAMAGE_EFFECTS.get()) {
                String[] splitted = StringUtils.split(s, "/");
                Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(splitted[0]));
                if (effect == null) {
                    throw new IllegalStateException("Can't find effect '" + splitted[0] + "'!");
                }
                int duration = Integer.parseInt(splitted[1]);
                int strength = Integer.parseInt(splitted[2]);
                rainDamageEffects.add(Triple.of(effect, duration, strength));
            }
        }
        return rainDamageEffects;
    }

    public static Map<ResourceLocation, Float> getSunDamageHelpers() {
        if (sunDamageHelpers == null) {
            sunDamageHelpers = new HashMap<>();
            for (String s : SUN_DAMAGE_HELPERS.get()) {
                String[] splitted = StringUtils.split(s, "/");
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(splitted[0]));
                if (item == null) {
                    throw new IllegalStateException("Can't find item '" + splitted[0] + "'!");
                }
                float factor = Float.parseFloat(splitted[1]);
                sunDamageHelpers.put(new ResourceLocation(splitted[0]), factor);
            }
        }
        return sunDamageHelpers;
    }

    public static Map<ResourceLocation, Float> getRainDamageHelpers() {
        if (rainDamageHelpers == null) {
            rainDamageHelpers = new HashMap<>();
            for (String s : RAIN_DAMAGE_HELPERS.get()) {
                String[] splitted = StringUtils.split(s, "/");
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(splitted[0]));
                if (item == null) {
                    throw new IllegalStateException("Can't find item '" + splitted[0] + "'!");
                }
                float factor = Float.parseFloat(splitted[1]);
                rainDamageHelpers.put(new ResourceLocation(splitted[0]), factor);
            }
        }
        return rainDamageHelpers;
    }

}
