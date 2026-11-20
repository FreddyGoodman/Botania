/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.item.AstrolabeItem;

import java.util.ArrayList;
import java.util.List;

public final class AstrolabePreviewHandler {
	public static void onWorldRenderLast(Camera camera, PoseStack ms, RenderBuffers buffers, Level level) {
		MultiBufferSource.BufferSource bufferSource = buffers.bufferSource();
		VertexConsumer buffer = bufferSource.getBuffer(RenderHelper.TRANSLUCENT);

		for (Player player : level.players()) {
			ItemStack currentStack = player.getMainHandItem();
			InteractionHand hand = InteractionHand.MAIN_HAND;
			if (currentStack.isEmpty() || !(currentStack.getItem() instanceof AstrolabeItem)) {
				currentStack = player.getOffhandItem();
				hand = InteractionHand.OFF_HAND;
			}

			if (!currentStack.isEmpty() && currentStack.getItem() instanceof AstrolabeItem) {
				Block block = AstrolabeItem.getBlock(currentStack, level.holderLookup(Registries.BLOCK));
				if (block != Blocks.AIR) {
					renderPlayerLook(ms, buffer, camera.position(), player, currentStack, hand);
				}
			}
		}

		bufferSource.endBatch(RenderHelper.TRANSLUCENT);
	}

	private static void renderPlayerLook(PoseStack ms, VertexConsumer buffer, Vec3 camera, Player player, ItemStack stack, InteractionHand hand) {
		Block blockToPlace = AstrolabeItem.getBlock(stack, player.level().holderLookup(Registries.BLOCK));
		int size = AstrolabeItem.getSize(stack);
		BlockPlaceContext ctx = AstrolabeItem.getBlockPlaceContext(player, hand, blockToPlace);
		List<BlockPos> placePositions = AstrolabeItem.getPlacePositions(ctx, size);
		if (ctx != null && AstrolabeItem.hasBlocks(stack, player, placePositions.size(), blockToPlace)) {
			for (BlockPos pos : placePositions) {
				BlockPlaceContext placeContext = getPlaceContext(player, ctx, pos);
				BlockState state = blockToPlace.getStateForPlacement(placeContext);
				if (state != null && placeContext.canPlace() && state.canSurvive(player.level(), pos)) {
					renderBlockAt(ms, buffer, state, pos, camera);
				}
			}
		}
	}

	private static BlockPlaceContext getPlaceContext(Player player, BlockPlaceContext ctx, BlockPos pos) {
		Vec3 newHitVec = new Vec3(pos.getX() + Mth.frac(ctx.getClickLocation().x()),
				pos.getY() + Mth.frac(ctx.getClickLocation().y()),
				pos.getZ() + Mth.frac(ctx.getClickLocation().z()));
		BlockHitResult newHit = new BlockHitResult(newHitVec, ctx.getClickedFace(), pos, false);
		return new BlockPlaceContext(player, ctx.getHand(), ctx.getItemInHand(), newHit);
	}

	private static void renderBlockAt(PoseStack pose, VertexConsumer buffer, BlockState state, BlockPos pos, Vec3 camera) {
		Minecraft minecraft = Minecraft.getInstance();
		var model = minecraft.getModelManager().getBlockStateModelSet().get(state);
		List<BlockStateModelPart> parts = new ArrayList<>();
		model.collectParts(RandomSource.create(42), parts);

		pose.pushPose();
		pose.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);
		var quadInstance = new QuadInstance();
		quadInstance.setLightCoords(0xF000F0);
		quadInstance.setOverlayCoords(OverlayTexture.NO_OVERLAY);
		for (BlockStateModelPart part : parts) {
			for (Direction direction : Direction.values()) {
				renderQuads(pose.last(), buffer, state, part.getQuads(direction), quadInstance);
			}
			renderQuads(pose.last(), buffer, state, part.getQuads(null), quadInstance);
		}
		pose.popPose();
	}

	private static void renderQuads(PoseStack.Pose pose, VertexConsumer buffer, BlockState state,
			List<BakedQuad> quads, QuadInstance quadInstance) {
		var colors = Minecraft.getInstance().getBlockColors();
		for (BakedQuad quad : quads) {
			int color = 0xFFFFFF;
			if (quad.materialInfo().isTinted()) {
				var source = colors.getTintSource(state, quad.materialInfo().tintIndex());
				if (source != null) {
					color = source.color(state);
				}
			}
			quadInstance.setColor(ARGB.color(102, color));
			buffer.putBakedQuad(pose, quad, quadInstance);
		}
	}
}
