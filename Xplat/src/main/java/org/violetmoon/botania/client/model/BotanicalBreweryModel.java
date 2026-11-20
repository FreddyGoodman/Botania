/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.helper.VecHelper;

public class BotanicalBreweryModel extends Model<Void> {
	private static final Identifier TEXTURE = Identifier.parse(ResourcesLib.MODEL_BOTANICAL_BREWERY);
	private final ModelPart top;
	private final ModelPart pole;
	private final ModelPart bottom;

	private final ModelPart plate;

	public BotanicalBreweryModel(ModelPart root) {
		super(root, RenderTypes::entitySolid);

		top = root.getChild("top");
		pole = root.getChild("pole");
		bottom = root.getChild("bottom");
		plate = root.getChild("plate");
	}

	public static MeshDefinition createMesh() {
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("top", CubeListBuilder.create().texOffs(8, 0)
				.addBox(-2.0F, -7.0F, -2.0F, 4, 1, 4),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("pole", CubeListBuilder.create()
				.addBox(-1.0F, -6.0F, -1.0F, 2, 10, 2),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(8, 5)
				.addBox(-2.0F, 4.0F, -2.0F, 4, 1, 4),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(8, 5)
				.addBox(5.0F, 0.0F, -2.0F, 4, 1, 4),
				PartPose.offset(0.0F, 17.0F, 0.0F));
		return mesh;
	}

	public void submitColumn(PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		RenderType layer = renderType(TEXTURE);
		collector.submitModelPart(pole, pose, layer, light, overlay, null);
		collector.submitModelPart(top, pose, layer, light, overlay, null);
		collector.submitModelPart(bottom, pose, layer, light, overlay, null);
	}

	public void submitPlate(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, float degrees) {
		pose.pushPose();
		pose.translate(0F, 17F / 16F, 0F);
		pose.mulPose(VecHelper.rotateY(degrees));
		pose.translate(0F, -17F / 16F, 0F);
		collector.submitModelPart(plate, pose, renderType(TEXTURE), light, overlay, null);
		pose.popPose();
	}
}
