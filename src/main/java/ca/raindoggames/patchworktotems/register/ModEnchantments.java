package ca.raindoggames.patchworktotems.register;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import ca.raindoggames.patchworktotems.enchantments.PatchBlastProtection;
import ca.raindoggames.patchworktotems.enchantments.PatchFireProtection;
import ca.raindoggames.patchworktotems.enchantments.PatchProjectileProtection;
import ca.raindoggames.patchworktotems.enchantments.PatchProtection;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PatchworkTotems.MODID);
	
	public static final RegistryObject<Enchantment> PATCH_PROTECTION_ALL = ENCHANTMENTS.register("patch_protection_all", () -> new PatchProtection());
	public static final RegistryObject<Enchantment> PATCH_PROTECTION_FIRE = ENCHANTMENTS.register("patch_protection_fire", () -> new PatchFireProtection());
	public static final RegistryObject<Enchantment> PATCH_PROTECTION_PROJECTILE = ENCHANTMENTS.register("patch_protection_projectile", () -> new PatchProjectileProtection());
	public static final RegistryObject<Enchantment> PATCH_PROTECTION_EXPLOSION = ENCHANTMENTS.register("patch_protection_explosion", () -> new PatchBlastProtection());
}
