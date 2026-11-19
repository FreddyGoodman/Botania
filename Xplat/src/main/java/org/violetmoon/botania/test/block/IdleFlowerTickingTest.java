/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.test.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.violetmoon.botania.api.block.WandBindable;
import org.violetmoon.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import org.violetmoon.botania.api.block_entity.FunctionalFlowerBlockEntity;
import org.violetmoon.botania.api.block_entity.SpecialFlowerBlockEntity;
import org.violetmoon.botania.common.block.flower.SpecialFlowerBlock;

import java.util.function.Consumer;

/**
 * This test checks simply places each defined special flower and lets it sit for a while.
 * Functional flowers start with a full buffer.
 */
public class IdleFlowerTickingTest {
	private static final BlockPos FLOWER_POS = new BlockPos(16, 16, 16);

	public static Consumer<GameTestHelper> testSpecialFlowerIdle(Identifier id) {
		return helper -> {
			Block block = BuiltInRegistries.BLOCK.getValue(id);
			if (block instanceof SpecialFlowerBlock) {
				helper.setBlock(FLOWER_POS.below(), Blocks.GRASS_BLOCK);
			}
			helper.setBlock(FLOWER_POS, block);
			SpecialFlowerBlockEntity specialFlower = helper.getBlockEntity(FLOWER_POS, SpecialFlowerBlockEntity.class);
			if (specialFlower instanceof BindableSpecialFlowerBlockEntity<?> && WandBindable.LOOKUP.find(specialFlower, null) == null) {
				helper.fail("Bindable special flower block entity is not registered as WandBindable");
				return;
			}
			if (specialFlower instanceof FunctionalFlowerBlockEntity functionalFlower) {
				functionalFlower.addMana(functionalFlower.getMaxMana());
			}
			helper.startSequence()
					.thenExecuteAfter(101, helper::killAllEntities)
					.thenSucceed();
		};
	}
}
