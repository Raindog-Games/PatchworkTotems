package ca.raindoggames.patchworktotems;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import org.slf4j.Logger;

import java.util.stream.Collectors;

import ca.raindoggames.patchworktotems.menu.SewingMenuScreen;
import ca.raindoggames.patchworktotems.register.ModBlockEntities;
import ca.raindoggames.patchworktotems.register.ModBlocks;
import ca.raindoggames.patchworktotems.register.ModItems;
import ca.raindoggames.patchworktotems.register.ModMenuTypes;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PatchworkTotems.MODID)
public class PatchworkTotems
{
	public static final String MODID = "patchworktotems";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public PatchworkTotems()
    {    
        ModItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModMenuTypes.MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
    	MenuScreens.register(ModMenuTypes.SEWING_MENU.get(), SewingMenuScreen::new);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
        
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            // register blockitems here         
            final IForgeRegistry<Item> registry = itemRegistryEvent.getRegistry();
            ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
            	final Item.Properties properties = new Item.Properties().tab(CreativeModeTab.TAB_SEARCH);
            	final BlockItem blockItem = new BlockItem(block, properties);
            	blockItem.setRegistryName(block.getRegistryName());
            	registry.register(blockItem);
            });
            
            LOGGER.debug("Registered BlockItems!");
        }
    }
}
