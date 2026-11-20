/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeBookPage;
import net.minecraft.client.gui.screens.recipebook.RecipeButton;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.client.core.RecipeBookAccess;
import org.violetmoon.botania.client.core.proxy.ClientProxy;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import org.violetmoon.botania.mixin.client.AbstractContainerScreenAccessor;
import org.violetmoon.botania.mixin.client.AbstractRecipeBookScreenAccessor;
import org.violetmoon.botania.mixin.client.RecipeBookComponentAccessor;
import org.violetmoon.botania.mixin.client.RecipeBookPageAccessor;
import org.violetmoon.botania.network.serverbound.IndexKeybindRequestPacket;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CorporeaInputHandler {

	public static final List<Supplier<ItemStack>> hoveredStackGetters = new ArrayList<>();
	public static Predicate<Screen> supportedGuiFilter = gui -> gui instanceof AbstractContainerScreen;

	public static boolean buttonPressed(KeyEvent event) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.level == null || !supportedGuiFilter.test(mc.screen)
				|| !ClientProxy.CORPOREA_REQUEST.matches(event)
				|| CorporeaIndexBlockEntity.getNearbyValidIndexes(mc.player).isEmpty()) {
			return false;
		}

		ItemStack stack = getStackUnderMouse();
		if (!stack.isEmpty()) {
			int count = 1;
			int max = stack.getMaxStackSize();

			if (event.hasShiftDown()) {
				count = max;
				if (event.hasControlDownWithQuirk()) {
					count /= 4;
				}
			} else if (event.hasControlDownWithQuirk()) {
				count = max / 2;
			}

			if (count > 0) {
				ItemStack requested = stack.copyWithCount(count);
				ClientXplatAbstractions.INSTANCE.sendToServer(new IndexKeybindRequestPacket(requested));
				return true;
			}
		}
		return false;
	}

	private static ItemStack getStackUnderMouse() {
		Screen screen = Minecraft.getInstance().screen;
		if (screen instanceof AbstractContainerScreen) {
			Slot slotUnderMouse = ((AbstractContainerScreenAccessor) screen).botania_getHoveredSlot();
			if (slotUnderMouse != null && slotUnderMouse.hasItem()) {
				return slotUnderMouse.getItem().copy();
			}

			if (screen instanceof AbstractRecipeBookScreen<?>) {
				RecipeBookComponent<?> recipeBook = ((AbstractRecipeBookScreenAccessor) screen).botania_getRecipeBookComponent();
				if (!recipeBook.isVisible()) {
					return getRecipeViewerStack();
				}
				RecipeBookComponentAccessor access = (RecipeBookComponentAccessor) recipeBook;
				RecipeBookPage page = access.botania_getRecipeBookPage();
				RecipeButton widget = ((RecipeBookPageAccessor) page).botania_getHoveredButton();
				if (widget != null) {
					return widget.getDisplayStack();
				}
				ItemStack stack = ((RecipeBookAccess) access.botania_getGhostSlots()).botania_getHoveredGhostRecipeStack();
				if (stack != null) {
					return stack;
				}
			}
		}

		return getRecipeViewerStack();
	}

	private static ItemStack getRecipeViewerStack() {
		for (var getter : CorporeaInputHandler.hoveredStackGetters) {
			ItemStack stack = getter.get();
			if (!stack.isEmpty()) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
}
