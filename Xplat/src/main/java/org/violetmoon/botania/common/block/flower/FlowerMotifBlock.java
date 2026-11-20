/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block.flower;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FlowerBlock;

import org.violetmoon.botania.common.item.CustomCreativeTabContents;
import org.violetmoon.botania.common.item.block.BlockItemTooltip;

import java.util.function.Consumer;

public class FlowerMotifBlock extends FlowerBlock implements CustomCreativeTabContents, BlockItemTooltip {
	private final boolean hidden;

	public FlowerMotifBlock(Holder<MobEffect> effect, int effectDuration, Properties properties, boolean hidden) {
		super(effect, effectDuration, properties);
		this.hidden = hidden;
	}

	@Override
	public void appendItemTooltip(Consumer<Component> tooltip) {
		if (hidden) {
			tooltip.accept(Component.translatable("block.botania.daybloom_motif.description").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.accept(Component.translatable("block.botania.hydroangeas_motif.description").withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void addToCreativeTab(Item me, Consumer<ItemStack> output) {
		if (!this.hidden) {
			output.accept(new ItemStack(me));
		}
	}
}
