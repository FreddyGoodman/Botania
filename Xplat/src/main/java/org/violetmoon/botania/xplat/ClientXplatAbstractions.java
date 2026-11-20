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

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.ServiceUtil;

public interface ClientXplatAbstractions {
	// Event firing
	void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, SubmitNodeCollector collector, int light, int overlay);

	// Networking
	void sendToServer(CustomPacketPayload packet);

	// Rendering stuff

	@Nullable
	BlockStateModelPart findBlockModelPart(Identifier id);

	default BlockStateModelPart getBlockModelPart(Identifier id) {
		return java.util.Objects.requireNonNull(findBlockModelPart(id), id::toString);
	}

	int getGuiRightHeight(Player player);
	/** Marks an animated sprite as "active" for Sodium, if present. */
	void markSpriteActive(TextureAtlasSprite sprite);

	ClientXplatAbstractions INSTANCE = ServiceUtil.findService(ClientXplatAbstractions.class, null);

	static ClientXplatAbstractions instance() {
		return INSTANCE;
	}
}
