/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.core.handler;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.client.render.block_entity.CorporeaCrystalCubeBlockEntityRenderer;
import org.violetmoon.botania.client.render.block_entity.ManaPumpBlockEntityRenderer;
import org.violetmoon.botania.client.render.block_entity.ManaSpreaderBlockEntityRenderer;
import org.violetmoon.botania.common.block.BotaniaBlocks;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class StandaloneBlockModels {
	public static final Identifier PHI_FLOWER = botaniaRL("icon/phiflower");
	public static final Identifier NERF_BAT = botaniaRL("icon/nerfbat");
	public static final Identifier GOLDFISH = botaniaRL("icon/goldfish");

	public static void register(ResourceManager resources, Consumer<Identifier> consumer) {
		consumer.accept(CorporeaCrystalCubeBlockEntityRenderer.CUBE_MODEL_ID);
		consumer.accept(PHI_FLOWER);
		consumer.accept(NERF_BAT);
		consumer.accept(GOLDFISH);
		consumer.accept(ManaPumpBlockEntityRenderer.PUMP_HEAD_ID);
		registerSpreaderComponents(consumer);
		registerTinyPotatoModels(resources, consumer);
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

	private static void registerTinyPotatoModels(ResourceManager resources, Consumer<Identifier> consumer) {
		var files = FileToIdConverter.json(ResourcesLib.PREFIX_MODELS + ResourcesLib.PREFIX_TINY_POTATO);
		files.listMatchingResources(resources).keySet().stream()
				.filter(file -> BotaniaAPI.MODID.equals(file.getNamespace()))
				.map(files::fileToId)
				.map(id -> id.withPrefix(ResourcesLib.PREFIX_TINY_POTATO + "/"))
				.forEach(consumer);
	}

	private StandaloneBlockModels() {}
}
