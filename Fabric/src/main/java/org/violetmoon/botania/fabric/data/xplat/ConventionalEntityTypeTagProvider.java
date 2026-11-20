/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package org.violetmoon.botania.fabric.data.xplat;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;

import org.violetmoon.botania.common.entity.BotaniaEntities;
import org.violetmoon.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class ConventionalEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
	public ConventionalEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		builder(ConventionalEntityTypeTags.BOSSES).add(BotaniaEntities.GAIA_GUARDIAN.builtInRegistryHolder().key());

		builder(BotaniaTags.Entities.NOT_CHARMABLE)
				.add(EntityType.WARDEN.builtInRegistryHolder().key())
				.addTag(ConventionalEntityTypeTags.BOSSES);

		builder(ConventionalEntityTypeTags.MINECARTS).add(BotaniaEntities.MANA_POOL_MINECART.builtInRegistryHolder().key());
	}

}
