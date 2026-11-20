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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.block.block_entity.red_string.RedStringBlockEntity;
import org.violetmoon.botania.common.helper.PlayerHelper;
import org.violetmoon.botania.common.item.WandOfTheForestItem;

import java.util.Random;

public class RedStringBlockEntityRenderer<T extends RedStringBlockEntity> implements BlockEntityRenderer<T, RedStringBlockEntityRenderer.State> {
	// 0 -> none, 10 -> full
	private static int transparency = 0;

	public static void tick() {
		Player player = Minecraft.getInstance().player;
		boolean hasWand = player != null && PlayerHelper.hasHeldItemClass(player, WandOfTheForestItem.class);
		if (transparency > 0 && !hasWand) {
			transparency--;
		} else if (transparency < 10 && hasWand) {
			transparency++;
		}
	}

	public RedStringBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(T tile, State state, float partialTicks, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTicks, camera, crumblingOverlay);
		BlockPos binding = tile.getBinding();
		state.span = binding == null ? Vec3.ZERO : Vec3.atLowerCornerOf(binding.subtract(tile.getBlockPos()));
		state.direction = tile.getOrientation();
		state.opacity = transparency / 10.0F;
		state.phase = -(ClientTickHandler.getEntityTicksInGame() + partialTicks) / 100F
				+ new Random(state.direction.ordinal() ^ tile.getBlockPos().asLong()).nextInt(10000);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.opacity <= 0 || state.span.equals(Vec3.ZERO)) {
			return;
		}

		pose.pushPose();
		pose.translate(0.5, 0.5, 0.5);
		collector.submitCustomGeometry(pose, RenderHelper.RED_STRING, (transform, buffer) -> {
			Vec3 step = state.span.normalize().scale(0.025);
			Vec3 current = step;
			double stride = step.length();
			int stepCount = (int) (state.span.length() / stride);
			double phase = state.phase;
			double jitter = Math.random() - 0.5;
			for (int i = 0; i < stepCount; i++) {
				vertex(transform, buffer, state.direction, current, state.opacity, jitter, phase);
				jitter = Math.random() - 0.5;
				current = current.add(step);
				phase += stride;
				vertex(transform, buffer, state.direction, current, state.opacity, jitter, phase);
			}
		});
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private Vec3 span = Vec3.ZERO;
		private Direction direction = Direction.NORTH;
		private float opacity;
		private double phase;
	}

	/**
	 * Add a vertex at the given position, but spiraled out perpendicular to {@code dir}
	 */
	private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, Direction dir, Vec3 position,
			float sizeAlpha, double rand, double l) {
		float ampl = (float) (0.15 * (Mth.sin((float) l * 2F) * 0.5 + 0.5) + 0.1) * sizeAlpha;

		float trigInput = (float) (l * 20.0);
		float sin = Mth.sin(trigInput);
		float cos = Mth.cos(trigInput);
		float lastTerm = (float) (rand * 0.05);

		float x = (float) position.x
				+ sin * ampl * killNonZero(dir.getStepX())
				+ lastTerm;
		float y = (float) position.y
				+ cos * ampl * killNonZero(dir.getStepY())
				+ lastTerm;
		float z = (float) position.z
				+ (dir.getStepY() == 0 ? sin : cos) * ampl * killNonZero(dir.getStepZ())
				+ lastTerm;

		buffer.addVertex(pose, x, y, z).setColor(0xFF, 0, 0, ARGB.as8BitChannel(sizeAlpha)).setLineWidth(1);
		switch (dir.getAxis().getPlane()) {
			case HORIZONTAL -> buffer.setNormal(pose, 0, 1, 0);
			case VERTICAL -> buffer.setNormal(pose, 1, 0, 0);
		}
	}

	private static int killNonZero(int diff) {
		if (diff != 0) {
			return 0;
		} else {
			return 1;
		}
	}

}
