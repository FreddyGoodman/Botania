/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.block_entity;

import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import org.violetmoon.botania.common.block.block_entity.StarfieldCreatorBlockEntity;
import org.violetmoon.botania.mixin.client.RenderTypeAccessor;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class StarfieldCreatorBlockEntityRenderer implements BlockEntityRenderer<StarfieldCreatorBlockEntity, BlockEntityRenderState> {
	private static final RenderPipeline PIPELINE = RenderPipeline.builder()
			.withLocation(botaniaRL("pipeline/starfield_creator"))
			.withVertexShader("core/rendertype_end_portal")
			.withFragmentShader(botaniaRL("core/starfield_creator"))
			.withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
			.withUniform("Projection", UniformType.UNIFORM_BUFFER)
			.withUniform("Fog", UniformType.UNIFORM_BUFFER)
			.withUniform("Globals", UniformType.UNIFORM_BUFFER)
			.withSampler("Sampler0")
			.withSampler("Sampler1")
			.withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.withShaderDefine("PORTAL_LAYERS", 15)
			.build();
	private static final RenderType STARFIELD = RenderTypeAccessor.botania_create("botania:starfield_creator", RenderSetup.builder(PIPELINE)
			.withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
			.withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION)
			.bufferSize(256)
			.createRenderSetup());

	public StarfieldCreatorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public BlockEntityRenderState createRenderState() {
		return new BlockEntityRenderState();
	}

	@Override
	public void submit(BlockEntityRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		collector.submitCustomGeometry(pose, STARFIELD, (transform, buffer) -> {
			float height = 0.24F;
			buffer.addVertex(transform.pose(), 0, height, 1);
			buffer.addVertex(transform.pose(), 1, height, 1);
			buffer.addVertex(transform.pose(), 1, height, 0);
			buffer.addVertex(transform.pose(), 0, height, 0);
		});
	}
}
