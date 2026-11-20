/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.xplat;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.ServiceUtil;

public interface ClientXplatAbstractions {
	// Event firing
	void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay);

	// Networking
	void sendToServer(CustomPacketPayload packet);

	// Rendering stuff

	/**
	 * Try to load the resource/standalone model with the specified resource location.
	 * Returns null if no model was found.
	 */
	@Nullable
	BakedModel getResourceModel(Identifier id);
	BlockStateModelPart getBlockModelPart(Identifier id);
	BakedModel wrapPlatformModel(BakedModel original);
	int getGuiRightHeight(Player player);
	void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap);
	void restoreLastFilter(AbstractTexture texture);
	/** Marks an animated sprite as "active" for Sodium, if present. */
	void markSpriteActive(TextureAtlasSprite sprite);

	ClientXplatAbstractions INSTANCE = ServiceUtil.findService(ClientXplatAbstractions.class, null);

	static ClientXplatAbstractions instance() {
		return INSTANCE;
	}
}
