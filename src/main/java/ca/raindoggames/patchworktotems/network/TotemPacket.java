package ca.raindoggames.patchworktotems.network;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class TotemPacket {
	private final Entity entity;
	private final ItemStack itemStack;
	
	public TotemPacket(ItemStack itemStack, Entity entity) {
		this.entity = entity;
		this.itemStack = itemStack;
	}
	
	public static void encode(TotemPacket packet, FriendlyByteBuf buffer) {
		buffer.writeInt(packet.entity.getId());
		buffer.writeItem(packet.itemStack);
	}
	
	public static TotemPacket decode(FriendlyByteBuf buffer) {
		int id = buffer.readInt();
		Minecraft minecraft = Minecraft.getInstance();
		Entity entity = minecraft.level.getEntity(id);
		ItemStack itemStack = buffer.readItem();
		return new TotemPacket(itemStack, entity);
	}
	
	public static void handle(final TotemPacket packet, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> TotemPacket.handlePacket(packet, ctx)));
		ctx.get().setPacketHandled(true);
	}
	
	@OnlyIn(Dist.CLIENT)
	private static void handlePacket(final TotemPacket packet, Supplier<NetworkEvent.Context> ctx) {
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.particleEngine.createTrackingEmitter(packet.entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
		Level level = minecraft.level;
		if (level != null) {
			level.playLocalSound(packet.entity.getX(), packet.entity.getY(), packet.entity.getZ(), SoundEvents.TOTEM_USE, packet.entity.getSoundSource(), 1.0f, 1.0f, false);
		}
		
		if (packet.entity == minecraft.player) {
			minecraft.gameRenderer.displayItemActivation(packet.itemStack);
		}
	}
}
