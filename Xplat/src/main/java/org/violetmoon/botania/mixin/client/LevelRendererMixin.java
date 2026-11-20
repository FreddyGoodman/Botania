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

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.SubmitNodeStorage;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.render.world.WorldOverlays;
import org.violetmoon.botania.common.item.AssemblyHaloItem;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Shadow
	@Final
	private RenderBuffers renderBuffers;
	@Shadow
	@Nullable
	private ClientLevel level;

	@Shadow
	@Final
	private SubmitNodeStorage submitNodeStorage;

	@Inject(
		method = "lambda$addMainPass$0", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderSolidFeatures()V"
		)
	)
	private void botania$submitHalo(CallbackInfo ci) {
		if (level != null) {
			Minecraft client = Minecraft.getInstance();
			AssemblyHaloItem.Rendering.submit(client.gameRenderer.getMainCamera().position(),
					client.getDeltaTracker().getGameTimeDeltaPartialTick(true), new PoseStack(), submitNodeStorage);
		}
	}

	@Inject(
		method = "lambda$addMainPass$0", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderTranslucentParticles()V"
		)
	)
	private void botania$renderOverlays(CallbackInfo ci) {
		if (level != null) {
			Minecraft client = Minecraft.getInstance();
			var delta = client.getDeltaTracker();
			WorldOverlays.renderWorldLast(client.gameRenderer.getMainCamera(),
					delta.getGameTimeDeltaPartialTick(false), delta.getGameTimeDeltaPartialTick(true),
					new PoseStack(), renderBuffers, level);
		}
	}
}
