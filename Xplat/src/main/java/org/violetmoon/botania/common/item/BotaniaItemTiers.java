/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.common.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;

import org.violetmoon.botania.common.lib.BotaniaTags;

import java.util.List;

public final class BotaniaItemTiers {
	public static final ToolMaterial MANASTEEL = new ToolMaterial(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 300, 6.2F, 2, 20, BotaniaTags.Items.REPAIRS_MANASTEEL);
	public static final ToolMaterial ELEMENTIUM = new ToolMaterial(
			BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 720, 6.2F, 2, 20, BotaniaTags.Items.REPAIRS_ELEMENTIUM);
	public static final ToolMaterial TERRASTEEL = new ToolMaterial(
			BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2300, 9, 4, 26, BotaniaTags.Items.REPAIRS_TERRASTEEL);
	public static final ToolMaterial VITREOUS = new ToolMaterial(
			BlockTags.INCORRECT_FOR_WOODEN_TOOL, 125, 3.9F, 0, 10,
			TagKey.create(Registries.ITEM, Identifier.parse("c:glass_blocks/colorless")));

	private BotaniaItemTiers() {}

	public static Tool vitreousTool() {
		var blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
		return new Tool(List.of(
				// Silktouched blocks take priority over the general tier restriction.
				Tool.Rule.minesAndDrops(blocks.getOrThrow(BotaniaTags.Blocks.VITREOUS_PICKAXE_SILKTOUCHED), VITREOUS.speed()),
				Tool.Rule.deniesDrops(blocks.getOrThrow(VITREOUS.incorrectBlocksForDrops())),
				Tool.Rule.minesAndDrops(blocks.getOrThrow(BotaniaTags.Blocks.MINEABLE_WITH_VITREOUS_PICKAXE), VITREOUS.speed())
		), 1.0F, 1, true);
	}
}
