/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.violetmoon.botania.client.render.entity.PlayerDecorationLayer;

@Mixin(AvatarRenderState.class)
public class AvatarRenderStateMixin implements PlayerDecorationLayer.DecorationState {
	@Unique
	private final PlayerDecorationLayer.State botania$decorations = new PlayerDecorationLayer.State();

	@Override
	public PlayerDecorationLayer.State botania$getDecorations() {
		return botania$decorations;
	}
}
