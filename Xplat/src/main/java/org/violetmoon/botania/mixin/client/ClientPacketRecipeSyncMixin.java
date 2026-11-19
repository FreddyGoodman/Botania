/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.violetmoon.botania.client.ClientRecipeSync;
import org.violetmoon.botania.common.handler.BotaniaRecipeIngredientsCache;

import java.util.ArrayList;
import java.util.List;

@Mixin(ClientPacketListener.class)
public class ClientPacketRecipeSyncMixin implements ClientRecipeSync {
	@Unique private RecipeMap botania$recipes = RecipeMap.EMPTY;
	@Unique private @Nullable List<RecipeHolder<?>> botania$pendingRecipes;

	@Override
	public RecipeMap botania$getRecipes() {
		return botania$recipes;
	}

	@Override
	public void botania$setRecipes(RecipeMap recipes) {
		botania$recipes = recipes;
		botania$pendingRecipes = null;
		BotaniaRecipeIngredientsCache.clearClientCache();
	}

	@Override
	public void botania$acceptRecipes(boolean first, boolean last, List<RecipeHolder<?>> recipes) {
		if (first) {
			botania$pendingRecipes = new ArrayList<>();
		}
		if (botania$pendingRecipes != null) {
			botania$pendingRecipes.addAll(recipes);
			if (last) {
				botania$setRecipes(RecipeMap.create(botania$pendingRecipes));
			}
		}
	}
}
