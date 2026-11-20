/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.patchouli.processor;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.client.patchouli.PatchouliUtils;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.ArrayList;
import java.util.List;

public class MultiCraftingProcessor implements IComponentProcessor {
	@SuppressWarnings("NotNullFieldNotInitialized")
	private List<RecipeView> recipes;
	@Nullable
	private List<RecipeView> recipes2;
	private boolean shapeless = true;
	private boolean shapeless2 = true;
	private int longestIngredientSize = 0;
	private int longestIngredientSize2 = 0;
	private boolean hasCustomHeading;

	@Override
	public void setup(Level level, IVariableProvider variables) {
		RecipeData result = getRecipeData(level, variables, "recipes");
		this.recipes = result.recipes();
		this.shapeless = result.shapeless();
		this.longestIngredientSize = result.longestIngredientSize();

		if (variables.has("recipes2")) {
			RecipeData result2 = getRecipeData(level, variables, "recipes2");
			this.recipes2 = result2.recipes();
			this.shapeless2 = result2.shapeless();
			this.longestIngredientSize2 = result2.longestIngredientSize();
		} else {
			this.recipes2 = null;
		}
		this.hasCustomHeading = variables.has("heading");
	}

	private static RecipeData getRecipeData(Level level, IVariableProvider variables, String recipesVariable) {
		List<String> names = variables.get(recipesVariable, level.registryAccess()).asStream(level.registryAccess()).map(IVariable::asString).toList();
		var views = new ArrayList<RecipeView>();
		var context = SlotDisplayContext.fromLevel(level);
		for (String name : names) {
			CraftingRecipe recipe = PatchouliUtils.getRecipe(level, RecipeType.CRAFTING, Identifier.parse(name));
			if (recipe == null) {
				BotaniaAPI.LOGGER.warn("Missing crafting recipe {}", name);
				continue;
			}
			for (RecipeDisplay display : recipe.display()) {
				switch (display) {
					case ShapedCraftingRecipeDisplay shaped -> {
						views.add(new RecipeView(shaped.ingredients().stream().map(slot -> slot.resolveForStacks(context)).toList(),
								shaped.result().resolveForFirstStack(context), shaped.width()));
					}
					case ShapelessCraftingRecipeDisplay shapeless -> {
						views.add(new RecipeView(shapeless.ingredients().stream().map(slot -> slot.resolveForStacks(context)).toList(),
								shapeless.result().resolveForFirstStack(context), 0));
					}
					default -> {
					}
				}
			}
		}
		boolean shapeless = views.stream().allMatch(view -> view.width == 0);
		int longest = views.stream().flatMap(view -> view.slots.stream()).mapToInt(List::size).max().orElse(0);
		return new RecipeData(views, shapeless, longest);
	}

	private record RecipeView(List<List<ItemStack>> slots, ItemStack result, int width) {
		List<ItemStack> inputAt(int index) {
			int column = index % 3;
			if (width != 0 && column >= width) {
				return List.of();
			}
			int slot = width == 0 ? index : index / 3 * width + column;
			return slot >= 0 && slot < slots.size() ? slots.get(slot) : List.of();
		}
	}

	private record RecipeData(List<RecipeView> recipes, boolean shapeless, int longestIngredientSize) {
	}

	@SuppressWarnings("NullableProblems")
	@Nullable
	@Override
	public IVariable process(Level level, String key) {
		if (key.equals("heading")) {
			if (!hasCustomHeading && !recipes.isEmpty()) {
				return IVariable.from(recipes.getFirst().result.getHoverName(), level.registryAccess());
			}
			return null;
		}
		IVariable recipes1Variable = processRecipes(level, key, recipes, "input", "output", "shapeless", longestIngredientSize, shapeless);
		if (recipes1Variable != null) {
			return recipes1Variable;
		}
		if (recipes2 != null) {
			return processRecipes(level, key, recipes2, "input2_", "output2", "shapeless2", longestIngredientSize2, shapeless2);
		}
		return null;
	}

	@Nullable
	private IVariable processRecipes(Level level, String key, List<RecipeView> recipeList, String inputKeyPrefix,
			String outputKey, String shapelessKey, int size, boolean isShapeless) {
		if (recipeList.isEmpty()) {
			return null;
		}
		if (key.startsWith(inputKeyPrefix) && key.length() == inputKeyPrefix.length() + 1) {
			int index = Integer.parseInt(key.substring(inputKeyPrefix.length())) - 1;
			List<List<ItemStack>> stacks = recipeList.stream().map(recipe -> recipe.inputAt(index)).toList();
			return PatchouliUtils.interweaveStacks(stacks, size, level);
		}
		if (key.equals(outputKey)) {
			return IVariable.wrapList(recipeList.stream()
					.map(r -> IVariable.from(r.result, level.registryAccess()))
					.toList(), level.registryAccess());
		}
		if (key.equals(shapelessKey)) {
			return IVariable.wrap(isShapeless, level.registryAccess());
		}
		return null;
	}
}
