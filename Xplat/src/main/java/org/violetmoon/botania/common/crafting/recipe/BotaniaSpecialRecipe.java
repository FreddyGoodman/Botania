package org.violetmoon.botania.common.crafting.recipe;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Function;

abstract class BotaniaSpecialRecipe extends CustomRecipe {
	private final CraftingBookCategory category;

	protected BotaniaSpecialRecipe(CraftingBookCategory category) {
		this.category = category;
	}

	@Override
	public final CraftingBookCategory category() {
		return category;
	}

	protected static <T extends BotaniaSpecialRecipe> RecipeSerializer<T> serializer(
			Function<CraftingBookCategory, T> factory) {
		MapCodec<T> codec = CraftingBookCategory.CODEC.optionalFieldOf("category", CraftingBookCategory.MISC)
				.xmap(factory, BotaniaSpecialRecipe::category);
		return new RecipeSerializer<>(codec, StreamCodec.composite(
				CraftingBookCategory.STREAM_CODEC, BotaniaSpecialRecipe::category, factory));
	}
}
