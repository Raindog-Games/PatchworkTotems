package ca.raindoggames.patchworktotems.menu;

import ca.raindoggames.patchworktotems.register.ModBlockEntities;
import ca.raindoggames.patchworktotems.register.ModBlocks;
import ca.raindoggames.patchworktotems.register.ModItems;
import ca.raindoggames.patchworktotems.register.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SewingMenu extends AbstractContainerMenu {
	private final RecipeType<?> recipeType;
	public static final int INGREDIENT_SLOT = 0;
	public static final int RESULT_SLOT = 1;
	public static final int SLOT_COUNT = 2;
	private static final int INV_SLOT_START = 2;
	private static final int INV_SLOT_END = 29;
	private static final int USE_ROW_SLOT_START = 29;
	private static final int USE_ROW_SLOT_END = 38;
	private final Container container = new SimpleContainer(1) {
		public void setChanged() {
	       super.setChanged();
	       SewingMenu.this.slotsChanged(this);
	    }
	};
	private final Container resultSlots = new ResultContainer();
	protected final Level level;
	private final ContainerLevelAccess access;
	
	public SewingMenu(int p_38969_, Inventory inv, FriendlyByteBuf buffer) {
		this(p_38969_, inv, inv.player.level.getBlockEntity(buffer.readBlockPos()));
	}

	public SewingMenu(int p_38969_, Inventory inv, BlockEntity entity) {
		super(ModMenuTypes.SEWING_MENU.get(), p_38969_);
		this.recipeType = SewingRecipe.TYPE;
	    this.level = inv.player.level;
	    this.access = ContainerLevelAccess.create(this.level, entity.getBlockPos());
	    this.addSlot(new Slot(container, 0, 56, 35));
	    
	    this.addSlot(new Slot(this.resultSlots, 1, 116, 35) {
	       public boolean mayPlace(ItemStack p_39630_) {
	          return false;
	       }

	       public void onTake(Player p_150574_, ItemStack p_150575_) {
	          SewingMenu.this.container.setItem(0, ItemStack.EMPTY);
	       }
	    });
	    
	    for(int i = 0; i < 3; ++i) {
	       for(int j = 0; j < 9; ++j) {
	          this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	       }
	    }

	    for(int k = 0; k < 9; ++k) {
	       this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
	    }
	}
	
	public void slotsChanged(Container p_39570_) {
	   super.slotsChanged(p_39570_);
	   if (p_39570_ == this.container) {
	      this.createResult();
	   }
	}
	
	// Not using recipe yet, just hard coded check if Totem inserted
	private void createResult() {
	   ItemStack itemstack = this.container.getItem(0);
	   if (!itemstack.isEmpty() && itemstack.getItem() == Items.TOTEM_OF_UNDYING) {
		   this.resultSlots.setItem(0, ModItems.PATCHTOTEM.get().getDefaultInstance());	   
	   }  
	   this.broadcastChanges();
	}
	
	public void removed(Player player) {
	   super.removed(player);
	   this.access.execute((p_39575_, p_39576_) -> {
	      this.clearContainer(player, this.container);
	   });
	}

	public boolean stillValid(Player player) {
		return stillValid(this.access, player, ModBlocks.SEWINGMACHINE.get());
	}
	
	public ItemStack quickMoveStack(Player player, int index) {
	      ItemStack itemstack = ItemStack.EMPTY;
	      Slot slot = this.slots.get(index);
	      if (slot != null && slot.hasItem()) {
	         ItemStack itemstack1 = slot.getItem();
	         itemstack = itemstack1.copy();
	         ItemStack itemstack2 = this.container.getItem(0);
	         if (index == 1) {
	            if (!this.moveItemStackTo(itemstack1, 2, 38, true)) {
	               return ItemStack.EMPTY;
	            }

	            slot.onQuickCraft(itemstack1, itemstack);
	         } else if (index != 0) {
	            if (!itemstack2.isEmpty()) {
	               if (index >= 2 && index < 29) {
	                  if (!this.moveItemStackTo(itemstack1, 29, 38, false)) {
	                     return ItemStack.EMPTY;
	                  }
	               } else if (index >= 29 && index < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false)) {
	                  return ItemStack.EMPTY;
	               }
	            } else if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
	               return ItemStack.EMPTY;
	            }
	         } else if (!this.moveItemStackTo(itemstack1, 2, 38, false)) {
	            return ItemStack.EMPTY;
	         }

	         if (itemstack1.isEmpty()) {
	            slot.set(ItemStack.EMPTY);
	         } else {
	            slot.setChanged();
	         }

	         if (itemstack1.getCount() == itemstack.getCount()) {
	            return ItemStack.EMPTY;
	         }

	         slot.onTake(player, itemstack1);
	      }

	      return itemstack;
	   }
}
