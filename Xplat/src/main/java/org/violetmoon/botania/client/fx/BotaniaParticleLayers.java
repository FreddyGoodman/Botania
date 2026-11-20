/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.client.fx;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaParticleLayers {
	public static final SingleQuadParticle.Layer ADDITIVE = layer("additive_particle", Identifier.withDefaultNamespace("core/particle"), CompareOp.LESS_THAN_OR_EQUAL);
	public static final SingleQuadParticle.Layer ADDITIVE_WITHOUT_DEPTH_TEST = layer("additive_particle_no_depth", Identifier.withDefaultNamespace("core/particle"), CompareOp.ALWAYS_PASS);
	public static final SingleQuadParticle.Layer CORRUPT = layer("corrupt_sparkle", botaniaRL("core/film_grain_particle"), CompareOp.LESS_THAN_OR_EQUAL);

	private BotaniaParticleLayers() {}

	public static boolean usesLinearFiltering(SingleQuadParticle.Layer layer) {
		return layer == ADDITIVE || layer == ADDITIVE_WITHOUT_DEPTH_TEST || layer == CORRUPT;
	}

	private static SingleQuadParticle.Layer layer(String name, Identifier fragmentShader, CompareOp depthTest) {
		var pipeline = RenderPipeline.builder()
				.withLocation(botaniaRL("pipeline/" + name))
				.withVertexShader("core/particle")
				.withFragmentShader(fragmentShader)
				.withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
				.withUniform("Projection", UniformType.UNIFORM_BUFFER)
				.withUniform("Fog", UniformType.UNIFORM_BUFFER)
				.withSampler("Sampler0")
				.withSampler("Sampler2")
				.withVertexFormat(DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
				.withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
				.withDepthStencilState(new DepthStencilState(depthTest, false))
				.build();
		return new SingleQuadParticle.Layer(true, TextureAtlas.LOCATION_PARTICLES, pipeline);
	}
}
