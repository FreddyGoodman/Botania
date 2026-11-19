/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.common.crafting.recipe;


import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.mixin.ShapelessRecipeAccessor;


public class LexiconReturningShapelessRecipe extends ShapelessRecipe {
	public static final WrappingRecipeSerializer<ShapelessRecipe, LexiconReturningShapelessRecipe> SERIALIZER =
			WrappingRecipeSerializer.of(ShapelessRecipe.class, ShapelessRecipe.SERIALIZER, LexiconReturningShapelessRecipe::new);

	private LexiconReturningShapelessRecipe(ShapelessRecipe recipe) {
		super(new Recipe.CommonInfo(recipe.showNotification()),
				new CraftingRecipe.CraftingBookInfo(recipe.category(), recipe.group()),
				((ShapelessRecipeAccessor) recipe).botania_getResult(),
				((ShapelessRecipeAccessor) recipe).botania_getIngredients());
	}

	@Override
	public ItemStack assemble(CraftingInput inv) {
		return ManaUpgradeRecipe.output(super.assemble(inv), inv);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); i++) {
			ItemStack itemStack = input.getItem(i);
			Item item = itemStack.getItem();
			if (item.hasCraftingRemainingItem()) {
				nonnulllist.set(i, new ItemStack(item.getCraftingRemainingItem()));
			} else if (itemStack.is(BotaniaItems.LEXICA_BOTANIA)) {
				nonnulllist.set(i, itemStack.copy());
			}
		}

		return nonnulllist;
	}

	@Override
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return SERIALIZER.serializer();
	}

}
