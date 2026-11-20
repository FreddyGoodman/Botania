/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.neoforge.integration.curios;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.client.render.AccessoryRenderRegistry;
import org.violetmoon.botania.common.item.equipment.bauble.BaubleItem;

import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

final class CuriosRendering implements ICurioRenderer {
	static void register(Item item) {
		ICurioRenderer.register(item, CuriosRendering::new);
	}

	@Override
	public <S extends LivingEntityRenderState, M extends EntityModel<? super S>> void render(ItemStack stack,
			SlotContext slotContext, PoseStack pose, SubmitNodeCollector collector, int light, S renderState,
			RenderLayerParent<S, M> renderLayerParent, EntityRendererProvider.Context context, float yaw, float pitch) {
		LivingEntity living = slotContext.entity();
		BaubleItem item = (BaubleItem) stack.getItem();
		if (!item.hasRender(stack, living) || !(renderLayerParent.getModel() instanceof HumanoidModel<?> model)) {
			return;
		}

		pose.pushPose();
		ItemStack cosmetic = item.getCosmeticItem(stack);
		if (!cosmetic.isEmpty()) {
			ICurioRenderer.get(cosmetic).render(cosmetic, slotContext, pose, collector, light,
					renderState, renderLayerParent, context, yaw, pitch);
		} else {
			var renderer = AccessoryRenderRegistry.get(stack);
			if (renderer != null) {
				renderer.submit(model, stack, living, pose, collector, light, renderState.ageInTicks);
			}
		}
		pose.popPose();
	}
}
