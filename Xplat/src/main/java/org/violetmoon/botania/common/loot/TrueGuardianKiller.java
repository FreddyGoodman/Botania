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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import org.violetmoon.botania.common.entity.GaiaGuardianEntity;

public class TrueGuardianKiller implements LootItemCondition {
	public static final TrueGuardianKiller INSTANCE = new TrueGuardianKiller();
	public static final MapCodec<TrueGuardianKiller> CODEC = MapCodec.unit(INSTANCE);

	private TrueGuardianKiller() {}

	@Override
	public boolean test(LootContext context) {
		Entity victim = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
		return victim instanceof GaiaGuardianEntity gg
				&& context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY) == gg.trueKiller;
	}

	@Override
	public MapCodec<TrueGuardianKiller> codec() {
		return CODEC;
	}

	public static LootItemCondition.Builder builder() {
		return Builder.INSTANCE;
	}

	private static class Builder implements LootItemCondition.Builder {
		public static final Builder INSTANCE = new Builder();

		@Override
		public LootItemCondition build() {
			return TrueGuardianKiller.INSTANCE;
		}
	}
}
