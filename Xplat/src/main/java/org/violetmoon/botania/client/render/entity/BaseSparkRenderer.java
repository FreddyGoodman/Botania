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
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.entity.SparkBaseEntity;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.Random;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public abstract class BaseSparkRenderer<T extends SparkBaseEntity> extends EntityRenderer<T, BaseSparkRenderer.State> {

	private final SpriteGetter sprites;
	private final TextureAtlasSprite starSprite;
	private final TextureAtlasSprite worldSprite;

	protected BaseSparkRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		sprites = ctx.getSprites();
		starSprite = getItemSprite(botaniaRL("item/spark_star"));
		worldSprite = getItemSprite(botaniaRL("item/mana_spark"));
	}

	protected TextureAtlasSprite getItemSprite(Identifier id) {
		return sprites.get(new SpriteId(TextureAtlas.LOCATION_ITEMS, id));
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(T entity, State state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.icon = getBaseIcon(entity);
		state.augment = getSpinningIcon(entity);
		state.time = (entity.level().getGameTime() % 24000) + partialTicks + new Random(entity.getId()).nextInt(200);
		state.alpha = entity.isInvisible() ? 0.1F : 0.9F;
		state.starColor = ARGB.color(ARGB.as8BitChannel(state.alpha), entity.getNetwork().getTextureDiffuseColor());
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		float time = state.time;
		float iconAlpha = (0.7F + 0.6F * (Mth.sin(time / 5) + 0.5F)) * state.alpha;
		int iconColor = ARGB.color(ARGB.as8BitChannel(iconAlpha), 0xFFFFFF);
		float scale = 0.75F + 0.1F * Mth.sin(time / 10);

		pose.pushPose();
		pose.scale(scale, scale, scale);
		pose.mulPose(camera.orientation);
		submitIcon(pose, collector, state.icon, iconColor);

		double x = Math.sin(time / 20) * 0.2;
		double y = Math.cos(time / 20) * 0.2;
		pose.pushPose();
		pose.translate(-0.02 + x, 0.24 + y, 0.005);
		pose.scale(0.2F, 0.2F, 0.2F);
		submitIcon(pose, collector, starSprite, state.starColor);
		pose.popPose();

		if (state.augment != null) {
			pose.translate(-0.02 - x, 0.24 - y, 0.005);
			pose.scale(0.2F, 0.2F, 0.2F);
			submitIcon(pose, collector, state.augment, iconColor);
		}
		pose.popPose();
	}

	protected TextureAtlasSprite getBaseIcon(T entity) {
		return worldSprite;
	}

	@Nullable
	protected TextureAtlasSprite getSpinningIcon(T entity) {
		return null;
	}

	private static void submitIcon(PoseStack pose, SubmitNodeCollector collector, TextureAtlasSprite icon, int color) {
		ClientXplatAbstractions.instance().markSpriteActive(icon);
		collector.submitCustomGeometry(pose, RenderHelper.SPARK, (transform, buffer) -> renderIcon(transform, buffer, icon, color));
	}

	private static void renderIcon(PoseStack.Pose pose, VertexConsumer buffer, TextureAtlasSprite icon, int color) {
		int fullbright = 0xF000F0;
		buffer.addVertex(pose, -0.5F, -0.25F, 0).setColor(color).setUv(icon.getU0(), icon.getV1()).setLight(fullbright);
		buffer.addVertex(pose, 0.5F, -0.25F, 0).setColor(color).setUv(icon.getU1(), icon.getV1()).setLight(fullbright);
		buffer.addVertex(pose, 0.5F, 0.75F, 0).setColor(color).setUv(icon.getU1(), icon.getV0()).setLight(fullbright);
		buffer.addVertex(pose, -0.5F, 0.75F, 0).setColor(color).setUv(icon.getU0(), icon.getV0()).setLight(fullbright);
	}

	public static class State extends EntityRenderState {
		private TextureAtlasSprite icon;
		@Nullable
		private TextureAtlasSprite augment;
		private float time;
		private float alpha;
		private int starColor;
	}
}
