/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting.recipe;


import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import org.violetmoon.botania.common.block.ShimmeringMushroomBlock;
import org.violetmoon.botania.common.item.WandOfTheForestItem;
import org.violetmoon.botania.common.item.material.MysticalPetalItem;
import org.violetmoon.botania.mixin.ShapedRecipeAccessor;


public class WandOfTheForestRecipe extends ShapedRecipe {
	public static final WrappingRecipeSerializer<ShapedRecipe, WandOfTheForestRecipe> SERIALIZER =
			WrappingRecipeSerializer.of(ShapedRecipe.class, ShapedRecipe.SERIALIZER, WandOfTheForestRecipe::new);

	private WandOfTheForestRecipe(ShapedRecipe recipe) {
		super(new Recipe.CommonInfo(recipe.showNotification()),
				new CraftingRecipe.CraftingBookInfo(recipe.category(), recipe.group()),
				((ShapedRecipeAccessor) recipe).botania_getPattern(),
				((ShapedRecipeAccessor) recipe).botania_getResult());
	}

	@Override
	public ItemStack assemble(CraftingInput inv) {
		DyeColor first = null;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			Item item = stack.getItem();

			DyeColor colorId;
			if (item instanceof MysticalPetalItem petal) {
				colorId = petal.color;
			} else if (item instanceof BlockItem block && block.getBlock() instanceof ShimmeringMushroomBlock mushroom) {
				colorId = mushroom.color;
			} else {
				continue;
			}
			if (first == null) {
				first = colorId;
			} else {
				return WandOfTheForestItem.setColors(super.assemble(inv), first, colorId);
			}
		}
		return WandOfTheForestItem.setColors(super.assemble(inv), first != null ? first : DyeColor.WHITE, DyeColor.WHITE);
	}

	@Override
	public RecipeSerializer<ShapedRecipe> getSerializer() {
		return SERIALIZER.serializer();
	}

}
