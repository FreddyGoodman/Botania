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
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.client.model.BotanicalBreweryModel;
import org.violetmoon.botania.common.block.block_entity.BotanicalBreweryBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.Arrays;

public class BotanicalBreweryBlockEntityRenderer implements
		BlockEntityRenderer<BotanicalBreweryBlockEntity, BotanicalBreweryBlockEntityRenderer.State>, BlockEntityItemRenderer.Display {
	private static final int ITEM_PLATES = 7;
	private final BotanicalBreweryModel model;
	private final ItemModelResolver itemModels;

	public BotanicalBreweryBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		model = new BotanicalBreweryModel(ctx.bakeLayer(BotaniaModelLayers.BREWERY));
		itemModels = ctx.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(BotanicalBreweryBlockEntity brewery, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(brewery, state, partialTick, camera, crumblingOverlay);
		state.time = ClientTickHandler.getEntityTicksInGame() + partialTick;
		if (state.items.length != brewery.inventorySize()) {
			state.items = new ItemStackRenderState[brewery.inventorySize()];
			Arrays.setAll(state.items, i -> new ItemStackRenderState());
		}
		for (int slot = 0; slot < state.items.length; slot++) {
			itemModels.updateForTopItem(state.items[slot], brewery.getItemHandler().getItem(slot),
					ItemDisplayContext.GROUND, brewery.getLevel(), null, 0);
		}
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		submitBrewery(state.time, state.items.length - 1, state.items, pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		submitBrewery(ClientTickHandler.getEntityTicksInGame() + partialTick, ITEM_PLATES, null, pose, collector, light, overlay);
	}

	private void submitBrewery(double time, int plates, ItemStackRenderState @Nullable [] items,
			PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		pose.pushPose();
		pose.scale(1F, -1F, -1F);
		pose.translate(0.5F, -1.5F + (float) Math.sin(time / 40) * 0.1F + 0.05F, -0.5F);
		float angle = (float) time / 16F;
		float poleRotation = -angle * 25F;

		pose.pushPose();
		pose.mulPose(VecHelper.rotateY(poleRotation));
		if (items != null && !items[0].isEmpty()) {
			pose.pushPose();
			pose.mulPose(VecHelper.rotateX(180));
			pose.translate(0F, -0.45F, 0F);
			pose.scale(0.25F, 0.25F, 0.25F);
			items[0].submit(pose, collector, light, overlay, 0);
			pose.popPose();
		}
		model.submitColumn(pose, collector, light, overlay);
		pose.popPose();

		float anglePerPlate = (float) (2 * Math.PI) / plates;
		for (int plate = 0; plate < plates; plate++) {
			float degrees = angle * 180F / (float) Math.PI;
			float offset = (float) Math.sin(time / 20 + plate * 40F) * 0.2F - 0.2F;
			pose.pushPose();
			pose.translate(0F, offset, 0F);
			if (items != null && !items[plate + 1].isEmpty()) {
				pose.pushPose();
				pose.mulPose(VecHelper.rotateY(degrees));
				pose.translate(0.3125F, 1.06F, 0.1245F);
				pose.mulPose(VecHelper.rotateX(-90F));
				pose.translate(0.125F, 0.125F, 0F);
				pose.scale(0.25F, 0.25F, 0.25F);
				items[plate + 1].submit(pose, collector, light, overlay, 0);
				pose.popPose();
			}
			model.submitPlate(pose, collector, light, overlay, degrees);
			pose.popPose();
			angle += anglePerPlate;
		}
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private ItemStackRenderState[] items = new ItemStackRenderState[0];
		private double time;
	}
}
