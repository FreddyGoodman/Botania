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
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.violetmoon.botania.api.mana.ManaItem;
import org.violetmoon.botania.mixin.ShapedRecipeAccessor;

public class ManaUpgradeRecipe extends ShapedRecipe {
	public static final WrappingRecipeSerializer<ShapedRecipe, ManaUpgradeRecipe> SERIALIZER =
			WrappingRecipeSerializer.of(ShapedRecipe.class, ShapedRecipe.SERIALIZER, ManaUpgradeRecipe::new);

	private ManaUpgradeRecipe(ShapedRecipe recipe) {
		super(new Recipe.CommonInfo(recipe.showNotification()),
				new CraftingRecipe.CraftingBookInfo(recipe.category(), recipe.group()),
				((ShapedRecipeAccessor) recipe).botania_getPattern(),
				((ShapedRecipeAccessor) recipe).botania_getResult());
	}

	public static ItemStack output(ItemStack output, CraftingInput inv) {
		ItemStack out = output.copy();
		var outItem = ManaItem.LOOKUP.find(out);
		if (outItem == null) {
			return out;
		}
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			var item = ManaItem.LOOKUP.find(stack);
			if (!stack.isEmpty() && item != null) {
				outItem.addMana(item.getMana());
			}
		}
		return out;
	}

	@Override
	public ItemStack assemble(CraftingInput inv) {
		return output(super.assemble(inv), inv);
	}

	@Override
	public RecipeSerializer<ShapedRecipe> getSerializer() {
		return SERIALIZER.serializer();
	}

}
