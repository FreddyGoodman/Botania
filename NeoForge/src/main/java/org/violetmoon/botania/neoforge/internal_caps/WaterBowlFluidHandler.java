/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.neoforge.internal_caps;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import org.violetmoon.botania.common.item.BotaniaItems;

public class WaterBowlFluidHandler extends ItemAccessResourceHandler<FluidResource> {
	public WaterBowlFluidHandler(ItemAccess access) {
		super(access, 1);
	}

	@Override
	protected FluidResource getResourceFrom(ItemResource resource, int index) {
		return resource.is(BotaniaItems.WATER_BOWL) ? FluidResource.of(Fluids.WATER) : FluidResource.EMPTY;
	}

	@Override
	protected int getAmountFrom(ItemResource resource, int index) {
		return resource.is(BotaniaItems.WATER_BOWL) ? FluidType.BUCKET_VOLUME : 0;
	}

	@Override
	protected ItemResource update(ItemResource resource, int index, FluidResource fluid, int amount) {
		return amount == 0 ? ItemResource.of(Items.BOWL) : ItemResource.EMPTY;
	}

	@Override
	public boolean isValid(int index, FluidResource fluid) {
		return false;
	}

	@Override
	protected int getCapacity(int index, FluidResource fluid) {
		return FluidType.BUCKET_VOLUME;
	}
}
