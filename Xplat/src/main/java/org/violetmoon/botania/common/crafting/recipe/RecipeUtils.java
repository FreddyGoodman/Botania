/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting.recipe;

import com.google.common.base.Suppliers;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.brew.BrewContainer;
import org.violetmoon.botania.api.recipe.BotaniaRecipe;
import org.violetmoon.botania.api.recipe.ProcessingRecipeInput;
import org.violetmoon.botania.api.recipe.RecipeWithCatalysts;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public final class RecipeUtils {
	/**
	 * Check if every ingredient in {@code recipe} is satisfied by an item in {@code input}.
	 *
	 * @param recipe          The recipe to match.
	 * @param input           The input items to match against.
	 * @param specialMatching Whether some of the ingredients define a dynamic item set and thus cannot be matched using
	 *                        the default method.
	 */
	public static boolean matches(BotaniaRecipe<? extends ProcessingRecipeInput> recipe, ProcessingRecipeInput input,
			boolean specialMatching) {
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		if (input.size() != ingredients.size()) {
			return false;
		}
		if (ingredients.size() == 1) {
			return ingredients.getFirst().test(input.getItem(0));
		}
		if (specialMatching) {
			return XplatAbstractions.instance().matchesWithCustomTestIngredients(input.getItems(), ingredients);
		}
		return input.getStackedContents().canCraft(ingredients, null);
	}

	/**
	 * Like the vanilla method on recipe interface, but specialHandler is called first, and if it returns
	 * nonnull, that result is used instead of vanilla's
	 */
	public static NonNullList<ItemStack> getRemainingItemsSub(RecipeInput inv, Function<ItemStack, @Nullable ItemStack> specialHandler) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		for (int i = 0; i < ret.size(); ++i) {
			ItemStack item = inv.getItem(i);
			ItemStack special = specialHandler.apply(item);
			if (special != null) {
				ret.set(i, special);
			} else {
				ItemStackTemplate remainder = item.getItem().getCraftingRemainder();
				if (remainder != null) {
					ret.set(i, remainder.create());
				}
			}
		}

		return ret;
	}

	/**
	 * Creates a {@link ProcessingRecipeInput} from the stacks in the provided item entities.
	 * Stack sizes greater than one are counted as that many input items.
	 */
	public static ProcessingRecipeInput getInputFromEntities(List<ItemEntity> itemEntities) {
		int count = itemEntities.stream().filter(Entity::isAlive).mapToInt(i -> i.getItem().getCount()).sum();
		ItemStack[] stacks = new ItemStack[count];
		int index = 0;
		for (ItemEntity entity : itemEntities) {
			ItemStack stack = entity.getItem();
			if (stack.getCount() == 1) {
				stacks[index++] = stack;
			} else {
				ItemStack copy = stack.copyWithCount(1);
				for (int i = 0; i < stack.getCount(); i++) {
					stacks[index++] = copy;
				}
			}
		}
		return new StacksProcessingRecipeInput(stacks);
	}

	/**
	 * Creates a {@link ProcessingRecipeInput} from the stacks in the provided list.
	 * Stack sizes greater than one are counted as that many input items.
	 */
	public static ProcessingRecipeInput getInputFromListWithoutUnstacking(List<ItemStack> itemStacks) {
		return new StacksProcessingRecipeInput(itemStacks.toArray(ItemStack[]::new));
	}

	/**
	 * Creates a {@link ProcessingRecipeInput} from the stacks in the provided container.
	 * Stack sizes greater than one are counted as that many input items.
	 */
	public static ProcessingRecipeInput getInputFromContainer(Container container) {
		int numSlots = container.getContainerSize();
		int count = IntStream.range(0, numSlots).map(i -> container.getItem(i).getCount()).sum();
		ItemStack[] stacks = new ItemStack[count];
		int index = 0;
		for (int slot = 0; slot < numSlots; slot++) {
			ItemStack stack = container.getItem(slot);
			if (stack.getCount() == 1) {
				stacks[index++] = stack;
			} else {
				ItemStack copy = stack.copyWithCount(1);
				for (int i = 0; i < stack.getCount(); i++) {
					stacks[index++] = copy;
				}
			}
		}
		return new StacksProcessingRecipeInput(stacks);
	}

	public static List<Ingredient> getIngredientsAndCatalysts(RecipeWithCatalysts<?> recipe) {
		if (recipe.getCatalysts().isEmpty()) {
			return recipe.getIngredients();
		}
		if (recipe.getIngredients().isEmpty()) {
			return recipe.getCatalysts();
		}
		List<Ingredient> combined = new ArrayList<>(recipe.getIngredients().size() + recipe.getCatalysts().size());
		combined.addAll(recipe.getIngredients());
		combined.addAll(recipe.getCatalysts());
		return combined;
	}

	@SuppressWarnings("SuspiciousToArrayCall")
	private static final Supplier<Item[]> BREW_CONTAINER_ITEMS_SUPPLIER = Suppliers.memoize(
			() -> BuiltInRegistries.ITEM.stream().filter(BrewContainer.class::isInstance).toArray(Item[]::new));

	/**
	 * Defines an {@link Ingredient} that matches any item implementing {@link BrewContainer}.
	 */
	public static Ingredient getBrewContainerIngredient() {
		return Ingredient.of(BREW_CONTAINER_ITEMS_SUPPLIER.get());
	}

	private RecipeUtils() {}
}
