/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.render.block_entity.BlockEntityItemRenderer;

@Mixin(SpecialModelWrapper.class)
public abstract class SpecialModelWrapperMixin {
	@Shadow
	@Final
	private SpecialModelRenderer<?> specialRenderer;

	@Inject(method = "update", at = @At("TAIL"))
	private void botania_markAnimated(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext context,
			@Nullable ClientLevel level, @Nullable ItemOwner owner, int seed, CallbackInfo ci) {
		if (specialRenderer instanceof BlockEntityItemRenderer renderer && renderer.isAnimated()) {
			state.setAnimated();
		}
	}
}
