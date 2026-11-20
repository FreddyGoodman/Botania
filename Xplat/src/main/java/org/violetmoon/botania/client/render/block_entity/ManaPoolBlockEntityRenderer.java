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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.mana.PoolOverlayProvider;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.render.ColorHandler;
import org.violetmoon.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import org.violetmoon.botania.common.block.mana.ManaPoolBlock;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.ArrayList;
import java.util.List;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class ManaPoolBlockEntityRenderer implements BlockEntityRenderer<ManaPoolBlockEntity, ManaPoolBlockEntityRenderer.State> {
	private final SpriteGetter sprites;
	private final TextureAtlasSprite waterSprite;

	public ManaPoolBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		sprites = ctx.sprites();
		waterSprite = waterSprite(sprites);
	}

	public static TextureAtlasSprite waterSprite(SpriteGetter sprites) {
		return sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, botaniaRL("block/mana_water")));
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaPoolBlockEntity pool, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(pool, state, partialTick, camera, crumblingOverlay);
		var blockState = pool.getBlockState();
		var pos = pool.getBlockPos();
		var level = pool.getLevel();
		var block = (ManaPoolBlock) blockState.getBlock();
		state.surface = Surface.forBlock(block, blockState);
		state.manaLevel = (float) pool.getCurrentMana() / pool.getMaxMana();

		state.model.clear();
		if (block.isFabulous()) {
			var models = Minecraft.getInstance().getModelManager().getBlockStateModelSet();
			models.get(blockState).collectParts(RandomSource.create(blockState.getSeed(pos)), state.model);
			state.tint[0] = ARGB.opaque(ColorHandler.getManaPoolColor(block, blockState, pos));
		}

		state.overlaySprite = null;
		if (level != null) {
			PoolOverlayProvider provider = PoolOverlayProvider.LOOKUP.find(level, pos.below());
			if (provider != null) {
				state.overlaySprite = sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, provider.getIcon(level, pos)));
				state.alpha = (float) ((Mth.sin((ClientTickHandler.getEntityTicksInGame() + partialTick) / 20F) + 1) * 0.3 + 0.2);
			}
		}
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (!state.model.isEmpty()) {
			collector.submitBlockModel(pose, RenderTypes.cutoutMovingBlock(), state.model, state.tint,
					state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		}
		if (state.overlaySprite != null) {
			submitSurface(pose, collector, state.surface, state.surface.bottom, state.overlaySprite,
					RenderHelper.ICON_OVERLAY, state.alpha, state.lightCoords);
		}
		submitMana(pose, collector, state.surface, waterSprite, state.manaLevel, state.lightCoords);
	}

	public static void submitMana(PoseStack pose, SubmitNodeCollector collector, Surface surface,
			TextureAtlasSprite water, float fraction, int light) {
		if (fraction > 0) {
			float y = Mth.clampedMap(fraction, 0, 1, surface.bottom, surface.top);
			submitSurface(pose, collector, surface, y, water, RenderHelper.getManaPoolWaterLayer(), 1F, light);
		}
	}

	private static void submitSurface(PoseStack pose, SubmitNodeCollector collector, Surface surface, float y,
			TextureAtlasSprite sprite, RenderType layer, float alpha, int light) {
		pose.pushPose();
		pose.translate(0, y, 0);
		pose.mulPose(VecHelper.rotateX(90));
		collector.submitCustomGeometry(pose, layer, (transform, buffer) -> RenderHelper.renderIconCropped(transform, buffer, surface.minX, surface.minZ, surface.maxX, surface.maxZ,
				sprite, 0xFFFFFF, alpha, light));
		pose.popPose();
	}

	public record Surface(float bottom, float top, int minX, int minZ, int maxX, int maxZ) {
		public static Surface forBlock(ManaPoolBlock block, BlockState state) {
			var inner = block.getInnerShape(state);
			var outer = block.getInteractionShape(state, null, null);
			return new Surface((float) inner.min(Direction.Axis.Y) + 0.001F,
					(float) outer.max(Direction.Axis.Y) - 1F / 16F,
					(int) (16 * inner.min(Direction.Axis.X)), (int) (16 * inner.min(Direction.Axis.Z)),
					(int) (16 * inner.max(Direction.Axis.X)), (int) (16 * inner.max(Direction.Axis.Z)));
		}
	}

	public static class State extends BlockEntityRenderState {
		private final List<BlockStateModelPart> model = new ArrayList<>();
		private final int[] tint = new int[1];
		private Surface surface;
		private float manaLevel;
		private @Nullable TextureAtlasSprite overlaySprite;
		private float alpha;
	}
}
