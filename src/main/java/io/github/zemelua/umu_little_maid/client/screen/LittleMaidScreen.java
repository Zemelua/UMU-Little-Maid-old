package io.github.zemelua.umu_little_maid.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.zemelua.umu_little_maid.UMULittleMaid;
import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import io.github.zemelua.umu_little_maid.inventory.MaidContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.items.IItemHandler;

public class LittleMaidScreen extends AbstractContainerScreen<MaidContainer> {
	private static final ResourceLocation MAID_SCREEN_TEXTURE = UMULittleMaid.location("textures/gui/little_maid.png");
	public static final ResourceLocation EMPTY_HELD_SLOT_TEXTURE = UMULittleMaid.location("gui/empty_held_item_slot");
	public static final ResourceLocation[] EMPTY_ARMOR_SLOT_TEXTURES = new ResourceLocation[]{
			UMULittleMaid.location("gui/empty_armor_slot_boots"), InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS,
			InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, UMULittleMaid.location("gui/empty_armor_slot_helmet")
	};

	public LittleMaidScreen(MaidContainer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, MAID_SCREEN_TEXTURE);

		int centerX = (this.width - this.imageWidth) / 2;
		int centerY = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, centerX, centerY, 0, 0, this.imageWidth, this.imageHeight);

		LittleMaidEntity maid = this.getMenu().getMaid();
		IItemHandler maidInventory = maid.getInventory();
		int inventorySize = maidInventory.getSlots();
		for (int i = 0; i < 3 && (i + 1) * 5 <= inventorySize; i++) {
			for (int j = 0; j < 5 && i * 5 + j + 1 <= inventorySize; j++) {
				this.blit(matrixStack, centerX + 79 + j * 18, centerY + 17 + i * 18, 0, 166, 18, 18);
			}
		}

		InventoryScreen.renderEntityInInventory(centerX + 51, centerY + 65, 25, centerX + 51 - mouseX, centerY + 25 - mouseY, maid);
	}
}
