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

import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.model.GaiaHeadSpecialRenderer;
import org.violetmoon.botania.client.render.block_entity.BlockEntityItemRenderer;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

@Mixin(SpecialModelRenderers.class)
public abstract class SpecialModelRenderersMixin {
	@Shadow
	@Final
	private static ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked<?>>> ID_MAPPER;

	@Inject(method = "bootstrap", at = @At("TAIL"))
	private static void botania_registerSpecialModels(CallbackInfo ci) {
		ID_MAPPER.put(botaniaRL("gaia_head"), GaiaHeadSpecialRenderer.Unbaked.CODEC);
		ID_MAPPER.put(botaniaRL("block_entity"), BlockEntityItemRenderer.Unbaked.CODEC);
	}
}
