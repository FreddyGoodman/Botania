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

import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.common.crafting.ElvenTradeRecipe;
import org.violetmoon.botania.common.crafting.LexiconElvenTradeRecipe;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.lib.ConventionalBotaniaTags;
import org.violetmoon.botania.data.util.BotaniaRecipeHelper;

import static org.violetmoon.botania.data.util.BotaniaRecipeHelper.recipeKey;

public class ElvenTradeProvider extends BotaniaFabricRecipeProvider {
	public ElvenTradeProvider(HolderLookup.Provider registries, RecipeOutput output, ConditionalOutput conditionalOutput) {
		super(registries, output, conditionalOutput);
	}

	@Override
	public void buildRecipes() {
		RecipeOutput consumer = output;
		oneToOneTrade(consumer, BotaniaBlocks.LIVINGWOOD_LOG, BotaniaBlocks.DREAMWOOD_LOG);
		oneToOneTrade(consumer, BotaniaBlocks.LIVINGWOOD, BotaniaBlocks.DREAMWOOD);

		twoToOneTrade(consumer, ConventionalBotaniaTags.Items.MANASTEEL_INGOTS, BotaniaItems.ELEMENTIUM_INGOT);
		twoToOneTrade(consumer, ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS, BotaniaBlocks.ELEMENTIUM_BLOCK);
		// TODO: a bit of a debugging/convenience recipe that ensures recipes with different input item types work:
		consumer.accept(recipeKey(id(BotaniaItems.ELEMENTIUM_INGOT, "_from_mixed_input")),
				new ElvenTradeRecipe(
						// ingot + block = 10 ingots, so resolve to a stack of 5 elementium ingots
						new ItemStackTemplate[] { new ItemStackTemplate(BotaniaItems.ELEMENTIUM_INGOT, 5) },
						tag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS),
						tag(ConventionalBotaniaTags.Items.MANASTEEL_STORAGE_BLOCKS)
				), null);

		oneToOneTrade(consumer, ConventionalBotaniaTags.Items.MANA_PEARL_GEMS, BotaniaItems.PIXIE_DUST);
		oneToOneTrade(consumer, ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS, BotaniaItems.DRAGONSTONE);
		oneToOneTrade(consumer, ConventionalBotaniaTags.Items.MANA_DIAMOND_STORAGE_BLOCKS, BotaniaBlocks.DRAGONSTONE_BLOCK);

		oneToOneTrade(consumer, Items.QUARTZ, BotaniaItems.ELVEN_QUARTZ);
		oneToOneTrade(consumer, BotaniaBlocks.MANAGLASS, BotaniaBlocks.ALFGLASS);

		returnRecipe(consumer, Items.IRON_INGOT);
		returnRecipe(consumer, Blocks.IRON_BLOCK);
		returnRecipe(consumer, Items.ENDER_PEARL);
		returnRecipe(consumer, Items.DIAMOND);
		returnRecipe(consumer, Blocks.DIAMOND_BLOCK);

		consumer.accept(recipeKey(id(BotaniaItems.LEXICA_BOTANIA, "_upgrade")), LexiconElvenTradeRecipe.INSTANCE, null);
	}

	private void oneToOneTrade(RecipeOutput recipeOutput, ItemLike input, ItemLike output) {
		recipeOutput.accept(recipeKey(id(output)),
				new ElvenTradeRecipe(singleOutput(output), Ingredient.of(input)), null);
	}

	private void oneToOneTrade(RecipeOutput recipeOutput, TagKey<Item> input, ItemLike output) {
		recipeOutput.accept(recipeKey(id(output)),
				new ElvenTradeRecipe(singleOutput(output), tag(input)), null);
	}

	private void twoToOneTrade(RecipeOutput recipeOutput, TagKey<Item> input, ItemLike output) {
		recipeOutput.accept(recipeKey(id(output)),
				new ElvenTradeRecipe(singleOutput(output), tag(input), tag(input)), null);
	}

	private void returnRecipe(RecipeOutput recipeOutput, ItemLike item) {
		recipeOutput.accept(recipeKey(id(item, "_return")),
				new ElvenTradeRecipe(singleOutput(item), Ingredient.of(item)), null);
	}

	private static ItemStackTemplate[] singleOutput(ItemLike output) {
		return new ItemStackTemplate[] { new ItemStackTemplate(output.asItem()) };
	}

	private static Identifier id(ItemLike item) {
		return BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.ELVEN_TRADE_TYPE, item);
	}

	private static Identifier id(ItemLike item, String suffix) {
		return BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.ELVEN_TRADE_TYPE, item, suffix);
	}

}
