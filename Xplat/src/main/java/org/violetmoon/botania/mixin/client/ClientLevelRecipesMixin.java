/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.crafting.RecipeMap;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes.RecipeView;

@Mixin(ClientLevel.class)
public class ClientLevelRecipesMixin implements RecipeView {
	@Shadow
	@Final
	private ClientPacketListener connection;

	@Override
	public RecipeMap botania$getRecipes() {
		return ((RecipeView) connection).botania$getRecipes();
	}
}
