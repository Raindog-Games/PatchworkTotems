package ca.raindoggames.patchworktotems.network;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkManager {
	private static final String PROTOCOL_VERSION = "1";
	static ResourceLocation RESOURCE_LOCATION = new ResourceLocation(PatchworkTotems.MODID, "network");
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			RESOURCE_LOCATION,
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	
	public static void initialize() {
		CHANNEL.registerMessage(0, TotemPacket.class, TotemPacket::encode, TotemPacket::decode, TotemPacket::handle);
	}
}
