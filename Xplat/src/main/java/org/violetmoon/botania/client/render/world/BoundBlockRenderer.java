/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.world;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.item.CoordBoundItem;
import org.violetmoon.botania.api.item.WireframeCoordinateListProvider;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.xplat.BotaniaConfig;

import java.util.SequencedMap;

public final class BoundBlockRenderer {
	private static final MultiBufferSource.BufferSource LINE_BUFFERS = MultiBufferSource.immediateWithBuffers(Util.make(() -> {
		SequencedMap<RenderType, ByteBufferBuilder> ret = new Object2ObjectLinkedOpenHashMap<>();
		ret.put(RenderHelper.LINE_1_NO_DEPTH, new ByteBufferBuilder(RenderHelper.LINE_1_NO_DEPTH.bufferSize()));
		ret.put(RenderHelper.LINE_4_NO_DEPTH, new ByteBufferBuilder(RenderHelper.LINE_4_NO_DEPTH.bufferSize()));
		ret.put(RenderHelper.LINE_5_NO_DEPTH, new ByteBufferBuilder(RenderHelper.LINE_5_NO_DEPTH.bufferSize()));
		ret.put(RenderHelper.LINE_8_NO_DEPTH, new ByteBufferBuilder(RenderHelper.LINE_8_NO_DEPTH.bufferSize()));
		return ret;
	}), new ByteBufferBuilder(786432)); //Todo confirm the capacity is correct. This is just taken from `RenderBuffers`

	private BoundBlockRenderer() {}

	public static void onWorldRenderLast(Camera camera, float partialTick, PoseStack ms, Level level) {
		Player player = Minecraft.getInstance().player;
		if (!BotaniaConfig.client().boundBlockWireframe()
				|| player == null
				|| player.level() != level) {
			return;
		}

		ms.pushPose();

		int color = ARGB.opaque(Mth.hsvToRgb((ClientTickHandler.getEntityTicksInGame() + partialTick) % 200 / 200F, 0.6F, 1F));

		if (!player.getMainHandItem().isEmpty()) {
			var coordBoundItem = CoordBoundItem.LOOKUP.find(player.getMainHandItem(), player.level());
			if (coordBoundItem != null) {
				BlockPos coords = coordBoundItem.getBinding();
				if (coords != null) {
					renderBlockOutlineAt(camera, ms, LINE_BUFFERS, player.level(), coords, color);
				}
			}
		}

		if (!player.getOffhandItem().isEmpty()) {
			var coordBoundItem = CoordBoundItem.LOOKUP.find(player.getOffhandItem(), player.level());
			if (coordBoundItem != null) {
				BlockPos coords = coordBoundItem.getBinding();
				if (coords != null) {
					renderBlockOutlineAt(camera, ms, LINE_BUFFERS, player.level(), coords, color);
				}
			}
		}

		renderWireframeProviders(camera, player.getInventory(), player, ms, color);
		renderWireframeProviders(camera, BotaniaAPI.instance().getAccessoriesInventory(player), player, ms, color);

		ms.popPose();
		LINE_BUFFERS.endBatch();
	}

	private static void renderWireframeProviders(Camera camera, Container inv,
			Player player, PoseStack ms, int color) {
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stackInSlot = inv.getItem(i);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof WireframeCoordinateListProvider provider) {
				List<BlockPos> coordsList = provider.getWireframesToDraw(player, stackInSlot);
				for (BlockPos coords : coordsList) {
					renderBlockOutlineAt(camera, ms, LINE_BUFFERS, player.level(), coords, color);
				}

				BlockPos coords = provider.getSourceWireframe(player, stackInSlot);
				if (coords != null && coords.getY() != Integer.MIN_VALUE) {
					renderBlockOutlineAt(camera, ms, LINE_BUFFERS, player.level(), coords, color, true);
				}
			}
		}
	}

	private static void renderBlockOutlineAt(Camera camera, PoseStack ms, MultiBufferSource buffers, Level level, BlockPos pos, int color) {
		renderBlockOutlineAt(camera, ms, buffers, level, pos, color, false);
	}

	private static void renderBlockOutlineAt(Camera camera, PoseStack ms, MultiBufferSource buffers, Level level, BlockPos pos, int color, boolean thick) {
		VoxelShape shape = level.getBlockState(pos).getShape(level, pos);

		if (!shape.isEmpty()) {
			Vec3 cameraPosition = camera.position();

			ms.pushPose();
			ms.translate(pos.getX() - cameraPosition.x, pos.getY() - cameraPosition.y, pos.getZ() - cameraPosition.z);
			var outline = thick ? RenderHelper.LINE_5_NO_DEPTH : RenderHelper.LINE_1_NO_DEPTH;
			var glow = thick ? RenderHelper.LINE_8_NO_DEPTH : RenderHelper.LINE_4_NO_DEPTH;
			ShapeRenderer.renderShape(ms, buffers.getBuffer(outline), shape, 0, 0, 0, color, thick ? 5 : 1);
			ShapeRenderer.renderShape(ms, buffers.getBuffer(glow), shape, 0, 0, 0, ARGB.color(64, color), thick ? 8 : 4);
			ms.popPose();
		}
	}
}
