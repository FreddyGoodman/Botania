/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.api.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.Objects;

/** A recipe processed by a Botania machine or block. */
public interface BotaniaRecipe<T extends RecipeInput> extends Recipe<T> {
	default NonNullList<Ingredient> getIngredients() {
		return NonNullList.create();
	}

	ItemStack getResultItem(HolderLookup.Provider registries);

	default NonNullList<ItemStack> getRemainingItems(T input) {
		NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);
		for (int i = 0; i < input.size(); i++) {
			ItemStackTemplate remainder = input.getItem(i).getItem().getCraftingRemainder();
			if (remainder != null) {
				remainders.set(i, remainder.create());
			}
		}
		return remainders;
	}

	boolean canCraftInDimensions(int width, int height);

	default ItemStack getToastSymbol() {
		return ItemStack.EMPTY;
	}

	@Override
	default String group() {
		return "";
	}

	@Override
	default boolean showNotification() {
		return false;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}

	@Override
	default PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	default RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC;
	}

	@SuppressWarnings("unchecked")
	static <T extends RecipeInput> RecipeType<? extends Recipe<T>> registeredType(Identifier id) {
		return (RecipeType<? extends Recipe<T>>) Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.getValue(id));
	}
}
