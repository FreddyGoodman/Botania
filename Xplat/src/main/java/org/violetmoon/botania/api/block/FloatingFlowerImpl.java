/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.api.block;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.common.block.block_entity.flower.BotaniaIslandTypes;

public class FloatingFlowerImpl implements FloatingFlower {
	private IslandType type = BotaniaIslandTypes.GRASS;

	@Override
	public IslandType getIslandType() {
		return type;
	}

	@Override
	public void setIslandType(IslandType type) {
		this.type = type;
	}

	@Override
	public void save(ValueOutput output) {
		var registry = BotaniaAPI.instance().getIslandTypeRegistry();
		output.store("islandType", Identifier.CODEC, registry.getKey(type));
	}

	@Override
	public void load(ValueInput input) {
		var registry = BotaniaAPI.instance().getIslandTypeRegistry();
		input.read("islandType", Identifier.CODEC)
				.map(registry::getValue)
				.ifPresent(this::setIslandType);
	}
}
