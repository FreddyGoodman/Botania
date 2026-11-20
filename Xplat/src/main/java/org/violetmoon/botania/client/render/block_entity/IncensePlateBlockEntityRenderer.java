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
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.common.block.block_entity.IncensePlateBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

public class IncensePlateBlockEntityRenderer implements BlockEntityRenderer<IncensePlateBlockEntity, IncensePlateBlockEntityRenderer.State> {
	private final ItemModelResolver itemModels;

	public IncensePlateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		itemModels = ctx.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(IncensePlateBlockEntity tile, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTick, camera, crumblingOverlay);
		itemModels.updateForTopItem(state.item, tile.getItemHandler().getItem(0), ItemDisplayContext.GROUND, tile.getLevel(), null, 0);
		state.facing = tile.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.item.isEmpty()) {
			return;
		}

		pose.pushPose();
		pose.translate(0.5F, 1.5F, 0.5F);
		int degrees = switch (state.facing) {
			case WEST -> 90;
			case SOUTH -> 180;
			case EAST -> 270;
			default -> 0;
		};
		pose.mulPose(VecHelper.rotateY(degrees));
		pose.translate(-0.11F, -1.35F, 0F);
		pose.scale(0.6F, 0.6F, 0.6F);
		state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private final ItemStackRenderState item = new ItemStackRenderState();
		private Direction facing = Direction.NORTH;
	}
}
