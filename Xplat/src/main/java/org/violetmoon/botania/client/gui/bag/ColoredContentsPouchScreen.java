/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.gui.bag;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.internal.Colored;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.helper.ColorHelper;
import org.violetmoon.botania.common.item.ColoredContentsPouchItem;

import java.util.ArrayList;
import java.util.List;

public class ColoredContentsPouchScreen extends AbstractContainerScreen<ColoredContentsPouchMenu> {

	private static final Identifier TEXTURE = Identifier.parse(ResourcesLib.GUI_FLOWER_BAG);
	private static final int TEXTURE_SIZE = 256;
	private static final int SLOT_SIZE = 16;
	private static final int GHOST_BACKGROUND_COLOR = 0xA0FFFFFF;

	public ColoredContentsPouchScreen(ColoredContentsPouchMenu container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title, 176, 202);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractBackground(graphics, mouseX, mouseY, partialTick);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 0, 0,
				imageWidth, imageHeight, TEXTURE_SIZE, TEXTURE_SIZE);
	}

	@Override
	protected void extractSlots(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		List<TagKey<Item>> itemTypes = ColoredContentsPouchItem.getStoredItemTypes(menu.getPouch());
		List<DyeColor> colors = ColorHelper.supportedColors().toList();
		var ghostSlots = new ArrayList<Slot>();
		for (Slot slot : menu.slots) {
			if (slot.container != menu.pouchInv || slot.hasItem()) {
				continue;
			}

			int typeIndex = slot.index / colors.size();
			int colorIndex = slot.index % colors.size();
			BuiltInRegistries.ITEM.get(itemTypes.get(typeIndex))
					.flatMap(holders -> holders.stream().map(Holder::value)
							.filter(item -> item instanceof Colored colored && colored.getColor() == colors.get(colorIndex))
							.findFirst())
					.ifPresent(item -> {
						graphics.fakeItem(new ItemStack(item), slot.x, slot.y);
						ghostSlots.add(slot);
					});
		}

		if (!ghostSlots.isEmpty()) {
			// Blend the same slot background over each ghost icon to leave it at 0x5F opacity.
			graphics.nextStratum();
			for (Slot slot : ghostSlots) {
				graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, slot.x, slot.y, slot.x, slot.y,
						SLOT_SIZE, SLOT_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, GHOST_BACKGROUND_COLOR);
			}
			graphics.nextStratum();
		}

		super.extractSlots(graphics, mouseX, mouseY);
	}

	@Override
	protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
		super.extractSlot(graphics, slot, mouseX, mouseY);
		if (slot.container == menu.pouchInv && slot.getItem().getCount() == 1) {
			graphics.itemDecorations(font, slot.getItem(), slot.x, slot.y, "1");
		}
	}
}
