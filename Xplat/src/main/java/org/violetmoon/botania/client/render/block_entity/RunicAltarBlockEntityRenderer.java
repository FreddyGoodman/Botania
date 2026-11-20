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

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.block.block_entity.RunicAltarBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.Arrays;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class RunicAltarBlockEntityRenderer implements BlockEntityRenderer<RunicAltarBlockEntity, RunicAltarBlockEntityRenderer.State> {
	private final ModelPart spinningCube;
	private final ItemModelResolver itemModels;
	private static final Identifier cubeTex = botaniaRL("textures/block/runic_altar_cube.png");

	public RunicAltarBlockEntityRenderer(BlockEntityRendererProvider.Context manager) {
		itemModels = manager.itemModelResolver();
		var mesh = new MeshDefinition();
		// todo 1.17 this doesn't properly map the full texture onto the cube. Needs more param fiddling.
		mesh.getRoot().addOrReplaceChild("cube", CubeListBuilder.create().addBox(0, 0, 0, 1, 1, 1), PartPose.ZERO);
		spinningCube = LayerDefinition.create(mesh, 16, 16).bakeRoot();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(RunicAltarBlockEntity altar, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(altar, state, partialTick, camera, crumblingOverlay);
		if (state.items.length != altar.inventorySize()) {
			state.items = new ItemStackRenderState[altar.inventorySize()];
			Arrays.setAll(state.items, i -> new ItemStackRenderState());
		}
		state.itemCount = 0;
		for (int slot = 0; slot < state.items.length; slot++) {
			var stack = altar.getItemHandler().getItem(slot);
			itemModels.updateForTopItem(state.items[slot], stack, ItemDisplayContext.GROUND, altar.getLevel(), null, 0);
			if (!stack.isEmpty()) {
				state.itemCount++;
			}
		}
		state.time = altar.getLevel().getGameTime() + (double) partialTick;
		state.starTicks = ClientTickHandler.getEntityTicksInGame() + partialTick;
		state.starScale = altar.getTargetMana() == 0 ? 0 : (float) altar.getCurrentMana() / altar.getTargetMana() / 75F;
		state.starSeed = altar.getBlockPos().getX() ^ altar.getBlockPos().getY() ^ altar.getBlockPos().getZ();
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.itemCount > 0) {
			float anglePer = 360F / state.itemCount;
			for (int slot = 0; slot < state.items.length; slot++) {
				ItemStackRenderState item = state.items[slot];
				if (item.isEmpty()) {
					continue;
				}
				pose.pushPose();
				pose.translate(0.5F, 1.25F, 0.5F);
				pose.mulPose(VecHelper.rotateY(anglePer * (slot + 1) + (float) (state.time % 360)));
				pose.translate(1.125F, 0F, 0.25F);
				pose.mulPose(VecHelper.rotateY(90F));
				pose.translate(0D, 0.075 * Math.sin((state.time + slot * 10) / 5), 0F);
				item.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
				pose.popPose();
			}
		}

		pose.pushPose();
		pose.translate(0.5F, 0.5F, 0.5F);
		submitSpinningCubes(pose, collector, 2, 15, state.time);
		pose.popPose();

		if (state.starScale != 0) {
			pose.pushPose();
			pose.translate(0.5F, 0.9F, 0.5F);
			RenderHelper.submitStar(pose, collector, 0x00E4D7, state.starScale, state.starSeed, state.starTicks);
			pose.popPose();
		}
	}

	private void submitSpinningCubes(PoseStack ms, SubmitNodeCollector collector, int cubes, int iters, double time) {
		for (int curIter = iters; curIter > 0; curIter--) {
			final float modifier = 6F;
			final float rotationModifier = 0.2F;
			final float radiusBase = 0.35F;
			final float radiusMod = 0.05F;

			double ticks = time - 1.3 * (iters - curIter);
			float offsetPerCube = 360f / cubes;

			ms.pushPose();
			ms.translate(-0.025F, 0.85F, -0.025F);
			for (int i = 0; i < cubes; i++) {
				float offset = offsetPerCube * i;
				float deg = (int) (ticks / rotationModifier % 360F + offset);
				float rad = VecHelper.toRadians(deg);
				float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
				float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
				float x = (float) (radiusX * Math.cos(rad));
				float z = (float) (radiusZ * Math.sin(rad));
				float y = (float) Math.cos((ticks + 50 * i) / 5F) / 10F;

				ms.pushPose();
				ms.translate(x, y, z);
				float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
				float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
				float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

				ms.mulPose(new Quaternionf().rotateAxis(rad, xRotate, yRotate, zRotate));
				float alpha = 1;
				if (curIter < iters) {
					alpha = (float) curIter / (float) iters * 0.4F;
				}

				var layer = curIter < iters ? RenderTypes.entityTranslucentCullItemTarget(cubeTex) : RenderTypes.entitySolid(cubeTex);
				int color = ARGB.colorFromFloat(alpha, 1, 1, 1);
				collector.submitModelPart(spinningCube, ms, layer, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
						null, false, false, color, null, 0);

				ms.popPose();
			}
			ms.popPose();
		}
	}

	public static class State extends BlockEntityRenderState {
		private ItemStackRenderState[] items = new ItemStackRenderState[0];
		private int itemCount;
		private double time;
		private float starTicks;
		private float starScale;
		private int starSeed;
	}
}
