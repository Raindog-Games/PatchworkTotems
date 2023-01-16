package ca.raindoggames.patchworktotems.register;


import ca.raindoggames.patchworktotems.PatchworkTotems;
import ca.raindoggames.patchworktotems.blockentity.TileSewingMachineEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, PatchworkTotems.MODID);
	
	public static final RegistryObject<BlockEntityType<TileSewingMachineEntity>> SEWING_MACHINE = 
			BLOCK_ENTITY_TYPES.register("sewing_machine",
					() -> BlockEntityType.Builder.of(TileSewingMachineEntity::new, ModBlocks.SEWINGMACHINE.get()).build(null));
}
