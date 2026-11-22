/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.AABB;

import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.core.helper.ShaderCompatibility;
import org.violetmoon.botania.common.entity.GaiaTrapEntity;

public class GaiaTrapRenderer extends EntityRenderer<GaiaTrapEntity, GaiaTrapRenderer.State> {

	public GaiaTrapRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(GaiaTrapEntity entity, State state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.bounds = entity.getBoundingBox().move(entity.position().scale(-1));
		state.brightness = (float) (Math.sin((ClientTickHandler.getEntityTicksInGame() + partialTick) / 20) + 1) * 0.2F + 0.6F;

		float age = entity.tickCount + partialTick;
		// Keep the ground warning legible after the shader pack applies lighting and fog.
		int peakAlpha = ShaderCompatibility.useVanillaPipelines() ? 96 : 32;
		if (entity.tickCount < 8) {
			state.alpha = (int) (peakAlpha * Math.clamp(age / 8F, 0F, 1F));
		} else if (entity.tickCount > 47) {
			state.alpha = (int) (peakAlpha * Math.clamp(1F - (age - 47) / 8F, 0F, 1F));
		} else {
			state.alpha = peakAlpha;
		}
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, pose, collector, camera);
		AABB bounds = state.bounds;
		float edge = 1F / 16F;
		float width = (float) bounds.getXsize() - edge;
		float length = (float) bounds.getZsize() - edge;
		int red = (int) (105 * state.brightness);
		int green = (int) (25 * state.brightness);
		int blue = (int) (145 * state.brightness);
		int alpha = state.alpha;

		pose.pushPose();
		pose.translate(bounds.minX, bounds.minY + RenderHelper.getOffY(), bounds.minZ);
		collector.submitCustomGeometry(pose, RenderHelper.getRectangleLayer(), (transform, buffer) -> RenderHelper.flatRectangle(buffer, transform, edge, width, 0, edge, length, red, green, blue, alpha));
		RenderHelper.incrementOffY();
		pose.popPose();
	}

	public static class State extends EntityRenderState {
		private AABB bounds;
		private float brightness;
		private int alpha;
	}
}
