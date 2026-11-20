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
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.PylonBlock;

import java.util.function.Consumer;

public final class BlockEntityItemRenderer implements NoDataSpecialModelRenderer {
	private final BlockEntity blockEntity;
	private final boolean direct;

	private BlockEntityItemRenderer(BlockEntity blockEntity, boolean direct) {
		this.blockEntity = blockEntity;
		this.direct = direct;
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.getBlockEntityRenderDispatcher().getRenderer(blockEntity) instanceof Display display) {
			float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);
			display.submitAsItem(blockEntity.getBlockState().getBlock(), direct, partialTick, pose, collector, light, overlay);
		}
	}

	public boolean isAnimated() {
		Block block = blockEntity.getBlockState().getBlock();
		return block instanceof PylonBlock || block == BotaniaBlocks.CORPOREA_INDEX
				|| block == BotaniaBlocks.BOTANICAL_BREWERY || block == BotaniaBlocks.TERU_TERU_BOZU;
	}

	@Override
	public void getExtents(Consumer<Vector3fc> consumer) {
		for (int corner = 0; corner < 8; corner++) {
			consumer.accept(new Vector3f(corner & 1, (corner >> 1) & 1, (corner >> 2) & 1));
		}
	}

	public interface Display {
		void submitAsItem(Block block, boolean direct, float partialTick, PoseStack pose, SubmitNodeCollector collector, int light, int overlay);
	}

	public record Unbaked(Block block, boolean direct) implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Unbaked::block),
				Codec.BOOL.optionalFieldOf("direct", false).forGetter(Unbaked::direct)
		).apply(instance, Unbaked::new));

		@Override
		public BlockEntityItemRenderer bake(BakingContext context) {
			if (block instanceof EntityBlock entityBlock) {
				BlockEntity entity = entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
				if (entity != null) {
					return new BlockEntityItemRenderer(entity, direct);
				}
			}
			return null;
		}

		@Override
		public MapCodec<Unbaked> type() {
			return CODEC;
		}
	}
}
