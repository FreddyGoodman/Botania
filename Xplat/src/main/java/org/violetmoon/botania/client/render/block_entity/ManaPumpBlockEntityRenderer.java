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
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.common.block.block_entity.mana.ManaPumpBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.List;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class ManaPumpBlockEntityRenderer implements BlockEntityRenderer<ManaPumpBlockEntity, ManaPumpBlockEntityRenderer.State> {
	public static final Identifier PUMP_HEAD_ID = botaniaRL("block/mana_pump_head");

	public ManaPumpBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaPumpBlockEntity pump, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(pump, state, partialTick, camera, crumblingOverlay);
		state.angle = switch (pump.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
			case SOUTH -> 180;
			case EAST -> -90;
			case WEST -> 90;
			default -> 0;
		};
		state.extension = Math.clamp(pump.innerRingPos + pump.moving * partialTick, 0F, 8F) / 14F;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		pose.mulPose(VecHelper.rotateY(state.angle));
		pose.translate(-0.5, 0, -0.5 + state.extension);
		var head = ClientXplatAbstractions.INSTANCE.getBlockModelPart(PUMP_HEAD_ID);
		collector.submitBlockModel(pose, RenderTypes.solidMovingBlock(), List.of(head), BlockModelRenderState.EMPTY_TINTS,
				state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private float angle;
		private float extension;
	}
}
