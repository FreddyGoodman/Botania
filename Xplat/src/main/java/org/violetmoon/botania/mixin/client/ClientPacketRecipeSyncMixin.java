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
import net.minecraft.world.item.crafting.RecipeMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.violetmoon.botania.client.ClientRecipeSync;
import org.violetmoon.botania.common.handler.BotaniaRecipeIngredientsCache;

@Mixin(ClientPacketListener.class)
public class ClientPacketRecipeSyncMixin implements ClientRecipeSync {
	@Unique
	private RecipeMap botania$recipes = RecipeMap.EMPTY;

	@Override
	public RecipeMap botania$getRecipes() {
		return botania$recipes;
	}

	@Override
	public void botania$setRecipes(RecipeMap recipes) {
		botania$recipes = recipes;
		BotaniaRecipeIngredientsCache.clearClientCache();
	}

}
