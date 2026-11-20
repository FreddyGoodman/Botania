/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.botania.client.patchouli.PatchouliUtils;

import vazkii.patchouli.client.book.page.PageMultiblock;

@Mixin(PageMultiblock.class)
public class PageMultiblockMixin {
	@WrapOperation(method = "extractRenderState", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/block/BlockModelResolver;update(Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/renderer/block/model/BlockDisplayContext;)V"))
	private void botania$extractWithStaticFlowers(BlockModelResolver resolver, BlockModelRenderState renderState, BlockState state,
			BlockDisplayContext context, Operation<Void> original) {
		boolean wasInVisualizer = PatchouliUtils.isInVisualizer();
		PatchouliUtils.setInVisualizer(true);
		try {
			original.call(resolver, renderState, state, context);
		} finally {
			PatchouliUtils.setInVisualizer(wasInVisualizer);
		}
	}
}
