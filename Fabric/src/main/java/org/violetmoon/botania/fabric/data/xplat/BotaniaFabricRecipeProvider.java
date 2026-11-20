/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */
package org.violetmoon.botania.fabric.data.xplat;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public abstract class BotaniaFabricRecipeProvider extends RecipeProvider {
	private final ConditionalOutput conditionalOutput;

	protected BotaniaFabricRecipeProvider(HolderLookup.Provider registries, RecipeOutput output, ConditionalOutput conditionalOutput) {
		super(registries, output);
		this.conditionalOutput = conditionalOutput;
	}

	protected RecipeOutput withConditions(RecipeOutput output, ResourceCondition... conditions) {
		return conditionalOutput.apply(output, conditions);
	}

	public static void add(FabricDataGenerator.Pack pack, String name, Factory factory) {
		pack.addProvider((output, registries) -> new FabricRecipeProvider(output, registries) {
			@Override
			protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput recipes) {
				return factory.create(registries, recipes, this::withConditions);
			}

			@Override
			public String getName() {
				return name;
			}
		});
	}

	@FunctionalInterface
	public interface Factory {
		BotaniaFabricRecipeProvider create(HolderLookup.Provider registries, RecipeOutput output, ConditionalOutput conditionalOutput);
	}

	@FunctionalInterface
	public interface ConditionalOutput {
		RecipeOutput apply(RecipeOutput output, ResourceCondition... conditions);
	}
}
