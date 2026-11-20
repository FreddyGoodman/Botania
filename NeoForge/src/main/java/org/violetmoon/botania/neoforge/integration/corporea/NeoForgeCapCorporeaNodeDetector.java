/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.neoforge.integration.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.corporea.CorporeaNode;
import org.violetmoon.botania.api.corporea.CorporeaNodeDetector;
import org.violetmoon.botania.api.corporea.CorporeaSpark;

public class NeoForgeCapCorporeaNodeDetector implements CorporeaNodeDetector {
	@Nullable
	@Override
	public CorporeaNode getNode(Level world, CorporeaSpark spark) {
		ResourceHandler<ItemResource> inv = getInventory(world, spark.getAttachPos());
		if (inv != null) {
			return new NeoForgeCapCorporeaNode(world, spark.getAttachPos(), inv, spark);
		}
		return null;
	}

	@Nullable
	private static ResourceHandler<ItemResource> getInventory(Level level, BlockPos pos) {
		var be = level.getBlockEntity(pos);

		if (be == null) {
			return null;
		}

		ResourceHandler<ItemResource> ret = level.getCapability(Capabilities.Item.BLOCK, pos, null, be, Direction.UP);
		if (ret == null) {
			ret = level.getCapability(Capabilities.Item.BLOCK, pos, null, be, null);
		}
		return ret;
	}
}
