/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.integration.rei;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.violetmoon.botania.api.recipe.BotaniaRecipe;
import org.violetmoon.botania.client.patchouli.PatchouliUtils;

import java.util.Collections;
import java.util.List;

public abstract class BotaniaRecipeDisplay<T extends BotaniaRecipe<?>> implements RecipeBackedDisplay {
	protected final RecipeHolder<? extends T> recipe;
	protected List<EntryIngredient> inputs;
	protected EntryIngredient outputs;

	public BotaniaRecipeDisplay(RecipeHolder<? extends T> recipe) {
		this.recipe = recipe;
		var level = Minecraft.getInstance().level;
		this.inputs = level == null ? List.of() : recipe.value().getIngredients().stream()
				.map(ingredient -> EntryIngredients.ofItemStacks(PatchouliUtils.displayStacks(ingredient, level)))
				.toList();
		var registries = level == null ? RegistryAccess.EMPTY : level.registryAccess();
		this.outputs = EntryIngredients.of(recipe.value().getResultItem(registries));
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return this.inputs;
	}

	abstract public int getManaCost();

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Collections.singletonList(this.outputs);
	}

	@Override
	public RecipeHolder<? extends T> recipeHolder() {
		return recipe;
	}
}
