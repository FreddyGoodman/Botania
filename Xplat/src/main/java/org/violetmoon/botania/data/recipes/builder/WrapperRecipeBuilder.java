/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package org.violetmoon.botania.data.recipes.builder;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import org.violetmoon.botania.common.crafting.recipe.WrappingRecipeSerializer;

public class WrapperRecipeBuilder<S extends Recipe<?>, T extends S> {
	private final WrappingRecipeSerializer<S, T> serializer;
	private final RecipeBuilder recipeBuilder;

	public WrapperRecipeBuilder(WrappingRecipeSerializer<S, T> serializer, RecipeBuilder recipeBuilder) {
		this.recipeBuilder = recipeBuilder;
		this.serializer = serializer;
	}

	public static <S extends Recipe<?>, T extends S> WrapperRecipeBuilder<S, T> wrap(WrappingRecipeSerializer<S, T> serializer,
			RecipeBuilder recipeBuilder) {
		return new WrapperRecipeBuilder<>(serializer, recipeBuilder);
	}

	public void save(RecipeOutput recipeOutput) {
		CapturingRecipeOutput capturingRecipeOutput = new CapturingRecipeOutput(recipeOutput);
		var output = capturingRecipeOutput.captureSave(recipeBuilder::save);

		T wrappedRecipe = serializer.wrap(output.getMiddle());
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, output.getLeft()), wrappedRecipe, output.getRight().orElse(null));
	}

	public void save(RecipeOutput recipeOutput, Identifier id) {
		CapturingRecipeOutput capturingRecipeOutput = new CapturingRecipeOutput(recipeOutput);
		var output = capturingRecipeOutput.captureSave(out -> recipeBuilder.save(out, ResourceKey.create(Registries.RECIPE, id)));

		T wrappedRecipe = serializer.wrap(output.getMiddle());
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, output.getLeft()), wrappedRecipe, output.getRight().orElse(null));
	}
}
