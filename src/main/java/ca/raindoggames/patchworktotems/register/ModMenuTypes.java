package ca.raindoggames.patchworktotems.register;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import ca.raindoggames.patchworktotems.menu.SewingMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, PatchworkTotems.MODID);
	
	public static final RegistryObject<MenuType<SewingMenu>> SEWING_MENU = 
			registerMenuType(SewingMenu::new, "sewing_menu");
	
	private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
		return MENU_TYPES.register(name, () -> IForgeMenuType.create(factory));
	}
}
