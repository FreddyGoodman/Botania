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
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.MoonPhase;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.core.SkyblockWorldInfo;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.render.world.SkyblockSkyRenderer;
import org.violetmoon.botania.xplat.BotaniaConfig;

@Mixin(value = SkyRenderer.class, priority = 900)
public class SkyRendererMixin {
	@Shadow
	@Final
	private GpuBuffer starBuffer;
	@Shadow
	@Final
	private RenderSystem.AutoStorageIndexBuffer quadIndices;
	@Shadow
	private int starIndexCount;

	@Unique
	private @Nullable SkyblockSkyRenderer.State botania$sky;

	@Inject(method = "extractRenderState", at = @At("RETURN"))
	private void botania$extractSky(ClientLevel level, float partialTick, Camera camera, SkyRenderState state, CallbackInfo ci) {
		var config = BotaniaConfig.client();
		boolean gardenOfGlass = level.getLevelData() instanceof SkyblockWorldInfo info && info.isGardenOfGlass();
		if (config.enableFancySkybox() && level.dimension() == Level.OVERWORLD
				&& (config.enableFancySkyboxInNormalWorlds() || gardenOfGlass)) {
			float celestialAngle = Mth.frac(state.sunAngle / Mth.TWO_PI);
			botania$sky = new SkyblockSkyRenderer.State(celestialAngle, state.rainBrightness,
					ClientTickHandler.getEntityTicksInGame() + partialTick, level.getOverworldClockTime());
		} else {
			botania$sky = null;
		}
	}

	@Inject(method = "renderSunMoonAndStars", at = @At("HEAD"))
	private void botania$renderSky(PoseStack pose, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase,
			float rainBrightness, float starBrightness, CallbackInfo ci) {
		if (botania$sky != null) {
			SkyblockSkyRenderer.renderExtras(pose, botania$sky);
			SkyblockSkyRenderer.renderStars(starBuffer, starIndexCount, quadIndices, botania$sky);
		}
	}

	@WrapOperation(
		method = "renderSunMoonAndStars", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/SkyRenderer;renderSun(FLcom/mojang/blaze3d/vertex/PoseStack;)V"
		)
	)
	private void botania$scaleSun(SkyRenderer renderer, float brightness, PoseStack pose, Operation<Void> original) {
		pose.pushPose();
		if (botania$sky != null) {
			pose.scale(2, 1, 2);
		}
		original.call(renderer, brightness, pose);
		pose.popPose();
	}

	@WrapOperation(
		method = "renderSunMoonAndStars", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/SkyRenderer;renderMoon(Lnet/minecraft/world/level/MoonPhase;FLcom/mojang/blaze3d/vertex/PoseStack;)V"
		)
	)
	private void botania$scaleMoon(SkyRenderer renderer, MoonPhase phase, float brightness, PoseStack pose, Operation<Void> original) {
		pose.pushPose();
		if (botania$sky != null) {
			pose.scale(3, 1, 3);
		}
		original.call(renderer, phase, brightness, pose);
		pose.popPose();
	}
}
