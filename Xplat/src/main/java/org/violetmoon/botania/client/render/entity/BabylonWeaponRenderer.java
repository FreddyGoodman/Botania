/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.StandaloneBlockModels;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.entity.BabylonWeaponEntity;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.List;
import java.util.Random;

public class BabylonWeaponRenderer extends EntityRenderer<BabylonWeaponEntity, BabylonWeaponRenderer.State> {
	public BabylonWeaponRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(BabylonWeaponEntity weapon, State state, float partialTick) {
		super.extractRenderState(weapon, state, partialTick);
		state.rotation = weapon.getRotation();
		int live = weapon.getLiveTicks();
		int delay = weapon.getDelay();
		float charge = Math.min(10F, Math.max(live, weapon.getChargeTicks()) + partialTick);
		state.circleAlpha = charge / 10F;
		float shrink = live > delay ? Math.min(1F, (live - delay + partialTick) * 0.2F) : 0;
		state.circleScale = 2F * (state.circleAlpha - shrink);

		Random random = new Random(weapon.getUUID().getMostSignificantBits());
		state.circleOffset = -0.3F + random.nextFloat() * 0.1F;
		state.circleRotation = charge * 9F + state.ageInTicks * 0.5F + random.nextFloat() * 360F;
		int variety = Math.floorMod(weapon.getVariety(), StandaloneBlockModels.TREASURY_WEAPONS.size());
		state.weapon = ClientXplatAbstractions.INSTANCE.findBlockModelPart(StandaloneBlockModels.TREASURY_WEAPONS.get(variety));
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		super.submit(state, pose, collector, camera);
		pose.pushPose();
		pose.mulPose(VecHelper.rotateY(state.rotation));

		if (state.weapon != null) {
			pose.pushPose();
			pose.translate(-0.75, 0, 1);
			pose.scale(1.5F, 1.5F, 1.5F);
			pose.mulPose(VecHelper.rotateY(90F));
			pose.mulPose(VecHelper.rotateZ(-45F));
			collector.submitBlockModel(pose, Sheets.translucentItemSheet(), List.of(state.weapon),
					BlockModelRenderState.EMPTY_TINTS, 0xF000F0, OverlayTexture.NO_OVERLAY, 0);
			pose.popPose();
		}

		pose.mulPose(VecHelper.rotateX(-90F));
		pose.translate(0, state.circleOffset, 0);
		float scale = state.circleScale;
		pose.scale(scale, scale, scale);
		pose.mulPose(VecHelper.rotateY(state.circleRotation));

		float alpha = state.circleAlpha;
		collector.submitCustomGeometry(pose, RenderHelper.getBabylonIconLayer(), (transform, buffer) -> {
			var matrix = transform.pose();
			buffer.addVertex(matrix, -1, 0, -1).setColor(1, 1, 1, alpha).setUv(0, 0);
			buffer.addVertex(matrix, -1, 0, 1).setColor(1, 1, 1, alpha).setUv(0, 1);
			buffer.addVertex(matrix, 1, 0, 1).setColor(1, 1, 1, alpha).setUv(1, 1);
			buffer.addVertex(matrix, 1, 0, -1).setColor(1, 1, 1, alpha).setUv(1, 0);
		});
		pose.popPose();
	}

	public static class State extends EntityRenderState {
		private float rotation;
		private float circleAlpha;
		private float circleScale;
		private float circleOffset;
		private float circleRotation;
		private @Nullable BlockStateModelPart weapon;
	}
}
