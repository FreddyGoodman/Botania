/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.model.BotaniaItemTint;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

@Mixin(ItemTintSources.class)
public abstract class ItemTintSourcesMixin {
	@Shadow
	@Final
	private static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ItemTintSource>> ID_MAPPER;

	@Inject(method = "bootstrap", at = @At("TAIL"))
	private static void botania_registerItemTints(CallbackInfo ci) {
		ID_MAPPER.put(botaniaRL("item_tint"), BotaniaItemTint.CODEC);
	}
}
