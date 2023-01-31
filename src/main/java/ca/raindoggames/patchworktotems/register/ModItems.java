package ca.raindoggames.patchworktotems.register;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import ca.raindoggames.patchworktotems.item.PatchEnderPearl;
import ca.raindoggames.patchworktotems.item.PatchTotem;
import ca.raindoggames.patchworktotems.item.PatternOfProtection;
import ca.raindoggames.patchworktotems.item.PatternOfProtectionExplosion;
import ca.raindoggames.patchworktotems.item.PatternOfProtectionFire;
import ca.raindoggames.patchworktotems.item.PatternOfProtectionProjectile;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PatchworkTotems.MODID);
	public static final RegistryObject<Item> PATCHTOTEM = ITEMS.register("patch_totem", () -> new PatchTotem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_SEARCH)));
	public static final RegistryObject<Item> PATCH_ENDER_PEARL = ITEMS.register("patch_ender_pearl", () -> new PatchEnderPearl(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_SEARCH)));
	public static final RegistryObject<Item> PATTERN_OF_PROTECTION = ITEMS.register("pattern_of_protection", () -> new PatternOfProtection(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_SEARCH)));
	public static final RegistryObject<Item> PATTERN_OF_PROTECTION_FIRE = ITEMS.register("pattern_of_protection_fire", () -> new PatternOfProtectionFire(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_SEARCH)));
	public static final RegistryObject<Item> PATTERN_OF_PROTECTION_PROJECTILE = ITEMS.register("pattern_of_protection_projectile", () -> new PatternOfProtectionProjectile(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_SEARCH)));
	public static final RegistryObject<Item> PATTERN_OF_PROTECTION_EXPLOSION = ITEMS.register("pattern_of_protection_explosion", () -> new PatternOfProtectionExplosion(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_SEARCH)));
}
