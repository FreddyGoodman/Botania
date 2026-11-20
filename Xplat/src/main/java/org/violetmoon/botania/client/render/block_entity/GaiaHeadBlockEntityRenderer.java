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
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.blockentity.state.SkullBlockRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.render.entity.GaiaGuardianRenderer;
import org.violetmoon.botania.common.block.block_entity.GaiaHeadBlockEntity;
import org.violetmoon.botania.mixin.client.SkullBlockRendererAccessor;

import java.util.function.Function;

public class GaiaHeadBlockEntityRenderer implements BlockEntityRenderer<GaiaHeadBlockEntity, SkullBlockRenderState> {
	private final Function<SkullBlock.Type, SkullModelBase> models;

	public GaiaHeadBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		models = models(ctx.entityModelSet());
	}

	public static Function<SkullBlock.Type, SkullModelBase> models(EntityModelSet models) {
		return Util.memoize(type -> SkullBlockRenderer.createModel(models, type));
	}

	public static SkullBlock.Type getViewType() {
		Entity view = Minecraft.getInstance().getCameraEntity();

		if (view instanceof WitherSkeleton || view instanceof WitherBoss) {
			return SkullBlock.Types.WITHER_SKELETON;
		}
		if (view instanceof AbstractSkeleton) {
			return SkullBlock.Types.SKELETON;
		}
		if (view instanceof Zombie) {
			return SkullBlock.Types.ZOMBIE;
		}
		if (view instanceof Creeper) {
			return SkullBlock.Types.CREEPER;
		}
		if (view instanceof AbstractPiglin) {
			return SkullBlock.Types.PIGLIN;
		}
		if (view instanceof EnderDragon) {
			return SkullBlock.Types.DRAGON;
		}
		return SkullBlock.Types.PLAYER;
	}

	public static RenderType getRenderType(SkullBlock.Type type) {
		if (type == SkullBlock.Types.PLAYER) {
			var player = GaiaGuardianRenderer.getViewingPlayer();
			return RenderHelper.getGaiaNoiseConstantLayer(player == null
					? DefaultPlayerSkin.getDefaultTexture() : player.getSkin().body().texturePath());
		}
		return RenderHelper.getGaiaNoiseConstantLayer(SkullBlockRendererAccessor.botania_getSkinByType().get(type));
	}

	@Override
	public SkullBlockRenderState createRenderState() {
		return new SkullBlockRenderState();
	}

	@Override
	public void extractRenderState(GaiaHeadBlockEntity head, SkullBlockRenderState state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(head, state, partialTick, camera, crumblingOverlay);
		state.skullType = getViewType();
		state.renderType = getRenderType(state.skullType);
		state.animationProgress = head.getAnimation(partialTick);
		BlockState block = head.getBlockState();
		state.transformation = block.getBlock() instanceof WallSkullBlock
				? SkullBlockRenderer.TRANSFORMATIONS.wallTransformation(block.getValue(WallSkullBlock.FACING))
				: SkullBlockRenderer.TRANSFORMATIONS.freeTransformations(block.getValue(SkullBlock.ROTATION));
	}

	@Override
	public void submit(SkullBlockRenderState state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.mulPose(state.transformation);
		SkullBlockRenderer.submitSkull(state.animationProgress, pose, collector, state.lightCoords,
				models.apply(state.skullType), state.renderType, 0, state.breakProgress);
		pose.popPose();
	}
}
