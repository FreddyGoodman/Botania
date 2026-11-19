/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.api.recipe;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public interface RunicAltarRecipe extends RecipeWithReagent<ProcessingRecipeInput>, RecipeWithCatalysts<ProcessingRecipeInput> {
	Identifier TYPE_ID = botaniaRL("runic_altar");
	Identifier HEAD_TYPE_ID = botaniaRL("runic_altar_head");

	int getMana();

	@Override
	default RecipeType<? extends Recipe<ProcessingRecipeInput>> getType() {
		return BotaniaRecipe.registeredType(TYPE_ID);
	}
}
