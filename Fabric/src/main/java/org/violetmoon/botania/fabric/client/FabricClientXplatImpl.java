/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.fabric.client;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricModelManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.fabric.item.TinyPotatoRenderCallback;
import org.violetmoon.botania.fabric.integration.sodium.SodiumFabricHelper;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FabricClientXplatImpl implements ClientXplatAbstractions {
	@Override
	public void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, SubmitNodeCollector collector, int light, int overlay) {
		TinyPotatoRenderCallback.EVENT.invoker().onRender(potato, name, tickDelta, ms, collector, light, overlay);
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		ClientPlayNetworking.send(packet);
	}

	private static final Map<Identifier, ExtraModelKey<BlockStateModelPart>> BLOCK_MODEL_KEYS = new ConcurrentHashMap<>();

	static ExtraModelKey<BlockStateModelPart> getBlockModelKey(Identifier id) {
		return BLOCK_MODEL_KEYS.computeIfAbsent(id, modelId -> ExtraModelKey.create(modelId::toString));
	}

	@Override
	public @Nullable BlockStateModelPart findBlockModelPart(Identifier id) {
		var models = (FabricModelManager) Minecraft.getInstance().getModelManager();
		var key = BLOCK_MODEL_KEYS.get(id);
		return key != null ? models.getModel(key) : null;
	}

	@Override
	public int getGuiRightHeight(Player player) {
		int rightHeight = 49;

		if (player.isEyeInFluid(FluidTags.WATER) || player.getAirSupply() < player.getMaxAirSupply()) {
			// shift up single row if player is underwater or still recovering air
			rightHeight += 10;
		}

		Entity playerVehicle = player.getVehicle();
		if (playerVehicle instanceof LivingEntity vehicle && vehicle.showVehicleHealth()) {
			// shift up if vehicle health requires more than one row (vanilla HUD limits vehicle hearts to 3 rows)
			rightHeight += Math.min(30, (int) (vehicle.getMaxHealth() + 0.5) / 2) - 1;
		}

		return rightHeight;
	}

	@Override
	public void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap) {
		((ExtendedTexture) texture).setFilterSave(filter, mipmap);
	}

	@Override
	public void restoreLastFilter(AbstractTexture texture) {
		((ExtendedTexture) texture).restoreLastFilter();
	}

	@Override
	public void markSpriteActive(TextureAtlasSprite sprite) {
		if (sodiumLoaded.get()) {
			SodiumFabricHelper.markSpriteActive(sprite);
		}
	}

	private final Supplier<Boolean> sodiumLoaded = Suppliers.memoize(() -> XplatAbstractions.INSTANCE.isModLoaded("sodium"));
}
