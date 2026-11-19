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
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;
import org.violetmoon.botania.common.block.block_entity.flower.generating.EntropinnyumBlockEntity;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.test.TestingUtil;

public class EntropinnyumUnethicalTntDetectionTest {

	private static final BlockPos POSITION_ENTROPINNYUM = new BlockPos(9, 1, 9);

	private static final BlockPos POSITION_FORCE_LENS_DETECTOR_RAIL = new BlockPos(7, 16, 10);
	private static final BlockPos POSITION_FORCE_LENS_PUSHED_SLIME_BLOCK = new BlockPos(6, 16, 10);
	private static final BlockPos POSITION_FORCE_LENS_MANA_SPREADER = new BlockPos(5, 16, 10);
	private static final BlockPos POSITION_FORCE_LENS_BUTTON = new BlockPos(5, 16, 8);

	private static final BlockPos POSITION_FORCE_RELAY_DETECTOR_RAIL = new BlockPos(4, 4, 10);
	private static final BlockPos POSITION_FORCE_RELAY_PUSHED_SLIME_BLOCK = new BlockPos(3, 4, 10);
	private static final BlockPos POSITION_FORCE_RELAY = new BlockPos(4, 3, 6);
	private static final BlockPos POSITION_FORCE_RELAY_BUTTON = new BlockPos(2, 4, 5);

	private static final BlockPos POSITION_ENTITYLESS_DUPER_BUTTON = new BlockPos(3, 13, 10);

	private static final BlockPos POSITION_ETHICAL_TNT_BLOCK_BUTTON = new BlockPos(9, 4, 9);

	private static Runnable checkEntropinnyumForUnethicalManaAmount(GameTestHelper helper) {
		return () -> {
			final var be = helper.getBlockEntity(POSITION_ENTROPINNYUM, BlockEntity.class);
			if (!(be instanceof EntropinnyumBlockEntity entropinnyum)) {
				throw helper.assertionException(Component.literal("Missing Entropinnyum at: " + POSITION_ENTROPINNYUM));
			}
			if (entropinnyum.getMana() != 3) {
				throw helper.assertionException(Component.literal("Wrong amount of mana: expected " + 3 + " but was " + entropinnyum.getMana()));
			}
		};
	}

	public void testForceLensDuper(GameTestHelper helper) {
		helper.startSequence()
				.thenExecute(() -> {
					var player = helper.makeMockPlayer(GameType.CREATIVE);
					var spreader = TestingUtil.assertBlockEntity(helper, POSITION_FORCE_LENS_MANA_SPREADER, BotaniaBlockEntities.MANA_SPREADER);
					TestingUtil.assertThat(helper, spreader.bindTo(player, new ItemStack(BotaniaItems.WAND_OF_THE_FOREST),
							helper.absolutePos(POSITION_FORCE_LENS_PUSHED_SLIME_BLOCK), Direction.UP),
							() -> "Failed to bind spreader");

					if (helper.getBlockState(POSITION_FORCE_LENS_DETECTOR_RAIL).getBlock() != Blocks.DETECTOR_RAIL) {
						throw helper.assertionException(Component.literal("Missing detector rail at: " +
								POSITION_FORCE_LENS_DETECTOR_RAIL));
					}
					helper.spawn(EntityType.MINECART, POSITION_FORCE_LENS_DETECTOR_RAIL);
					helper.pressButton(POSITION_FORCE_LENS_BUTTON);
				})
				.thenExecuteAfter(130, checkEntropinnyumForUnethicalManaAmount(helper))
				.thenSucceed();
	}

	public void testForceRelayDuper(GameTestHelper helper) {
		helper.startSequence()
				.thenExecute(() -> {
					if (helper.getBlockState(POSITION_FORCE_RELAY).getBlock() != BotaniaBlocks.FORCE_RELAY) {
						throw helper.assertionException(Component.literal("Missing force relay at: " + POSITION_FORCE_RELAY));
					}
					TestingUtil.bindForceRelayTarget(helper, POSITION_FORCE_RELAY,
							POSITION_FORCE_RELAY_PUSHED_SLIME_BLOCK);

					if (helper.getBlockState(POSITION_FORCE_RELAY_DETECTOR_RAIL).getBlock() != Blocks.DETECTOR_RAIL) {
						throw helper.assertionException(Component.literal("Missing detector rail at: " +
								POSITION_FORCE_RELAY_DETECTOR_RAIL));
					}
					helper.spawn(EntityType.MINECART, POSITION_FORCE_RELAY_DETECTOR_RAIL);
				})
				.thenExecuteAfter(1, () -> helper.pressButton(POSITION_FORCE_RELAY_BUTTON))
				.thenExecuteAfter(90, checkEntropinnyumForUnethicalManaAmount(helper))
				.thenSucceed();
	}

	public void testEntitylessDuper(GameTestHelper helper) {
		helper.startSequence()
				.thenExecute(() -> helper.pressButton(POSITION_ENTITYLESS_DUPER_BUTTON))
				.thenExecuteAfter(95, checkEntropinnyumForUnethicalManaAmount(helper))
				.thenSucceed();
	}

	public void testLegitimateTnt(GameTestHelper helper) {
		helper.startSequence()
				.thenExecute(() -> helper.pressButton(POSITION_ETHICAL_TNT_BLOCK_BUTTON))
				.thenExecuteAfter(95, () -> {
					final var be = helper.getBlockEntity(POSITION_ENTROPINNYUM, BlockEntity.class);
					if (!(be instanceof EntropinnyumBlockEntity entropinnyum)) {
						throw helper.assertionException(Component.literal("Missing Entropinnyum at: " + POSITION_ENTROPINNYUM));
					}
					if (entropinnyum.getMana() != entropinnyum.getMaxMana()) {
						throw helper.assertionException(Component.literal("Wrong amount of mana: expected " + entropinnyum.getMaxMana() + " but was " + entropinnyum.getMana()));
					}
				})
				.thenSucceed();
	}
}
