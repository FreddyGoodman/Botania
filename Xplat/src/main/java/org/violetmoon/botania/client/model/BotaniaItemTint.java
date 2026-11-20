/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.render.ColorHandler;

public record BotaniaItemTint(int layer) implements ItemTintSource {
	public static final MapCodec<BotaniaItemTint> CODEC = ExtraCodecs.NON_NEGATIVE_INT.fieldOf("layer")
			.xmap(BotaniaItemTint::new, BotaniaItemTint::layer);

	@Override
	public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
		return ColorHandler.getItemColor(stack, layer);
	}

	@Override
	public MapCodec<BotaniaItemTint> type() {
		return CODEC;
	}
}
