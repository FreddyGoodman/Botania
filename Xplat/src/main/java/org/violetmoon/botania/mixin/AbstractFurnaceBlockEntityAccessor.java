/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface AbstractFurnaceBlockEntityAccessor {
	@Invoker("canBurn")
	static boolean botania_canBurn(NonNullList<ItemStack> items, int containerLimit, ItemStack result) {
		throw new AssertionError();
	}

	@Accessor("items")
	NonNullList<ItemStack> botania_getItems();

	@Accessor("quickCheck")
	RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> botania_getQuickCheck();

	@Accessor("litTimeRemaining")
	int botania_getLitTime();

	@Accessor("litTimeRemaining")
	void botania_setLitTime(int burnTime);

	@Accessor("cookingTimer")
	int botania_getCookingProgress();

	@Accessor("cookingTotalTime")
	int botania_getCookingTotalTime();

	@Accessor("cookingTimer")
	void botania_setCookingProgress(int cookTime);
}
