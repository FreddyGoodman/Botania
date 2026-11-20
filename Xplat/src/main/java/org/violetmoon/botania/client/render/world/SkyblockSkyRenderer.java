/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.world;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.helper.VecHelper;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;

public final class SkyblockSkyRenderer {
	private static final RenderType[] PLANETS = IntStream.range(0, 6)
			.mapToObj(index -> RenderHelper.skyLayer(Identifier.parse(ResourcesLib.MISC_PLANET + index + ".png"), false))
			.toArray(RenderType[]::new);
	private static final RenderType RAYS = RenderHelper.skyLayer(Identifier.parse(ResourcesLib.MISC_SKYBOX), true);
	private static final RenderType RAINBOW = RenderHelper.skyLayer(Identifier.parse(ResourcesLib.MISC_RAINBOW), false);
	private static final float[] PLANET_SIZES = { 20, 12, 15, 25, 10, 40 };
	private static final int RAY_SEGMENTS = 90;

	private SkyblockSkyRenderer() {}

	public record State(float celestialAngle, float rain, float age, long worldTime) {
		private float distanceFromNoon() {
			return Math.min(celestialAngle, 1F - celestialAngle);
		}
	}

	public static void renderExtras(PoseStack pose, State state) {
		float rayAlpha = Math.max(0F, state.distanceFromNoon() - 0.3F) * state.rain();
		renderPlanets(pose, Math.clamp(Math.max(0.1F, rayAlpha) * 4, 0, 1));
		renderRays(pose, state.age(), rayAlpha);
		renderRainbow(pose, state);
	}

	private static void renderPlanets(PoseStack pose, float alpha) {
		pose.pushPose();
		pose.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(90), 0.5F, 0.5F, 0F));
		int color = ARGB.color(ARGB.as8BitChannel(alpha), 0xFFFFFF);

		for (int planet = 0; planet < PLANETS.length; planet++) {
			float size = PLANET_SIZES[planet];
			Matrix4f matrix = pose.last().pose();
			RenderType layer = PLANETS[planet];
			var buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, layer.format());
			buffer.addVertex(matrix, -size, 100, -size).setUv(0, 0).setColor(color);
			buffer.addVertex(matrix, size, 100, -size).setUv(1, 0).setColor(color);
			buffer.addVertex(matrix, size, 100, size).setUv(1, 1).setColor(color);
			buffer.addVertex(matrix, -size, 100, size).setUv(0, 1).setColor(color);
			layer.draw(buffer.buildOrThrow());

			switch (planet) {
				case 0 -> pose.mulPose(VecHelper.rotateX(70));
				case 1 -> pose.mulPose(VecHelper.rotateZ(120));
				case 2 -> pose.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(80), 1, 0, 1));
				case 3 -> pose.mulPose(VecHelper.rotateZ(100));
				case 4 -> pose.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(-60), 1, 0, 0.5F));
			}
		}
		pose.popPose();
	}

	private static void renderRays(PoseStack pose, float age, float alpha) {
		if (alpha <= 0) {
			return;
		}
		pose.pushPose();
		pose.translate(0, -1, 0);
		pose.mulPose(VecHelper.rotateX(220));
		int opacity = ARGB.as8BitChannel(alpha);

		pose.mulPose(VecHelper.rotateY(age * 0.1F));
		drawRing(pose, RAYS, 20, 10, age * 0.4F, ARGB.color(opacity, 0xFFFFFF));
		pose.mulPose(VecHelper.rotateX(20));
		pose.mulPose(VecHelper.rotateY(age * 0.02F));
		drawRing(pose, RAYS, 20, 14, age * 0.08F, ARGB.color(opacity, 0xFF6666));
		pose.mulPose(VecHelper.rotateX(50));
		pose.mulPose(VecHelper.rotateY(age * 0.2F));
		drawRing(pose, RAYS, 20, 6, age * 0.8F, ARGB.color(opacity, 0x66FFB3));
		pose.popPose();
	}

	private static void renderRainbow(PoseStack pose, State state) {
		float angle = state.celestialAngle();
		float alpha = 0.25F - Math.min(0.25F, angle > 0.25F ? 1F - angle : angle);
		if (alpha <= 0) {
			return;
		}
		long day = (state.worldTime() + 1000) / 24000L;
		Random random = new Random(day * 0xFF);

		pose.pushPose();
		pose.mulPose(VecHelper.rotateY(random.nextFloat() * 360F));
		pose.mulPose(VecHelper.rotateZ(random.nextFloat() * 360F));
		drawRing(pose, RAINBOW, 10, 0, 0, ARGB.color(ARGB.as8BitChannel(alpha), 0xFFFFFF));
		pose.popPose();
	}

	private static void drawRing(PoseStack pose, RenderType layer, float radius, int waves, float rotation, int color) {
		var buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, layer.format());
		Matrix4f matrix = pose.last().pose();
		for (int segment = 0; segment < RAY_SEGMENTS; segment += 2) {
			ringVertex(buffer, matrix, segment - 1, radius, waves, rotation, 1, color);
			ringVertex(buffer, matrix, segment - 1, radius, waves, rotation, 0, color);
			ringVertex(buffer, matrix, segment + 1, radius, waves, rotation, 0, color);
			ringVertex(buffer, matrix, segment + 1, radius, waves, rotation, 1, color);
		}
		layer.draw(buffer.buildOrThrow());
	}

	private static void ringVertex(VertexConsumer buffer, Matrix4f matrix, int segment, float radius,
			int waves, float rotation, float v, int color) {
		float degrees = segment * 360F / RAY_SEGMENTS + rotation;
		float radians = degrees * Mth.DEG_TO_RAD;
		float y = Mth.sin((float) Math.PI * waves * segment / RAY_SEGMENTS) + 2F * v;
		buffer.addVertex(matrix, Mth.cos(radians) * radius, y, Mth.sin(radians) * radius)
				.setUv(degrees / 360F, v).setColor(color);
	}

	public static void renderStars(GpuBuffer stars, int indexCount, RenderSystem.AutoStorageIndexBuffer indices, State state) {
		float alpha = state.rain() * Math.max(0.1F, state.distanceFromNoon() * 2);
		if (alpha <= 0) {
			return;
		}
		var target = Minecraft.getInstance().getMainRenderTarget();
		var indexBuffer = indices.getBuffer(indexCount);
		float angle = (state.age() + 2000) * 0.005F * Mth.DEG_TO_RAD;
		float[][] colors = { { 1, 1, 1 }, { 0.5F, 1, 1 }, { 1, 0.75F, 0.75F } };
		float[] rotations = { 3, 1, 2 };
		var transforms = RenderSystem.getDynamicUniforms();

		GpuBufferSlice[] layers = new GpuBufferSlice[6];
		for (int axis = 0; axis < 2; axis++) {
			float brightness = axis == 0 ? alpha : alpha * 0.25F;
			for (int layer = 0; layer < colors.length; layer++) {
				var view = new Matrix4f(RenderSystem.getModelViewMatrix());
				if (axis == 0) {
					view.rotateY(angle * rotations[layer]);
				} else {
					view.rotateZ(angle * rotations[layer]);
				}
				float[] color = colors[layer];
				var tint = new Vector4f(color[0] * brightness, color[1] * brightness, color[2] * brightness, brightness);
				layers[axis * colors.length + layer] = transforms.writeTransform(view, tint, new Vector3f(), new Matrix4f());
			}
		}

		try (var pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
				() -> "Botania sky stars", target.getColorTextureView(), OptionalInt.empty(),
				target.getDepthTextureView(), OptionalDouble.empty())) {
			pass.setPipeline(RenderPipelines.STARS);
			RenderSystem.bindDefaultUniforms(pass);
			pass.setVertexBuffer(0, stars);
			pass.setIndexBuffer(indexBuffer, indices.type());
			for (var layer : layers) {
				pass.setUniform("DynamicTransforms", layer);
				pass.drawIndexed(0, 0, indexCount, 1);
			}
		}
	}
}
