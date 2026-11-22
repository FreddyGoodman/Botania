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

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerModelType;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.helper.ShaderCompatibility;
import org.violetmoon.botania.client.model.GaiaGuardianModel;
import org.violetmoon.botania.client.model.armor.ArmorModels;
import org.violetmoon.botania.common.entity.GaiaGuardianEntity;

public class GaiaGuardianRenderer extends MobRenderer<GaiaGuardianEntity, GaiaGuardianRenderer.State, GaiaGuardianModel> {
	private final GaiaGuardianModel normalModel;
	private final GaiaGuardianModel slimModel;
	private final GaiaGuardianTextures compatibilityTextures = new GaiaGuardianTextures();

	public GaiaGuardianRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new GaiaGuardianModel(ctx.bakeLayer(ModelLayers.PLAYER), false), 0F);
		normalModel = getModel();
		slimModel = new GaiaGuardianModel(ctx.bakeLayer(ModelLayers.PLAYER_SLIM), true);
		addLayer(new CustomHeadLayer<>(this, ctx.getModelSet(), ctx.getPlayerSkinRenderCache()));
		addLayer(new WingsLayer<>(this, ctx.getModelSet(), ctx.getEquipmentRenderer()));
		addLayer(new ItemInHandLayer<>(this));
		ArmorModels.init(ctx);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(GaiaGuardianEntity guardian, State state, float partialTick) {
		super.extractRenderState(guardian, state, partialTick);
		HumanoidMobRenderer.extractHumanoidRenderState(guardian, state, partialTick, itemModelResolver);
		state.leftArmPose = HumanoidModel.ArmPose.EMPTY;
		state.rightArmPose = HumanoidModel.ArmPose.EMPTY;

		AbstractClientPlayer player = getViewingPlayer();
		var skin = player == null ? DefaultPlayerSkin.get(guardian.getUUID()) : player.getSkin();
		state.texture = skin.body().texturePath();
		state.slim = skin.model() == PlayerModelType.SLIM;
		state.showHat = player != null && player.isModelPartShown(PlayerModelPart.HAT);
		state.showJacket = player != null && player.isModelPartShown(PlayerModelPart.JACKET);
		state.showLeftPants = player != null && player.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
		state.showRightPants = player != null && player.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
		state.showLeftSleeve = player != null && player.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
		state.showRightSleeve = player != null && player.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);

		int invulTime = guardian.getInvulTime();
		float grainIntensity;
		float disfiguration;
		if (invulTime > 0) {
			grainIntensity = Math.min(1F, invulTime * 0.05F);
			disfiguration = grainIntensity * 0.3F;
		} else {
			disfiguration = (0.025F + guardian.hurtTime * ((1F - 0.15F) / 20F)) / 2F;
			grainIntensity = 0.05F + guardian.hurtTime * ((1F - 0.15F) / 10F);
		}
		state.compatibility = ShaderCompatibility.useVanillaPipelines();
		state.noiseFrame = (int) state.ageInTicks / 2;
		state.compatibilityDistortion = state.compatibility ? disfiguration : 0F;
		if (state.compatibility) {
			state.texture = compatibilityTextures.get(state.texture, grainIntensity, state.noiseFrame);
		}

		// The noise pipeline decodes the model tint as grain and positional distortion.
		int grain = ARGB.as8BitChannel(Math.clamp(grainIntensity, 0F, 1F));
		int distortion = Math.round(Math.clamp(disfiguration, 0F, 1F) * 65535);
		state.shaderTint = 0xFF000000 | grain << 16 | distortion;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		model = state.slim ? slimModel : normalModel;
		super.submit(state, pose, collector, camera);
	}

	@Nullable
	public static AbstractClientPlayer getViewingPlayer() {
		Minecraft minecraft = Minecraft.getInstance();
		return minecraft.getCameraEntity() instanceof AbstractClientPlayer clientPlayer ? clientPlayer : minecraft.player;
	}

	@Override
	public Identifier getTextureLocation(State state) {
		return state.texture;
	}

	@Override
	protected boolean isBodyVisible(State state) {
		return true;
	}

	@Override
	protected int getModelTint(State state) {
		return state.compatibility ? 0xFFFFFFFF : state.shaderTint;
	}

	public static class State extends HumanoidRenderState {
		private Identifier texture = DefaultPlayerSkin.getDefaultTexture();
		private boolean slim;
		private boolean compatibility;
		private int shaderTint;
		public int noiseFrame;
		public float compatibilityDistortion;
		public boolean showHat;
		public boolean showJacket;
		public boolean showLeftPants;
		public boolean showRightPants;
		public boolean showLeftSleeve;
		public boolean showRightSleeve;
	}
}
