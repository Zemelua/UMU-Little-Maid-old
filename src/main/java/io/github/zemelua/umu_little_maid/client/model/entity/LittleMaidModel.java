package io.github.zemelua.umu_little_maid.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zemelua.umu_little_maid.entity.LittleMaidEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class LittleMaidModel extends EntityModel<LittleMaidEntity> {
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart skirt;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public LittleMaidModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);

		this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.skirt = root.getChild("skirt");
		this.rightArm = root.getChild("right_arm");
		this.leftArm = root.getChild("left_arm");
		this.rightLeg = root.getChild("right_leg");
		this.leftLeg = root.getChild("left_leg");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox("head", -4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(0, 32).addBox("hat", -4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 8.0F, 0.0F)
		);
		partDefinition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 16).addBox("body", -3.0F, 0.0F, -2.0F, 6.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(0, 48).addBox("jacket", -3.0F, 0.0F, -2.0F, 6.0F, 9.0F, 4.0F, new CubeDeformation(0.25F)),
				PartPose.offset(0.0F, 8.0F, 0.0F)
		);
		partDefinition.addOrReplaceChild("skirt", CubeListBuilder.create()
						.texOffs(32, 0).addBox("skirt", -4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
						.texOffs(32, 33).addBox("apron", -4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
				PartPose.offset(0.0F, 14.0F, 0.0F)
		);
		partDefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(20, 16).addBox("right_arm", -1.0F, -1.5F, -1.5F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(20, 50).addBox("right_sleeve", -1.0F, -1.5F, -1.5F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.25F)),
				PartPose.offset(-4.0F, 9.5F, 0.5F)
		);
		partDefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(28, 16).addBox("left_arm", -1.0F, -1.5F, -1.5F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(28, 50).addBox("left_sleeve", -1.0F, -1.5F, -1.5F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.25F)),
				PartPose.offset(4.0F, 9.5F, 0.5F)
		);
		partDefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(36, 16).addBox("right_leg", -1.5F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(36, 50).addBox("right_pants", -1.5F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)),
				PartPose.offset(-1.5F, 17.0F, 0.0F)
		);
		partDefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.texOffs(50, 16).addBox("left_leg", -1.5F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
						.texOffs(50, 50).addBox("left_pants", -1.5F, 0.0F, -2.0F, 3.0F, 7.0F, 4.0F, new CubeDeformation(0.25F)),
				PartPose.offset(1.5F, 17.0F, 0.0F)
		);

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void prepareMobModel(LittleMaidEntity entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		if (entity.isSitting()) {
			this.head.zRot = (float) Math.toRadians(13.7F);
			this.rightArm.xRot = (float) Math.toRadians(-42.0);
			this.rightArm.yRot = (float) Math.toRadians(0.0F);
			this.rightArm.zRot = (float) Math.toRadians(-25.0F);
			this.leftArm.xRot = (float) Math.toRadians(-42.0F);
			this.leftArm.yRot = (float) Math.toRadians(0.0F);
			this.leftArm.zRot = (float) Math.toRadians(25.0F);
		} else {
			this.head.zRot = 0.0F;
			this.rightArm.zRot = (float) Math.toRadians(15.0F);
			this.leftArm.zRot = (float) Math.toRadians(-15.0F);
		}
	}

	@Override
	public void setupAnim(LittleMaidEntity entity, float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch) {
		this.head.xRot = (float) Math.toRadians(headPitch);
		this.head.yRot = (float) Math.toRadians(netHeadYaw);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.head.render(matrixStack, buffer, light, overlay, red, green, blue, alpha);
		this.body.render(matrixStack, buffer, light, overlay, red, green, blue, alpha);
		this.skirt.render(matrixStack, buffer, light, overlay, red, green, blue, alpha);
		this.rightArm.render(matrixStack, buffer, light, overlay, red, green, blue, alpha);
		this.leftArm.render(matrixStack, buffer, light, overlay, red, green, blue, alpha);
		this.rightLeg.render(matrixStack, buffer, light, overlay, red, green, blue, alpha);
		this.leftLeg.render(matrixStack, buffer, light, overlay, red, green, blue, alpha);
	}
}
