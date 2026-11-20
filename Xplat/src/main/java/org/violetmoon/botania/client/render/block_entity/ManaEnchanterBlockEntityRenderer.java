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
import com.mojang.math.Axis;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class ManaEnchanterBlockEntityRenderer implements BlockEntityRenderer<ManaEnchanterBlockEntity, ManaEnchanterBlockEntityRenderer.State> {
	private final TextureAtlasSprite overlaySprite;
	private final ItemModelResolver itemModels;
	private final RandomSource itemRandom = RandomSource.create();

	public ManaEnchanterBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		overlaySprite = ctx.sprites().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, botaniaRL("block/mana_enchanter_overlay")));
		itemModels = ctx.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaEnchanterBlockEntity enchanter, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(enchanter, state, partialTick, camera, crumblingOverlay);
		var stack = enchanter.itemToEnchant;
		itemModels.updateForTopItem(state.items.item, stack, ItemDisplayContext.GROUND, enchanter.getLevel(), null, 0);
		state.items.count = ItemClusterRenderState.getRenderedAmount(stack.getCount());
		state.items.seed = ItemClusterRenderState.getSeedForItemStack(stack);
		state.ticks = ClientTickHandler.getEntityTicksInGame() + partialTick;
		state.phase = RandomSource.create(enchanter.getBlockPos().asLong()).nextFloat() * Mth.TWO_PI;

		float opacity = switch (enchanter.stage) {
			case GATHER_MANA -> Math.min(20, enchanter.stageTicks) / 20F;
			case RESET -> (20 - enchanter.stageTicks) / 20F;
			case DO_ENCHANT -> 1F;
			default -> 0F;
		};
		state.alpha = (float) ((Math.sin(state.ticks / 8D) + 1D) / 5D + 0.4D) * opacity;
		state.rising = enchanter.stage == ManaEnchanterBlockEntity.State.DO_ENCHANT || enchanter.stage == ManaEnchanterBlockEntity.State.RESET;
		state.riseTicks = enchanter.stageTicks + enchanter.stage3EndTicks + partialTick;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (!state.items.item.isEmpty()) {
			pose.pushPose();
			var bounds = state.items.item.getModelBoundingBox();
			float bob = Mth.sin(state.ticks / 10F + state.phase) * 0.1F + 0.1F;
			pose.translate(0.5, 1.25 + bob + 0.0625 - bounds.minY, 0.5);
			pose.mulPose(Axis.YP.rotation(ItemEntity.getSpin(state.ticks, state.phase)));
			ItemEntityRenderer.submitMultipleFromCount(pose, collector, state.lightCoords, state.items, itemRandom, bounds);
			pose.popPose();
		}

		float alpha = state.alpha;
		if (alpha <= 0) {
			return;
		}
		pose.pushPose();
		pose.mulPose(VecHelper.rotateX(90F));
		pose.translate(-2F, -2F, -0.001F);

		if (state.rising) {
			float ticks = state.riseTicks;
			float rise = Math.min(20, ticks) / 20F * 1.15F;
			float scale = ticks < 10 ? 1F : 1F - Math.min(20, ticks - 10) / 20F * 0.75F;
			pose.translate(2.5F, 2.5F, -rise);
			pose.scale(scale, scale, 1F);
			pose.mulPose(VecHelper.rotateZ(ticks * 2));
			pose.translate(-2.5F, -2.5F, 0F);
		}

		int light = state.lightCoords;
		collector.submitCustomGeometry(pose, RenderHelper.getEnchanterLayer(), (transform, buffer) -> {
			PoseStack geometry = new PoseStack();
			geometry.last().set(transform);
			RenderHelper.renderIconFullBright(geometry, buffer, 0, 0, 5, 5, 0, 0, 16, 16, overlaySprite, 0xFFFFFF, alpha, light);
		});
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private final ItemClusterRenderState items = new ItemClusterRenderState();
		private float ticks;
		private float phase;
		private float alpha;
		private boolean rising;
		private float riseTicks;
	}
}
