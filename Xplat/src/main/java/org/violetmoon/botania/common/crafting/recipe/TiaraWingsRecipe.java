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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.equipment.bauble.FlugelTiaraItem;

import java.util.List;
import java.util.Optional;

public class TiaraWingsRecipe extends BotaniaSpecialRecipe {
	public static final RecipeSerializer<TiaraWingsRecipe> SERIALIZER = new RecipeSerializer<>(Serializer.CODEC, Serializer.STREAM_CODEC);

	private final Optional<Ingredient> material;
	private final int variant;

	public TiaraWingsRecipe(CraftingBookCategory category, Optional<Ingredient> material, int variant) {
		super(category);
		if (material.isPresent() && material.get().test(BotaniaItems.FLUGEL_TIARA.getDefaultInstance())) {
			throw new IllegalArgumentException("Material cannot be a Flügel Tiara");
		}
		this.material = material;
		this.variant = variant;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (input.ingredientCount() != (material.isEmpty() ? 1 : 2)) {
			return false;
		}

		boolean foundTiara = false;
		boolean foundMaterial = material.isEmpty();

		for (ItemStack stack : input.items()) {
			if (stack.isEmpty()) {
				continue;
			}
			if (!foundTiara && matchesTiara(stack)) {
				foundTiara = true;
			} else if (!foundMaterial && material.orElseThrow().test(stack)) {
				foundMaterial = true;
			} else {
				return false;
			}
		}

		return foundMaterial && foundTiara;
	}

	private boolean matchesTiara(ItemStack stack) {
		return stack.is(BotaniaItems.FLUGEL_TIARA) && FlugelTiaraItem.getVariant(stack) != variant;
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		ItemStack tiara = input.items().stream().filter(stack -> stack.is(BotaniaItems.FLUGEL_TIARA))
				.findFirst().orElseThrow().copy();
		FlugelTiaraItem.setVariant(tiara, variant);
		return tiara;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public List<Ingredient> getIngredients() {
		Ingredient tiara = Ingredient.of(BotaniaItems.FLUGEL_TIARA);
		return material.map(ingredient -> List.of(tiara, ingredient)).orElseGet(() -> List.of(tiara));
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		ItemStack tiara = new ItemStack(BotaniaItems.FLUGEL_TIARA);
		FlugelTiaraItem.setVariant(tiara, variant);
		return tiara;
	}

	@Override
	public String group() {
		return "botania:flugel_tiara_wings";
	}

	public Optional<Ingredient> material() {
		return material;
	}

	public int variant() {
		return variant;
	}

	@Override
	public RecipeSerializer<TiaraWingsRecipe> getSerializer() {
		return SERIALIZER;
	}

	private static final class Serializer {
		private static final MapCodec<TiaraWingsRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.EQUIPMENT).forGetter(TiaraWingsRecipe::category),
				Ingredient.CODEC.optionalFieldOf("material").forGetter(TiaraWingsRecipe::material),
				ExtraCodecs.intRange(0, FlugelTiaraItem.WING_TYPES).fieldOf("variant").forGetter(TiaraWingsRecipe::variant)
		).apply(instance, TiaraWingsRecipe::new));
		private static final StreamCodec<RegistryFriendlyByteBuf, TiaraWingsRecipe> STREAM_CODEC = StreamCodec.composite(
				CraftingBookCategory.STREAM_CODEC, TiaraWingsRecipe::category,
				Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, TiaraWingsRecipe::material,
				ByteBufCodecs.VAR_INT, TiaraWingsRecipe::variant,
				TiaraWingsRecipe::new
		);

	}
}
