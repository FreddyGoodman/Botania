/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.core.RecipeBookAccess;

@Mixin(GhostSlots.class)
public class GhostSlotsMixin implements RecipeBookAccess {
	@Unique
	@Nullable
	private ItemStack botania_hoveredGhostRecipeStack;

	@Override
	@Nullable
	public ItemStack botania_getHoveredGhostRecipeStack() {
		return botania_hoveredGhostRecipeStack;
	}

	@Inject(method = { "extractTooltip", "clear" }, at = @At("HEAD"))
	private void botania$clearHoveredStack(CallbackInfo ci) {
		botania_hoveredGhostRecipeStack = null;
	}

	@ModifyExpressionValue(
		method = "extractTooltip", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/recipebook/GhostSlots$GhostSlot;getItem(I)Lnet/minecraft/world/item/ItemStack;"
		)
	)
	private ItemStack botania$captureHoveredStack(ItemStack stack) {
		botania_hoveredGhostRecipeStack = stack;
		return stack;
	}
}
