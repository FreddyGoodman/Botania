/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.block.PetalApothecary;
import org.violetmoon.botania.api.mana.ManaReceiver;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.PetalApothecaryBlock;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaRetainerBlockEntity;
import org.violetmoon.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import org.violetmoon.botania.common.item.BotaniaItems;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class PortParityTests {
	private PortParityTests() {}

	public static void register(BiConsumer<Consumer<GameTestHelper>, Identifier> registrar) {
		registrar.accept(PortParityTests::defaultAim, botaniaRL("verification/mana_spreader_default_aim"));
		registrar.accept(PortParityTests::recipe, botaniaRL("verification/apothecary_remembered_recipe"));
		registrar.accept(PortParityTests::apple, botaniaRL("verification/corporea_interceptor_apple"));
		registrar.accept(PortParityTests::boots, botaniaRL("verification/corporea_interceptor_boots"));

		var fixtures = System.getProperty("botania.gametest.fixtures");
		if (fixtures != null) {
			StructureUtils.testStructuresSourceDir = Path.of(fixtures);
		}
	}

	private static final String ID = "mana.spreader.default_aim";
	private static final String[] LANES = { "pulse", "mana", "elven", "gaia" };
	private static final BlockPos SPREADER = new BlockPos(2, 1, 1);
	private static final BlockPos POOL = new BlockPos(3, 1, 1);
	private static final BlockPos SINK = new BlockPos(4, 1, 1);
	private static final BlockPos LEVER = new BlockPos(1, 2, 2);

	private static void defaultAim(GameTestHelper helper) {
		var player = helper.makeMockPlayer(GameType.CREATIVE);
		for (int i = 0; i < LANES.length; i++) {
			var spreader = helper.getBlockEntity(SPREADER.south(i), ManaSpreaderBlockEntity.class);
			if (!spreader.bindTo(player, new ItemStack(BotaniaItems.WAND_OF_THE_FOREST),
					helper.absolutePos(SINK.south(i)), Direction.UP)) {
				throw new IllegalStateException("Could not aim " + LANES[i] + " spreader");
			}
		}
		sample(helper, 0, "before_action");
		helper.pullLever(LEVER);
		for (int i = 1; i <= 10; i++) {
			int tick = i;
			helper.runAtTickTime(tick, () -> {
				sample(helper, tick, "after_tick");
				if (tick == 10) {
					BotaniaAPI.LOGGER.info("BOTANIA_VERIFICATION_COMPLETE {}", ID);
					helper.succeed();
				}
			});
		}
	}

	private static void sample(GameTestHelper helper, int tick, String phase) {
		for (int i = 0; i < LANES.length; i++) {
			observe(helper, tick, phase, "pool." + LANES[i], POOL.south(i));
			observe(helper, tick, phase, "sink." + LANES[i], SINK.south(i));
		}
	}

	private static void observe(GameTestHelper helper, int tick, String phase, String subject, BlockPos pos) {
		var receiver = (ManaReceiver) helper.getBlockEntity(pos, BlockEntity.class);
		BotaniaAPI.LOGGER.info("BOTANIA_VERIFICATION_SAMPLE {} {} {} {} mana {}",
				ID, tick, phase, subject, receiver.getCurrentMana());
	}

	private static final BlockPos APOTHECARY = new BlockPos(1, 2, 1);
	private static final BlockPos WILDCARD = new BlockPos(6, 5, 7);
	private static final BlockPos BOOTS = new BlockPos(4, 5, 7);

	private static void recipe(GameTestHelper helper) {
		String id = "apothecary.remembered_recipe";
		for (int i = 0; i < 4; i++) {
			spawn(helper, BotaniaItems.WHITE_MYSTICAL_PETAL);
		}
		helper.runAtTickTime(1, () -> spawn(helper, Items.MELON_SEEDS));
		helper.runAtTickTime(11, () -> {
			count(helper, id, 11, "pure_daisy", BotaniaBlocks.PURE_DAISY.asItem());
			count(helper, id, 11, "white_petals", BotaniaItems.WHITE_MYSTICAL_PETAL);
			count(helper, id, 11, "melon_seeds", Items.MELON_SEEDS);
			var state = helper.getBlockState(APOTHECARY);
			sample(id, 11, "apothecary", "fluid",
					state.getValue(PetalApothecaryBlock.FLUID).getSerializedName());
			helper.setBlock(APOTHECARY, state.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.WATER));
			helper.killAllEntities();
			spawn(helper, Items.MELON_SEEDS);
			spawn(helper, Items.PUMPKIN_SEEDS);
		});
		helper.runAtTickTime(21, () -> {
			count(helper, id, 21, "melon_seeds", Items.MELON_SEEDS);
			count(helper, id, 21, "pumpkin_seeds", Items.PUMPKIN_SEEDS);
			complete(helper, id);
		});
	}

	private static void apple(GameTestHelper helper) {
		request(helper, "corporea.interceptor.apple", new BlockPos(6, 6, 4));
	}

	private static void boots(GameTestHelper helper) {
		request(helper, "corporea.interceptor.boots", new BlockPos(4, 6, 4));
	}

	private static void request(GameTestHelper helper, String id, BlockPos button) {
		helper.pressButton(button);
		helper.runAtTickTime(1, () -> {
			sample(id, 1, "wildcard", "pending", pending(helper, WILDCARD));
			sample(id, 1, "boots", "pending", pending(helper, BOOTS));
			complete(helper, id);
		});
	}

	private static boolean pending(GameTestHelper helper, BlockPos pos) {
		return helper.getBlockEntity(pos, CorporeaRetainerBlockEntity.class).hasPendingRequest();
	}

	private static void spawn(GameTestHelper helper, Item item) {
		helper.spawnItem(item, 1.5F, 3.5F, 1.5F);
	}

	private static void count(GameTestHelper helper, String id, int tick, String subject, Item item) {
		var area = new AABB(helper.absolutePos(APOTHECARY.above())).inflate(1.5);
		int count = helper.getLevel().getEntitiesOfClass(ItemEntity.class, area,
				entity -> entity.getItem().is(item)).stream().mapToInt(entity -> entity.getItem().getCount()).sum();
		sample(id, tick, subject, "item_count", count);
	}

	private static void sample(String id, int tick, String subject, String field, Object value) {
		BotaniaAPI.LOGGER.info("BOTANIA_VERIFICATION_SAMPLE {} {} after_tick {} {} {}",
				id, tick, subject, field, value);
	}

	private static void complete(GameTestHelper helper, String id) {
		BotaniaAPI.LOGGER.info("BOTANIA_VERIFICATION_COMPLETE {}", id);
		helper.succeed();
	}
}
