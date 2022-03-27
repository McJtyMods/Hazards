package com.mcjty.hazards.setup;


import com.mcjty.hazards.content.RadiationBlock;
import com.mcjty.hazards.content.RadiationMonitorItem;
import com.mcjty.hazards.content.RadiationTile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mcjty.hazards.Hazards.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registration {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static final RegistryObject<RadiationBlock>[] RADIATION_BLOCKS = new RegistryObject[RadiationTile.MAX_TIERS * 2];
    public static final RegistryObject<BlockItem>[] RADIATION_ITEMS = new RegistryObject[RadiationTile.MAX_TIERS * 2];
    public static final RegistryObject<BlockEntityType<RadiationTile>>[] RADIATION_TILES = new RegistryObject[RadiationTile.MAX_TIERS * 2];

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        TILES.register(bus);

        for (int i = 0 ; i < RadiationTile.MAX_TIERS * 2 ; i++) {
            int finalI = i;
            BlockBehaviour.Properties properties;
            if (i < RadiationTile.MAX_TIERS) {
                properties = BlockBehaviour.Properties.of(Material.STONE)
                        .requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                        .noDrops();
            } else {
                properties = BlockBehaviour.Properties.of(Material.STONE)
                        .requiresCorrectToolForDrops().strength(-1.0F, 3600000.0F)
                        .noDrops();
            }
            RADIATION_BLOCKS[i] = BLOCKS.register("radiation_block" + i,
                    () -> new RadiationBlock(properties, (pos, state) -> new RadiationTile(RADIATION_TILES[finalI].get(), pos, state, finalI)));
            RADIATION_ITEMS[i] = ITEMS.register("radiation_block" + i, () -> new BlockItem(RADIATION_BLOCKS[finalI].get(), createStandardProperties()));
            RADIATION_TILES[i] = TILES.register("radiation_block" + i, () -> BlockEntityType.Builder.of((pos, state) -> new RadiationTile(RADIATION_TILES[finalI].get(), pos, state, finalI), RADIATION_BLOCKS[finalI].get()).build(null));
        }
    }

    public static final RegistryObject<RadiationMonitorItem> RADIATION_MONITOR = ITEMS.register("radiation_monitor", () -> new RadiationMonitorItem(createStandardProperties().stacksTo(1)));

    private static Item.Properties createStandardProperties() {
        return new Item.Properties().tab(CreativeModeTab.TAB_MISC);
    }

}
