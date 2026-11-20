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
import org.violetmoon.botania.common.item.equipment.bauble.FlugelTiaraItem;
import org.violetmoon.botania.common.item.equipment.bauble.ThirdEyeItem;
import org.violetmoon.botania.common.item.relic.KeyOfTheKingsLawItem;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class StandaloneBlockModels {
	public static final Identifier PHI_FLOWER = botaniaRL("icon/phiflower");
	public static final Identifier NERF_BAT = botaniaRL("icon/nerfbat");
	public static final Identifier GOLDFISH = botaniaRL("icon/goldfish");
	public static final Identifier TERRASTEEL_WILL = botaniaRL("icon/will_flame");
	public static final Identifier BLOOD_PENDANT_CHAIN = botaniaRL("icon/tainted_blood_pendant_chain");
	public static final Identifier BLOOD_PENDANT_GEM = botaniaRL("icon/tainted_blood_pendant_gem");
	public static final Identifier PYROCLAST_GEM = botaniaRL("icon/pyroclast_pendant_gem");
	public static final Identifier CRIMSON_GEM = botaniaRL("icon/crimson_pendant_gem");
	public static final Identifier SPECTATOR_GEM = botaniaRL("icon/the_spectator_gem");
	public static final Identifier CIRRUS_GEM = botaniaRL("icon/cirrus_amulet_gem");
	public static final Identifier NIMBUS_GEM = botaniaRL("icon/nimbus_amulet_gem");
	public static final Identifier SNOWFLAKE_GEM = botaniaRL("icon/snowflake_pendant_gem");
	public static final Identifier ORANGE_SHADES = botaniaRL("item/cosmetic_orange_shades");
	public static final Identifier KAMUI_EYE = botaniaRL("item/cosmetic_kamui_eye");
	public static final List<Identifier> THIRD_EYE_LAYERS = IntStream.range(0, ThirdEyeItem.Renderer.NUM_LAYERS)
			.mapToObj(index -> botaniaRL("icon/third_eye_" + index)).toList();
	public static final List<Identifier> TIARA_WINGS = IntStream.rangeClosed(1, FlugelTiaraItem.WING_TYPES)
			.mapToObj(index -> botaniaRL("icon/tiara_wing_" + index)).toList();
	public static final List<Identifier> TREASURY_WEAPONS = IntStream.range(0, KeyOfTheKingsLawItem.WEAPON_TYPES)
			.mapToObj(index -> botaniaRL("icon/gate_weapon_" + index)).toList();

	public static void register(ResourceManager resources, Consumer<Identifier> consumer) {
		consumer.accept(CorporeaCrystalCubeBlockEntityRenderer.CUBE_MODEL_ID);
		Stream.of(PHI_FLOWER, NERF_BAT, GOLDFISH, TERRASTEEL_WILL,
				BLOOD_PENDANT_CHAIN, BLOOD_PENDANT_GEM, PYROCLAST_GEM, CRIMSON_GEM, SPECTATOR_GEM,
				CIRRUS_GEM, NIMBUS_GEM, SNOWFLAKE_GEM, ORANGE_SHADES, KAMUI_EYE).forEach(consumer);
		TREASURY_WEAPONS.forEach(consumer);
		THIRD_EYE_LAYERS.forEach(consumer);
		TIARA_WINGS.forEach(consumer);
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
