/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.ints.Int2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.violetmoon.botania.api.recipe.ProcessingRecipeInput;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ElvenTradeRecipe implements org.violetmoon.botania.api.recipe.ElvenTradeRecipe {
	public static final RecipeSerializer<ElvenTradeRecipe> SERIALIZER = new RecipeSerializer<>(Serializer.CODEC, Serializer.STREAM_CODEC);
	private final ImmutableList<ItemStackTemplate> outputs;
	private final NonNullList<Ingredient> ingredients;

	public ElvenTradeRecipe(ItemStack[] outputs, Ingredient... ingredients) {
		this(Arrays.asList(outputs), Arrays.asList(ingredients));
	}

	public ElvenTradeRecipe(List<ItemStack> outputs, List<Ingredient> ingredients) {
		this(outputs.stream().map(ItemStackTemplate::fromNonEmptyStack).toList(), ingredients);
	}

	private ElvenTradeRecipe(Collection<ItemStackTemplate> outputs, List<Ingredient> ingredients) {
		this.outputs = ImmutableList.copyOf(outputs);
		this.ingredients = NonNullList.of(null, ingredients.toArray(Ingredient[]::new));
		validateIngredients();
	}

	private void validateIngredients() {
		// Fabric doesn't provide a way to access ingredient->input mappings for their custom matcher,
		// so by way of collective punishment, Neoforge doesn't get to use that type of ingredient either for now.
		// TODO (maybe, eventually): submit PR to Fabric API, then come back to look at this and RunicAltarRecipe
		var customTestIngredients = ingredients.stream()
				.filter(XplatAbstractions.instance()::requiresCustomTesting).toList();
		if (!customTestIngredients.isEmpty()) {
			throw new IllegalArgumentException(
					"Ingredients with custom test implementations are not supported: " + customTestIngredients);
		}
	}

	@Override
	public RecipeSerializer<ElvenTradeRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.ELVEN_GATEWAY_CORE);
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs.stream().map(ItemStackTemplate::create).toList();
	}

	@Override
	public Optional<AssemblyResult> tryAssemble(ProcessingRecipeInput input) {
		if (ingredients.size() == 1) {
			// single ingredient, map to first matching slot
			Ingredient ingredient = ingredients.getFirst();
			for (int slot = 0; slot < input.size(); slot++) {
				if (ingredient.test(input.getItem(slot))) {
					return Optional.of(new AssemblyResult(getOutputs(), slot));
				}
			}
		} else {
			List<Holder<Item>> matchedIngredientItems = new ArrayList<>(ingredients.size());
			if (input.getStackedContents().canCraft(ingredients, matchedIngredientItems::add)) {
				// TODO: figure out how to further optimize this
				Int2IntSortedMap matchedInputSlots = new Int2IntLinkedOpenHashMap(ingredients.size());
				for (int ingredientIndex = 0; ingredientIndex < ingredients.size(); ingredientIndex++) {
					Holder<Item> inputItem = matchedIngredientItems.get(ingredientIndex);
					for (int inputIndex = 0; inputIndex < input.size(); inputIndex++) {
						if (input.getItem(inputIndex).is(inputItem)) {
							int alreadyAssigned = matchedInputSlots.get(inputIndex);
							if (alreadyAssigned < input.getItem(inputIndex).getCount()) {
								matchedInputSlots.put(inputIndex, alreadyAssigned + 1);
								break;
							}
						}
					}
				}
				return Optional.of(new AssemblyResult(getOutputs(), matchedInputSlots));
			}
		}
		return Optional.empty();
	}

	public static final class Serializer {
		public static final MapCodec<ElvenTradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ExtraCodecs.nonEmptyList(ItemStackTemplate.CODEC.listOf()).fieldOf("output").forGetter(recipe -> recipe.outputs),
				ExtraCodecs.nonEmptyList(Ingredient.CODEC.listOf()).fieldOf("ingredients").forGetter(ElvenTradeRecipe::getIngredients)
		).apply(instance, ElvenTradeRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, ElvenTradeRecipe> STREAM_CODEC = StreamCodec.composite(
				ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.outputs,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), ElvenTradeRecipe::getIngredients,
				ElvenTradeRecipe::new
		);

	}
}
