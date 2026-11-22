/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.patchouli.client.handler.MultiblockVisualizationHandler;

@Mixin(MultiblockVisualizationHandler.class)
public class PatchouliMultiblockRenderMixin {
	@Inject(method = "onWorldRenderLast", at = @At("HEAD"), cancellable = true, remap = false)
	private void skipLateMultiblockSubmission(CallbackInfo ci) {
		// LevelRendererMixin submits the hologram before the translucent features are drawn.
		ci.cancel();
	}
}
