/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.violetmoon.botania.client.model.PlatformModels;
import org.violetmoon.botania.common.block.PlatformBlock;

@Mixin(ModelBlockRenderer.class)
public abstract class ModelBlockRendererMixin {
	@ModifyVariable(method = "computeTintColor", at = @At("HEAD"), argsOnly = true)
	private BlockState botania_useCamouflageTintState(BlockState state, @Local(argsOnly = true) BlockAndTintGetter level,
			@Local(argsOnly = true) BlockPos pos) {
		if (state.getBlock() instanceof PlatformBlock) {
			BlockState camouflage = PlatformModels.camouflage(level, pos);
			if (camouflage != null) {
				return camouflage;
			}
		}
		return state;
	}
}
