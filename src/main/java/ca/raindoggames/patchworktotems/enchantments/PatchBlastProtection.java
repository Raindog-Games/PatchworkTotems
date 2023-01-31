package ca.raindoggames.patchworktotems.enchantments;

import java.util.Arrays;

import ca.raindoggames.patchworktotems.register.ModEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class PatchBlastProtection extends Enchantment {
	public PatchBlastProtection() {
	   super(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR, EquipmentSlot.values());
	}

	public int getMaxLevel() {
	   return 4;
	}

	public int getDamageProtection(int damage, DamageSource source) {
		if (source.isBypassInvul()) {
	       return 0;
	    }
	    return source.isExplosion() ? damage * 2 : 0;
	}
	
	public static double getExplosionKnockbackAfterDampener(LivingEntity entity, double knockback) {
	   int i = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PATCH_PROTECTION_EXPLOSION.get(), entity);
	   if (i > 0) {
		   knockback -= (double)Mth.floor(knockback * (double)((float)i * 0.15F));
	   }

	   return knockback;
	}

	public boolean checkCompatibility(Enchantment other) {
	   Enchantment[] incompatibleEnchants = new Enchantment[] {
			    Enchantments.ALL_DAMAGE_PROTECTION,
				Enchantments.FIRE_PROTECTION,
				Enchantments.PROJECTILE_PROTECTION,
				Enchantments.BLAST_PROTECTION,
				ModEnchantments.PATCH_PROTECTION_ALL.get(),
				ModEnchantments.PATCH_PROTECTION_FIRE.get(),
				ModEnchantments.PATCH_PROTECTION_PROJECTILE.get()
	   };
	   return super.checkCompatibility(other) && !Arrays.asList(incompatibleEnchants).contains(other);
	}
	
	@Override
	public boolean isTreasureOnly() {
		return true;
	}
	
	@Override
	public boolean isTradeable() {
		return false;
	}
	
	@Override
	public boolean isAllowedOnBooks() {
		return true;
	}
	
	@Override
	public Component getFullname(int level) {
		MutableComponent levelStr = new TranslatableComponent("enchantment.level." + level);
		MutableComponent mutablecomponent = new TranslatableComponent(this.getDescriptionId(), levelStr);
	    mutablecomponent.withStyle(ChatFormatting.GRAY);
	    return mutablecomponent;
	}
}
