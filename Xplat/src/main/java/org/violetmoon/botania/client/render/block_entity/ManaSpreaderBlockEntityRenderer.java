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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.violetmoon.botania.api.state.BotaniaStateProperties;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import org.violetmoon.botania.common.helper.ColorHelper;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class ManaSpreaderBlockEntityRenderer implements BlockEntityRenderer<ManaSpreaderBlockEntity, ManaSpreaderBlockEntityRenderer.State> {
	public static final Map<DyeColor, Identifier> WOOL_PADDING_IDS = new EnumMap<>(
			ColorHelper.supportedColors().collect(Collectors.toMap(Function.identity(),
					color -> botaniaRL("block/" + color.getSerializedName() + "_spreader_padding"))));

	private final ItemModelResolver itemModels;

	public ManaSpreaderBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		itemModels = context.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(ManaSpreaderBlockEntity spreader, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(spreader, state, partialTick, camera, crumblingOverlay);
		var blockState = spreader.getBlockState();
		var block = spreader.getSpreaderBlock();
		long seed = blockState.getSeed(spreader.getBlockPos());
		state.time = ClientTickHandler.getEntityTicksInGame() + partialTick + new Random(seed).nextFloat() * 360;
		state.rotation.set(VecHelper.rotateY(180 - spreader.getYRot())).mul(VecHelper.rotateX(-spreader.getXRot()));
		state.tint[0] = block.isRainbowRendered()
				? ARGB.opaque(Mth.hsvToRgb((state.time % 180) / 180, 0.4F, 0.9F)) : -1;
		state.body.clear();
		Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(blockState).collectParts(RandomSource.create(seed), state.body);

		state.core = ClientXplatAbstractions.INSTANCE.getBlockModelPart(block.getCoreModelId());
		state.padding = block.isRainbowRendered() ? block.getOptionalColor().map(WOOL_PADDING_IDS::get)
				.map(ClientXplatAbstractions.INSTANCE::getBlockModelPart).orElse(null) : null;
		state.scaffolding = blockState.getValue(BotaniaStateProperties.HAS_SCAFFOLDING)
				? ClientXplatAbstractions.INSTANCE.getBlockModelPart(block.getScaffoldingModelId()) : null;
		itemModels.updateForTopItem(state.lens, spreader.getItemHandler().getItem(0), ItemDisplayContext.NONE, spreader.getLevel(), null, 0);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0.5, 0.5, 0.5);
		pose.mulPose(state.rotation);
		pose.translate(-0.5, -0.5, -0.5);
		submitParts(pose, collector, state.body, state.tint, state.lightCoords);

		pose.pushPose();
		pose.translate(0.5, 0.5, 0.5);
		pose.mulPose(VecHelper.rotateY(state.time % 360));
		pose.translate(-0.5, -0.5 + Mth.sin(state.time / 20F) * 0.05F, -0.5);
		submitParts(pose, collector, List.of(state.core), BlockModelRenderState.EMPTY_TINTS, state.lightCoords);
		pose.popPose();

		if (!state.lens.isEmpty()) {
			pose.pushPose();
			pose.translate(0.5F, 0.5F, 0.094F);
			pose.mulPose(VecHelper.rotateZ(180));
			pose.mulPose(VecHelper.rotateX(180));
			// Keep the lens clear of the front face.
			pose.scale(0.997F, 0.997F, 1F);
			state.lens.submit(pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			pose.popPose();
		}

		if (state.padding != null) {
			pose.pushPose();
			pose.translate(0.5, 0.5, 0.5);
			pose.mulPose(VecHelper.rotateX(-90));
			pose.mulPose(VecHelper.rotateY(180));
			pose.translate(-0.5, -0.5, -0.5);
			submitParts(pose, collector, List.of(state.padding), state.tint, state.lightCoords);
			pose.popPose();
		}
		pose.popPose();

		if (state.scaffolding != null) {
			submitParts(pose, collector, List.of(state.scaffolding), state.tint, state.lightCoords);
		}
	}

	private static void submitParts(PoseStack pose, SubmitNodeCollector collector, List<BlockStateModelPart> parts, int[] tints, int light) {
		collector.submitBlockModel(pose, RenderTypes.cutoutMovingBlock(), parts, tints, light, OverlayTexture.NO_OVERLAY, 0);
	}

	public static class State extends BlockEntityRenderState {
		private final List<BlockStateModelPart> body = new ArrayList<>();
		private final ItemStackRenderState lens = new ItemStackRenderState();
		private final Quaternionf rotation = new Quaternionf();
		private final int[] tint = new int[1];
		private BlockStateModelPart core;
		private @Nullable BlockStateModelPart padding;
		private @Nullable BlockStateModelPart scaffolding;
		private float time;
	}
}
