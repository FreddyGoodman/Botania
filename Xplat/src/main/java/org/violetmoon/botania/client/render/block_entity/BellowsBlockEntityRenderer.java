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

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.BellowsModel;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.common.block.block_entity.mana.BellowsBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

public class BellowsBlockEntityRenderer implements BlockEntityRenderer<BellowsBlockEntity, BellowsBlockEntityRenderer.State>, BlockEntityItemRenderer.Display {
	private static final Identifier texture = Identifier.parse(ResourcesLib.MODEL_BELLOWS);
	private final BellowsModel model;

	public BellowsBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		model = new BellowsModel(ctx.bakeLayer(BotaniaModelLayers.BELLOWS));
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(BellowsBlockEntity bellows, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(bellows, state, partialTick, camera, crumblingOverlay);
		state.angle = switch (bellows.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			case NORTH -> 180F;
			case EAST -> 270F;
			case WEST -> 90F;
			default -> 0F;
		};
		state.extension = Math.max(0.1F, 1F - (bellows.movePos + bellows.moving * partialTick + 0.1F));
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		submitBellows(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.angle, state.extension);
	}

	@Override
	public void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		submitBellows(pose, collector, light, overlay, 0F, 1F);
	}

	private void submitBellows(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, float angle, float extension) {
		pose.pushPose();
		pose.translate(0.5F, 1.5F, 0.5F);
		pose.scale(1F, -1F, -1F);
		pose.mulPose(VecHelper.rotateY(angle));
		collector.submitCustomGeometry(pose, model.renderType(texture), (transform, buffer) -> {
			PoseStack geometry = new PoseStack();
			geometry.last().set(transform);
			model.render(geometry, buffer, light, overlay, -1, extension);
		});
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private float angle;
		private float extension;
	}
}
