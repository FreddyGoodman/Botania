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
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.proxy.ClientProxy;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.client.model.TeruTeruBozuModel;
import org.violetmoon.botania.common.block.block_entity.TeruTeruBozuBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.Random;

public class TeruTeruBozuBlockEntityRenderer implements BlockEntityRenderer<TeruTeruBozuBlockEntity, TeruTeruBozuBlockEntityRenderer.State>, BlockEntityItemRenderer.Display {

	private static final Identifier texture = Identifier.parse(ResourcesLib.MODEL_TERU_TERU_BOZU);
	private static final Identifier textureHalloween = Identifier.parse(ResourcesLib.MODEL_TERU_TERU_BOZU_HALLOWEEN);
	private final TeruTeruBozuModel model;

	public TeruTeruBozuBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		model = new TeruTeruBozuModel(ctx.bakeLayer(BotaniaModelLayers.TERU_TERU_BOZU));
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(TeruTeruBozuBlockEntity teru, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(teru, state, partialTick, camera, crumblingOverlay);
		state.time = ClientTickHandler.getEntityTicksInGame() + partialTick
				+ new Random(teru.getBlockPos().hashCode()).nextInt(1000);
		state.raining = teru.getLevel() != null && teru.getLevel().isRaining();
		state.texture = ClientProxy.dootDoot ? textureHalloween : texture;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.mulPose(VecHelper.rotateX(180));
		pose.translate(0.5F, -1.25F + (float) Math.sin(state.time * 0.01F) * 0.05F, -0.5F);
		pose.mulPose(VecHelper.rotateY((float) (state.time * 0.3)));
		pose.mulPose(VecHelper.rotateZ(4F * (float) Math.sin(state.time * 0.05F)));
		pose.scale(0.75F, 0.75F, 0.75F);
		collector.submitModel(model, state.raining, pose, state.texture, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
		pose.popPose();
	}

	@Override
	public void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		var level = Minecraft.getInstance().level;
		pose.pushPose();
		pose.mulPose(VecHelper.rotateX(180));
		pose.translate(0.5F, -1.25F, -0.5F);
		collector.submitModel(model, level != null && level.isRaining(), pose, ClientProxy.dootDoot ? textureHalloween : texture,
				light, overlay, 0, null);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private double time;
		private boolean raining;
		private Identifier texture = TeruTeruBozuBlockEntityRenderer.texture;
	}
}
