/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.level.Level;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.recipe.ProcessingRecipeInput;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.crafting.recipe.RecipeUtils;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class RunicAltarRecipe implements org.violetmoon.botania.api.recipe.RunicAltarRecipe {
	public static final RecipeSerializer<RunicAltarRecipe> SERIALIZER = new RecipeSerializer<>(Serializer.CODEC, Serializer.STREAM_CODEC);
	private final ItemStackTemplate output;
	private final Ingredient reagent;
	private final NonNullList<Ingredient> ingredients;
	private final NonNullList<Ingredient> catalysts;
	private final int mana;
	// Datapack decoding runs before tag contents are bound.
	private final Supplier<Boolean> distinctIngredientRoles = Suppliers.memoize(this::validateItemRoles);

	public RunicAltarRecipe(ItemStack output, Ingredient reagent, int mana, Ingredient[] ingredients, Ingredient[] catalysts) {
		this(ItemStackTemplate.fromNonEmptyStack(output), reagent, mana, ingredients, catalysts);
	}

	public RunicAltarRecipe(ItemStackTemplate output, Ingredient reagent, int mana, Ingredient[] ingredients, Ingredient[] catalysts) {
		int numIngredients = ingredients.length;
		int numCatalysts = catalysts.length;
		Preconditions.checkArgument(numIngredients + numCatalysts > 0, "Must have at least one ingredient or catalyst");
		Preconditions.checkArgument(numIngredients + numCatalysts <= 16, "Cannot have more than 16 ingredients and/or catalysts");
		validateIngredients(ingredients, catalysts, reagent);
		this.output = output;
		this.reagent = reagent;
		this.ingredients = NonNullList.of(null, ingredients);
		this.catalysts = NonNullList.of(null, catalysts);
		this.mana = mana;
	}

	private void validateIngredients(Ingredient[] ingredients, Ingredient[] catalysts, Ingredient reagent) {
		// let's just entirely avoid this big headache
		var customTestIngredients =
				Stream.concat(Stream.concat(Stream.of(ingredients), Stream.of(catalysts)), Stream.of(reagent))
						.filter(XplatAbstractions.instance()::requiresCustomTesting).toList();
		if (!customTestIngredients.isEmpty()) {
			throw new IllegalArgumentException("Ingredients with custom test implementations are not supported: " + customTestIngredients);
		}

		// fallback check: test ingredient, catalyst and reagent definitions against each other directly
		List<Ingredient> ingredientList = List.of(ingredients);
		List<Ingredient> catalystsList = List.of(catalysts);
		if (ingredientList.contains(reagent) || catalystsList.contains(reagent)) {
			throw new IllegalArgumentException("Reagent definition must not occur in ingredients or catalysts: " + reagent);
		}
		if (Stream.of(catalysts).anyMatch(ingredientList::contains)) {
			throw new IllegalArgumentException("Ingredients and catalysts must not contain identical entries: %s/%s"
					.formatted(ingredientList, catalystsList));
		}

	}

	private boolean validateItemRoles() {
		var ingredientItems = new ReferenceOpenHashSet<Item>(ingredients.size());
		var catalystItems = new ReferenceOpenHashSet<Item>(catalysts.size());
		var reagentItems = new ReferenceOpenHashSet<Item>(1);
		ingredients.stream().flatMap(Ingredient::items)
				.map(Holder::value).forEach(ingredientItems::add);
		catalysts.stream().flatMap(Ingredient::items)
				.map(Holder::value).forEach(catalystItems::add);
		reagent.items().map(Holder::value).forEach(reagentItems::add);

		var ingredientsOverlap = ingredientItems.clone();
		ingredientsOverlap.retainAll(catalystItems);
		if (!ingredientsOverlap.isEmpty()) {
			BotaniaAPI.LOGGER.error("Ignoring runic altar recipe for {}. " + "The following item types can match both as ingredient and as catalyst: " + "{}", output, ingredientsOverlap);
			return false;
		}

		ingredientItems.retainAll(reagentItems);
		if (!ingredientItems.isEmpty()) {
			BotaniaAPI.LOGGER.error("Ignoring runic altar recipe for {}. " + "The following item types can match both as ingredient and as reagent: " + "{}", output, ingredientItems);
			return false;
		}

		catalystItems.retainAll(reagentItems);
		if (!catalystItems.isEmpty()) {
			BotaniaAPI.LOGGER.error("Ignoring runic altar recipe for {}. " + "The following item types can match both as catalyst and as reagent: " + "{}", output, catalystItems);
			return false;
		}

		return true;
	}

	private static RunicAltarRecipe of(List<Ingredient> ingredients, List<Ingredient> catalysts, Ingredient reagent, int mana, ItemStackTemplate output) {
		return new RunicAltarRecipe(output, reagent, mana, ingredients.toArray(Ingredient[]::new), catalysts.toArray(Ingredient[]::new));
	}

	@Override
	public boolean matches(ProcessingRecipeInput input, Level world) {
		if (input.size() != ingredients.size() + catalysts.size()) {
			return false;
		}
		return distinctIngredientRoles.get() && input.getStackedContents().canCraft(RecipeUtils.getIngredientsAndCatalysts(this), null);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(ProcessingRecipeInput input) {
		Set<Holder<Item>> catalystItems = new HashSet<>();
		if (!catalysts.isEmpty()) {
			// we know ingredients and catalysts are disjoined sets of item types, so just try matching all catalysts
			input.getStackedContents().canCraft(catalysts, catalystItems::add);
		}
		NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
		for (int slot = 0; slot < input.size(); slot++) {
			ItemStack inputStack = input.getItem(slot);
			if (catalystItems.contains(inputStack.typeHolder())) {
				remainingItems.set(slot, inputStack);
			} else {
				ItemStack craftRemainder = XplatAbstractions.instance().getCraftingRemainingItem(inputStack);
				if (!craftRemainder.isEmpty()) {
					remainingItems.set(slot, craftRemainder);
				}
			}
		}

		return remainingItems;
	}

	@Override
	public final ItemStack getResultItem(HolderLookup.Provider registries) {
		return output.create();
	}

	@Override
	public ItemStack assemble(ProcessingRecipeInput inv) {
		return output.create();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= ingredients.size() + catalysts.size();
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public NonNullList<Ingredient> getCatalysts() {
		return catalysts;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.RUNIC_ALTAR);
	}

	@Override
	public RecipeSerializer<? extends RunicAltarRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public int getMana() {
		return mana;
	}

	public ItemStack getOutput() {
		return output.create();
	}

	@Override
	public Ingredient getReagent() {
		return reagent;
	}

	public ItemStackTemplate outputTemplate() {
		return output;
	}

	public static final class Serializer {
		private record RecipeData(List<Ingredient> ingredients, List<Ingredient> catalysts, Ingredient reagent, int mana, ItemStackTemplate output) {
			private DataResult<RunicAltarRecipe> decode() {
				try {
					return DataResult.success(of(ingredients, catalysts, reagent, mana, output));
				} catch (IllegalArgumentException exception) {
					return DataResult.error(exception::getMessage);
				}
			}

			private static RecipeData encode(RunicAltarRecipe recipe) {
				return new RecipeData(recipe.ingredients, recipe.catalysts, recipe.reagent, recipe.mana, recipe.output);
			}
		}

		public static final MapCodec<RunicAltarRecipe> CODEC = RecordCodecBuilder.<RecipeData>mapCodec(instance -> instance.group(
				Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(RecipeData::ingredients),
				Ingredient.CODEC.listOf().fieldOf("catalysts").forGetter(RecipeData::catalysts),
				Ingredient.CODEC.fieldOf("reagent").forGetter(RecipeData::reagent),
				ExtraCodecs.POSITIVE_INT.fieldOf("mana").forGetter(RecipeData::mana),
				ItemStackTemplate.CODEC.fieldOf("output").forGetter(RecipeData::output)
		).apply(instance, RecipeData::new)).flatXmap(RecipeData::decode, recipe -> DataResult.success(RecipeData.encode(recipe)));
		public static final StreamCodec<RegistryFriendlyByteBuf, RunicAltarRecipe> STREAM_CODEC = StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RunicAltarRecipe::getIngredients,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RunicAltarRecipe::getCatalysts,
				Ingredient.CONTENTS_STREAM_CODEC, RunicAltarRecipe::getReagent,
				ByteBufCodecs.VAR_INT, RunicAltarRecipe::getMana,
				ItemStackTemplate.STREAM_CODEC, RunicAltarRecipe::outputTemplate,
				RunicAltarRecipe::of
		);

	}
}
