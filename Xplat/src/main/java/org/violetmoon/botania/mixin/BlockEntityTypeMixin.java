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

import net.minecraft.world.level.block.entity.BlockEntityType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
	@ModifyReturnValue(method = "onlyOpCanSetNbt", at = @At("RETURN"))
	private boolean botania$restrictCellularBlockData(boolean opOnly) {
		return opOnly || (Object) this == BotaniaBlockEntities.CELLULAR_BLOCK;
	}
}
