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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.client.model.HourglassModel;
import org.violetmoon.botania.common.block.HoveringHourglassBlock;
import org.violetmoon.botania.common.block.block_entity.HoveringHourglassBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.Random;

public class HoveringHourglassBlockEntityRenderer implements BlockEntityRenderer<HoveringHourglassBlockEntity, HoveringHourglassBlockEntityRenderer.State>, BlockEntityItemRenderer.Display {
	private static final double ANIM_HORIZONTAL_TICK_SCALE = 0.05;
	private static final double ANIM_VERTICAL_TICK_SCALE = 0.04;
	private static final double ANIM_TICK_CYCLE = 2 * Math.PI / ANIM_HORIZONTAL_TICK_SCALE / ANIM_VERTICAL_TICK_SCALE;

	final Identifier texture = Identifier.parse(ResourcesLib.MODEL_HOVERING_HOURGLASS);
	private final HourglassModel model;

	public HoveringHourglassBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		model = new HourglassModel(ctx.bakeLayer(BotaniaModelLayers.HOURGLASS));
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(HoveringHourglassBlockEntity hourglass, State renderState, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(hourglass, renderState, partialTick, camera, crumblingOverlay);
		Level level = hourglass.getLevel();
		BlockState state = hourglass.getBlockState();
		double time = level == null
				? 0
				: ClientTickHandler.getEntityTicksInGame() % ANIM_TICK_CYCLE
						+ partialTick
						+ new Random(state.getSeed(hourglass.getBlockPos())).nextDouble(ANIM_TICK_CYCLE);
		boolean flipped = state.getValue(HoveringHourglassBlock.FLIPPED);

		renderState.time = time;
		renderState.rotation = 0F;

		if (level != null) {
			float targetRotation = flipped ? 180 : 0;
			if (hourglass.lastRotation == -1) {
				// clientside rotation was not initialized yet
				hourglass.lastRotation = targetRotation;
				hourglass.rotation = targetRotation;
			} else if (hourglass.rotation != targetRotation) {
				float rotationDiff = targetRotation - hourglass.lastRotation;
				float rotationProgress = Math.clamp(
						((level.getGameTime() - hourglass.rotationStartTime) + partialTick)
								/ HoveringHourglassBlockEntity.FLIP_TICKS,
						0, 1);
				if (rotationProgress >= 1) {
					hourglass.lastRotation = targetRotation;
					hourglass.rotation = targetRotation;
				} else {
					hourglass.rotation = hourglass.lastRotation + rotationProgress * rotationDiff;
				}
			}
			renderState.rotation = hourglass.rotation;
		}

		boolean active = state.getValue(HoveringHourglassBlock.ACTIVE);
		int totalTicks = hourglass.getTotalTime();
		float currentTicks = Math.max(0, hourglass.getTime() - (active ? partialTick : 0));
		renderState.bottomFraction = totalTicks > 0 ? currentTicks / totalTicks : 0;
		renderState.topFraction = totalTicks > 0 ? 1 - renderState.bottomFraction : 0;
		renderState.flipped = flipped;
		renderState.color = hourglass.getColor();
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		submitHourglass(state, pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		submitHourglass(new State(), pose, collector, light, overlay);
	}

	private void submitHourglass(State state, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		pose.pushPose();
		double xt = 0.5 + Math.cos(state.time * ANIM_HORIZONTAL_TICK_SCALE) * 0.025;
		double yt = 0.55 + Math.sin(state.time * ANIM_VERTICAL_TICK_SCALE) * 0.05;
		double zt = 0.5 + Math.sin(state.time * ANIM_HORIZONTAL_TICK_SCALE) * 0.025;
		pose.translate(xt, yt, zt);
		pose.mulPose(VecHelper.rotateZ(state.rotation));
		pose.scale(1F, -1F, -1F);
		int color = state.color | 0xFF000000;
		float top = state.topFraction;
		float bottom = state.bottomFraction;
		boolean flipped = state.flipped;
		collector.submitCustomGeometry(pose, model.renderType(texture), (transform, buffer) -> {
			PoseStack geometry = new PoseStack();
			geometry.last().set(transform);
			model.render(geometry, buffer, light, overlay, color, top, bottom, flipped);
		});
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private double time;
		private float rotation;
		private float topFraction;
		private float bottomFraction;
		private boolean flipped;
		private int color;
	}
}
