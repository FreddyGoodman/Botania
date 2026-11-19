/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeHolder;

import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;

import java.util.List;
import java.util.Set;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public record RecipeSyncPayload(boolean first, boolean last, List<RecipeHolder<?>> recipes) implements CustomPacketPayload {
	private static final int RECIPES_PER_PACKET = 128;
	public static final Type<RecipeSyncPayload> ID = new Type<>(botaniaRL("recipe_sync"));
	public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSyncPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL, RecipeSyncPayload::first,
			ByteBufCodecs.BOOL, RecipeSyncPayload::last,
			RecipeHolder.STREAM_CODEC.apply(ByteBufCodecs.list(RECIPES_PER_PACKET)), RecipeSyncPayload::recipes,
			RecipeSyncPayload::new);

	@Override
	public Type<RecipeSyncPayload> type() {
		return ID;
	}

	public static void sendRecipes(ServerPlayer player) {
		if (!ServerPlayNetworking.canSend(player, ID)) {
			return;
		}
		var types = Set.copyOf(BotaniaRecipeTypes.synchronizedTypes());
		List<RecipeHolder<?>> recipes = player.level().recipeAccess().getRecipes().stream()
				.filter(recipe -> types.contains(recipe.value().getType()))
				.toList();
		for (int start = 0; start < recipes.size(); start += RECIPES_PER_PACKET) {
			int end = Math.min(start + RECIPES_PER_PACKET, recipes.size());
			ServerPlayNetworking.send(player, new RecipeSyncPayload(start == 0, end == recipes.size(), recipes.subList(start, end)));
		}
		if (recipes.isEmpty()) {
			ServerPlayNetworking.send(player, new RecipeSyncPayload(true, true, List.of()));
		}
	}
}
