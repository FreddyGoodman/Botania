/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.core.helper;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;

import org.joml.Matrix4f;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.common.item.equipment.bauble.FlugelTiaraItem;
import org.violetmoon.botania.mixin.client.RenderTypeAccessor;
import org.violetmoon.botania.xplat.BotaniaConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class RenderHelper {
	private static final DepthStencilState READ_DEPTH = new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false);
	private static final DepthStencilState NO_DEPTH = new DepthStencilState(CompareOp.ALWAYS_PASS, false);

	private static final RenderPipeline OVERLAY_LINES = linePipeline("overlay_lines", NO_DEPTH);
	private static final RenderType STAR = colorLayer("star", VertexFormat.Mode.TRIANGLES, BlendFunction.LIGHTNING, READ_DEPTH,
			true, OutputTarget.MAIN_TARGET, false);
	public static final RenderType RECTANGLE = colorLayer("rectangle_highlight", VertexFormat.Mode.QUADS, BlendFunction.TRANSLUCENT,
			DepthStencilState.DEFAULT, false, OutputTarget.ITEM_ENTITY_TARGET, true);
	public static final RenderType CIRCLE = colorLayer("circle_highlight", VertexFormat.Mode.TRIANGLES, BlendFunction.TRANSLUCENT,
			DepthStencilState.DEFAULT, false, OutputTarget.ITEM_ENTITY_TARGET, false);
	public static final RenderType LIGHTNING = colorLayer("lightning", VertexFormat.Mode.QUADS, BlendFunction.LIGHTNING,
			DepthStencilState.DEFAULT, true, OutputTarget.MAIN_TARGET, true);
	public static final RenderType RED_STRING = lineLayer("red_string", linePipeline("red_string", DepthStencilState.DEFAULT), OutputTarget.ITEM_ENTITY_TARGET, 128);
	public static final RenderType LINE_1_NO_DEPTH = lineLayer("line_1_no_depth", OVERLAY_LINES, OutputTarget.MAIN_TARGET, 128);
	public static final RenderType LINE_4_NO_DEPTH = lineLayer("line_4_no_depth", OVERLAY_LINES, OutputTarget.MAIN_TARGET, 128);
	public static final RenderType LINE_5_NO_DEPTH = lineLayer("line_5_no_depth", OVERLAY_LINES, OutputTarget.MAIN_TARGET, 64);
	public static final RenderType LINE_8_NO_DEPTH = lineLayer("line_8_no_depth", OVERLAY_LINES, OutputTarget.MAIN_TARGET, 64);
	private static final RenderPipeline LIT_OVERLAY_PIPELINE = litOverlayPipeline("lit_overlay", false);
	public static final RenderType SPARK = litOverlay("spark", LIT_OVERLAY_PIPELINE, TextureAtlas.LOCATION_ITEMS, 256, true);
	public static final RenderType ICON_OVERLAY = litOverlay("icon_overlay", LIT_OVERLAY_PIPELINE, TextureAtlas.LOCATION_BLOCKS, 128, true);
	private static final RenderType PLAIN_OVERLAY = litOverlay("plain_overlay", LIT_OVERLAY_PIPELINE, TextureAtlas.LOCATION_BLOCKS, 128, false);
	private static final RenderType MANA_POOL_WATER = litOverlay("mana_pool_water", litOverlayPipeline("mana_pool", true), TextureAtlas.LOCATION_BLOCKS, 128, false);
	private static final RenderType TERRA_PLATE = litOverlay("terrestrial_agglomeration_plate_rune", litOverlayPipeline("terrestrial_agglomeration_plate_rune", true), TextureAtlas.LOCATION_BLOCKS, 128, false);
	private static final RenderType ENCHANTER = litOverlay("mana_enchanter_rune", litOverlayPipeline("mana_enchanter_rune", true), TextureAtlas.LOCATION_BLOCKS, 128, false);

	private static final RenderPipeline HALO_PIPELINE = texturedPipeline("halo", false, true);
	private static final RenderPipeline ATLAS_HALO_PIPELINE = texturedPipeline("atlas_halo", true, true);
	private static final RenderPipeline TEXTURED_NO_CULL = texturedPipeline("textured_no_cull", false, false);
	private static final RenderPipeline TEXTURED_CULL = texturedPipeline("textured_cull", true, false);
	private static final Identifier BABYLON_TEXTURE = Identifier.parse(ResourcesLib.MISC_BABYLON);
	private static final RenderType LIGHT_RELAY = texturedLayer("luminizer", ATLAS_HALO_PIPELINE, TextureAtlas.LOCATION_BLOCKS, OutputTarget.ITEM_ENTITY_TARGET, true, false);
	private static final RenderType LIGHT_RELAY_PLAIN = texturedLayer("luminizer_plain", TEXTURED_CULL, TextureAtlas.LOCATION_BLOCKS, OutputTarget.ITEM_ENTITY_TARGET, true, false);
	private static final RenderType BABYLON_ICON = texturedLayer("babylon", HALO_PIPELINE, BABYLON_TEXTURE, OutputTarget.ITEM_ENTITY_TARGET, true, false);
	private static final RenderType BABYLON_ICON_PLAIN = texturedLayer("babylon_plain", TEXTURED_NO_CULL, BABYLON_TEXTURE, OutputTarget.ITEM_ENTITY_TARGET, true, false);
	private static final RenderType HALO = texturedLayer("halo", HALO_PIPELINE, FlugelTiaraItem.textureHalo, OutputTarget.MAIN_TARGET, true, false);
	private static final RenderType HALO_PLAIN = texturedLayer("halo_plain", TEXTURED_NO_CULL, FlugelTiaraItem.textureHalo, OutputTarget.MAIN_TARGET, true, false);
	private static final Function<Identifier, RenderType> ASSEMBLY_HALO = Util.memoize(texture ->
			texturedLayer("assembly_halo", TEXTURED_NO_CULL, texture, OutputTarget.MAIN_TARGET, false, true));

	private static final RenderPipeline PYLON_PIPELINE = geometryPipeline("pylon", "core/entity", DefaultVertexFormat.ENTITY, VertexFormat.Mode.QUADS)
			.withFragmentShader(botaniaRL("core/pylon"))
			.withUniform("Fog", UniformType.UNIFORM_BUFFER)
			.withUniform("Globals", UniformType.UNIFORM_BUFFER)
			.withUniform("Lighting", UniformType.UNIFORM_BUFFER)
			.withSampler("Sampler0")
			.withSampler("Sampler1")
			.withSampler("Sampler2")
			.withCull(false)
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.withDepthStencilState(DepthStencilState.DEFAULT)
			.build();
	private static final Function<Identifier, RenderType> PYLON_GLOW = Util.memoize(texture ->
			pylonGlow(texture, PYLON_PIPELINE, OutputTarget.ITEM_ENTITY_TARGET));
	private static final Function<Identifier, RenderType> PYLON_GLOW_DIRECT = Util.memoize(texture ->
			pylonGlow(texture, PYLON_PIPELINE, OutputTarget.MAIN_TARGET));
	private static final Function<Identifier, RenderType> PYLON_GLOW_PLAIN = Util.memoize(texture ->
			pylonGlow(texture, RenderPipelines.ENTITY_TRANSLUCENT, OutputTarget.ITEM_ENTITY_TARGET));
	private static final Function<Identifier, RenderType> PYLON_GLOW_DIRECT_PLAIN = Util.memoize(texture ->
			pylonGlow(texture, RenderPipelines.ENTITY_TRANSLUCENT, OutputTarget.MAIN_TARGET));

	public static final RenderType TRANSLUCENT = RenderTypes.entityTranslucentCullItemTarget(TextureAtlas.LOCATION_BLOCKS);

	private static final int ITEM_AND_PADDING_WIDTH = 20;

	private static final double INITIAL_OFFSET = 0.005;
	private static final double OFFSET_INCREMENT = 0.001;
	// Global y offset so that overlapping gaia traps or radius descriptors do not Z-fight
	private static double offY = INITIAL_OFFSET;

	public static double getOffY() {
		return offY;
	}

	public static void incrementOffY() {
		offY += OFFSET_INCREMENT;
	}

	public static void onWorldRenderLast() {
		offY = INITIAL_OFFSET;
	}

	private static RenderPipeline.Builder geometryPipeline(String name, String shader, VertexFormat format, VertexFormat.Mode mode) {
		return RenderPipeline.builder()
				.withLocation(botaniaRL("pipeline/" + name))
				.withVertexShader(shader)
				.withFragmentShader(shader)
				.withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
				.withUniform("Projection", UniformType.UNIFORM_BUFFER)
				.withVertexFormat(format, mode);
	}

	private static RenderType colorLayer(String name, VertexFormat.Mode mode, BlendFunction blend, DepthStencilState depth,
			boolean cull, OutputTarget target, boolean sort) {
		RenderPipeline pipeline = geometryPipeline(name, "core/position_color", DefaultVertexFormat.POSITION_COLOR, mode)
				.withCull(cull)
				.withColorTargetState(new ColorTargetState(blend))
				.withDepthStencilState(depth)
				.build();
		var setup = RenderSetup.builder(pipeline).bufferSize(256).setOutputTarget(target);
		if (sort) {
			setup.sortOnUpload();
		}
		return RenderTypeAccessor.botania_create(ResourcesLib.PREFIX_MOD + name, setup.createRenderSetup());
	}

	private static RenderPipeline linePipeline(String name, DepthStencilState depth) {
		return geometryPipeline(name, "core/rendertype_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH, VertexFormat.Mode.LINES)
				.withUniform("Fog", UniformType.UNIFORM_BUFFER)
				.withUniform("Globals", UniformType.UNIFORM_BUFFER)
				.withCull(false)
				.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
				.withDepthStencilState(depth)
				.build();
	}

	private static RenderType lineLayer(String name, RenderPipeline pipeline, OutputTarget target, int bufferSize) {
		return RenderTypeAccessor.botania_create(ResourcesLib.PREFIX_MOD + name, RenderSetup.builder(pipeline)
				.bufferSize(bufferSize)
				.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
				.setOutputTarget(target)
				.createRenderSetup());
	}

	private RenderHelper() {}

	private static RenderPipeline litOverlayPipeline(String name, boolean animated) {
		var pipeline = geometryPipeline(name, "core/particle", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS)
				.withUniform("Fog", UniformType.UNIFORM_BUFFER)
				.withSampler("Sampler0")
				.withSampler("Sampler2")
				.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
				.withDepthStencilState(DepthStencilState.DEFAULT);
		if (animated) {
			pipeline.withFragmentShader(botaniaRL("core/" + name))
					.withUniform("Globals", UniformType.UNIFORM_BUFFER);
		}
		return pipeline.build();
	}

	private static RenderType litOverlay(String name, RenderPipeline pipeline, Identifier atlas, int bufferSize, boolean outline) {
		var setup = RenderSetup.builder(pipeline)
				.bufferSize(bufferSize)
				.withTexture("Sampler0", atlas)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.useLightmap();
		if (outline) {
			setup.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE);
		}
		return RenderTypeAccessor.botania_create(ResourcesLib.PREFIX_MOD + name, setup.createRenderSetup());
	}

	private static RenderPipeline texturedPipeline(String name, boolean cull, boolean animated) {
		var pipeline = geometryPipeline(name, "core/position_tex_color", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
				.withSampler("Sampler0")
				.withCull(cull)
				.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
				.withDepthStencilState(DepthStencilState.DEFAULT);
		if (animated) {
			pipeline.withFragmentShader(botaniaRL("core/halo"))
					.withUniform("Globals", UniformType.UNIFORM_BUFFER);
		}
		return pipeline.build();
	}

	private static RenderType texturedLayer(String name, RenderPipeline pipeline, Identifier texture, OutputTarget target,
			boolean outline, boolean linear) {
		var setup = RenderSetup.builder(pipeline).bufferSize(64).setOutputTarget(target);
		if (linear) {
			setup.withTexture("Sampler0", texture, () -> RenderSystem.getSamplerCache().getRepeat(FilterMode.LINEAR)).sortOnUpload();
		} else {
			setup.withTexture("Sampler0", texture);
		}
		if (outline) {
			setup.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE);
		}
		return RenderTypeAccessor.botania_create(ResourcesLib.PREFIX_MOD + name, setup.createRenderSetup());
	}

	public static RenderType skyLayer(Identifier texture, boolean additive) {
		String name = additive ? "sky_additive" : "sky_textured";
		var pipeline = geometryPipeline(name, "core/position_tex_color", DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
				.withSampler("Sampler0")
				.withCull(false)
				.withDepthStencilState(NO_DEPTH)
				.withColorTargetState(new ColorTargetState(additive ? BlendFunction.ADDITIVE : BlendFunction.TRANSLUCENT))
				.build();
		return texturedLayer(name, pipeline, texture, OutputTarget.MAIN_TARGET, false, true);
	}

	private static RenderType pylonGlow(Identifier texture, RenderPipeline pipeline, OutputTarget target) {
		return RenderTypeAccessor.botania_create("botania:pylon_glow", RenderSetup.builder(pipeline)
				.bufferSize(128)
				.withTexture("Sampler0", texture)
				.setOutputTarget(target)
				.useLightmap()
				.useOverlay()
				.createRenderSetup());
	}

	public static RenderType getPylonGlow(Identifier texture, boolean direct) {
		if (BotaniaConfig.client().useShaders()) {
			return (direct ? PYLON_GLOW_DIRECT : PYLON_GLOW).apply(texture);
		}
		return (direct ? PYLON_GLOW_DIRECT_PLAIN : PYLON_GLOW_PLAIN).apply(texture);
	}

	public static RenderType getManaPoolWaterLayer() {
		return BotaniaConfig.client().useShaders() ? MANA_POOL_WATER : PLAIN_OVERLAY;
	}

	public static RenderType getTerraPlateLayer() {
		return BotaniaConfig.client().useShaders() ? TERRA_PLATE : PLAIN_OVERLAY;
	}

	public static RenderType getEnchanterLayer() {
		return BotaniaConfig.client().useShaders() ? ENCHANTER : PLAIN_OVERLAY;
	}

	public static RenderType getLightRelayLayer() {
		return BotaniaConfig.client().useShaders() ? LIGHT_RELAY : LIGHT_RELAY_PLAIN;
	}

	public static RenderType getBabylonIconLayer() {
		return BotaniaConfig.client().useShaders() ? BABYLON_ICON : BABYLON_ICON_PLAIN;
	}

	public static RenderType getTiaraHaloLayer() {
		return BotaniaConfig.client().useShaders() ? HALO : HALO_PLAIN;
	}

	public static RenderType getHaloLayer(Identifier texture) {
		return ASSEMBLY_HALO.apply(texture);
	}

	private static final Function<Identifier, RenderType> GAIA_NOISE_DYNAMIC_LAYER = gaiaNoiseLayer("gaia_noise_dynamic", true);
	private static final Function<Identifier, RenderType> GAIA_NOISE_CONSTANT_LAYER = gaiaNoiseLayer("gaia_noise_constant", false);

	private static Function<Identifier, RenderType> gaiaNoiseLayer(String name, boolean dynamic) {
		var builder = geometryPipeline(name, "core/entity", DefaultVertexFormat.ENTITY, VertexFormat.Mode.QUADS)
				.withVertexShader(botaniaRL("core/gaia_noise"))
				.withFragmentShader(botaniaRL("core/gaia_noise"))
				.withUniform("Fog", UniformType.UNIFORM_BUFFER)
				.withUniform("Globals", UniformType.UNIFORM_BUFFER)
				.withUniform("Lighting", UniformType.UNIFORM_BUFFER)
				.withSampler("Sampler0")
				.withSampler("Sampler1")
				.withSampler("Sampler2")
				.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
				.withDepthStencilState(DepthStencilState.DEFAULT)
				.withCull(false);
		if (dynamic) {
			builder.withShaderDefine("DYNAMIC_NOISE");
		}
		RenderPipeline pipeline = builder.build();
		return Util.memoize(texture -> RenderTypeAccessor.botania_create(ResourcesLib.PREFIX_MOD + name,
				RenderSetup.builder(pipeline)
						.withTexture("Sampler0", texture)
						.useLightmap()
						.useOverlay()
						.bufferSize(256)
						.affectsCrumbling()
						.sortOnUpload()
						.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
						.createRenderSetup()));
	}

	public static RenderType getGaiaNoiseDynamicLayer(Identifier texture) {
		return BotaniaConfig.client().useShaders() ? GAIA_NOISE_DYNAMIC_LAYER.apply(texture) : RenderTypes.entityTranslucent(texture);
	}

	public static RenderType getGaiaNoiseConstantLayer(Identifier texture) {
		return BotaniaConfig.client().useShaders() ? GAIA_NOISE_CONSTANT_LAYER.apply(texture) : RenderTypes.entityTranslucent(texture);
	}

	public static void drawAlfheimPortal(GuiGraphicsExtractor graphics, int x, int y) {
		var sprite = Minecraft.getInstance().getAtlasManager()
				.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, botaniaRL("block/alfheim_portal_swirl")));
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, 48, 48);
	}

	public static void drawTexturedModalRect(GuiGraphicsExtractor gui, Identifier textureId, int x, int y, int u, int v, int width, int height) {
		gui.blit(RenderPipelines.GUI_TEXTURED, textureId, x, y, u, v, width, height, 256, 256);
	}

	public static void drawTexturedModalRect(GuiGraphicsExtractor gui, Identifier textureId, int x, int y, int u, int v, int width, int height, int color) {
		gui.blit(RenderPipelines.GUI_TEXTURED, textureId, x, y, u, v, width, height, 256, 256, color);
	}

	public static void renderStar(PoseStack ms, MultiBufferSource buffers, int color, float xScale, float yScale, float zScale, long seed, float partialTicks) {
		float ticks = ClientTickHandler.getEntityTicksInGame() + partialTicks;
		renderStar(ms, buffers.getBuffer(STAR), color, xScale, yScale, zScale, seed, ticks);
	}

	public static void submitStar(PoseStack pose, SubmitNodeCollector collector, int color, float scale, long seed, float ticks) {
		collector.submitCustomGeometry(pose, STAR, (transform, buffer) -> {
			PoseStack geometry = new PoseStack();
			geometry.mulPose(transform.pose());
			renderStar(geometry, buffer, color, scale, scale, scale, seed, ticks);
		});
	}

	private static void renderStar(PoseStack ms, VertexConsumer buffer, int color, float xScale, float yScale, float zScale, long seed, float ticks) {
		float semiPeriodTicks = 200;
		float f1 = Mth.abs(Mth.sin((float) Math.PI / semiPeriodTicks * ticks)) * 0.9F + 0.1F; // shift to [0.1, 1.0]

		float f2 = f1 > 0.F ? (f1 - 0.7F) / 0.2F : 0;
		Random random = new Random(seed);

		ms.pushPose();
		ms.scale(xScale, yScale, zScale);

		for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
			ms.mulPose(VecHelper.rotateX(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateY(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateZ(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateX(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateY(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateZ(random.nextFloat() * 360F + f1 * 90F));
			float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
			float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
			int alphaColor = ARGB.color(ARGB.as8BitChannel(f1), color);
			Matrix4f mat = ms.last().pose();
			Runnable center = () -> buffer.addVertex(mat, 0, 0, 0).setColor(alphaColor);
			Runnable[] vertices = {
					() -> buffer.addVertex(mat, -0.866F * f4, f3, -0.5F * f4).setColor(0, 0, 0, 0),
					() -> buffer.addVertex(mat, 0.866F * f4, f3, -0.5F * f4).setColor(0, 0, 0, 0),
					() -> buffer.addVertex(mat, 0, f3, 1F * f4).setColor(0, 0, 0, 0),
					() -> buffer.addVertex(mat, -0.866F * f4, f3, -0.5F * f4).setColor(0, 0, 0, 0)
			};
			triangleFan(center, vertices);
		}

		ms.popPose();
	}

	public static void triangleFan(Runnable center, Runnable... vertices) {
		triangleFan(center, Arrays.asList(vertices));
	}

	/**
	 * With a buffer in GL_TRIANGLES mode, emulates GL_TRIANGLE_FAN on the CPU.
	 * This is because batching of GL_TRIANGLE_FAN makes no sense (the vertices would bleed into one massive fan)
	 */
	public static void triangleFan(Runnable center, List<Runnable> vertices) {
		for (int i = 0; i < vertices.size() - 1; i++) {
			center.run();
			vertices.get(i).run();
			vertices.get(i + 1).run();
		}
	}

	public static void flatRectangle(VertexConsumer buffer, Matrix4f mat,
			float xMin, float xMax, float y, float zMin, float zMax,
			int r, int g, int b, int a) {
		buffer.addVertex(mat, xMax, y, zMin).setColor(r, g, b, a);
		buffer.addVertex(mat, xMin, y, zMin).setColor(r, g, b, a);
		buffer.addVertex(mat, xMin, y, zMax).setColor(r, g, b, a);
		buffer.addVertex(mat, xMax, y, zMax).setColor(r, g, b, a);
	}

	private static final RenderPipeline GUI_PROGRESS_PIE = geometryPipeline("gui_progress_pie", "core/position_tex_color",
			DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
			.withFragmentShader(botaniaRL("core/gui_progress_pie"))
			.withSampler("Sampler0")
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.build();
	private static final Identifier PROGRESS_PIE_TEXTURE = Identifier.parse(ResourcesLib.GUI_MANA_HUD);

	public static void renderProgressPie(GuiGraphicsExtractor gui, float partialTick, int x, int y, float progress, ItemStack stack) {
		int degrees = (int) (360 * Mth.clamp(progress, 0F, 1F));
		if (degrees > 0) {
			float pulse = 0.5F + 0.2F * (Mth.cos(ClientTickHandler.getUiAnimationTicks() / 10) * 0.5F + 0.5F);
			int parameters = ARGB.color(ARGB.as8BitChannel(pulse), degrees >> 8, degrees & 0xFF, 0);
			// The blit provides normalized UVs and GUI clipping. Its color carries the wedge size and opacity.
			gui.blit(GUI_PROGRESS_PIE, PROGRESS_PIE_TEXTURE, x - 2, y - 2, 0, 0, 20, 20, 1, 1, 1, 1, parameters);
		}
		gui.item(stack, x, y);
	}

	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 *
	 * @param startX   Start x position in blocks
	 * @param startY   Start position in blocks
	 * @param endX     End x position in blocks
	 * @param endY     End y position in blocks
	 *
	 * @param uvStartX UV start x position in "pixels" (1/16th sprite size)
	 * @param uvStartY UV start position in "pixels" (1/16th sprite size)
	 * @param uvEndX   UV end x position in "pixels" (1/16th sprite size)
	 * @param uvEndY   UV end y position in "pixels" (1/16th sprite size)
	 */
	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			float startX, float startY, float endX, float endY,
			float uvStartX, float uvStartY, float uvEndX, float uvEndY,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		renderIcon(ms.last(), buffer, startX, startY, endX, endY, uvStartX, uvStartY, uvEndX, uvEndY, icon, color, alpha, light);
	}

	public static void renderIconFullBright(PoseStack.Pose pose, VertexConsumer buffer, TextureAtlasSprite icon, float alpha) {
		renderIcon(pose, buffer, 0, 0, 1, 1, 0, 0, 16, 16, icon, 0xFFFFFF, alpha, 0xF000F0);
	}

	private static void renderIcon(
			PoseStack.Pose pose, VertexConsumer buffer,
			float startX, float startY, float endX, float endY,
			float uvStartX, float uvStartY, float uvEndX, float uvEndY,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		Matrix4f mat = pose.pose();
		float red = ((color >> 16) & 0xFF) / 255F;
		float green = ((color >> 8) & 0xFF) / 255F;
		float blue = (color & 0xFF) / 255F;

		buffer.addVertex(mat, startX, endY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvStartX / 16F), icon.getV(uvEndY / 16F)).setLight(light);
		buffer.addVertex(mat, endX, endY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvEndX / 16F), icon.getV(uvEndY / 16F)).setLight(light);
		buffer.addVertex(mat, endX, startY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvEndX / 16F), icon.getV(uvStartY / 16F)).setLight(light);
		buffer.addVertex(mat, startX, startY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvStartX / 16F), icon.getV(uvStartY / 16F)).setLight(light);
	}

	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 *
	 * @param uvStartX UV start x position in "pixels" (1/16th sprite size)
	 * @param uvStartY UV start position in "pixels" (1/16th sprite size)
	 * @param uvEndX   UV end x position in "pixels" (1/16th sprite size)
	 * @param uvEndY   UV end y position in "pixels" (1/16th sprite size)
	 */
	public static void renderIconCropped(
			PoseStack ms, VertexConsumer buffer,
			int uvStartX, int uvStartY, int uvEndX, int uvEndY,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		renderIconCropped(ms.last(), buffer, uvStartX, uvStartY, uvEndX, uvEndY, icon, color, alpha, light);
	}

	public static void renderIconCropped(
			PoseStack.Pose pose, VertexConsumer buffer,
			int uvStartX, int uvStartY, int uvEndX, int uvEndY,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		renderIcon(
				pose, buffer,
				uvStartX / 16F, uvStartY / 16F, uvEndX / 16F, uvEndY / 16F,
				uvStartX, uvStartY, uvEndX, uvEndY,
				icon, color, alpha, light
		);
	}

	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 * Renders the icon at a 1 block size with a full 16x16 UV
	 */
	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		renderIconCropped(
				ms, buffer,
				0, 0, 16, 16,
				icon, color, alpha, light
		);
	}

	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 * Renders the icon in fullbright, at a 1 block size with a full 16x16 UV
	 */
	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			TextureAtlasSprite icon, int color, float alpha) {
		int fullbright = 0xF000F0;
		renderIconFullBright(ms, buffer, icon, color, alpha, fullbright);
	}

	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 * Renders the icon in fullbright, with no color modification, at a 1 block size with a full 16x16 UV
	 */
	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			TextureAtlasSprite icon, float alpha) {
		renderIconFullBright(ms, buffer, icon, 0xFFFFFF, alpha);
	}

	/*
	* Renders a transparent black box with a soft border. The parameters describe the inner box, there will also be drawn
	* another box that is 2px bigger in each direction
	*/
	public static void renderHUDBox(GuiGraphicsExtractor gui, int startX, int startY, int endX, int endY) {
		gui.fill(startX, startY, endX, endY, 0x40000000);
		gui.fill(startX - 2, startY - 2, endX + 2, endY + 2, 0x40000000);
	}

	/*
	* Renders an item and its name, vertically centered next to it. Renders nothing if the stack is empty
	* Note: The item renderer does not respect the PoseStack
	*/
	public static void renderItemWithName(GuiGraphicsExtractor gui, Font font, ItemStack itemStack, int startX, int startY, int color) {
		if (!itemStack.isEmpty()) {
			gui.text(font, itemStack.getHoverName(), startX + ITEM_AND_PADDING_WIDTH, startY + 4, color);
			gui.item(itemStack, startX, startY);
		}
	}

	public static void renderItemWithNameCentered(GuiGraphicsExtractor gui, Window window, Font font, ItemStack itemStack, int startY, int color) {
		int centerX = window.getGuiScaledWidth() / 2;
		int startX = centerX - itemWithNameWidth(itemStack, font) / 2;
		renderItemWithName(gui, font, itemStack, startX, startY, color);
	}

	/*
	* Returns the width of an item and its text, as rendered by renderItemWithName
	*/
	public static int itemWithNameWidth(ItemStack itemStack, Font font) {
		return ITEM_AND_PADDING_WIDTH + font.width(itemStack.getHoverName());
	}

}
