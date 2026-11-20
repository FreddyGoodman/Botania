/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric.integration.trinkets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.client.render.AccessoryRenderRegistry;
import org.violetmoon.botania.common.item.equipment.bauble.BaubleItem;

import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.client.TrinketRenderer;
import eu.pb4.trinkets.api.client.TrinketRendererRegistry;

final class TrinketsRendering implements TrinketRenderer {
	static void register(Item item) {
		TrinketRendererRegistry.registerRenderer(item, new TrinketsRendering());
	}

	@Override
	public void submit(ItemStack stack, TrinketSlotAccess slot, EntityModel<? extends LivingEntityRenderState> contextModel,
			PoseStack pose, SubmitNodeCollector collector, int light, LivingEntityRenderState state, float yaw, float pitch) {
		LivingEntity living = slot.inventory().getAttachment().getEntity();
		BaubleItem item = (BaubleItem) stack.getItem();
		if (!item.hasRender(stack, living) || !(contextModel instanceof HumanoidModel<?> model)) {
			return;
		}

		ItemStack cosmetic = item.getCosmeticItem(stack);
		if (!cosmetic.isEmpty()) {
			TrinketRendererRegistry.getRenderer(cosmetic.getItem())
					.ifPresent(renderer -> renderer.submit(cosmetic, slot, contextModel, pose, collector, light, state, yaw, pitch));
		} else {
			var renderer = AccessoryRenderRegistry.get(stack);
			if (renderer != null) {
				renderer.submit(model, stack, living, pose, collector, light, state.ageInTicks);
			}
		}
	}
}
