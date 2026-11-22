/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.client.render.entity;

import com.mojang.blaze3d.platform.NativeImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

import org.violetmoon.botania.api.BotaniaAPI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

final class GaiaGuardianTextures {
	private static final int NOISE_STEPS = 4;
	private static final int NOISE_FRAMES = 4;
	private static final int TEXTURE_SIZE = 128;

	private final Map<TextureKey, Identifier> textures = new HashMap<>();

	Identifier get(Identifier skin, float intensity, int frame) {
		int grain = Math.round(Mth.clamp(intensity, 0F, 1F) * NOISE_STEPS);
		int noiseFrame = grain == 0 ? 0 : Math.floorMod(frame, NOISE_FRAMES);
		return textures.computeIfAbsent(new TextureKey(skin, grain, noiseFrame), GaiaGuardianTextures::create);
	}

	private static Identifier create(TextureKey key) {
		var minecraft = Minecraft.getInstance();
		var textures = minecraft.getTextureManager();
		Identifier skin = key.skin();
		Identifier result = botaniaRL("gaia_skin/" + skin.getNamespace() + '/' + skin.getPath() + '/' + key.grain() + '/' + key.frame());
		try {
			// Downloaded skins retain their decoded image. Built-in skins can be read from resources.
			if (textures.getTexture(skin) instanceof DynamicTexture dynamic && dynamic.getPixels() != null) {
				textures.register(result, new DynamicTexture(result::toString, recolor(dynamic.getPixels(), key)));
			} else {
				try (var stream = minecraft.getResourceManager().open(skin); var pixels = NativeImage.read(stream)) {
					textures.register(result, new DynamicTexture(result::toString, recolor(pixels, key)));
				}
			}
			return result;
		} catch (IOException e) {
			BotaniaAPI.LOGGER.warn("Could not read Gaia Guardian skin {} for shader compatibility", skin, e);
			return skin;
		}
	}

	private static NativeImage recolor(NativeImage skin, TextureKey key) {
		// Preserve the skin's UVs and alpha. Higher-resolution grain keeps the waves visibly noisy.
		var image = new NativeImage(TEXTURE_SIZE, TEXTURE_SIZE, false);
		var random = RandomSource.create(key.frame());
		float grain = (float) key.grain() / NOISE_STEPS;
		for (int y = 0; y < TEXTURE_SIZE; y++) {
			for (int x = 0; x < TEXTURE_SIZE; x++) {
				int pixel = skin.getPixel(x * skin.getWidth() / TEXTURE_SIZE, y * skin.getHeight() / TEXTURE_SIZE);
				float grayscale = (0.3F * ARGB.red(pixel) + 0.59F * ARGB.green(pixel) + 0.11F * ARGB.blue(pixel)) / 4.5F;
				int shade = Math.round(Mth.lerp(grain, grayscale, random.nextFloat() * 255F));
				image.setPixel(x, y, ARGB.color(ARGB.alpha(pixel), shade, shade, shade));
			}
		}
		return image;
	}

	private record TextureKey(Identifier skin, int grain, int frame) {
	}
}
