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
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.botania.client.fx.BotaniaParticleLayers;

import java.util.Map;

@Mixin(QuadParticleRenderState.class)
public class QuadParticleRenderStateMixin {
	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/AbstractTexture;getSampler()Lcom/mojang/blaze3d/textures/GpuSampler;"))
	private GpuSampler botania_filterParticleAtlas(AbstractTexture texture, Operation<GpuSampler> original, @Local Map.Entry<SingleQuadParticle.Layer, ?> layer) {
		return BotaniaParticleLayers.usesLinearFiltering(layer.getKey())
				? RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
				: original.call(texture);
	}
}
