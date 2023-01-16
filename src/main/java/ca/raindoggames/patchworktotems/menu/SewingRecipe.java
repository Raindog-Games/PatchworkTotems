package ca.raindoggames.patchworktotems.menu;

import java.util.Optional;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

// Code modelled from "warping" custom recipe in Architect's Palette mod by jsburger
// https://github.com/jsburger/Architects-Palette
public class SewingRecipe implements Recipe<Container> {
	public static SewingRecipe.SewingRecipeType TYPE = new SewingRecipeType();
	public static final Serializer SERIALIZER = new Serializer();
	
	private final Ingredient input;
	private final ItemStack output;
	private ResourceLocation dimension;
	private ResourceLocation id;
	
	public SewingRecipe(ResourceLocation id, Ingredient input, ItemStack output, ResourceLocation dimension) {
		this.input = input;
		this.output = output;
		this.dimension = dimension;
		this.id = id;
	}
	
	public ResourceLocation getDimension() {
		return this.dimension;
	}
	
	public Ingredient getInput() {
		return this.input;
	}

	@Override
	public boolean matches(Container inv, Level worldIn) {
		return this.input.test(inv.getItem(0));
	}

	@Override
	public ItemStack assemble(Container inv) {
		return this.getResultItem().copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return this.output;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(this.input);
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return SewingRecipe.TYPE;
	}
	
	private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SewingRecipe> {

		Serializer() {
			this.setRegistryName(new ResourceLocation(PatchworkTotems.MODID, "sewing"));
		}
		
		@Override
		public SewingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			final JsonElement inputElement = GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient");
			final Ingredient input = Ingredient.fromJson(inputElement);
			final ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			final ResourceLocation dimensionId = new ResourceLocation(GsonHelper.getAsString(json, "dimension"));
			return new SewingRecipe(recipeId, input, output, dimensionId);
		}

		@Nullable
		@Override
		public SewingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			final Ingredient input = Ingredient.fromNetwork(buffer);
			final ItemStack output = buffer.readItem();
			final ResourceLocation dimensionId = buffer.readResourceLocation();
			return new SewingRecipe(recipeId, input, output, dimensionId);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SewingRecipe recipe) {
			recipe.input.toNetwork(buffer);
			buffer.writeItem(recipe.output);
			buffer.writeResourceLocation(recipe.dimension);
		}
	}
	
	public static class SewingRecipeType implements RecipeType<SewingRecipe> {
		@Override
		public String toString() {
			return PatchworkTotems.MODID.concat(":sewing");
		}
		
		public <C extends Container> Optional<SewingRecipe> find(C inv, Level world) {
			return world.getRecipeManager().getRecipeFor(SewingRecipe.TYPE, inv, world);	
		}	
	}
}
