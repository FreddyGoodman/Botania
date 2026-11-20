/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public interface BlockItemTooltip {
	void appendItemTooltip(Consumer<Component> tooltip);

	static BlockItem createItem(Block block, Item.Properties properties) {
		if (block instanceof BlockItemTooltip details) {
			return new BlockItem(block, properties) {
				@Override
				public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
						Consumer<Component> tooltip, TooltipFlag flag) {
					super.appendHoverText(stack, context, display, tooltip, flag);
					details.appendItemTooltip(tooltip);
				}
			};
		}
		return new BlockItem(block, properties);
	}
}
