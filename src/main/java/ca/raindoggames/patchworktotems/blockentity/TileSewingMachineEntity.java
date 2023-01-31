package ca.raindoggames.patchworktotems.blockentity;

import ca.raindoggames.patchworktotems.menu.SewingMenu;
import ca.raindoggames.patchworktotems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileSewingMachineEntity extends BlockEntity implements Container, MenuProvider, Nameable {
	protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

	public TileSewingMachineEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SEWING_MACHINE.get(), pos, state);
	}

	@Override
	public void clearContent() {
		this.items.clear();	
	}

	@Override
	public AbstractContainerMenu createMenu(int p_39954_, Inventory inv, Player player) {
		return new SewingMenu(p_39954_, inv, this);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("container.sewing_machine");
	}

	@Override
	public int getContainerSize() {
		return this.items.size();
	}

	@Override
	public boolean isEmpty() {
		ItemStack itemstack1 = this.getItem(0);
		ItemStack itemstack2 = this.getItem(1);
		return itemstack1.isEmpty() && itemstack2.isEmpty();
	}

	@Override
	public ItemStack getItem(int p_18941_) {
		return items.get(p_18941_);
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		return ContainerHelper.removeItem(this.items, p_18942_, p_18943_);
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_18951_) {
		return ContainerHelper.takeItem(this.items, p_18951_);
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
	    this.items.set(index, itemStack);
	    if (itemStack.getCount() > this.getMaxStackSize()) {
	    	itemStack.setCount(this.getMaxStackSize());
	    }
	}

	@Override
	public boolean stillValid(Player player) {
	   if (this.level.getBlockEntity(this.worldPosition) != this) {
	      return false;
	   } else {
	      return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
	   }
	}
	
	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
		//TileSewingMachineEntity entity = (TileSewingMachineEntity) be;
		
		// code called each tick (20 times per second)
	}

	@Override
	public Component getName() {
		return new TranslatableComponent("container.sewing_machine");
	}
}
