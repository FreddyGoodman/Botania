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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.SpectralRailBlock;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartFabricMixin {
	@Inject(at = @At("HEAD"), method = "moveAlongTrack")
	private void handleOnRail(ServerLevel level, CallbackInfo ci) {
		AbstractMinecart self = (AbstractMinecart) (Object) this;
		BlockPos pos = self.getCurrentBlockPosOrRailBelow();
		BlockState state = level.getBlockState(pos);
		if (state.is(BotaniaBlocks.SPECTRAL_RAIL)) {
			((SpectralRailBlock) BotaniaBlocks.SPECTRAL_RAIL).onMinecartPass(state, level, pos, self);
		}

	}

}
