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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.violetmoon.botania.api.block_entity.RadiusDescriptor;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.block.block_entity.LuminizerBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.common.item.equipment.bauble.ManaseerMonocleItem;

public class LuminizerBlockEntityRenderer implements BlockEntityRenderer<LuminizerBlockEntity, LuminizerBlockEntityRenderer.State> {

	public LuminizerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(LuminizerBlockEntity tile, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(tile, state, partialTick, camera, crumblingOverlay);
		Minecraft mc = Minecraft.getInstance();
		state.showRadius = mc.getCameraEntity() instanceof LivingEntity view && ManaseerMonocleItem.hasMonocle(view)
				&& SpecialFlowerBlockEntityRenderer.hasBindingAttempt(view, tile.getBlockPos());
		state.radiusColor = SpecialFlowerBlockEntityRenderer.radiusColor(partialTick);
		state.sprite = mc.getModelManager().getBlockStateModelSet().getParticleMaterial(tile.getBlockState()).sprite();
		state.rotation = ClientTickHandler.getEntityTicksInGame() + partialTick;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.showRadius) {
			SpecialFlowerBlockEntityRenderer.submitRadius(state.blockPos,
					new RadiusDescriptor.Circle(state.blockPos, LuminizerBlockEntity.MAX_DIST), state.radiusColor, pose, collector);
		}

		pose.pushPose();
		pose.translate(0.5, 0.3, 0.5);
		pose.scale(0.75F, 0.75F, 0.75F);
		pose.mulPose(camera.orientation);
		pose.translate(0F, 0.25F, 0F);
		pose.mulPose(VecHelper.rotateZ(state.rotation));
		pose.translate(0F, -0.25F, 0F);
		TextureAtlasSprite sprite = state.sprite;
		collector.submitCustomGeometry(pose, RenderHelper.getLightRelayLayer(), (transform, buffer) -> renderIcon(transform, buffer, sprite));
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private boolean showRadius;
		private int radiusColor;
		private TextureAtlasSprite sprite;
		private float rotation;
	}

	private static void renderIcon(PoseStack.Pose pose, VertexConsumer buffer, TextureAtlasSprite icon) {
		float size = icon.getU1() - icon.getU0();
		float pad = size / 8F;
		float f = icon.getU0() + pad;
		float f1 = icon.getU1() - pad;
		float f2 = icon.getV0() + pad;
		float f3 = icon.getV1() - pad;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		Matrix4f mat = pose.pose();
		buffer.addVertex(mat, 0.0F - f5, 0.0F - f6, 0.0F).setColor(1F, 1F, 1F, 1F).setUv(f, f3);
		buffer.addVertex(mat, f4 - f5, 0.0F - f6, 0.0F).setColor(1F, 1F, 1F, 1F).setUv(f1, f3);
		buffer.addVertex(mat, f4 - f5, f4 - f6, 0.0F).setColor(1F, 1F, 1F, 1F).setUv(f1, f2);
		buffer.addVertex(mat, 0.0F - f5, f4 - f6, 0.0F).setColor(1F, 1F, 1F, 1F).setUv(f, f2);

	}

}
