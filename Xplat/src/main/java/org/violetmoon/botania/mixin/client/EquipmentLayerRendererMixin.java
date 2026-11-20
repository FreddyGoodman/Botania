/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.client.model.armor.ArmorModels;
import org.violetmoon.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

@Mixin(EquipmentLayerRenderer.class)
public abstract class EquipmentLayerRendererMixin {
	@Inject(
		method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;"
				+ "Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;"
				+ "Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;"
				+ "Lnet/minecraft/client/renderer/SubmitNodeCollector;II)V",
		at = @At("HEAD"), cancellable = true
	)
	private void botania_submitCustomArmor(EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> asset,
			Model<?> originalModel, Object state, ItemStack stack, PoseStack pose, SubmitNodeCollector collector,
			int light, int outline, CallbackInfo ci) {
		if (!(stack.getItem() instanceof ManasteelArmorItem armor) || !(state instanceof HumanoidRenderState humanoid)) {
			return;
		}
		var model = ArmorModels.get(stack);
		if (model == null) {
			return;
		}
		Identifier texture = armor.getArmorTexture(stack, armor.getEquipmentSlot());
		collector.order(1).submitModel(model, humanoid, pose, RenderTypes.armorCutoutNoCull(texture),
				light, OverlayTexture.NO_OVERLAY, -1, null, outline, null);
		if (stack.hasFoil()) {
			collector.order(2).submitModel(model, humanoid, pose, RenderTypes.armorEntityGlint(),
					light, OverlayTexture.NO_OVERLAY, -1, null, outline, null);
		}
		ci.cancel();
	}
}
