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
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.common.block.AnimatedTorchBlock;
import org.violetmoon.botania.common.block.block_entity.AnimatedTorchBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.Random;

public class AnimatedTorchBlockEntityRenderer implements BlockEntityRenderer<AnimatedTorchBlockEntity, AnimatedTorchBlockEntityRenderer.State> {
	private static final double ANIM_HORIZONTAL_TICK_SCALE = 0.05;
	private static final double ANIM_VERTICAL_TICK_SCALE = 0.04;
	private static final double ANIM_TICK_CYCLE = 2 * Math.PI / ANIM_HORIZONTAL_TICK_SCALE / ANIM_VERTICAL_TICK_SCALE;

	private static final BlockDisplayContext DISPLAY_CONTEXT = BlockDisplayContext.create();

	private final BlockModelResolver blockModels;

	public AnimatedTorchBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.blockModels = ctx.blockModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(AnimatedTorchBlockEntity torch, State state, float partialTicks, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(torch, state, partialTicks, camera, crumblingOverlay);
		Level level = torch.getLevel();
		double time = level == null
				? 0
				: ClientTickHandler.getEntityTicksInGame() % ANIM_TICK_CYCLE
						+ partialTicks
						+ new Random(torch.getBlockState().getSeed(torch.getBlockPos())).nextDouble(ANIM_TICK_CYCLE);

		state.rotation = 0;
		if (level != null) {
			float targetRotation = torch.getBlockState().getValue(AnimatedTorchBlock.FACING).toYRot();
			if (torch.lastRotation == -1) {
				// clientside rotation was not initialized yet
				torch.lastRotation = targetRotation;
				torch.rotation = targetRotation;
			} else if (torch.rotation != targetRotation) {
				float rotationDiff = targetRotation - torch.lastRotation;
				if (rotationDiff > 180) {
					rotationDiff -= 360;
				} else if (rotationDiff < -180) {
					rotationDiff += 360;
				}
				float rotationProgress = Math.clamp(
						((level.getGameTime() - torch.rotationStartTime) + partialTicks)
								/ AnimatedTorchBlockEntity.ROTATION_TICKS,
						0, 1);
				if (rotationProgress >= 1) {
					torch.lastRotation = targetRotation;
					torch.rotation = targetRotation;
				} else {
					torch.rotation = torch.lastRotation + rotationProgress * rotationDiff;
				}
			}
			state.rotation = torch.rotation;
		}

		state.x = -0.5 + Math.cos(time * ANIM_HORIZONTAL_TICK_SCALE) * 0.025;
		state.y = -0.3 + Math.sin(time * ANIM_VERTICAL_TICK_SCALE) * 0.05;
		state.z = -0.5 + Math.sin(time * ANIM_HORIZONTAL_TICK_SCALE) * 0.025;

		BlockState torchState = Blocks.REDSTONE_TORCH.defaultBlockState()
				.setValue(RedstoneTorchBlock.LIT, !torch.getBlockState().getValue(AnimatedTorchBlock.TRIGGERED));
		blockModels.update(state.model, torchState, DISPLAY_CONTEXT);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0.5, 0.2, 0.5);
		pose.mulPose(VecHelper.rotateX(90));
		pose.mulPose(VecHelper.rotateZ(state.rotation));
		pose.translate(state.x, state.y, state.z);
		state.model.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private final BlockModelRenderState model = new BlockModelRenderState();
		private float rotation;
		private double x;
		private double y;
		private double z;
	}
}
