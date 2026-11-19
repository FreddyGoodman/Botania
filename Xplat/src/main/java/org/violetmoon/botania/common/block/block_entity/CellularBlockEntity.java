/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.flower.generating.DandelifeonBlockEntity;
import org.violetmoon.botania.common.block.block_entity.flower.generating.DandelifeonBlockEntity.Cell;

public class CellularBlockEntity extends BlockEntity {
	private static final String TAG_GENERATION = "generation";
	private static final String TAG_TICKED = "ticked";
	private static final String TAG_FLOWER_COORDS = "flowerPos";
	private static final String TAG_VALID_COORDS = "validPos";

	private int generation;
	private int nextGeneration;
	private boolean ticked;
	@Nullable
	private BlockPos flowerCoords;
	@Nullable
	private BlockPos validCoords;

	public CellularBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.CELLULAR_BLOCK, pos, state);
	}

	public void setGeneration(int gen) {
		generation = gen;
	}

	public void setNextGeneration(DandelifeonBlockEntity flower, int gen) {
		nextGeneration = gen;
		getLevel().scheduleTick(getBlockPos(), BotaniaBlocks.CELLULAR_BLOCK, 1);
		if (!ticked) {
			claim(flower);
			ticked = true;
		} else if (!getBlockPos().equals(validCoords) || !flower.getEffectivePos().equals(flowerCoords)) {
			level.removeBlock(worldPosition, false);
		}
	}

	public void claim(DandelifeonBlockEntity flower) {
		if (!ticked) {
			flowerCoords = flower.getEffectivePos();
			validCoords = getBlockPos();
		}
	}

	public void update(Level level) {
		if (nextGeneration == Cell.DEAD) {
			level.removeBlock(getBlockPos(), false);
		}
		generation = nextGeneration;
	}

	public boolean hasActiveParent(DandelifeonBlockEntity dandie) {
		return flowerCoords != null
				&& dandie.getLevel().getBlockEntity(flowerCoords) instanceof DandelifeonBlockEntity parent
				&& parent.shouldUpdateThisTick() && parent.isPowered();
	}

	public int getGeneration() {
		return generation;
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		output.putInt(TAG_GENERATION, generation);
		output.putBoolean(TAG_TICKED, ticked);
		if (ticked) {
			if (flowerCoords != null) {
				output.store(TAG_FLOWER_COORDS, BlockPos.CODEC, flowerCoords);
			}
			if (validCoords != null) {
				output.store(TAG_VALID_COORDS, BlockPos.CODEC, validCoords);
			}
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		generation = input.getIntOr(TAG_GENERATION, 0);
		ticked = input.getBooleanOr(TAG_TICKED, false);
		if (ticked) {
			flowerCoords = input.read(TAG_FLOWER_COORDS, BlockPos.CODEC).orElse(null);
			validCoords = input.read(TAG_VALID_COORDS, BlockPos.CODEC).orElse(null);
		}
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		// targeting Create here, sorry about any instances of https://xkcd.com/1172/
		return true;
	}
}
