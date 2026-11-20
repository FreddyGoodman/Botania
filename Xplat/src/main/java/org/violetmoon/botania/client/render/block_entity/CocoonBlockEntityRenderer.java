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
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.common.block.block_entity.CocoonBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

public class CocoonBlockEntityRenderer implements BlockEntityRenderer<CocoonBlockEntity, CocoonBlockEntityRenderer.State> {
	private static final BlockDisplayContext DISPLAY_CONTEXT = BlockDisplayContext.create();

	private final BlockModelResolver blockModels;

	public CocoonBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.blockModels = ctx.blockModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(CocoonBlockEntity cocoon, State state, float partialTicks, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(cocoon, state, partialTicks, camera, crumblingOverlay);
		state.rotation = 0;
		float period = 60F - (float) cocoon.timePassed / CocoonBlockEntity.TOTAL_TIME * 30F;
		float time = cocoon.timePassed + partialTicks;
		if (time > 0 && cocoon.timePassed % period < 10) {
			float radians = time % period / 5 * (float) Math.PI * 2;
			state.rotation = (float) Math.sin(radians) * (float) Math.log(time);
		}
		blockModels.update(state.model, cocoon.getBlockState(), DISPLAY_CONTEXT);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0.5, 0, 0);
		pose.mulPose(VecHelper.rotateX(state.rotation));
		pose.translate(-0.5, 0, 0);
		state.model.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private final BlockModelRenderState model = new BlockModelRenderState();
		private float rotation;
	}
}
