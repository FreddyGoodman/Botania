/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.loot;

import com.mojang.serialization.MapCodec;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.BiConsumer;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaLootModifiers {
	public static void submitLootConditions(BiConsumer<MapCodec<? extends LootItemCondition>, Identifier> consumer) {
		consumer.accept(TrueGuardianKiller.CODEC, botaniaRL("true_guardian_killer"));
		consumer.accept(EnableRelics.CODEC, botaniaRL("enable_relics"));
		consumer.accept(RealPlayerCondition.CODEC, botaniaRL("killed_by_player"));
	}

	public static void submitLootFunctions(BiConsumer<MapCodec<? extends LootItemFunction>, Identifier> consumer) {
		consumer.accept(BindUuid.CODEC, botaniaRL("bind_uuid"));
	}
}
