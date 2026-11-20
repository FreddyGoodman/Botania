/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FarmlandBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.botania.common.block.flower.FloatingFlowerBaseBlock;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {
	@ModifyReturnValue(method = "shouldMaintainFarmland", at = @At("RETURN"))
	private static boolean botania$maintainUnderFloatingFlowers(boolean maintains, BlockGetter level, BlockPos pos) {
		return maintains || level.getBlockState(pos.above()).getBlock() instanceof FloatingFlowerBaseBlock;
	}
}
