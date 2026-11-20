/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.core.proxy.ClientProxy;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.client.model.PixieModel;
import org.violetmoon.botania.common.entity.PixieEntity;

public class PixieRenderer extends MobRenderer<PixieEntity, PixieRenderer.State, PixieModel> {

	public PixieRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new PixieModel(ctx.bakeLayer(BotaniaModelLayers.PIXIE)), 0.0F);
	}

	@Override
	public Identifier getTextureLocation(State state) {
		return ClientProxy.dootDoot
				? Identifier.parse(ResourcesLib.MODEL_PIXIE_HALLOWEEN)
				: Identifier.parse(ResourcesLib.MODEL_PIXIE);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(PixieEntity entity, State state, float partialTick) {
		super.extractRenderState(entity, state, partialTick);
		state.gaia = entity.isGaia();
	}

	@Override
	protected RenderType getRenderType(State state, boolean visible, boolean translucent, boolean outline) {
		if (visible && state.gaia) {
			return RenderHelper.getGaiaNoiseConstantLayer(getTextureLocation(state));
		}
		return super.getRenderType(state, visible, translucent, outline);
	}

	public static class State extends LivingEntityRenderState {
		private boolean gaia;
	}
}
