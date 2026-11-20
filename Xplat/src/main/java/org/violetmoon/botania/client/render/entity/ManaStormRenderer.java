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

import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.entity.ManaStormEntity;

public class ManaStormRenderer extends EntityRenderer<ManaStormEntity, ManaStormRenderer.State> {
	public ManaStormRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaStormEntity storm, State state, float partialTick) {
		super.extractRenderState(storm, state, partialTick);
		float progress = (float) storm.burstsFired / ManaStormEntity.TOTAL_BURSTS;
		float fading = storm.deathTime == 0 ? 0 : (storm.deathTime + partialTick) / ManaStormEntity.DEATH_TIME;
		state.scale = 0.05F + (progress - fading) * 1.95F;
		state.seed = storm.getUUID().getMostSignificantBits();
		state.ticks = ClientTickHandler.getEntityTicksInGame() + partialTick;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		RenderHelper.submitStar(pose, collector, 0x00FF00, state.scale, state.seed, state.ticks);
	}

	public static class State extends EntityRenderState {
		private float scale;
		private long seed;
		private float ticks;
	}
}
