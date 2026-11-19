/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.handler;

import com.google.common.collect.ImmutableList;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.recipe.OrechidRecipe;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.function.ToIntFunction;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class OrechidManager implements ResourceManagerReloadListener {
	private static final Map<RecipeMap, Cache> CACHE = Collections.synchronizedMap(new WeakHashMap<>());

	private static class Cache {
		final Map<RecipeType<? extends OrechidRecipe>, Map<BlockState, List<? extends OrechidRecipe>>> byType = new IdentityHashMap<>();
		final Map<RecipeType<? extends OrechidRecipe>, Object2IntOpenHashMap<BlockState>> displayWeights = new IdentityHashMap<>();
		final Map<RecipeType<? extends OrechidRecipe>, IntIntPair> manaCostRanges = new IdentityHashMap<>();
	}

	private static Cache cache(RecipeMap recipes) {
		return CACHE.computeIfAbsent(recipes, _ -> new Cache());
	}

	public static void registerListener() {
		XplatAbstractions.INSTANCE.registerReloadListener(PackType.SERVER_DATA, botaniaRL("orechid"), new OrechidManager());
	}

	@Override
	public void onResourceManagerReload(ResourceManager manager) {
		CACHE.clear();
	}

	public static <T extends OrechidRecipe> Collection<T> getMatchingRecipes(
			Level level,
			RecipeType<T> type,
			BlockState state) {
		var recipes = BotaniaRecipeTypes.recipes(level);
		final var byState = cache(recipes).byType.computeIfAbsent(type, t -> new IdentityHashMap<>());
		final var list = byState.computeIfAbsent(state, s -> {
			var builder = ImmutableList.<T>builder();
			for (var recipe : recipes.byType(type)) {
				if (recipe.value().getInput().test(state)) {
					builder.add(recipe.value());
				}
			}
			return builder.build();
		});

		@SuppressWarnings("unchecked") // we only add T's to this list in the above loop
		List<T> result = (List<T>) list;
		return result;
	}

	public static int getTotalDisplayWeightAt(Level level, RecipeType<? extends OrechidRecipe> type, BlockState state, @Nullable BlockPos pos) {
		return pos == null
				? getCachedTotalDisplayWeightWithoutPosition(level, type, state)
				: calculateTotalDisplayWeightAtPosition(level, type, state, pos);
	}

	public static int getMinManaCost(Level level, RecipeType<? extends OrechidRecipe> type) {
		return cache(BotaniaRecipeTypes.recipes(level)).manaCostRanges.computeIfAbsent(type, t -> calculateManaCostRange(level, t)).firstInt();
	}

	public static int getMaxManaCost(Level level, RecipeType<? extends OrechidRecipe> type) {
		return cache(BotaniaRecipeTypes.recipes(level)).manaCostRanges.computeIfAbsent(type, t -> calculateManaCostRange(level, t)).secondInt();
	}

	private static int getCachedTotalDisplayWeightWithoutPosition(Level level, RecipeType<? extends OrechidRecipe> type, BlockState state) {
		final var byState = cache(BotaniaRecipeTypes.recipes(level)).displayWeights.computeIfAbsent(type, t -> new Object2IntOpenHashMap<>());
		return byState.computeIfAbsent(state, s -> calculateTotalDisplayWeightAtPosition(level, type, state, null));
	}

	private static int calculateTotalDisplayWeightAtPosition(Level level, RecipeType<? extends OrechidRecipe> type, BlockState state, @Nullable BlockPos pos) {
		final var recipeList = getMatchingRecipes(level, type, state);
		if (recipeList.isEmpty()) {
			return 0;
		}

		ToIntFunction<OrechidRecipe> weightFunction = pos != null
				? r -> r.getWeight(level, pos)
				: OrechidRecipe::getWeight;
		return recipeList.stream().mapToInt(weightFunction).sum();
	}

	private static IntIntPair calculateManaCostRange(Level level, RecipeType<? extends OrechidRecipe> type) {
		final var recipes = BotaniaRecipeTypes.recipes(level).byType(type);
		return IntIntPair.of(
				recipes.stream().map(RecipeHolder::value).mapToInt(OrechidRecipe::getManaCost).min().orElse(0),
				recipes.stream().map(RecipeHolder::value).mapToInt(OrechidRecipe::getManaCost).max().orElse(0));
	}

}
