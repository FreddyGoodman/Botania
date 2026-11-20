/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.integration.rei;

import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.recipe.*;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;

import java.util.Optional;

interface RecipeBackedDisplay extends Display {
	DisplaySerializer<RecipeBackedDisplay> SERIALIZER = DisplaySerializer.of(
			RecordCodecBuilder.mapCodec(instance -> instance.group(
					Recipe.KEY_CODEC.fieldOf("id").forGetter((RecipeBackedDisplay display) -> display.recipeHolder().id()),
					Recipe.CODEC.fieldOf("recipe").forGetter((RecipeBackedDisplay display) -> display.recipeHolder().value())
			).apply(instance, RecipeBackedDisplay::fromRecipe)),
			RecipeHolder.STREAM_CODEC.map(holder -> fromRecipe(holder.id(), holder.value()), RecipeBackedDisplay::recipeHolder),
			false);

	RecipeHolder<?> recipeHolder();

	@Override
	default Optional<Identifier> getDisplayLocation() {
		return Optional.of(recipeHolder().id().identifier());
	}

	@Override
	default DisplaySerializer<RecipeBackedDisplay> getSerializer() {
		return SERIALIZER;
	}

	private static RecipeBackedDisplay fromRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) {
		RecipeBackedDisplay display = create(new RecipeHolder<>(id, recipe));
		if (display == null) {
			throw new IllegalArgumentException("Unsupported Botania recipe display: " + id.identifier());
		}
		return display;
	}

	@Nullable
	static RecipeBackedDisplay create(RecipeHolder<?> holder) {
		var id = holder.id();
		return switch (holder.value()) {
			case PetalApothecaryRecipe recipe -> new PetalApothecaryREIDisplay(new RecipeHolder<>(id, recipe));
			case BotanicalBreweryRecipe recipe -> new BreweryREIDisplay(new RecipeHolder<>(id, recipe));
			case ElvenTradeRecipe recipe -> new ElvenTradeREIDisplay(new RecipeHolder<>(id, recipe));
			case ManaInfusionRecipe recipe -> new ManaPoolREIDisplay(new RecipeHolder<>(id, recipe));
			case PureDaisyRecipe recipe -> new PureDaisyREIDisplay(new RecipeHolder<>(id, recipe));
			case RunicAltarRecipe recipe -> new RunicAltarREIDisplay(new RecipeHolder<>(id, recipe));
			case TerrestrialAgglomerationRecipe recipe -> new TerrestrialAgglomerationREIDisplay(new RecipeHolder<>(id, recipe));
			case OrechidRecipe recipe -> createOrechidDisplay(new RecipeHolder<>(id, recipe));
			default -> null;
		};
	}

	private static RecipeBackedDisplay createOrechidDisplay(RecipeHolder<OrechidRecipe> recipe) {
		RecipeType<?> type = recipe.value().getType();
		if (type == BotaniaRecipeTypes.ORECHID_TYPE) {
			return new OrechidREIDisplay(recipe);
		}
		if (type == BotaniaRecipeTypes.ORECHID_IGNEM_TYPE) {
			return new OrechidIgnemREIDisplay(recipe);
		}
		return new MarimorphosisREIDisplay(recipe);
	}
}
