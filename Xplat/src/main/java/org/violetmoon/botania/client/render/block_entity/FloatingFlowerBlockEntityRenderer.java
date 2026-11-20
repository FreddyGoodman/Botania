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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.block.FloatingFlower;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.model.FloatingFlowerModel;
import org.violetmoon.botania.common.block.block_entity.flower.FloatingFlowerBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.xplat.BotaniaConfig;

import java.util.List;
import java.util.Random;

public class FloatingFlowerBlockEntityRenderer implements BlockEntityRenderer<FloatingFlowerBlockEntity, FloatingFlowerBlockEntityRenderer.State> {
	public FloatingFlowerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(FloatingFlowerBlockEntity tile, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTick, camera, crumblingOverlay);
		extractIsland(tile, tile.getFloatingData(), state, partialTick);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		submitIsland(state, pose, collector);
	}

	static void extractIsland(BlockEntity tile, @Nullable FloatingFlower data, State state, float partialTick) {
		state.parts = List.of();
		if (data == null || BotaniaConfig.client().staticFloaters()) {
			return;
		}
		var models = Minecraft.getInstance().getModelManager().getBlockStateModelSet();
		if (models.get(tile.getBlockState()) instanceof FloatingFlowerModel model) {
			state.parts = model.parts(data.getIslandType());
			state.time = ClientTickHandler.getEntityTicksInGame() + partialTick
					+ new Random(tile.getBlockPos().hashCode()).nextInt(1000);
		}
	}

	static void submitIsland(State state, PoseStack pose, SubmitNodeCollector collector) {
		if (state.parts.isEmpty()) {
			return;
		}
		double time = state.time;
		pose.pushPose();
		pose.translate(0.5F, 0, 0.5F);
		pose.mulPose(VecHelper.rotateY(-((float) time * 0.5F)));
		pose.translate(-0.5, Math.sin(time * 0.05F) * 0.1, 0.5);
		pose.mulPose(VecHelper.rotateX(4F * (float) Math.sin(time * 0.04F)));
		pose.mulPose(VecHelper.rotateY(90));

		for (BlockStateModelPart part : state.parts) {
			var layer = (part.materialFlags() & BakedQuad.FLAG_TRANSLUCENT) != 0
					? RenderTypes.translucentMovingBlock() : RenderTypes.cutoutMovingBlock();
			collector.submitBlockModel(pose, layer, List.of(part), BlockModelRenderState.EMPTY_TINTS,
					state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private List<BlockStateModelPart> parts = List.of();
		private double time;
	}
}
