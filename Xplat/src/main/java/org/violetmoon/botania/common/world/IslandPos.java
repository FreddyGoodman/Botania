/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;

public record IslandPos(int x, int z) {
	public static final Codec<IslandPos> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("x").forGetter(IslandPos::x),
			Codec.INT.fieldOf("z").forGetter(IslandPos::z)
	).apply(instance, IslandPos::new));

	public BlockPos getCenter() {
		return new BlockPos(x << 8, 64, z << 8);
	}
}
