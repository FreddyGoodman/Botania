/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.block.WandBindable;
import org.violetmoon.botania.api.block.Wandable;
import org.violetmoon.botania.common.handler.BotaniaSounds;
import org.violetmoon.botania.common.helper.ForcePushHelper;
import org.violetmoon.botania.common.item.lens.ForceLens;
import org.violetmoon.botania.network.clientbound.ParticleBeamEffectPacket;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.*;

public class ForceRelayBlock extends Block {

	public ForceRelayBlock(Properties builder) {
		super(builder.pushReaction(PushReaction.PUSH_ONLY));
	}

	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
		BlockState newState = level.getBlockState(pos);
		var data = WorldData.get(level);

		Direction movementContextDirection = ForcePushHelper.getMovementContextDirection();
		if (movedByPiston && (movementContextDirection != null || newState.is(Blocks.MOVING_PISTON))) {
			var pistonDirection = movementContextDirection != null
					? movementContextDirection
					: newState.getValue(MovingPistonBlock.FACING);
			// if being moved as part of a retracting sticky piston's block structure, reverse movement direction
			var moveDirection = ForcePushHelper.isExtendingMovementContext() ? pistonDirection : pistonDirection.getOpposite();

			var destPos = data.mapping.get(pos);
			if (destPos != null) {
				BlockPos newSrcPos = pos.relative(moveDirection);

				{
					// Move source side of our binding along
					data.mapping.remove(pos);
					data.mapping.put(newSrcPos, destPos);
					data.setDirty();
				}

				if (!newState.is(Blocks.MOVING_PISTON) || newState.getValue(MovingPistonBlock.TYPE) == PistonType.DEFAULT) {
					// Move the actual bound blocks
					if (ForceLens.moveBlocks(level, destPos.relative(moveDirection.getOpposite()), moveDirection, pos)) {
						// Move dest side of our binding
						data.mapping.put(newSrcPos, data.mapping.get(newSrcPos).relative(moveDirection));
					}
				}
			}
		} else {
			if (data.mapping.remove(pos) != null) {
				data.setDirty();
			}
		}
	}

	public record ForceRelayWandable(Level level, BlockPos pos) implements Wandable {
		@Override
		public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
			if (player == null || level.isClientSide()) {
				return false;
			}

			var data = WorldData.get(level);
			if (XplatAbstractions.INSTANCE.isDevEnvironment()) {
				BotaniaAPI.LOGGER.info("PistonRelay pairs");
				for (var e : data.mapping.entrySet()) {
					BotaniaAPI.LOGGER.info("{} -> {}", e.getKey(), e.getValue());
				}
			}
			BlockPos dest = data.mapping.get(pos);
			if (dest != null) {
				XplatAbstractions.INSTANCE.sendToNear(level, pos, new ParticleBeamEffectPacket(pos, dest));
			}

			return true;
		}
	}

	public record ForceRelayBindable(Level level, BlockPos sourcePos) implements WandBindable {
		@Override
		public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
			if (!level.isClientSide()) {
				addBinding(level, sourcePos, pos);

				XplatAbstractions.INSTANCE.sendToNear(level, pos, new ParticleBeamEffectPacket(sourcePos, pos));
			}
			level.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.DING, SoundSource.PLAYERS, 1F, 1F);
			return true;
		}

	}

	public static void addBinding(Level level, BlockPos sourcePos, BlockPos targetPos) {
		if (level.isClientSide()) {
			BotaniaAPI.LOGGER.warn("Tried to bind Force Relay on client: {}/{}", sourcePos, targetPos);
			return;
		}
		WorldData data = WorldData.get(level);
		data.mapping.put(sourcePos.immutable(), targetPos.immutable());
		data.setDirty();
	}

	public static class WorldData extends SavedData {

		private static final Codec<WorldData> CODEC = Binding.CODEC.listOf().fieldOf("bindings")
				.xmap(WorldData::new, WorldData::getBindings).codec();
		private static final SavedDataType<WorldData> TYPE = new SavedDataType<>(
				BotaniaAPI.botaniaRL("piston_relay_pairs"), WorldData::new, CODEC, DataFixTypes.LEVEL);

		public final Map<BlockPos, BlockPos> mapping = new HashMap<>();

		public WorldData() {}

		private WorldData(List<Binding> bindings) {
			for (Binding binding : bindings) {
				mapping.put(binding.source(), binding.target());
			}
		}

		private List<Binding> getBindings() {
			return mapping.entrySet().stream().map(entry -> new Binding(entry.getKey(), entry.getValue())).toList();
		}

		public static WorldData get(Level world) {
			return ((ServerLevel) world).getDataStorage().computeIfAbsent(TYPE);
		}

		private record Binding(BlockPos source, BlockPos target) {
			private static final Codec<Binding> CODEC = RecordCodecBuilder.create(instance -> instance.group(
					BlockPos.CODEC.fieldOf("source").forGetter(Binding::source),
					BlockPos.CODEC.fieldOf("target").forGetter(Binding::target)
			).apply(instance, Binding::new));
		}
	}
}
