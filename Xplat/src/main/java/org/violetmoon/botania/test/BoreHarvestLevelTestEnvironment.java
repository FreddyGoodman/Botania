/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.test;

import com.mojang.serialization.MapCodec;

import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;

import org.violetmoon.botania.xplat.BotaniaConfig;

import java.util.function.BiConsumer;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public record BoreHarvestLevelTestEnvironment(int harvestLevel) implements TestEnvironmentDefinition<BotaniaConfig.ConfigAccess> {
	private static final MapCodec<BoreHarvestLevelTestEnvironment> CODEC = ExtraCodecs.intRange(0, 3)
			.fieldOf("level").xmap(BoreHarvestLevelTestEnvironment::new, BoreHarvestLevelTestEnvironment::harvestLevel);

	public static void register(BiConsumer<MapCodec<? extends TestEnvironmentDefinition<?>>, Identifier> registrar) {
		registrar.accept(CODEC, botaniaRL("bore_harvest_level"));
	}

	@Override
	public BotaniaConfig.ConfigAccess setup(ServerLevel level) {
		var previous = BotaniaConfig.common();
		BotaniaConfig.setCommon(new DelegatingConfigAccess(previous) {
			@Override
			public int harvestLevelBore() {
				return harvestLevel;
			}
		});
		return previous;
	}

	@Override
	public void teardown(ServerLevel level, BotaniaConfig.ConfigAccess previous) {
		BotaniaConfig.setCommon(previous);
	}

	@Override
	public MapCodec<BoreHarvestLevelTestEnvironment> codec() {
		return CODEC;
	}
}
