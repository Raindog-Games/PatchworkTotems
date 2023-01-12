package ca.raindoggames.patchworktotems.register;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import ca.raindoggames.patchworktotems.block.SewingMachine;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PatchworkTotems.MODID);
	
	public static final RegistryObject<SewingMachine> SEWINGMACHINE = BLOCKS.register("sewing_machine", () -> new SewingMachine(BlockBehaviour.Properties.of(Material.METAL)));
}
