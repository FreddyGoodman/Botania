/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting.recipe;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.violetmoon.botania.mixin.ShapelessRecipeAccessor;


public class ShapelessManaUpgradeRecipe extends ShapelessRecipe {
	public static final WrappingRecipeSerializer<ShapelessRecipe, ShapelessManaUpgradeRecipe> SERIALIZER =
			WrappingRecipeSerializer.of(ShapelessRecipe.class, ShapelessRecipe.SERIALIZER, ShapelessManaUpgradeRecipe::new);

	private ShapelessManaUpgradeRecipe(ShapelessRecipe recipe) {
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
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return SERIALIZER.serializer();
	}

}
