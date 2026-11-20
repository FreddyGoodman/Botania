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
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class TerrestrialAgglomerationPlateBlockEntityRenderer implements BlockEntityRenderer<TerrestrialAgglomerationPlateBlockEntity, TerrestrialAgglomerationPlateBlockEntityRenderer.State> {
	private final TextureAtlasSprite overlaySprite;

	public TerrestrialAgglomerationPlateBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		overlaySprite = ctx.sprites().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, botaniaRL("block/terrestrial_agglomeration_plate_overlay")));
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(TerrestrialAgglomerationPlateBlockEntity plate, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(plate, state, partialTick, camera, crumblingOverlay);
		float alphaMod = Math.min(1.0F, plate.getCompletion() / 0.1F);
		state.alpha = (float) ((Math.sin((ClientTickHandler.getEntityTicksInGame() + partialTick) / 8D) + 1D) / 5D + 0.6D) * alphaMod;
	}

	@Override
	public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		float alpha = state.alpha;
		if (alpha <= 0) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0F, 3F / 16F + 0.001F, 0F);
		poseStack.mulPose(VecHelper.rotateX(90F));
		collector.submitCustomGeometry(poseStack, RenderHelper.getTerraPlateLayer(),
				(pose, buffer) -> RenderHelper.renderIconFullBright(pose, buffer, overlaySprite, alpha));
		poseStack.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private float alpha;
	}
}
