package com.mcjty.hazards.setup;


import com.mcjty.hazards.content.RadiationTile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
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
    public static ForgeConfigSpec.IntValue RADIATION_TICKS;
    public static ForgeConfigSpec.IntValue DAMAGE_TICKS;
    public static ForgeConfigSpec.BooleanValue AFFECT_VILLAGERS;
    public static ForgeConfigSpec.BooleanValue AFFECT_PASSIVE_CREATURES;

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> SUN_DIMENSIONS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> RAIN_DIMENSIONS;
    private static Set<ResourceLocation> sunDimensions = null;
    private static Set<ResourceLocation> rainDimensions = null;

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> SUN_DAMAGE_EFFECTS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> RAIN_DAMAGE_EFFECTS;
    private static Set<Triple<MobEffect, Integer, Integer>> sunDamageEffects = null;
    private static Set<Triple<MobEffect, Integer, Integer>> rainDamageEffects = null;

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> SUN_DAMAGE_HELPERS;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> RAIN_DAMAGE_HELPERS;
    private static Map<ResourceLocation, Float> sunDamageHelpers = null;
    private static Map<ResourceLocation, Float> rainDamageHelpers = null;

    public static ForgeConfigSpec.DoubleValue RADIUS_BLOCK[] = new ForgeConfigSpec.DoubleValue[RadiationTile.MAX_TIERS];
    public static ForgeConfigSpec.DoubleValue DAMAGE_BLOCK[] = new ForgeConfigSpec.DoubleValue[RadiationTile.MAX_TIERS];
    private static ForgeConfigSpec.ConfigValue<List<? extends String>>[] RADIATION_BLOCK_EFFECTS = new ForgeConfigSpec.ConfigValue[RadiationTile.MAX_TIERS];
    private static Set<Triple<MobEffect, Integer, Integer>>[] radiationBlockEffects = null;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>>[] RADIATION_HELPERS = new ForgeConfigSpec.ConfigValue[RadiationTile.MAX_TIERS];
    private static Map<ResourceLocation, Float>[] radiationHelpers = null;


    public static void register() {
        SERVER_BUILDER.comment("General settings").push("general");

        DAMAGE_TICKS = SERVER_BUILDER
                .comment("If bad rain or bad sun is enabled then this is the amount of ticks to wait between every damage")
                .defineInRange("damageTicks", 10, 0, 10000);
        RADIATION_TICKS = SERVER_BUILDER
                .comment("The amount of ticks to wait between radiation damage from the radiation block")
                .defineInRange("radiationDamageTicks", 20, 0, 10000);
        AFFECT_VILLAGERS = SERVER_BUILDER
                .comment("If true then all bad effects will also happen to villagers")
                .define("affectVillagers", false);
        AFFECT_PASSIVE_CREATURES = SERVER_BUILDER
                .comment("If true then all bad effects will also happen passive creatures")
                .define("affectPassiveCreatures", false);

        registerBadRainConfigs();
        registerBadSunConfigs();
        registerRadiationConfigs();

        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }

    private static void registerRadiationConfigs() {
        SERVER_BUILDER.comment("Radiation block settings").push("radiation");

        SERVER_BUILDER.comment("The maximum distance from the radiation block where players will be affected").push("radius");
        for (int i = 0 ; i < RadiationTile.MAX_TIERS ; i++) {
            RADIUS_BLOCK[i] = SERVER_BUILDER
                    .defineInRange("radiusBlock" + i, 10.0 * (i + 1), 0, 10000);
        }
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("The maximum damage (as applied next to the radiation block) per configured amount of ticks").push("damage");
        for (int i = 0 ; i < RadiationTile.MAX_TIERS ; i++) {
            DAMAGE_BLOCK[i] = SERVER_BUILDER
                    .defineInRange("damageBlock" + i, 0.5 * (i + 1), 0, 10000);
        }
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("A list of potion effects that are applied for the radiation block. Format is <effectname>/<duration>/<strength>").push("effects");
        for (int i = 0 ; i < RadiationTile.MAX_TIERS ; i++) {
            RADIATION_BLOCK_EFFECTS[i] = SERVER_BUILDER
                    .defineList("radiationBlockEffects" + i, Arrays.asList("minecraft:poison/50/" + (i+1)),
                            s -> s instanceof String);
        }
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("A list of items that can help reduce radiation damage from the radiation block. Format is <itemname>/<factor> where a factor 1 means that it will reduce all damage and 0 means no damage is reduced").push("items");
        for (int i = 0 ; i < RadiationTile.MAX_TIERS ; i++) {
            RADIATION_HELPERS[i] = SERVER_BUILDER
                    .defineList("radiationHelpers" + i, Arrays.asList("minecraft:diamond_helmet/.6", "minecraft:netherite_helmet/.9"),
                            s -> s instanceof String);
        }
        SERVER_BUILDER.pop();

        SERVER_BUILDER.pop();
    }

    private static void registerBadSunConfigs() {
        SERVER_BUILDER.comment("Bad sun settings").push("sun");

        BAD_SUN = SERVER_BUILDER
                .comment("If true the sun will hurt")
                .define("badSun", false);
        SUN_DAMAGE = SERVER_BUILDER
                .comment("If bad sun is enabled then this is the amount of damage done to the player (note that this damage is scaled based on how high the sun is in the sky, the value here represents the maximum value that you get when the sun is at its peak)")
                .defineInRange("sunDamage", 2.0, 0, 10000);
        SUN_DAMAGE_EFFECTS = SERVER_BUILDER
                .comment("A list of potion effects that are applied when bad sun is enabled and the player is unprotected. Format is <effectname>/<duration>/<strength>")
                .defineList("sunDamageEffects", new ArrayList<>(),
                        s -> s instanceof String);
        SUN_DAMAGE_HELPERS = SERVER_BUILDER
                .comment("A list of items that can help reduce sun damage. Format is <itemname>/<factor> where a factor 1 means that it will reduce all damage and 0 means no damage is reduced")
                .defineList("sunDamageHelpers", new ArrayList<>(),
                        s -> s instanceof String);
        SUN_DIMENSIONS = SERVER_BUILDER
                .comment("A list of dimensions where sun damage can occur")
                .defineList("sunDamageDimensions", Arrays.asList("minecraft:overworld"),
                        s -> s instanceof String);

        SERVER_BUILDER.pop();
    }

    private static void registerBadRainConfigs() {
        SERVER_BUILDER.comment("Bad rain settings").push("rain");

        BAD_RAIN = SERVER_BUILDER
                .comment("If true the rain will hurt")
                .define("badRain", true);
        RAIN_DAMAGE = SERVER_BUILDER
                .comment("If bad rain is enabled then this is the amount of damage done to the player")
                .defineInRange("rainDamage", 0.5, 0, 100000);
        RAIN_DAMAGE_EFFECTS = SERVER_BUILDER
                .comment("A list of potion effects that are applied when bad rain is enabled and the player is in the rain. Format is <effectname>/<duration>/<strength>")
                .defineList("rainDamageEffects", new ArrayList<>(),
                        s -> s instanceof String);
        RAIN_DAMAGE_HELPERS = SERVER_BUILDER
                .comment("A list of items that can help reduce rain damage. Format is <itemname>/<factor> where a factor 1 means that it will reduce all damage and 0 means no damage is reduced")
                .defineList("rainDamageHelpers", Arrays.asList("minecraft:diamond_helmet/.6", "minecraft:netherite_helmet/.9"),
                        s -> s instanceof String);
        RAIN_DIMENSIONS = SERVER_BUILDER
                .comment("A list of dimensions where rain damage can occur")
                .defineList("rainDamageDimensions", Arrays.asList("minecraft:overworld"),
                        s -> s instanceof String);

        SERVER_BUILDER.pop();
    }

    public static Set<Triple<MobEffect, Integer, Integer>> getSunDamageEffects() {
        if (sunDamageEffects == null) {
            sunDamageEffects = new HashSet<>();
            parsePotionEffects(SUN_DAMAGE_EFFECTS, sunDamageEffects);
        }
        return sunDamageEffects;
    }

    public static Set<Triple<MobEffect, Integer, Integer>> getRainDamageEffects() {
        if (rainDamageEffects == null) {
            rainDamageEffects = new HashSet<>();
            parsePotionEffects(RAIN_DAMAGE_EFFECTS, rainDamageEffects);
        }
        return rainDamageEffects;
    }

    public static Map<ResourceLocation, Float> getSunDamageHelpers() {
        if (sunDamageHelpers == null) {
            sunDamageHelpers = new HashMap<>();
            parseItems(SUN_DAMAGE_HELPERS, sunDamageHelpers);
        }
        return sunDamageHelpers;
    }

    public static Map<ResourceLocation, Float> getRainDamageHelpers() {
        if (rainDamageHelpers == null) {
            rainDamageHelpers = new HashMap<>();
            parseItems(RAIN_DAMAGE_HELPERS, rainDamageHelpers);
        }
        return rainDamageHelpers;
    }

    public static Set<Triple<MobEffect, Integer, Integer>> getRadiationBlockEffects(int tier) {
        if (radiationBlockEffects == null) {
            radiationBlockEffects = new Set[RadiationTile.MAX_TIERS];
            for (int i = 0 ; i < RadiationTile.MAX_TIERS ; i++) {
                radiationBlockEffects[i] = new HashSet<>();
                parsePotionEffects(RADIATION_BLOCK_EFFECTS[i], radiationBlockEffects[i]);
            }
        }
        return radiationBlockEffects[tier];
    }

    public static Map<ResourceLocation, Float> getRadiationHelpers(int tier) {
        if (radiationHelpers == null) {
            radiationHelpers = new HashMap[RadiationTile.MAX_TIERS];
            for (int i = 0 ; i < RadiationTile.MAX_TIERS ; i++) {
                radiationHelpers[i] = new HashMap<>();
                parseItems(RADIATION_HELPERS[i], radiationHelpers[i]);
            }
        }
        return radiationHelpers[tier];
    }

    public static Set<ResourceLocation> getSunDimensions() {
        if (sunDimensions == null) {
            sunDimensions = new HashSet<>();
            parseDimensions(SUN_DIMENSIONS, sunDimensions);
        }
        return sunDimensions;
    }

    public static Set<ResourceLocation> getRainDimensions() {
        if (rainDimensions == null) {
            rainDimensions = new HashSet<>();
            parseDimensions(RAIN_DIMENSIONS, rainDimensions);
        }
        return rainDimensions;
    }

    private static void parsePotionEffects(ForgeConfigSpec.ConfigValue<List<? extends String>> list, Set<Triple<MobEffect, Integer, Integer>> effects) {
        for (String s : list.get()) {
            String[] splitted = StringUtils.split(s, "/");
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(splitted[0]));
            if (effect == null) {
                throw new IllegalStateException("Can't find effect '" + splitted[0] + "'!");
            }
            int duration = Integer.parseInt(splitted[1]);
            int strength = Integer.parseInt(splitted[2]);
            effects.add(Triple.of(effect, duration, strength));
        }
    }

    private static void parseItems(ForgeConfigSpec.ConfigValue<List<? extends String>> list, Map<ResourceLocation, Float> damageHelpers) {
        for (String s : list.get()) {
            String[] splitted = StringUtils.split(s, "/");
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(splitted[0]));
            if (item == null) {
                throw new IllegalStateException("Can't find item '" + splitted[0] + "'!");
            }
            float factor = Float.parseFloat(splitted[1]);
            damageHelpers.put(new ResourceLocation(splitted[0]), factor);
        }
    }

    private static void parseDimensions(ForgeConfigSpec.ConfigValue<List<? extends String>> list, Set<ResourceLocation> dimensions) {
        for (String s : list.get()) {
            dimensions.add(new ResourceLocation(s));
        }
    }
}
