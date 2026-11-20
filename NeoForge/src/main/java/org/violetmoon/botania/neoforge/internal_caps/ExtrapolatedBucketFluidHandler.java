/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.neoforge.internal_caps;

import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.VoidingResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class ExtrapolatedBucketFluidHandler extends VoidingResourceHandler<FluidResource> {
	public ExtrapolatedBucketFluidHandler() {
		super(FluidResource.EMPTY);
	}

	@Override
	public int insert(int index, FluidResource fluid, int amount, TransactionContext transaction) {
		return super.insert(index, fluid, Math.min(FluidType.BUCKET_VOLUME, amount), transaction);
	}
}
