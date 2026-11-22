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
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.violetmoon.botania.api.block_entity.RadiusDescriptor;
import org.violetmoon.botania.api.block_entity.SpecialFlowerBlockEntity;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.helper.PlayerHelper;
import org.violetmoon.botania.common.item.WandOfTheForestItem;
import org.violetmoon.botania.common.item.equipment.bauble.ManaseerMonocleItem;

public class SpecialFlowerBlockEntityRenderer<T extends SpecialFlowerBlockEntity> implements BlockEntityRenderer<T, SpecialFlowerBlockEntityRenderer.State> {
	private static final int INNER_ALPHA = 32;
	private static final int OUTER_ALPHA = 64;
	private static final float FRAME_WIDTH = 1F / 16F;
	private static final float Y_OFFSET_INNER = 1F / 16F;
	private static final float Y_OFFSET_OUTER = FRAME_WIDTH + FRAME_WIDTH / 4F;
	private static final int TOTAL_ANGLES = 360;

	public SpecialFlowerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(T flower, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(flower, state, partialTick, camera, crumblingOverlay);
		FloatingFlowerBlockEntityRenderer.extractIsland(flower, flower.getFloatingData(), state, partialTick);
		state.primary = null;
		state.secondary = null;
		if (!(Minecraft.getInstance().getCameraEntity() instanceof LivingEntity view) || !ManaseerMonocleItem.hasMonocle(view)) {
			return;
		}

		boolean binding = hasBindingAttempt(view, flower.getBlockPos());
		var hit = Minecraft.getInstance().hitResult;
		if (binding || hit instanceof BlockHitResult target && target.getType() == BlockHitResult.Type.BLOCK && target.getBlockPos().equals(flower.getBlockPos())) {
			state.primary = flower.getRadius();
			state.secondary = flower.getSecondaryRadius();
			state.binding = binding;
			state.color = radiusColor(partialTick);
		}
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		FloatingFlowerBlockEntityRenderer.submitIsland(state, pose, collector);
		if (state.primary != null || state.secondary != null) {
			pose.pushPose();
			if (state.binding) {
				pose.translate(0, 0.005, 0);
			}
			submitRadius(state.blockPos, state.primary, state.color, pose, collector);
			pose.translate(0, 0.002, 0);
			submitRadius(state.blockPos, state.secondary, state.color, pose, collector);
			pose.popPose();
		}
	}

	public static int radiusColor(float partialTick) {
		return Mth.hsvToRgb((ClientTickHandler.getEntityTicksInGame() + partialTick) % 200 / 200F, 0.6F, 1F);
	}

	public static void submitRadius(BlockPos pos, @Nullable RadiusDescriptor descriptor, int color,
			PoseStack pose, SubmitNodeCollector collector) {
		if (descriptor == null) {
			return;
		}
		pose.pushPose();
		pose.translate(0, RenderHelper.getOffY(), 0);
		if (descriptor instanceof RadiusDescriptor.Circle circle) {
			var center = circle.subtileCoords();
			pose.translate(center.getX() - pos.getX() + 0.5, center.getY() - pos.getY(), center.getZ() - pos.getZ() + 0.5);
			double radius = circle.radius();
			collector.submitCustomGeometry(pose, RenderHelper.getCircleLayer(), (transform, buffer) -> {
				drawCircle(transform.pose(), buffer, radius - FRAME_WIDTH, Y_OFFSET_INNER, ARGB.color(INNER_ALPHA, color));
				drawCircle(transform.pose(), buffer, radius, Y_OFFSET_OUTER, ARGB.color(OUTER_ALPHA, color));
			});
		} else if (descriptor instanceof RadiusDescriptor.Rectangle rectangle) {
			var box = rectangle.aabb();
			pose.translate(box.minX - pos.getX(), box.minY - pos.getY(), box.minZ - pos.getZ());
			float x = (float) box.getXsize();
			float z = (float) box.getZsize();
			collector.submitCustomGeometry(pose, RenderHelper.getRectangleLayer(), (transform, buffer) -> {
				int r = ARGB.red(color);
				int g = ARGB.green(color);
				int b = ARGB.blue(color);
				RenderHelper.flatRectangle(buffer, transform, FRAME_WIDTH, x - FRAME_WIDTH, Y_OFFSET_INNER,
						FRAME_WIDTH, z - FRAME_WIDTH, r, g, b, INNER_ALPHA);
				RenderHelper.flatRectangle(buffer, transform, 0, x, Y_OFFSET_OUTER, 0, z, r, g, b, OUTER_ALPHA);
			});
		}
		RenderHelper.incrementOffY();
		pose.popPose();
	}

	private static void drawCircle(Matrix4f pose, VertexConsumer buffer, double radius, float y, int color) {
		float lastX = (float) radius;
		float lastZ = 0;
		for (int angle = 1; angle <= TOTAL_ANGLES; angle++) {
			double radians = -angle * (2 * Math.PI / TOTAL_ANGLES);
			float x = (float) (Math.cos(radians) * radius);
			float z = (float) (Math.sin(radians) * radius);
			buffer.addVertex(pose, 0, y, 0).setColor(color);
			buffer.addVertex(pose, lastX, y, lastZ).setColor(color);
			buffer.addVertex(pose, x, y, z).setColor(color);
			lastX = x;
			lastZ = z;
		}
	}

	public static boolean hasBindingAttempt(LivingEntity view, BlockPos tilePos) {
		var stack = PlayerHelper.getFirstHeldItemClass(view, WandOfTheForestItem.class);
		return !stack.isEmpty() && WandOfTheForestItem.getBindMode(stack)
				&& WandOfTheForestItem.getBindingAttempt(stack).map(GlobalPos::pos).filter(tilePos::equals).isPresent();
	}

	public static class State extends FloatingFlowerBlockEntityRenderer.State {
		private @Nullable RadiusDescriptor primary;
		private @Nullable RadiusDescriptor secondary;
		private boolean binding;
		private int color;
	}
}
