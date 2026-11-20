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
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.violetmoon.botania.api.block.PetalApothecary;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.Arrays;

public class PetalApothecaryBlockEntityRenderer implements BlockEntityRenderer<PetalApothecaryBlockEntity, PetalApothecaryBlockEntityRenderer.State> {
	private final TextureAtlasSprite waterSprite;
	private final TextureAtlasSprite lavaSprite;
	private final ItemModelResolver itemModels;

	public PetalApothecaryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		waterSprite = context.sprites().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, Identifier.withDefaultNamespace("block/water_still")));
		lavaSprite = context.sprites().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, Identifier.withDefaultNamespace("block/lava_still")));
		itemModels = context.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(PetalApothecaryBlockEntity apothecary, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(apothecary, state, partialTick, camera, crumblingOverlay);
		state.fluid = apothecary.getFluid();
		state.itemCount = 0;
		if (state.fluid != PetalApothecary.State.WATER) {
			return;
		}

		var level = apothecary.getLevel();
		state.waterColor = level instanceof ClientLevel clientLevel
				? BiomeColors.getAverageWaterColor(clientLevel, apothecary.getBlockPos()) : -1;
		state.ticks = (ClientTickHandler.getEntityTicksInGame() + partialTick) * 0.5;
		if (state.items.length != apothecary.inventorySize()) {
			state.items = new ItemStackRenderState[apothecary.inventorySize()];
			Arrays.setAll(state.items, i -> new ItemStackRenderState());
		}
		for (int slot = 0; slot < state.items.length; slot++) {
			var stack = apothecary.getItemHandler().getItem(slot);
			if (stack.isEmpty()) {
				break;
			}
			itemModels.updateForTopItem(state.items[slot], stack, ItemDisplayContext.GROUND, level, null, 0);
			state.itemCount++;
		}
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		boolean lava = state.fluid == PetalApothecary.State.LAVA;
		if (!lava && state.fluid != PetalApothecary.State.WATER) {
			return;
		}
		pose.pushPose();
		pose.translate(0.5, 1.25, 0.5);
		if (state.itemCount > 0) {
			submitIngredients(state, pose, collector);
		}

		TextureAtlasSprite sprite = lava ? lavaSprite : waterSprite;
		int color = lava ? -1 : state.waterColor;
		float alpha = lava ? 1F : 0.7F;
		int light = lava ? LightCoordsUtil.FULL_BRIGHT : state.lightCoords;
		pose.translate(-0.5F, -0.3125F, -0.5F);
		pose.mulPose(VecHelper.rotateX(90));
		pose.scale(1F / 16F, 1F / 16F, 1F / 16F);
		collector.order(1).submitCustomGeometry(pose, Sheets.translucentBlockSheet(),
				(transform, buffer) -> renderIcon(transform, buffer, sprite, color, alpha, light));
		pose.popPose();
	}

	private void submitIngredients(State state, PoseStack pose, SubmitNodeCollector collector) {
		double ticks = state.ticks;
		float offsetPerItem = 360F / state.itemCount;
		float radiusX = (float) (1.2F + 0.1F * Math.sin(ticks / 6F));
		float radiusZ = (float) (1.2F + 0.1F * Math.cos(ticks / 6F));
		float xRotate = (float) Math.sin(ticks * 0.25F) / 2F;
		float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
		float zRotate = (float) Math.cos(ticks * 0.25F) / 2F;

		pose.pushPose();
		pose.translate(-0.05F, -0.38F, 0F);
		pose.scale(0.125F, 0.125F, 0.125F);
		for (int i = 0; i < state.itemCount; i++) {
			float angle = VecHelper.toRadians((int) (ticks / 0.25F % 360F + offsetPerItem * i));
			double y = Math.cos((ticks + 50 * i) / 5F) / 10F;
			pose.pushPose();
			pose.translate(radiusX * Math.cos(angle), y, radiusZ * Math.sin(angle));
			pose.translate(0.0625F, 0.0625F, 0.0625F);
			pose.mulPose(new Quaternionf().rotateAxis(angle, xRotate, yRotate, zRotate));
			pose.translate(-0.0625F, -0.0625F, -0.0625F);
			state.items[i].submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			pose.popPose();
		}
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private ItemStackRenderState[] items = new ItemStackRenderState[0];
		private PetalApothecary.State fluid;
		private int itemCount;
		private int waterColor;
		private double ticks;
	}

	private static void renderIcon(PoseStack.Pose pose, VertexConsumer builder, TextureAtlasSprite sprite, int color, float alpha, int light) {
		int red = ((color >> 16) & 0xFF);
		int green = ((color >> 8) & 0xFF);
		int blue = (color & 0xFF);
		Matrix4f mat = pose.pose();
		int start = 3;
		int end = 13;
		// TODO: fluid content should probably be part of the blockstate model, if we limit it to water and lava anyway
		builder.addVertex(mat, start, end, 0).setColor(red, green, blue, (int) (alpha * 255F))
				.setUv(sprite.getU(start / 16f), sprite.getV(end / 16f)).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
		builder.addVertex(mat, end, end, 0).setColor(red, green, blue, (int) (alpha * 255F))
				.setUv(sprite.getU(end / 16f), sprite.getV(end / 16f)).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
		builder.addVertex(mat, end, start, 0).setColor(red, green, blue, (int) (alpha * 255F))
				.setUv(sprite.getU(end / 16f), sprite.getV(start / 16f)).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
		builder.addVertex(mat, start, start, 0).setColor(red, green, blue, (int) (alpha * 255F))
				.setUv(sprite.getU(start / 16f), sprite.getV(start / 16f)).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0, 0, 1);
	}

}
