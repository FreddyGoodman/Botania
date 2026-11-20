/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.core.handler;

import net.minecraft.resources.Identifier;

import org.violetmoon.botania.client.render.block_entity.CorporeaCrystalCubeBlockEntityRenderer;
import org.violetmoon.botania.client.render.block_entity.ManaPumpBlockEntityRenderer;
import org.violetmoon.botania.client.render.block_entity.ManaSpreaderBlockEntityRenderer;
import org.violetmoon.botania.common.block.BotaniaBlocks;

import java.util.function.Consumer;
import java.util.stream.Stream;

public final class StandaloneBlockModels {
	public static void register(Consumer<Identifier> consumer) {
		consumer.accept(CorporeaCrystalCubeBlockEntityRenderer.CUBE_MODEL_ID);
		consumer.accept(ManaPumpBlockEntityRenderer.PUMP_HEAD_ID);
		registerSpreaderComponents(consumer);
	}

	private static void registerSpreaderComponents(Consumer<Identifier> consumer) {
		Stream.of(BotaniaBlocks.MANA_SPREADER, BotaniaBlocks.PULSE_MANA_SPREADER,
				BotaniaBlocks.ELVEN_MANA_SPREADER, BotaniaBlocks.GAIA_MANA_SPREADER
		)
				.flatMap(spreader -> Stream
						.of(spreader.getCoreModelId(), spreader.getScaffoldingModelId()))
				.distinct()
				.forEach(consumer);
		ManaSpreaderBlockEntityRenderer.WOOL_PADDING_IDS.values().forEach(consumer);
	}

	private StandaloneBlockModels() {}
}
