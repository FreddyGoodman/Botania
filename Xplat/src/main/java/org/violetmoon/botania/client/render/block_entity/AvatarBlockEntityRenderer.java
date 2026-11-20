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

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.item.AvatarWieldable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.model.AvatarModel;
import org.violetmoon.botania.client.model.BotaniaModelLayers;
import org.violetmoon.botania.common.block.block_entity.AvatarBlockEntity;
import org.violetmoon.botania.common.helper.VecHelper;

public class AvatarBlockEntityRenderer implements BlockEntityRenderer<AvatarBlockEntity, AvatarBlockEntityRenderer.State>, BlockEntityItemRenderer.Display {

	private static final float[] ROTATIONS = new float[] {
			180F, 0F, 90F, 270F
	};

	private static final Identifier texture = Identifier.parse(ResourcesLib.MODEL_AVATAR);
	private final AvatarModel model;
	private final ItemModelResolver itemModels;

	public AvatarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		model = new AvatarModel(context.bakeLayer(BotaniaModelLayers.AVATAR));
		itemModels = context.itemModelResolver();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(AvatarBlockEntity avatar, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(avatar, state, partialTick, camera, crumblingOverlay);
		state.facing = avatar.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		ItemStack stack = avatar.getItemHandler().getItem(0);
		itemModels.updateForTopItem(state.item, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, avatar.getLevel(), null, 0);
		AvatarWieldable wieldable = stack.isEmpty() ? null : AvatarWieldable.LOOKUP.find(stack, avatar);
		state.overlayTexture = wieldable == null ? null : wieldable.getOverlayResource();
		state.alpha = (float) Math.sin((ClientTickHandler.getEntityTicksInGame() + partialTick) / 20D) / 2F + 0.5F;
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		submitAvatar(state, pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
	}

	@Override
	public void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		submitAvatar(new State(), pose, collector, light, overlay);
	}

	private void submitAvatar(State state, PoseStack pose, SubmitNodeCollector collector, int light, int overlay) {
		pose.pushPose();
		pose.translate(0.5F, 1.6F, 0.5F);
		pose.scale(1F, -1F, -1F);
		pose.mulPose(VecHelper.rotateY(ROTATIONS[state.facing.get3DDataValue() - 2]));
		collector.submitModel(model, null, pose, texture, light, overlay, 0, state.breakProgress);

		if (!state.item.isEmpty()) {
			pose.pushPose();
			pose.scale(0.6F, 0.6F, 0.6F);
			pose.translate(-0.5F, 2F, -0.25F);
			pose.mulPose(VecHelper.rotateX(-70));
			state.item.submit(pose, collector, light, overlay, 0);
			pose.popPose();
		}

		if (state.overlayTexture != null) {
			pose.scale(1.01F, 1.01F, 1.01F);
			pose.translate(0F, -0.01F, 0F);
			collector.submitModel(model, null, pose, RenderTypes.entityTranslucent(state.overlayTexture),
					LightCoordsUtil.FULL_BRIGHT, overlay, ARGB.colorFromFloat(state.alpha, 1, 1, 1), null, 0, null);
		}
		pose.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private final ItemStackRenderState item = new ItemStackRenderState();
		private Direction facing = Direction.SOUTH;
		private @Nullable Identifier overlayTexture;
		private float alpha;
	}
}
