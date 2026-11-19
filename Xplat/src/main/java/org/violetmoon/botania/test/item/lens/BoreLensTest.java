/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.test.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneLampBlock;

import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.test.TestingUtil;

public class BoreLensTest {
	private void testMine(GameTestHelper helper, Block toMine, boolean expectSuccess) {
		var spreaderPos = new BlockPos(1, 2, 0);
		var manaVoidPos = new BlockPos(1, 2, 3);
		var lampPos = new BlockPos(2, 2, 2);
		var buttonPos = new BlockPos(2, 2, 0);
		var testPos = new BlockPos(1, 2, 1);
		var player = helper.makeMockPlayer(GameType.CREATIVE);

		// Because Mojang removed the block entity rotation methods, we need to ensure
		// that the spreader is pointed at the mana void properly, in case the test
		// structure is placed rotated.
		var spreader = TestingUtil.assertBlockEntity(helper, spreaderPos, BotaniaBlockEntities.MANA_SPREADER);
		TestingUtil.assertThat(helper, spreader.bindTo(player, new ItemStack(BotaniaItems.WAND_OF_THE_FOREST), helper.absolutePos(manaVoidPos), Direction.UP),
				() -> "Failed to bind spreader");
		helper.setBlock(testPos, toMine);

		if (expectSuccess) {
			helper.startSequence()
					.thenExecuteAfter(1, () -> helper.pressButton(buttonPos))
					.thenWaitUntil(() -> helper.assertBlockProperty(lampPos, RedstoneLampBlock.LIT, true))
					.thenSucceed();
		} else {
			// todo can we use failIf or something else that doesn't hardcode a tick count?
			helper.startSequence()
					.thenExecuteAfter(1, () -> helper.pressButton(buttonPos))
					.thenExecuteFor(60, () -> helper.assertBlockProperty(lampPos, RedstoneLampBlock.LIT, false))
					.thenSucceed();
		}
	}

	public void testSnow(GameTestHelper helper) {
		testMine(helper, Blocks.SNOW_BLOCK, true);
	}

	public void testStone(GameTestHelper helper) {
		testMine(helper, Blocks.STONE, true);
	}

	public void testLog(GameTestHelper helper) {
		testMine(helper, Blocks.OAK_LOG, true);
	}

	public void testIronBlockTooLow(GameTestHelper helper) {
		testMine(helper, Blocks.IRON_BLOCK, false);
	}

	public void testIronBlock(GameTestHelper helper) {
		testMine(helper, Blocks.IRON_BLOCK, true);
	}

	public void testGoldOreTooLow(GameTestHelper helper) {
		testMine(helper, Blocks.GOLD_ORE, false);
	}

	public void testGoldOre(GameTestHelper helper) {
		testMine(helper, Blocks.GOLD_ORE, true);
	}

	public void testObsidianTooLow(GameTestHelper helper) {
		testMine(helper, Blocks.OBSIDIAN, false);
	}

	public void testObsidian(GameTestHelper helper) {
		testMine(helper, Blocks.OBSIDIAN, true);
	}
}
