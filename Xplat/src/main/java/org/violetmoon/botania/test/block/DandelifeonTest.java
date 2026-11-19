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
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;

import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.flower.generating.DandelifeonBlockEntity;

public class DandelifeonTest {

	private static final BlockPos LEVER_POS = new BlockPos(29, 2, 12);
	private static final BlockPos TARGET_POS = new BlockPos(10, 3, 11);
	private static final BlockPos LOWER_SQUARE = new BlockPos(32, 3, 11);
	private static final BlockPos UPPER_SQUARE = new BlockPos(32, 3, 12);
	private static final int EXPECTED_MANA = 2460;

	public void testDandelifeonTransferring(GameTestHelper helper) {
		helper.startSequence().thenExecute(() -> {
			helper.pullLever(LEVER_POS);
		}).thenExecuteAfter(24 * DandelifeonBlockEntity.SPEED, () -> {
			// test the still life is present
			for (int x = LOWER_SQUARE.getX(); x <= UPPER_SQUARE.getX(); x++) {
				for (int y = LOWER_SQUARE.getY(); y <= UPPER_SQUARE.getY(); y++) {
					for (int z = LOWER_SQUARE.getZ(); z <= UPPER_SQUARE.getZ(); z++) {
						helper.assertBlockPresent(BotaniaBlocks.CELLULAR_BLOCK, new BlockPos(x, y, z));
					}
				}
			}
			var dandie = helper.getBlockEntity(TARGET_POS, DandelifeonBlockEntity.class);
			// test the region was nuked
			int radius = dandie.getRange();
			for (int i = -radius; i <= radius; i++) {
				for (int j = -radius; j <= radius; j++) {
					helper.assertBlockNotPresent(BotaniaBlocks.CELLULAR_BLOCK, new BlockPos(i + TARGET_POS.getX(), TARGET_POS.getY(), j + TARGET_POS.getZ()));
				}
			}
			// test mana value is correct
			if (dandie.getMana() != EXPECTED_MANA) {
				throw helper.assertionException(Component.literal("Wrong amount of mana: expected " + EXPECTED_MANA + " but was " + dandie.getMana()));
			}
		}).thenSucceed();
	}
}
