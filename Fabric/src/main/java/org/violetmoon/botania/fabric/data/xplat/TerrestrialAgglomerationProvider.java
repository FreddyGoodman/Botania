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
import net.minecraft.world.item.ItemStackTemplate;

import org.violetmoon.botania.common.block.mana.ManaPoolBlock;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.common.crafting.TerrestrialAgglomerationRecipe;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.lib.ConventionalBotaniaTags;
import org.violetmoon.botania.data.util.BotaniaRecipeHelper;

import static org.violetmoon.botania.data.util.BotaniaRecipeHelper.recipeKey;

public class TerrestrialAgglomerationProvider extends BotaniaFabricRecipeProvider {
	public TerrestrialAgglomerationProvider(HolderLookup.Provider registries, RecipeOutput output, ConditionalOutput conditionalOutput) {
		super(registries, output, conditionalOutput);
	}

	@Override
	public void buildRecipes() {
		output.accept(recipeKey(BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.TERRA_PLATE_TYPE,
				BotaniaItems.TERRASTEEL_INGOT)),
				new TerrestrialAgglomerationRecipe(
						ManaPoolBlock.MAX_MANA / 2,
						new ItemStackTemplate(BotaniaItems.TERRASTEEL_INGOT),
						tag(ConventionalBotaniaTags.Items.MANASTEEL_INGOTS),
						tag(ConventionalBotaniaTags.Items.MANA_PEARL_GEMS),
						tag(ConventionalBotaniaTags.Items.MANA_DIAMOND_GEMS)
				),
				null);
	}
}
