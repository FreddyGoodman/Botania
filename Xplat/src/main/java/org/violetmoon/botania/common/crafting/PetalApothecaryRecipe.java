/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.violetmoon.botania.api.recipe.ProcessingRecipeInput;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.crafting.recipe.RecipeUtils;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.Arrays;
import java.util.List;

public class PetalApothecaryRecipe implements org.violetmoon.botania.api.recipe.PetalApothecaryRecipe {
	public static final RecipeSerializer<PetalApothecaryRecipe> SERIALIZER = new RecipeSerializer<>(Serializer.CODEC, Serializer.STREAM_CODEC);
	private final ItemStackTemplate output;
	private final Ingredient reagent;
	private final NonNullList<Ingredient> ingredients;
	private final boolean requireSpecialMatching;

	public PetalApothecaryRecipe(ItemStack output, Ingredient reagent, Ingredient... ingredients) {
		this(ItemStackTemplate.fromNonEmptyStack(output), reagent, ingredients);
	}

	private PetalApothecaryRecipe(ItemStackTemplate output, Ingredient reagent, Ingredient... ingredients) {
		this.output = output;
		this.reagent = reagent;
		this.ingredients = NonNullList.of(null, ingredients);
		this.requireSpecialMatching = Arrays.stream(ingredients).anyMatch(XplatAbstractions.instance()::requiresCustomTesting);
	}

	private static PetalApothecaryRecipe of(ItemStackTemplate output, Ingredient reagent, List<Ingredient> ingredients) {
		return new PetalApothecaryRecipe(output, reagent, ingredients.toArray(Ingredient[]::new));
	}

	@Override
	public boolean matches(ProcessingRecipeInput input, Level level) {
		return RecipeUtils.matches(this, input, requireSpecialMatching);
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
		return width * height >= ingredients.size();
	}

	public ItemStack getOutput() {
		return output.create();
	}

	@Override
	public Ingredient getReagent() {
		return reagent;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.PETAL_APOTHECARY);
	}

	@Override
	public RecipeSerializer<? extends PetalApothecaryRecipe> getSerializer() {
		return SERIALIZER;
	}

	private ItemStackTemplate outputTemplate() {
		return output;
	}

	public static final class Serializer {
		public static final MapCodec<PetalApothecaryRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ItemStackTemplate.CODEC.fieldOf("output").forGetter(PetalApothecaryRecipe::outputTemplate),
				Ingredient.CODEC.fieldOf("reagent").forGetter(PetalApothecaryRecipe::getReagent),
				Ingredient.CODEC.listOf(1, 16).fieldOf("ingredients").forGetter(PetalApothecaryRecipe::getIngredients)
		).apply(instance, PetalApothecaryRecipe::of));
		public static final StreamCodec<RegistryFriendlyByteBuf, PetalApothecaryRecipe> STREAM_CODEC = StreamCodec.composite(
				ItemStackTemplate.STREAM_CODEC, PetalApothecaryRecipe::outputTemplate,
				Ingredient.CONTENTS_STREAM_CODEC, PetalApothecaryRecipe::getReagent,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), PetalApothecaryRecipe::getIngredients,
				PetalApothecaryRecipe::of
		);

	}
}
