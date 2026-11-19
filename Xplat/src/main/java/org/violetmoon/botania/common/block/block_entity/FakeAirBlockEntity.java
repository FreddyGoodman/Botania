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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.common.block.block_entity.flower.functional.BubbellBlockEntity;

public class FakeAirBlockEntity extends BlockEntity {
	private static final String TAG_FLOWER_POS = "flowerPos";

	@Nullable
	private BlockPos flowerPos;

	public FakeAirBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.FAKE_AIR, pos, state);
	}

	public void setFlower(BlockEntity tile) {
		flowerPos = tile.getBlockPos();
		setChanged();
	}

	public boolean canStay() {
		return flowerPos != null && BubbellBlockEntity.isValidBubbell(level, flowerPos);
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		if (flowerPos != null) {
			output.store(TAG_FLOWER_POS, BlockPos.CODEC, flowerPos);
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		flowerPos = input.read(TAG_FLOWER_POS, BlockPos.CODEC).orElse(null);
	}

}
