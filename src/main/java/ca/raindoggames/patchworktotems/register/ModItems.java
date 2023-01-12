package ca.raindoggames.patchworktotems.register;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import ca.raindoggames.patchworktotems.item.PatchTotem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PatchworkTotems.MODID);
	public static final RegistryObject<Item> PATCHTOTEM = ITEMS.register("patch_totem", () -> new PatchTotem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_SEARCH)));
}
