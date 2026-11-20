/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.BossEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.core.handler.BossBarHandler;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
	@Inject(method = "extractBar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/world/BossEvent;)V",
		at = @At("HEAD"), cancellable = true)
	private void botania_extractGaiaBar(GuiGraphicsExtractor gui, int x, int y, BossEvent bossEvent, CallbackInfo ci) {
		if (BossBarHandler.renderBar(gui, x, y, bossEvent)) {
			ci.cancel();
		}
	}
}
