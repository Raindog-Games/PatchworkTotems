package ca.raindoggames.patchworktotems.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import ca.raindoggames.patchworktotems.PatchworkTotems;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SewingMenuScreen extends AbstractContainerScreen<SewingMenu> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(PatchworkTotems.MODID, "textures/gui/container/sewing_menu.png");

	public SewingMenuScreen(SewingMenu p_97741_, Inventory p_97742_, Component p_97743_) {
		super(p_97741_, p_97742_, p_97743_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		
		this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
		renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, delta);
		renderTooltip(poseStack, mouseX, mouseY);
	}
}
