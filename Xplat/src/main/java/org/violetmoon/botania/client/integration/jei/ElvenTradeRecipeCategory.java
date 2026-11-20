/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.recipe.ElvenTradeRecipe;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.common.block.BotaniaBlocks;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class ElvenTradeRecipeCategory extends BotaniaRecipeCategoryBase<ElvenTradeRecipe> {

	public static final RecipeType<ElvenTradeRecipe> TYPE = RecipeType.create(BotaniaAPI.MODID, "elven_trade", ElvenTradeRecipe.class);
	private final IDrawable overlay;

	public ElvenTradeRecipeCategory(IGuiHelper guiHelper) {
		super(145, 95, Component.translatable("botania.nei.elvenTrade"),
				guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BotaniaBlocks.ELVEN_GATEWAY_CORE)), null);
		overlay = guiHelper.createDrawable(botaniaRL("textures/gui/elven_trade_overlay.png"), 0, 15, 140, 90);
	}

	@Override
	public RecipeType<ElvenTradeRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	public void draw(ElvenTradeRecipe recipe, IRecipeSlotsView slotsView, GuiGraphicsExtractor gui, double mouseX, double mouseY) {
		super.draw(recipe, slotsView, gui, mouseX, mouseY);
		overlay.draw(gui, 0, 4);
		RenderHelper.drawAlfheimPortal(gui, 22, 25);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ElvenTradeRecipe recipe, IFocusGroup focusGroup) {
		int posX = 42;
		for (var ingredient : recipe.getIngredients()) {
			builder.addSlot(RecipeIngredientRole.INPUT, posX, 0)
					.addIngredients(ingredient);
			posX += 18;
		}

		int outIdx = 0;
		for (var stack : recipe.getOutputs()) {
			builder.addSlot(RecipeIngredientRole.OUTPUT, 93 + outIdx % 2 * 20, 41 + outIdx / 2 * 20)
					.addItemStack(stack);
			outIdx++;
		}
	}
}
