/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package org.violetmoon.botania.common.crafting.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Function;

public record WrappingRecipeSerializer<S extends Recipe<?>, T extends S>(
		Class<S> vanillaType, RecipeSerializer<S> serializer, Function<S, T> wrapper) {
	public static <S extends Recipe<?>, T extends S> WrappingRecipeSerializer<S, T> of(
			Class<S> vanillaType, RecipeSerializer<S> vanillaSerializer, Function<S, T> wrapper) {
		return new WrappingRecipeSerializer<>(vanillaType, new RecipeSerializer<>(
				vanillaSerializer.codec().xmap(wrapper, Function.identity()),
				vanillaSerializer.streamCodec().map(wrapper, Function.identity())), wrapper);
	}

	public T wrap(Recipe<?> recipe) {
		if (!vanillaType.isInstance(recipe)) {
			throw new IllegalArgumentException("Unsupported recipe type to wrap: " + recipe.getType());
		}
		return wrapper.apply(vanillaType.cast(recipe));
	}
}
