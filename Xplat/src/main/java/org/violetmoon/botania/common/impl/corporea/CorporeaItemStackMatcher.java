/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.impl.corporea;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.violetmoon.botania.api.corporea.CorporeaRequestMatcher;
import org.violetmoon.botania.common.helper.DataComponentHelper;

public class CorporeaItemStackMatcher implements CorporeaRequestMatcher {
	private static final String TAG_REQUEST_STACK = "requestStack";
	private static final String TAG_REQUEST_CHECK_NBT = "requestCheckNBT";

	private final ItemStack match;
	private final boolean checkNBT;

	public CorporeaItemStackMatcher(ItemStack match, boolean checkNBT) {
		this.match = match;
		this.checkNBT = checkNBT;
	}

	@Override
	public boolean test(ItemStack stack) {
		return !stack.isEmpty() && !match.isEmpty() && ItemStack.isSameItem(stack, match) && (!checkNBT || DataComponentHelper.matchTagAndManaFullness(stack, match));
	}

	public static CorporeaItemStackMatcher load(ValueInput input) {
		return new CorporeaItemStackMatcher(input.read(TAG_REQUEST_STACK, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY), input.getBooleanOr(TAG_REQUEST_CHECK_NBT, false));
	}

	@Override
	public void save(ValueOutput output) {
		output.store(TAG_REQUEST_STACK, ItemStack.OPTIONAL_CODEC, match);
		output.putBoolean(TAG_REQUEST_CHECK_NBT, checkNBT);
	}

	@Override
	public Component getRequestName() {
		return match.getDisplayName();
	}
}
