package ca.raindoggames.patchworktotems;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import org.slf4j.Logger;

import java.util.stream.Collectors;

import ca.raindoggames.patchworktotems.menu.SewingMenuScreen;
import ca.raindoggames.patchworktotems.register.ModBlockEntities;
import ca.raindoggames.patchworktotems.register.ModBlocks;
import ca.raindoggames.patchworktotems.register.ModItems;
import ca.raindoggames.patchworktotems.register.ModMenuTypes;
import ca.raindoggames.patchworktotems.network.NetworkManager;
import ca.raindoggames.patchworktotems.network.TotemPacket;

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
        MinecraftForge.EVENT_BUS.addListener(this::preventDeath);
        
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
    	NetworkManager.initialize();
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
    
    private void preventDeath(LivingDamageEvent event) {
		LivingEntity entity = event.getEntityLiving();
		ItemStack itemstack = null;
		if (entity.getHealth() - event.getAmount() < 1) {
			if (entity.getOffhandItem().toString().contains("patch_totem")) {
				itemstack = entity.getItemInHand(InteractionHand.OFF_HAND);
		    } else if (entity.getMainHandItem().toString().contains("patch_totem")) {
		    	itemstack = entity.getItemInHand(InteractionHand.MAIN_HAND);
		    }
			
			if (itemstack != null) {
				if (entity instanceof ServerPlayer) {
	               ServerPlayer serverplayer = (ServerPlayer)entity;
	               serverplayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING), 1);
	               NetworkManager.CHANNEL.sendTo(new TotemPacket(itemstack, serverplayer), serverplayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	            }
				NetworkManager.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new TotemPacket(itemstack, entity));			
				
				entity.setHealth(1.0F);
				entity.removeAllEffects();
				entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
				entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
				entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
				entity.level.broadcastEntityEvent(entity, (byte)35);
				itemstack.shrink(1);
				
				event.setCanceled(true);
			}
		}
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
