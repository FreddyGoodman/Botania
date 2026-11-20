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

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.state.BotaniaStateProperties;
import org.violetmoon.botania.api.state.enums.AlfheimPortalState;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class AlfheimPortalBlockEntityRenderer implements BlockEntityRenderer<AlfheimPortalBlockEntity, AlfheimPortalBlockEntityRenderer.State> {
	private final TextureAtlasSprite portalSprite;
	private final RenderType portalLayer;

	public AlfheimPortalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.portalSprite = ctx.sprites().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, botaniaRL("block/alfheim_portal_swirl")));
		this.portalLayer = RenderTypes.entityTranslucentCullItemTarget(portalSprite.atlasLocation());
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(AlfheimPortalBlockEntity portal, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(portal, state, partialTick, camera, crumblingOverlay);
		state.portalState = portal.getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE);
		state.alpha = Math.min(1, ((float) Math.sin((ClientTickHandler.getEntityTicksInGame() + partialTick) / 8) + 1) / 7 + 0.6f)
				* ((float) Math.min(AlfheimPortalBlockEntity.TICKS_UNTIL_FULLY_OPENED, portal.ticksOpen)
						/ AlfheimPortalBlockEntity.TICKS_UNTIL_FULLY_OPENED)
				* 0.5F;
	}

	@Override
	public void submit(State state, PoseStack ms, SubmitNodeCollector collector, CameraRenderState camera) {
		if (state.portalState == AlfheimPortalState.OFF) {
			return;
		}
		float alpha = state.alpha;

		ms.pushPose();
		if (state.portalState == AlfheimPortalState.ON_X) {
			ms.translate(0.3125, 1, 2);
			ms.mulPose(VecHelper.rotateY(90));
		} else {
			ms.translate(-1, 1, 0.3125);
		}
		collector.submitCustomGeometry(ms, portalLayer, (pose, buffer) -> renderIcon(pose, buffer, portalSprite, alpha));
		ms.popPose();

		ms.pushPose();
		if (state.portalState == AlfheimPortalState.ON_X) {
			ms.translate(0.6875, 1, -1);
			ms.mulPose(VecHelper.rotateY(90));
		} else {
			ms.translate(2, 1, 0.6875);
		}
		ms.mulPose(VecHelper.rotateY(180));
		collector.submitCustomGeometry(ms, portalLayer, (pose, buffer) -> renderIcon(pose, buffer, portalSprite, alpha));
		ms.popPose();
	}

	private static void renderIcon(PoseStack.Pose pose, VertexConsumer buffer, TextureAtlasSprite icon, float alpha) {
		int packedColor = ARGB.color(ARGB.as8BitChannel(alpha), 0xFFFFFF);
		buffer.addVertex(pose, 0, 3, 0).setColor(packedColor).setUv(icon.getU0(), icon.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 1, 0, 0);
		buffer.addVertex(pose, 3, 3, 0).setColor(packedColor).setUv(icon.getU1(), icon.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 1, 0, 0);
		buffer.addVertex(pose, 3, 0, 0).setColor(packedColor).setUv(icon.getU1(), icon.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 1, 0, 0);
		buffer.addVertex(pose, 0, 0, 0).setColor(packedColor).setUv(icon.getU0(), icon.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(0xF000F0).setNormal(pose, 1, 0, 0);
	}

	public static class State extends BlockEntityRenderState {
		private AlfheimPortalState portalState = AlfheimPortalState.OFF;
		private float alpha;
	}
}
