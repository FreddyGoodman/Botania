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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.violetmoon.botania.common.block.mana.ManaSpreaderBlock;
import org.violetmoon.botania.common.helper.ColorHelper;
import org.violetmoon.botania.mixin.ShapelessRecipeAccessor;

public class ShapelessUncoverSpreaderRecipe extends ShapelessRecipe {
	public static final WrappingRecipeSerializer<ShapelessRecipe, ShapelessUncoverSpreaderRecipe> SERIALIZER =
			WrappingRecipeSerializer.of(ShapelessRecipe.class, ShapelessRecipe.SERIALIZER, ShapelessUncoverSpreaderRecipe::new);

	private ShapelessUncoverSpreaderRecipe(ShapelessRecipe recipe) {
		super(new Recipe.CommonInfo(recipe.showNotification()),
				new CraftingRecipe.CraftingBookInfo(recipe.category(), recipe.group()),
				((ShapelessRecipeAccessor) recipe).botania_getResult(),
				((ShapelessRecipeAccessor) recipe).botania_getIngredients());
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (int i = 0; i < nonnulllist.size(); i++) {
			Item item = input.getItem(i).getItem();
			if (item instanceof BlockItem blockItem
					&& blockItem.getBlock() instanceof ManaSpreaderBlock manaSpreaderBlock
					&& manaSpreaderBlock.getOptionalColor().isPresent()) {
				nonnulllist.set(i, new ItemStack(ColorHelper.WOOL_MAP.apply(manaSpreaderBlock.getOptionalColor().get())));
			}
		}

		return nonnulllist;
	}

	@Override
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return SERIALIZER.serializer();
	}

}
