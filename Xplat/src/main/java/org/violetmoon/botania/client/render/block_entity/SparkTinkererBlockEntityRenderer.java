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
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.common.block.block_entity.SparkTinkererBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

public class SparkTinkererBlockEntityRenderer implements BlockEntityRenderer<SparkTinkererBlockEntity, SparkTinkererBlockEntityRenderer.State> {
	private final ItemModelResolver itemModels;

	public SparkTinkererBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		itemModels = ctx.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(SparkTinkererBlockEntity tile, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTick, camera, crumblingOverlay);
		itemModels.updateForTopItem(state.item, tile.getItemHandler().getItem(0), ItemDisplayContext.GROUND, tile.getLevel(), null, 0);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.item.isEmpty()) {
			return;
		}

		pose.pushPose();
		pose.mulPose(VecHelper.rotateX(90));
		pose.translate(1.0F, -0.125F, -0.25F);
		pose.mulPose(VecHelper.rotateY(180));
		pose.translate(0.5F, 0.5F, 0);
		state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private final ItemStackRenderState item = new ItemStackRenderState();
	}
}
