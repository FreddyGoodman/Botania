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
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.*;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.PylonBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.Random;

public class PylonBlockEntityRenderer implements BlockEntityRenderer<PylonBlockEntity, PylonBlockEntityRenderer.State>, BlockEntityItemRenderer.Display {

	public static final Identifier MANA_TEXTURE = Identifier.parse(ResourcesLib.MODEL_PYLON_MANA);
	public static final Identifier NATURA_TEXTURE = Identifier.parse(ResourcesLib.MODEL_PYLON_NATURA);
	public static final Identifier GAIA_TEXTURE = Identifier.parse(ResourcesLib.MODEL_PYLON_GAIA);

	private final ManaPylonModel manaModel;
	private final NaturaPylonModel naturaModel;
	private final GaiaPylonModel gaiaModel;

	public PylonBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		manaModel = new ManaPylonModel(ctx.bakeLayer(BotaniaModelLayers.PYLON_MANA));
		naturaModel = new NaturaPylonModel(ctx.bakeLayer(BotaniaModelLayers.PYLON_NATURA));
		gaiaModel = new GaiaPylonModel(ctx.bakeLayer(BotaniaModelLayers.PYLON_GAIA));
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(PylonBlockEntity pylon, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(pylon, state, partialTick, camera, crumblingOverlay);
		state.block = pylon.getBlockState().getBlock();
		state.time = ClientTickHandler.getEntityTicksInGame() + partialTick
				+ new Random(pylon.getBlockPos().hashCode()).nextInt(360);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		submitPylon(state.block, false, false, state.time, pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		submitPylon(block, true, direct, 0, pose, collector, light, overlay);
	}

	private void submitPylon(Block type, boolean item, boolean direct, float worldTime, PoseStack ms,
			SubmitNodeCollector collector, int light, int overlay) {
		PylonModel model;
		Identifier texture;
		if (BotaniaBlocks.NATURA_PYLON.equals(type)) {
			model = naturaModel;
			texture = NATURA_TEXTURE;
		} else if (BotaniaBlocks.GAIA_PYLON.equals(type)) {
			model = gaiaModel;
			texture = GAIA_TEXTURE;
		} else {
			model = manaModel;
			texture = MANA_TEXTURE;
		}

		ms.pushPose();

		ms.translate(0, item ? 1.35 : 1.5, 0);
		ms.scale(1.0F, -1.0F, -1.0F);

		ms.pushPose();
		ms.translate(0.5F, 0F, -0.5F);
		if (!item) {
			ms.mulPose(VecHelper.rotateY(worldTime * 1.5F));
		}

		RenderType layer = RenderTypes.entityTranslucent(texture);

		collector.submitCustomGeometry(ms, layer, (transform, buffer) -> {
			PoseStack geometry = new PoseStack();
			geometry.last().set(transform);
			model.renderRing(geometry, buffer, light, overlay);
		});
		ms.popPose();

		ms.pushPose();
		if (!item) {
			ms.translate(0D, Math.sin(worldTime / 20D) / 17.5, 0D);
		}

		ms.translate(0.5F, 0F, -0.5F);
		if (!item) {
			ms.mulPose(VecHelper.rotateY(-worldTime));
		}

		collector.submitCustomGeometry(ms, RenderHelper.getPylonGlow(texture, direct), (transform, buffer) -> {
			PoseStack geometry = new PoseStack();
			geometry.last().set(transform);
			model.renderCrystal(geometry, buffer, light, overlay);
		});

		ms.popPose();

		ms.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private Block block = BotaniaBlocks.MANA_PYLON;
		private float time;
	}
}
