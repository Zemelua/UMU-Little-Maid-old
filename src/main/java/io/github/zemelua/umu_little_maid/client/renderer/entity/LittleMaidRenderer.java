package io.github.zemelua.umu_little_maid.client.renderer.entity;

import io.github.zemelua.umu_little_maid.UMULittleMaid;
import io.github.zemelua.umu_little_maid.client.model.ModModelLayers;
import io.github.zemelua.umu_little_maid.client.model.entity.LittleMaidModel;
import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class LittleMaidRenderer extends MobRenderer<LittleMaidEntity, LittleMaidModel> {
	private static final ResourceLocation LITTLE_MAID_TEXTURE = UMULittleMaid.location("textures/entity/little_maid/little_maid.png");

	public LittleMaidRenderer(EntityRendererProvider.Context context) {
		super(context, new LittleMaidModel(context.bakeLayer(ModModelLayers.LITTLE_MAID)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(LittleMaidEntity mob) {
		return LITTLE_MAID_TEXTURE;
	}
}
