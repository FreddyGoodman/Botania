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
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.common.crafting.PetalApothecaryRecipe;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.lib.BotaniaTags;
import org.violetmoon.botania.common.lib.ConventionalBotaniaTags;
import org.violetmoon.botania.data.util.BotaniaRecipeHelper;
import org.violetmoon.botania.fabric.data.FabricDatagenInitializer;

import java.util.Arrays;

import static org.violetmoon.botania.data.util.BotaniaRecipeHelper.recipeKey;

public class PetalApothecaryProvider extends BotaniaFabricRecipeProvider {

	public PetalApothecaryProvider(HolderLookup.Provider registries, RecipeOutput output, ConditionalOutput conditionalOutput) {
		super(registries, output, conditionalOutput);
	}

	@Override
	public void buildRecipes() {
		RecipeOutput consumer = output;
		Ingredient white = tag(BotaniaTags.Items.PETALS_WHITE);
		Ingredient orange = tag(BotaniaTags.Items.PETALS_ORANGE);
		Ingredient magenta = tag(BotaniaTags.Items.PETALS_MAGENTA);
		Ingredient lightBlue = tag(BotaniaTags.Items.PETALS_LIGHT_BLUE);
		Ingredient yellow = tag(BotaniaTags.Items.PETALS_YELLOW);
		Ingredient lime = tag(BotaniaTags.Items.PETALS_LIME);
		Ingredient pink = tag(BotaniaTags.Items.PETALS_PINK);
		Ingredient gray = tag(BotaniaTags.Items.PETALS_GRAY);
		Ingredient lightGray = tag(BotaniaTags.Items.PETALS_LIGHT_GRAY);
		Ingredient cyan = tag(BotaniaTags.Items.PETALS_CYAN);
		Ingredient purple = tag(BotaniaTags.Items.PETALS_PURPLE);
		Ingredient blue = tag(BotaniaTags.Items.PETALS_BLUE);
		Ingredient brown = tag(BotaniaTags.Items.PETALS_BROWN);
		Ingredient green = tag(BotaniaTags.Items.PETALS_GREEN);
		Ingredient red = tag(BotaniaTags.Items.PETALS_RED);
		Ingredient black = tag(BotaniaTags.Items.PETALS_BLACK);
		Ingredient runeWater = Ingredient.of(BotaniaItems.RUNE_OF_WATER);
		Ingredient runeFire = Ingredient.of(BotaniaItems.RUNE_OF_FIRE);
		Ingredient runeEarth = Ingredient.of(BotaniaItems.RUNE_OF_EARTH);
		Ingredient runeAir = Ingredient.of(BotaniaItems.RUNE_OF_AIR);
		Ingredient runeSpring = Ingredient.of(BotaniaItems.RUNE_OF_SPRING);
		Ingredient runeSummer = Ingredient.of(BotaniaItems.RUNE_OF_SUMMER);
		Ingredient runeAutumn = Ingredient.of(BotaniaItems.RUNE_OF_AUTUMN);
		Ingredient runeWinter = Ingredient.of(BotaniaItems.RUNE_OF_WINTER);
		Ingredient runeMana = Ingredient.of(BotaniaItems.RUNE_OF_MANA);
		Ingredient runeLust = Ingredient.of(BotaniaItems.RUNE_OF_LUST);
		Ingredient runeGluttony = Ingredient.of(BotaniaItems.RUNE_OF_GLUTTONY);
		Ingredient runeGreed = Ingredient.of(BotaniaItems.RUNE_OF_GREED);
		Ingredient runeSloth = Ingredient.of(BotaniaItems.RUNE_OF_SLOTH);
		Ingredient runeWrath = Ingredient.of(BotaniaItems.RUNE_OF_WRATH);
		Ingredient runeEnvy = Ingredient.of(BotaniaItems.RUNE_OF_ENVY);
		Ingredient runePride = Ingredient.of(BotaniaItems.RUNE_OF_PRIDE);

		Ingredient redstoneRoot = Ingredient.of(BotaniaItems.REDSTONE_ROOT);
		Ingredient pixieDust = tag(ConventionalBotaniaTags.Items.PIXIE_DUSTS);
		Ingredient enderEssence = tag(BotaniaTags.Items.ENDER_ESSENCES);
		Ingredient gaiaSpirit = Ingredient.of(BotaniaItems.GAIA_SPIRIT);

		make(consumer, BotaniaBlocks.PURE_DAISY, white, white, white, white);
		make(consumer, BotaniaBlocks.MANASTAR, lightBlue, green, red, cyan);

		make(consumer, BotaniaBlocks.ENDOFLAME, brown, brown, red, lightGray);
		make(consumer, BotaniaBlocks.HYDROANGEAS, blue, blue, cyan, cyan);
		make(consumer, BotaniaBlocks.THERMALILY, red, orange, orange, runeEarth, runeFire);
		make(consumer, BotaniaBlocks.ROSA_ARCANA, pink, pink, purple, purple, lime, runeMana);
		make(consumer, BotaniaBlocks.MUNCHDEW, lime, lime, red, red, green, runeGluttony);
		make(consumer, BotaniaBlocks.ENTROPINNYUM, red, red, gray, gray, white, white, runeWrath, runeFire);
		make(consumer, BotaniaBlocks.KEKIMURUS, white, white, orange, orange, brown, brown, runeGluttony, pixieDust);
		make(consumer, BotaniaBlocks.GOURMARYLLIS, lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer);
		make(consumer, BotaniaBlocks.NARSLIMMUS, lime, lime, green, green, black, runeSummer, runeWater);
		make(consumer, BotaniaBlocks.SPECTROLUS, red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust);
		make(consumer, BotaniaBlocks.RAFFLOWSIA, purple, purple, green, green, black, runeEarth, runePride, pixieDust);
		make(consumer, BotaniaBlocks.SHULK_ME_NOT, purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath);
		make(consumer, BotaniaBlocks.DANDELIFEON, purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, redstoneRoot, gaiaSpirit);

		make(consumer, BotaniaBlocks.JADED_AMARANTHUS, purple, lime, green, runeSpring, redstoneRoot);
		make(consumer, BotaniaBlocks.BELLETHORNE, red, red, red, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaBlocks.DREADTHORNE, black, black, black, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaBlocks.HEISEI_DREAM, magenta, magenta, purple, pink, runeWrath, pixieDust);
		make(consumer, BotaniaBlocks.TIGERSEYE, yellow, brown, orange, lime, runeAutumn);

		make(withConditions(consumer, FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION), BotaniaBlocks.ORECHID,
				gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);

		make(consumer, BotaniaBlocks.ORECHID_IGNEM, red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust);
		make(consumer, BotaniaBlocks.FALLEN_KANADE, white, white, yellow, yellow, orange, runeSpring);
		make(consumer, BotaniaBlocks.EXOFLAME, red, red, gray, lightGray, runeFire, runeSummer);
		make(consumer, BotaniaBlocks.AGRICARNATION, lime, lime, green, yellow, runeSpring, redstoneRoot);
		make(consumer, BotaniaBlocks.HOPPERHOCK, gray, gray, lightGray, lightGray, runeAir, redstoneRoot);
		make(consumer, BotaniaBlocks.TANGLEBERRIE, cyan, cyan, gray, lightGray, runeAir, runeEarth);
		make(consumer, BotaniaBlocks.JIYUULIA, pink, pink, purple, lightGray, runeWater, runeAir);
		make(consumer, BotaniaBlocks.RANNUNCARPUS, orange, orange, yellow, runeEarth, redstoneRoot);
		make(consumer, BotaniaBlocks.HYACIDUS, purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot);
		make(consumer, BotaniaBlocks.POLLIDISIAC, red, red, pink, pink, orange, runeLust, runeFire);
		make(consumer, BotaniaBlocks.CLAYCONIA, lightGray, lightGray, gray, cyan, runeEarth);
		make(consumer, BotaniaBlocks.LOONIUM, green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust);
		make(consumer, BotaniaBlocks.DAFFOMILL, white, white, brown, yellow, runeAir, redstoneRoot);
		make(consumer, BotaniaBlocks.VINCULOTUS, black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot);
		make(consumer, BotaniaBlocks.SPECTRANTHEMUM, white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, enderEssence);
		make(consumer, BotaniaBlocks.MEDUMONE, brown, brown, gray, gray, runeEarth, redstoneRoot);
		make(consumer, BotaniaBlocks.MARIMORPHOSIS, gray, yellow, green, red, runeEarth, runeFire, redstoneRoot);
		make(consumer, BotaniaBlocks.BUBBELL, cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust);
		make(consumer, BotaniaBlocks.SOLEGNOLIA, brown, brown, red, blue, redstoneRoot);
		make(consumer, BotaniaBlocks.BERGAMUTE, orange, green, green, redstoneRoot);
		make(consumer, BotaniaBlocks.LABELLIA, yellow, yellow, blue, white, black, runeAutumn, redstoneRoot, pixieDust);

		make(consumer, BotaniaBlocks.DAYBLOOM_MOTIF, yellow, yellow, orange, lightBlue);
		make(consumer, BotaniaBlocks.NIGHTSHADE_MOTIF, black, black, purple, gray);

		var profile = DataComponentPatch.builder()
				.set(DataComponents.PROFILE, ResolvableProfile.createUnresolved("Vazkii"))
				.build();
		var vazkiiHead = new ItemStackTemplate(Items.PLAYER_HEAD, profile);
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		consumer.accept(recipeKey(BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.PETAL_APOTHECARY_TYPE, "vazkii_head")),
				new PetalApothecaryRecipe(vazkiiHead, tag(BotaniaTags.Items.SEED_APOTHECARY_REAGENT), inputs), null);
	}

	private void make(RecipeOutput consumer, ItemLike output, Ingredient... ingredients) {
		consumer.accept(recipeKey(BotaniaRecipeHelper.deriveRecipeId(BotaniaRecipeTypes.PETAL_APOTHECARY_TYPE, output)),
				new PetalApothecaryRecipe(new ItemStackTemplate(output.asItem()), tag(BotaniaTags.Items.SEED_APOTHECARY_REAGENT), ingredients), null);
	}

}
