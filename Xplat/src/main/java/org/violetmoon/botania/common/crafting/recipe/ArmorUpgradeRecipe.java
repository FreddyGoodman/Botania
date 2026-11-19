/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting.recipe;


import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.violetmoon.botania.mixin.ShapedRecipeAccessor;


public class ArmorUpgradeRecipe extends ShapedRecipe {
	public static final WrappingRecipeSerializer<ShapedRecipe, ArmorUpgradeRecipe> SERIALIZER =
			WrappingRecipeSerializer.of(ShapedRecipe.class, ShapedRecipe.SERIALIZER, ArmorUpgradeRecipe::new);

	private ArmorUpgradeRecipe(ShapedRecipe recipe) {
		super(new Recipe.CommonInfo(recipe.showNotification()),
				new CraftingRecipe.CraftingBookInfo(recipe.category(), recipe.group()),
				((ShapedRecipeAccessor) recipe).botania_getPattern(),
				((ShapedRecipeAccessor) recipe).botania_getResult());
	}

	@Override
	public ItemStack assemble(CraftingInput inv) {
		ItemStack out = super.assemble(inv);
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.getItem() instanceof ArmorItem) {
				// TODO: verify this works as intended
				out.applyComponents(stack.getComponentsPatch());
				break;
			}
		}
		return out;
	}

	@Override
	public RecipeSerializer<ShapedRecipe> getSerializer() {
		return SERIALIZER.serializer();
	}

}
