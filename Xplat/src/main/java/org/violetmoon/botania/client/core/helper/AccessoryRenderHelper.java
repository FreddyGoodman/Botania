/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.core.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.ArrayList;
import java.util.List;

public final class AccessoryRenderHelper {

	/**
	 * Rotates the render for a bauble correctly if the player is sneaking.
	 */
	public static void rotateIfSneaking(PoseStack ms, LivingEntity living) {
		if (living.isCrouching()) {
			ms.translate(0F, 0.2F, 0F);
			ms.mulPose(VecHelper.rotateX(90F / (float) Math.PI));
		}
	}

	public static ItemStackRenderState itemState(ItemStack stack, LivingEntity entity) {
		var state = new ItemStackRenderState();
		Minecraft.getInstance().getItemModelResolver().updateForLiving(state, stack, ItemDisplayContext.NONE, entity);
		return state;
	}

	public static void submitItem(ItemStack stack, LivingEntity entity, PoseStack pose, SubmitNodeCollector collector, int light) {
		itemState(stack, entity).submit(pose, collector, light, OverlayTexture.NO_OVERLAY, 0);
	}

	public static void submitIcon(Identifier id, PoseStack pose, SubmitNodeCollector collector, int light) {
		var model = ClientXplatAbstractions.INSTANCE.findBlockModelPart(id);
		if (model != null) {
			collector.submitBlockModel(pose, Sheets.cutoutItemSheet(), List.of(model),
					BlockModelRenderState.EMPTY_TINTS, light, OverlayTexture.NO_OVERLAY, 0);
		}
	}

	public static List<BakedQuad> iconQuads(BlockStateModelPart model) {
		var quads = new ArrayList<>(model.getQuads(null));
		for (Direction side : Direction.values()) {
			quads.addAll(model.getQuads(side));
		}
		return List.copyOf(quads);
	}

	public static void submitCenteredIcon(List<BakedQuad> quads, ItemStack stack, PoseStack pose, SubmitNodeCollector collector, int light) {
		pose.pushPose();
		pose.translate(-0.5, -0.5, -0.5);
		var foil = stack.hasFoil() ? ItemStackRenderState.FoilType.STANDARD : ItemStackRenderState.FoilType.NONE;
		collector.submitItem(pose, ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, 0, BlockModelRenderState.EMPTY_TINTS, quads, foil);
		pose.popPose();
	}

	public static void submitTintedIcon(List<BakedQuad> quads, PoseStack pose, SubmitNodeCollector collector, int light, int color) {
		var layer = (color >>> 24) < 255 ? Sheets.translucentItemSheet() : Sheets.cutoutItemSheet();
		collector.submitCustomGeometry(pose, layer, (transform, buffer) -> {
			var instance = new QuadInstance();
			instance.setColor(color);
			instance.setLightCoords(light);
			instance.setOverlayCoords(OverlayTexture.NO_OVERLAY);
			for (var quad : quads) {
				buffer.putBakedQuad(transform, quad, instance);
			}
		});
	}

	public static void submitCenteredTintedIcon(Identifier id, PoseStack pose, SubmitNodeCollector collector, int light, int color) {
		var model = ClientXplatAbstractions.INSTANCE.findBlockModelPart(id);
		if (model != null) {
			pose.pushPose();
			pose.translate(-0.5, -0.5, -0.5);
			submitTintedIcon(iconQuads(model), pose, collector, light, color);
			pose.popPose();
		}
	}
}
