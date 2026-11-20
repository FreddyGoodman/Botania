/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.core.handler;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.entity.GaiaGuardianEntity;
import org.violetmoon.botania.xplat.BotaniaConfig;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class BossBarHandler {

	private BossBarHandler() {}

	// Only access on the client thread!
	public static final Set<GaiaGuardianEntity> bosses = Collections.newSetFromMap(new WeakHashMap<>());
	private static final Identifier BAR_TEXTURE = Identifier.parse(ResourcesLib.GUI_BOSS_BAR);

	private static final RenderPipeline BOSS_BAR_PIPELINE = RenderPipeline.builder()
			.withLocation(botaniaRL("pipeline/gaia_boss_bar"))
			.withVertexShader("core/position_tex_color")
			.withFragmentShader(botaniaRL("core/gaia_boss_bar"))
			.withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
			.withUniform("Projection", UniformType.UNIFORM_BUFFER)
			.withUniform("Globals", UniformType.UNIFORM_BUFFER)
			.withSampler("Sampler0")
			.withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
			.withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
			.build();

	public static boolean renderBar(GuiGraphicsExtractor gui, int x, int y, BossEvent bossEvent) {
		for (GaiaGuardianEntity boss : bosses) {
			if (boss.getBossInfoUuid().equals(bossEvent.getId())) {
				int playerX = x + 160;
				int playerY = y + 12;
				gui.item(new ItemStack(Items.PLAYER_HEAD), playerX, playerY);
				gui.text(Minecraft.getInstance().font, Integer.toString(boss.getInitialPlayerCount()),
						playerX + 15, playerY + 4, 0xFFFFFFFF);
				RenderHelper.drawTexturedModalRect(gui, BAR_TEXTURE, x, y, 0, 0, 185, 15);
				drawHealthBar(gui, boss, x + 2, y + 4, bossEvent.getProgress());
				return true;
			}
		}
		return false;
	}

	private static void drawHealthBar(GuiGraphicsExtractor gui, GaiaGuardianEntity boss, int x, int y, float progress) {
		progress = Mth.clamp(progress, 0F, 1F);
		int width = (int) (181 * progress);
		if (width <= 0) {
			return;
		}

		if (BotaniaConfig.client().useShaders()) {
			float grain = Mth.clamp(Math.max(boss.isHardMode() ? 0.5F : 0F, boss.getInvulTime() / 20F), 0F, 1F);
			int encodedProgress = Math.round(progress * 65535);
			int shaderColor = ARGB.color(255, ARGB.as8BitChannel(grain), encodedProgress >> 8, encodedProgress & 0xFF);
			gui.blit(BOSS_BAR_PIPELINE, BAR_TEXTURE, x, y, 0, 15, width, 7, 256, 256, shaderColor);
		} else {
			gui.blit(RenderPipelines.GUI_TEXTURED, BAR_TEXTURE, x, y, 0, 15, width, 7, 256, 256);
		}
	}
}
