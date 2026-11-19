/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.test.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.brew.BrewContainer;
import org.violetmoon.botania.api.recipe.BotaniaRecipe;
import org.violetmoon.botania.api.recipe.BotanicalBreweryRecipe;
import org.violetmoon.botania.api.recipe.ElvenTradeRecipe;
import org.violetmoon.botania.api.recipe.OrechidRecipe;
import org.violetmoon.botania.api.recipe.PureDaisyRecipe;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.common.crafting.LexiconElvenTradeRecipe;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests the consistency of recipe output item rarities using several rules.
 */
public class RarityConsistencyTest {
	private static final Set<RecipeType<?>> RARITY_UPGRADE_TYPES = Set.of(BotaniaRecipeTypes.TERRA_PLATE_TYPE);
	private static final Set<RecipeSerializer<?>> RARITY_UPGRADE_SERIALIZERS = Set.of(LexiconElvenTradeRecipe.SERIALIZER);
	private static final List<Rarity> ORDERED_RARITIES = List.of(Rarity.values());
	private static final Map<Item, Rarity> RARITY_OVERRIDES = Map.of(
			Items.GOLDEN_APPLE, Rarity.COMMON
	);

	public void checkRecipeOutputConsistency(GameTestHelper helper) {
		boolean failedAnyChecks = false;
		Set<Item> craftableItems = new HashSet<>();
		for (RecipeHolder<?> recipeHolder : helper.getLevel().recipeAccess().getRecipes()) {
			Rarity highestIngredientRarity = Rarity.COMMON;
			Set<ItemStack> highestRarityIngredients = new LinkedHashSet<>();
			Recipe<?> recipe = recipeHolder.value();
			Collection<ItemStack> resultItems = getResultItems(helper, recipe);
			if (resultItems.isEmpty() && recipeHolder.id().identifier().getNamespace().equals(BotaniaAPI.MODID)) {
				BotaniaAPI.LOGGER.warn("Empty recipe output for {} ({})", recipeHolder, recipe.getType());
			}
			for (ItemStack resultItem : resultItems) {
				craftableItems.add(resultItem.getItem());

				if (!resultItem.typeHolder().unwrapKey().orElseThrow().identifier().getNamespace()
						.equals(BotaniaAPI.MODID)) {
					// only check recipes that make Botania items
					continue;
				}
				var ingredients = recipe instanceof BotaniaRecipe<?> botaniaRecipe ? botaniaRecipe.getIngredients() : recipe.placementInfo().ingredients();
				for (Ingredient ingredient : ingredients) {
					var items = ingredient.items().map(ItemStack::new).toList();
					Rarity ingredientRarity = items.stream()
							.map(this::getRarity)
							.min(Comparator.naturalOrder())
							.orElse(Rarity.COMMON);
					int rarityDifference = ingredientRarity.compareTo(highestIngredientRarity);
					if (rarityDifference > 0) {
						highestIngredientRarity = ingredientRarity;
						highestRarityIngredients.clear();
						highestRarityIngredients.addAll(items);
					} else if (rarityDifference == 0) {
						highestRarityIngredients.addAll(items);
					}
				}
				Rarity outputRarity = resultItem.getRarity();
				int rarityDifference = compareRarities(outputRarity, highestIngredientRarity);

				if (rarityDifference !=
						(RARITY_UPGRADE_TYPES.contains(recipe.getType()) ||
								RARITY_UPGRADE_SERIALIZERS.contains(recipe.getSerializer()) ? 1 : 0)) {
					BotaniaAPI.LOGGER.error(
							"Item {} obtained from recipe {} ({}) has rarity {} while the highest ingredient rarity is {} ({})",
							resultItem.getItem(), recipeHolder, recipe.getType(), outputRarity, highestIngredientRarity,
							highestRarityIngredients);
					failedAnyChecks = true;
				} else if (rarityDifference != 0) {
					BotaniaAPI.LOGGER.info("Rarity of {} is changed by recipe {} ({}) to {} from {} ({})",
							resultItem.getItem(), recipeHolder, recipe.getType(), outputRarity, highestIngredientRarity,
							highestRarityIngredients);
				}
			}
		}

		for (Item item : BuiltInRegistries.ITEM) {
			if (craftableItems.contains(item)) {
				continue;
			}
			if (!BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(BotaniaAPI.MODID)) {
				continue;
			}

			if (new ItemStack(item).getRarity() == Rarity.COMMON) {
				BotaniaAPI.LOGGER.warn("Uncraftable common item: {}", item);
				//failedAnyChecks = true;
			}
		}

		helper.assertFalse(failedAnyChecks, "There are inconsistent rarities. See log for details.");
		helper.succeed();
	}

	private Rarity getRarity(ItemStack stack) {
		return RARITY_OVERRIDES.getOrDefault(stack.getItem(), stack.getRarity());
	}

	private static Collection<ItemStack> getResultItems(GameTestHelper helper, Recipe<?> recipe) {
		switch (recipe) {
			case ElvenTradeRecipe tradeRecipe -> {
				return tradeRecipe.getOutputs();
			}
			case OrechidRecipe orechidRecipe -> {
				return orechidRecipe.getOutput().getDisplayedStacks();
			}
			case BotanicalBreweryRecipe brewRecipe -> {
				return BuiltInRegistries.ITEM.stream()
						.filter(BrewContainer.class::isInstance)
						.filter(item -> BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(BotaniaAPI.MODID))
						.map(ItemStack::new)
						.map(brewRecipe::getOutput)
						.toList();
			}
			case PureDaisyRecipe pureDaisyRecipe -> {
				return pureDaisyRecipe.getOutput().getDisplayedStacks();
			}
			case BotaniaRecipe<?> botaniaRecipe -> {
				ItemStack result = botaniaRecipe.getResultItem(helper.getLevel().registryAccess());
				return result.isEmpty() ? List.of() : List.of(result);
			}
			default -> {
				var context = SlotDisplayContext.fromLevel(helper.getLevel());
				return recipe.display().stream()
						.flatMap(display -> display.result().resolveForStacks(context).stream())
						.toList();
			}
		}
	}

	private static int compareRarities(Rarity a, Rarity b) {
		return ORDERED_RARITIES.indexOf(a) - ORDERED_RARITIES.indexOf(b);
	}
}
