/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.core.handler.RenderLexicon;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

	@Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
	private void renderFirstPersonItem(LivingEntity livingEntity, ItemStack stack, ItemDisplayContext transformType,
			PoseStack poseStack, SubmitNodeCollector collector, int light, CallbackInfo ci) {
		if (RenderLexicon.renderHand(stack, transformType, poseStack, collector, light)) {
			ci.cancel();
		}
	}
}
