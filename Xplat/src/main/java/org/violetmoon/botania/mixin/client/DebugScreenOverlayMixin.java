/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import net.minecraft.client.gui.components.DebugScreenOverlay;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.violetmoon.botania.client.core.handler.DebugHandler;

import java.util.List;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
	@ModifyArg(
		method = "extractRenderState", at = @At(
			value = "INVOKE", ordinal = 0,
			target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;extractLines(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Ljava/util/List;Z)V"
		),
		index = 1
	)
	private List<String> addBotaniaDebugText(List<String> lines) {
		DebugHandler.onDrawDebugText(lines);
		return lines;
	}
}
