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

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaCrystalCubeBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.List;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class CorporeaCrystalCubeBlockEntityRenderer implements BlockEntityRenderer<CorporeaCrystalCubeBlockEntity, CorporeaCrystalCubeBlockEntityRenderer.State> {
	public static final Identifier CUBE_MODEL_ID = botaniaRL("block/corporea_crystal_cube_glass");

	private final Font font;
	private final ItemModelResolver itemModels;
	private final RandomSource itemRandom = RandomSource.create();

	public CorporeaCrystalCubeBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		font = ctx.font();
		itemModels = ctx.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(CorporeaCrystalCubeBlockEntity cube, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(cube, state, partialTick, camera, crumblingOverlay);
		var stack = cube.getRequestTarget();
		itemModels.updateForTopItem(state.items.item, stack, ItemDisplayContext.GROUND, cube.getLevel(), null, 0);
		state.items.count = ItemClusterRenderState.getRenderedAmount(stack.getCount());
		state.items.seed = ItemClusterRenderState.getSeedForItemStack(stack);
		state.ticks = ClientTickHandler.getEntityTicksInGame() + partialTick;
		state.phase = RandomSource.create(cube.getBlockPos().asLong()).nextFloat() * Mth.TWO_PI;
		state.label = null;
		if (!stack.isEmpty() && !cube.isHiddenCount()) {
			int count = cube.getItemCount();
			String label;
			int color;
			if (count <= 9_999) {
				label = Integer.toString(count);
				color = DyeColor.WHITE.getTextColor();
			} else if (count > 9_999_999) {
				label = count / 1_000_000 + "M";
				color = DyeColor.GREEN.getTextColor();
			} else {
				label = count / 1_000 + "K";
				color = DyeColor.YELLOW.getTextColor();
			}
			state.label = FormattedCharSequence.forward(label, Style.EMPTY);
			state.labelWidth = font.width(label);
			state.labelColor = ARGB.color(160, color);
			state.labelShadow = ARGB.color(160, (color & 0xfcfcfc) >> 2);
		}
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		float bob = Mth.sin(state.ticks / 10F + state.phase) * 0.1F + 0.1F;
		pose.pushPose();
		pose.translate(0.5F, 1.5F, 0.5F);
		pose.scale(1F, -1F, -1F);
		pose.translate(0F, bob / -7F, 0F);

		if (!state.items.item.isEmpty()) {
			pose.pushPose();
			pose.translate(0F, 0.96F, 0F);
			pose.scale(0.64F, 0.64F, 0.64F);
			pose.mulPose(VecHelper.rotateZ(180));
			var bounds = state.items.item.getModelBoundingBox();
			pose.translate(0, bob + 0.0625 - bounds.minY, 0);
			pose.mulPose(Axis.YP.rotation(ItemEntity.getSpin(state.ticks, state.phase)));
			ItemEntityRenderer.submitMultipleFromCount(pose, collector, state.lightCoords, state.items, itemRandom, bounds);
			pose.popPose();
		}

		pose.pushPose();
		pose.translate(-0.5F, 0.25F, -0.5F);
		var glass = ClientXplatAbstractions.INSTANCE.getBlockModelPart(CUBE_MODEL_ID);
		collector.submitBlockModel(pose, RenderTypes.translucentMovingBlock(), List.of(glass), BlockModelRenderState.EMPTY_TINTS,
				state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();

		if (state.label != null) {
			submitLabels(state, pose, collector);
		}
		pose.popPose();
	}

	private static void submitLabels(State state, PoseStack pose, SubmitNodeCollector collector) {
		pose.scale(1F / 64F, 1F / 64F, 1F / 64F);
		pose.translate(0F, 55F, 0F);
		float x = -state.labelWidth / 2;
		for (int side = 0; side < 4; side++) {
			pose.mulPose(VecHelper.rotateY(90));
			pose.pushPose();
			pose.translate(0, 0, -16.5F);
			collector.submitText(pose, x, 0, state.label, false, Font.DisplayMode.NORMAL, state.lightCoords, state.labelColor, 0, 0);
			pose.translate(0, 0, 0.1F);
			collector.submitText(pose, x + 1, 1, state.label, false, Font.DisplayMode.NORMAL, state.lightCoords, state.labelShadow, 0, 0);
			pose.popPose();
		}
	}

	public static class State extends BlockEntityRenderState {
		private final ItemClusterRenderState items = new ItemClusterRenderState();
		private float ticks;
		private float phase;
		private @Nullable FormattedCharSequence label;
		private int labelWidth;
		private int labelColor;
		private int labelShadow;
	}
}
