/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.neoforge.client;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.neoforge.item.TinyPotatoRenderEvent;
import org.violetmoon.botania.neoforge.client.integration.embeddium.EmbeddiumHelper;
import org.violetmoon.botania.neoforge.client.integration.sodium.SodiumNeoforgeHelper;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NeoForgeClientXplatImpl implements ClientXplatAbstractions {
	@Override
	public void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, SubmitNodeCollector collector, int light, int overlay) {
		NeoForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, name, tickDelta, ms, collector, light, overlay));
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		PacketDistributor.sendToServer(packet);
	}

	private static final Map<Identifier, StandaloneModelKey<BlockStateModelPart>> BLOCK_MODEL_KEYS = new ConcurrentHashMap<>();

	static StandaloneModelKey<BlockStateModelPart> getBlockModelKey(Identifier id) {
		return BLOCK_MODEL_KEYS.computeIfAbsent(id, modelId -> new StandaloneModelKey<>(modelId::toString));
	}

	@Override
	public @Nullable BlockStateModelPart findBlockModelPart(Identifier id) {
		var models = Minecraft.getInstance().getModelManager();
		var key = BLOCK_MODEL_KEYS.get(id);
		return key != null ? models.getStandaloneModel(key) : null;
	}

	@Override
	public int getGuiRightHeight(Player player) {
		return Minecraft.getInstance().gui.rightHeight;
	}

	@Override
	public void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap) {
		texture.setBlurMipmap(filter, mipmap);
	}

	@Override
	public void restoreLastFilter(AbstractTexture texture) {
		texture.restoreLastBlurMipmap();
	}

	@Override
	public void markSpriteActive(TextureAtlasSprite sprite) {
		if (sodiumLoaded.get()) {
			SodiumNeoforgeHelper.markSpriteActive(sprite);
		}
		if (embeddiumLoaded.get()) {
			EmbeddiumHelper.markSpriteActive(sprite);
		}
	}

	private final Supplier<Boolean> sodiumLoaded = Suppliers.memoize(() -> XplatAbstractions.INSTANCE.isModLoaded("sodium"));
	private final Supplier<Boolean> embeddiumLoaded = Suppliers.memoize(() -> XplatAbstractions.INSTANCE.isModLoaded("embeddium"));
}
