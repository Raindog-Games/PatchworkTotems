package ca.raindoggames.patchworktotems.event;

import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ca.raindoggames.patchworktotems.PatchworkTotems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class PreventDeathEvent {

	@SubscribeEvent
	public static void onEvent(LivingDamageEvent event) {
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
	               CriteriaTriggers.USED_TOTEM.trigger(serverplayer, itemstack);
	            }
	
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
}
