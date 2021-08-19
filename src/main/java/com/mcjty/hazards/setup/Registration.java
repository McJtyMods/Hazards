package com.mcjty.hazards.setup;


import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.mcjty.hazards.Hazards.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);

    public static final RegistryObject<RadiationBlock>[] RADIATION_BLOCKS = new RegistryObject[RadiationTile.MAX_TIERS * 2];
    public static final RegistryObject<BlockItem>[] RADIATION_ITEMS = new RegistryObject[RadiationTile.MAX_TIERS * 2];
    public static final RegistryObject<TileEntityType<RadiationTile>>[] RADIATION_TILES = new RegistryObject[RadiationTile.MAX_TIERS * 2];

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);

        for (int i = 0 ; i < RadiationTile.MAX_TIERS * 2 ; i++) {
            int finalI = i;
            AbstractBlock.Properties properties;
            if (i < RadiationTile.MAX_TIERS) {
                properties = AbstractBlock.Properties.of(Material.STONE)
                        .requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                        .noDrops();
            } else {
                properties = AbstractBlock.Properties.of(Material.STONE)
                        .requiresCorrectToolForDrops().strength(-1.0F, 3600000.0F)
                        .noDrops();
            }
            RADIATION_BLOCKS[i] = BLOCKS.register("radiation_block" + i,
                    () -> new RadiationBlock(properties, () -> new RadiationTile(RADIATION_TILES[finalI].get(), finalI)));
            RADIATION_ITEMS[i] = ITEMS.register("radiation_block" + i, () -> new BlockItem(RADIATION_BLOCKS[finalI].get(), createStandardProperties()));
            RADIATION_TILES[i] = TILES.register("radiation_block" + i, () -> TileEntityType.Builder.of(() -> new RadiationTile(RADIATION_TILES[finalI].get(), finalI), RADIATION_BLOCKS[finalI].get()).build(null));
        }
    }

    private static Item.Properties createStandardProperties() {
        return new Item.Properties().tab(ItemGroup.TAB_MISC);
    }

}
