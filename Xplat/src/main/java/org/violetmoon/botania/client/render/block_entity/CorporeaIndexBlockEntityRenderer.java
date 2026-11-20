/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

public class CorporeaIndexBlockEntityRenderer implements BlockEntityRenderer<CorporeaIndexBlockEntity, CorporeaIndexBlockEntityRenderer.State>, BlockEntityItemRenderer.Display {
	private static final RenderType LAYER = RenderTypes.entityCutoutCull(Identifier.parse(ResourcesLib.MODEL_CORPOREA_INDEX));
	private static final float ANGLE = (float) Math.sin(Math.toRadians(45));
	private final ModelPart ring;
	private final ModelPart cube;

	public CorporeaIndexBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		ModelPart root = ctx.bakeLayer(BotaniaModelLayers.CORPOREA_INDEX);
		this.ring = root.getChild("ring");
		this.cube = root.getChild("cube");
	}

	public static MeshDefinition createMesh() {
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("ring", CubeListBuilder.create()
				.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.ZERO);
		root.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(32, 0)
				.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.ZERO);
		return mesh;
	}

	@Override
	public boolean shouldRenderOffScreen() {
		return true;
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(CorporeaIndexBlockEntity index, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(index, state, partialTick, camera, crumblingOverlay);
		state.ticks = ClientTickHandler.getEntityTicksInGame() + partialTick;
		state.height = (float) ((Math.cos((index.ticksWithCloseby + (index.hasCloseby ? partialTick : 0)) / 10F) * 0.5 + 0.5) * 0.25);
		state.starRadius = 0;
		if (index.closeby > 0F) {
			state.starRadius = (float) CorporeaIndexBlockEntity.RADIUS * index.closeby
					+ (index.closeby == 1F ? 0F : index.hasCloseby ? partialTick : -partialTick) * 0.2F;
			state.starAngle = (index.ticksWithCloseby + partialTick) * 2 * Math.PI / 180;
			state.starSeed = index.getBlockPos().getX() ^ index.getBlockPos().getY() ^ index.getBlockPos().getZ();
		}
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		submitIndex(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.ticks * 2, state.height);
		if (state.starRadius > 0F) {
			double x = Math.cos(state.starAngle) * state.starRadius;
			double z = Math.sin(state.starAngle) * state.starRadius;
			submitStar(pose, collector, state, x, z);
			submitStar(pose, collector, state, -x, -z);
			submitStar(pose, collector, state, x, -z);
			submitStar(pose, collector, state, -x, z);
		}
		pose.popPose();
	}

	private static void submitStar(PoseStack pose, SubmitNodeCollector collector, State state, double x, double z) {
		pose.pushPose();
		pose.translate(x, 0.3, z);
		RenderHelper.submitStar(pose, collector, 0xFF00FF, 0.02F, state.starSeed, state.ticks);
		pose.popPose();
	}

	@Override
	public void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		pose.scale(1.3F, 1.3F, 1.3F);
		pose.translate(0, -0.1, 0);
		submitIndex(pose, collector, light, overlay, (ClientTickHandler.getEntityTicksInGame() + partialTick) * 2, 0);
		pose.popPose();
	}

	private void submitIndex(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, float rotation, float height) {
		pose.pushPose();
		pose.mulPose(VecHelper.rotateY(rotation));
		pose.translate(0, 0.5F + height / 2F, 0);
		var tilt = new Quaternionf().rotateAxis(VecHelper.toRadians(60F), ANGLE, 0F, ANGLE);

		pose.mulPose(tilt);
		collector.submitModelPart(ring, pose, LAYER, light, overlay, null);
		pose.scale(0.875F, 0.875F, 0.875F);
		pose.mulPose(tilt);
		pose.mulPose(VecHelper.rotateY(rotation));
		collector.submitModelPart(ring, pose, LAYER, light, overlay, null);
		pose.scale(0.875F, 0.875F, 0.875F);
		pose.mulPose(tilt);
		pose.mulPose(VecHelper.rotateY(rotation));
		collector.submitModelPart(cube, pose, LAYER, light, overlay, null);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private float ticks;
		private float height;
		private float starRadius;
		private double starAngle;
		private int starSeed;
	}
}
