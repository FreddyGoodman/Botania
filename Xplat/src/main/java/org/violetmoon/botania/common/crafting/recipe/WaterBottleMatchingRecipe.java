/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting.recipe;


import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

import org.violetmoon.botania.mixin.ShapedRecipeAccessor;

import java.util.ArrayList;
import java.util.List;

public class WaterBottleMatchingRecipe extends ShapedRecipe {
	public static final WrappingRecipeSerializer<ShapedRecipe, WaterBottleMatchingRecipe> SERIALIZER =
			WrappingRecipeSerializer.of(ShapedRecipe.class, ShapedRecipe.SERIALIZER, WaterBottleMatchingRecipe::new);

	public WaterBottleMatchingRecipe(String group, CraftingBookCategory category, ShapedRecipePattern pattern, ItemStack result) {
		super(new Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(category, group),
				pattern, ItemStackTemplate.fromNonEmptyStack(result));
	}

	private WaterBottleMatchingRecipe(ShapedRecipe recipe) {
		super(new Recipe.CommonInfo(recipe.showNotification()),
				new CraftingRecipe.CraftingBookInfo(recipe.category(), recipe.group()),
				((ShapedRecipeAccessor) recipe).botania_getPattern(),
				((ShapedRecipeAccessor) recipe).botania_getResult());
	}

	@Override
	public List<RecipeDisplay> display() {
		var pattern = ((ShapedRecipeAccessor) this).botania_getPattern();
		var ingredients = pattern.ingredients();
		var plainPotion = new ItemStack(Items.POTION);
		var water = ItemStackTemplate.fromNonEmptyStack(PotionContents.createItemStack(Items.POTION, Potions.WATER));
		return super.display().stream().map(display -> {
			if (!(display instanceof ShapedCraftingRecipeDisplay shaped)) {
				return display;
			}
			var slots = new ArrayList<>(shaped.ingredients());
			for (int i = 0; i < ingredients.size(); ++i) {
				var ingredient = ingredients.get(i);
				if (ingredient.isPresent() && ingredient.get().test(plainPotion)) {
					slots.set(i, new SlotDisplay.ItemStackSlotDisplay(water));
				}
			}
			return new ShapedCraftingRecipeDisplay(shaped.width(), shaped.height(), slots, shaped.result(), shaped.craftingStation());
		}).toList();
	}

	@Override
	public boolean matches(CraftingInput craftingContainer, Level level) {
		if (!super.matches(craftingContainer, level)) {
			return false;
		}
		for (int i = 0; i < craftingContainer.size(); i++) {
			var item = craftingContainer.getItem(i);
			if (item.is(Items.POTION) && !item.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public RecipeSerializer<ShapedRecipe> getSerializer() {
		return SERIALIZER.serializer();
	}

}
