/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package org.violetmoon.botania.neoforge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;

import org.violetmoon.botania.api.BotaniaAPI;

import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class BotaniaCuriosDataProvider extends CuriosDataProvider {
	public BotaniaCuriosDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(BotaniaAPI.MODID, output, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries) {
		createEntities("default_player_slots").addEntities(EntityType.PLAYER).addSlots(
				"belt", "body", "charm", "head", "necklace", "ring");
		createSlot("ring").operation("ADD").size(1);
	}
}
