/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerExplosion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.botania.common.item.equipment.bauble.BenevolentGoddessCharmItem;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerExplosion.class)
public abstract class ExplosionFabricMixin {
	@Inject(method = "calculateExplodedPositions", at = @At("HEAD"), cancellable = true)
	private void protectNearbyBlocks(CallbackInfoReturnable<List<BlockPos>> callback) {
		var explosion = (ServerExplosion) (Object) this;
		if (BenevolentGoddessCharmItem.shouldProtectExplosion(explosion.level(), explosion.center())) {
			callback.setReturnValue(new ArrayList<>());
		}
	}
}
