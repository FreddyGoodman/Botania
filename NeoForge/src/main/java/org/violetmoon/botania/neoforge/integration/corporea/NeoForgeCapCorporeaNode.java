/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.neoforge.integration.corporea;

import com.google.common.collect.ImmutableList;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import org.violetmoon.botania.api.corporea.CorporeaRequest;
import org.violetmoon.botania.api.corporea.CorporeaSpark;
import org.violetmoon.botania.common.impl.corporea.AbstractCorporeaNode;

import java.util.List;

public class NeoForgeCapCorporeaNode extends AbstractCorporeaNode {
	protected final ResourceHandler<ItemResource> inv;

	public NeoForgeCapCorporeaNode(Level world, BlockPos pos, ResourceHandler<ItemResource> inv, CorporeaSpark spark) {
		super(world, pos, spark);
		this.inv = inv;
	}

	@Override
	public List<ItemStack> countItems(CorporeaRequest request) {
		return iterateOverSlots(request, false);
	}

	@Override
	public List<ItemStack> extractItems(CorporeaRequest request) {
		return iterateOverSlots(request, true);
	}

	protected List<ItemStack> iterateOverSlots(CorporeaRequest request, boolean doit) {
		ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

		try (var transaction = Transaction.openRoot()) {
			for (int i = inv.size() - 1; i >= 0; i--) {
				ItemResource resource = inv.getResource(i);
				ItemStack stackAt = resource.toStack(inv.getAmountAsInt(i));
				if (stackAt.isEmpty() || !request.getMatcher().test(stackAt)) {
					continue;
				}

				request.trackFound(stackAt.getCount());
				int needed = request.getStillNeeded();
				int amount = needed == -1 ? stackAt.getCount() : Math.min(stackAt.getCount(), needed);
				if (amount == 0) {
					continue;
				}

				int extracted = inv.extract(i, resource, amount, transaction);
				request.trackSatisfied(extracted);
				if (extracted == 0) {
					continue;
				}
				if (doit) {
					builder.addAll(breakDownBigStack(resource.toStack(extracted)));
					getSpark().onItemExtracted(stackAt);
					request.trackExtracted(extracted);
				} else {
					builder.add(resource.toStack(extracted));
				}
			}
			if (doit && !getSpark().isCreative()) {
				transaction.commit();
			}
		}

		return builder.build();
	}

}
