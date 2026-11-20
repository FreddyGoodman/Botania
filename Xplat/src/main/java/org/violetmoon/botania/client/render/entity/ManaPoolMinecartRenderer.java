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

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.violetmoon.botania.client.render.block_entity.ManaPoolBlockEntityRenderer;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.mana.ManaPoolBlock;
import org.violetmoon.botania.common.entity.ManaPoolMinecartEntity;

public class ManaPoolMinecartRenderer extends AbstractMinecartRenderer<ManaPoolMinecartEntity, ManaPoolMinecartRenderer.State> {
	private final TextureAtlasSprite water;
	private final ManaPoolBlockEntityRenderer.Surface surface;

	public ManaPoolMinecartRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, ModelLayers.MINECART);
		water = ManaPoolBlockEntityRenderer.waterSprite(ctx.getSprites());
		surface = ManaPoolBlockEntityRenderer.Surface.forBlock(BotaniaBlocks.MANA_POOL, BotaniaBlocks.MANA_POOL.defaultBlockState());
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaPoolMinecartEntity cart, State state, float partialTick) {
		super.extractRenderState(cart, state, partialTick);
		state.manaLevel = (float) cart.getMana() / ManaPoolBlock.MAX_MANA;
	}

	@Override
	protected void submitMinecartContents(State state, BlockModelRenderState displayModel, PoseStack pose,
			SubmitNodeCollector collector, int light) {
		super.submitMinecartContents(state, displayModel, pose, collector, light);
		ManaPoolBlockEntityRenderer.submitMana(pose, collector, surface, water, state.manaLevel, light);
	}

	public static class State extends MinecartRenderState {
		private float manaLevel;
	}
}
