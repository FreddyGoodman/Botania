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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.mana.BasicLensItem;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.common.block.block_entity.mana.ManaPrismBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

public class ManaPrismBlockEntityRenderer implements BlockEntityRenderer<ManaPrismBlockEntity, ManaPrismBlockEntityRenderer.State> {
	private final ItemModelResolver itemModels;

	public ManaPrismBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		itemModels = ctx.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaPrismBlockEntity tile, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTick, camera, crumblingOverlay);
		ItemStack stack = tile.getItemHandler().getItem(0);
		if (!(stack.getItem() instanceof BasicLensItem)) {
			state.item.clear();
			return;
		}
		itemModels.updateForTopItem(state.item, stack, ItemDisplayContext.NONE, tile.getLevel(), null, 0);
		state.offset = (float) Math.sin((ClientTickHandler.getEntityTicksInGame() + partialTick) * 0.05F) * 0.5F * (1F - 1F / 16F) * 0.997F - 0.5F;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.item.isEmpty()) {
			return;
		}

		pose.pushPose();
		pose.mulPose(VecHelper.rotateX(90));
		pose.translate(0.5F, 0.5F, state.offset);
		pose.scale(1.003F, 1.003F, 1F);
		state.item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private final ItemStackRenderState item = new ItemStackRenderState();
		private float offset;
	}
}
