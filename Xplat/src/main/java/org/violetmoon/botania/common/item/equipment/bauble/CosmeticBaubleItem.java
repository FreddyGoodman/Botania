/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import org.violetmoon.botania.api.item.CosmeticBauble;
import org.violetmoon.botania.client.core.handler.StandaloneBlockModels;
import org.violetmoon.botania.client.core.helper.AccessoryRenderHelper;
import org.violetmoon.botania.client.render.AccessoryRenderRegistry;
import org.violetmoon.botania.client.render.AccessoryRenderer;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.common.proxy.Proxy;

import java.util.function.Consumer;

public class CosmeticBaubleItem extends BaubleItem implements CosmeticBauble {

	public enum Variant {
		BLACK_BOWTIE, BLACK_TIE, RED_GLASSES(true), PUFFY_SCARF,
		ENGINEER_GOGGLES(true), EYEPATCH(true), WICKED_EYEPATCH(true), RED_RIBBONS(true),
		PINK_FLOWER_BUD(true), POLKA_DOTTED_BOWS(true), BLUE_BUTTERFLY(true), CAT_EARS(true),
		WITCH_PIN, DEVIL_TAIL, KAMUI_EYE, GOOGLY_EYES(true),
		FOUR_LEAF_CLOVER, CLOCK_EYE(true), UNICORN_HORN(true), DEVIL_HORNS(true),
		HYPER_PLUS(true), BOTANIST_EMBLEM, ANCIENT_MASK(true), EERIE_MASK(true),
		ALIEN_ANTENNA(true), ANAGLYPH_GLASSES(true), ORANGE_SHADES(true), GROUCHO_GLASSES(true),
		THICK_EYEBROWS(true), LUSITANIC_SHIELD, TINY_POTATO_MASK(true), QUESTGIVER_MARK(true),
		THINKING_HAND(true);

		private final boolean isHead;

		Variant(boolean isHead) {
			this.isHead = isHead;
		}

		Variant() {
			this(false);
		}
	}

	private final Variant variant;

	public CosmeticBaubleItem(Variant variant, Properties props) {
		super(props);
		this.variant = variant;
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flags) {
		if (variant == Variant.THINKING_HAND) {
			tooltip.accept(Component.translatable("botaniamisc.cosmeticThinking").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		} else {
			tooltip.accept(Component.translatable("botaniamisc.cosmeticBauble").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		}
		super.appendHoverText(stack, context, display, tooltip, flags);
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void submit(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, SubmitNodeCollector collector, int light, float ageInTicks) {
			Variant variant = ((CosmeticBaubleItem) stack.getItem()).variant;
			var item = AccessoryRenderHelper.itemState(stack, living);
			if (variant.isHead) {
				bipedModel.head.translateAndRotate(ms);
				switch (variant) {
					case RED_GLASSES, ENGINEER_GOGGLES, ANAGLYPH_GLASSES -> {
						ms.translate(0, -0.225, -0.3);
						ms.scale(0.7F, -0.7F, -0.7F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case EYEPATCH -> {
						ms.translate(0.125, -0.225, -0.3);
						ms.mulPose(VecHelper.rotateY(180F));
						ms.scale(0.3F, -0.3F, -0.3F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case WICKED_EYEPATCH -> {
						ms.translate(-0.125, -0.225, -0.3);
						ms.scale(0.3F, -0.3F, -0.3F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case RED_RIBBONS -> {
						ms.translate(0, -0.65, 0.2);
						ms.mulPose(VecHelper.rotateY(180F));
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case PINK_FLOWER_BUD -> {
						ms.translate(0.275, -0.6, 0);
						ms.mulPose(VecHelper.rotateY(-90F));
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case POLKA_DOTTED_BOWS -> {
						ms.pushPose();
						ms.translate(0.275, -0.4, 0);
						ms.mulPose(VecHelper.rotateY(-90F));
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
						ms.popPose();
						ms.translate(-0.275, -0.4, 0);
						ms.mulPose(VecHelper.rotateY(90F));
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case BLUE_BUTTERFLY -> {
						ms.pushPose();
						ms.translate(0.275, -0.4, 0);
						ms.mulPose(VecHelper.rotateY(45F));
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
						ms.popPose();
						ms.translate(0.275, -0.4, 0);
						ms.mulPose(VecHelper.rotateY(-45F));
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case CAT_EARS -> {
						ms.translate(0F, -0.5F, -0.175F);
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case GOOGLY_EYES -> {
						ms.translate(0, -0.225, -0.3);
						ms.scale(0.9F, -0.9F, -0.9F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case CLOCK_EYE -> {
						ms.translate(0.1, -0.225, -0.3F);
						ms.scale(0.4F, -0.4F, -0.4F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case UNICORN_HORN -> {
						ms.translate(0, -0.7, -0.3);
						ms.mulPose(VecHelper.rotateY(-90F));
						ms.scale(0.6F, -0.6F, -0.6F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case DEVIL_HORNS -> {
						ms.translate(0F, -0.4F, -0.175F);
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case HYPER_PLUS -> {
						ms.translate(-0.15F, -0.45F, -0.3F);
						ms.scale(0.2F, -0.2F, -0.2F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
						ms.translate(1.45F, 0F, 0F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case ANCIENT_MASK -> {
						ms.translate(0, -0.3, -0.3);
						ms.scale(0.7F, -0.7F, -0.7F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case EERIE_MASK -> {
						ms.translate(0, -0.25, -0.3);
						ms.scale(0.75F, -0.75F, -0.75F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case ALIEN_ANTENNA -> {
						ms.translate(0, -0.65, 0.2);
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case ORANGE_SHADES -> {
						ms.translate(0, -0.3, -0.3);
						ms.scale(0.7F, -0.7F, -0.7F);
						int color = 0xFFFFFF | (178 << 24);
						AccessoryRenderHelper.submitCenteredTintedIcon(StandaloneBlockModels.ORANGE_SHADES, ms, collector, light, color);
					}
					case GROUCHO_GLASSES -> {
						ms.translate(0, -0.1, -0.3);
						ms.scale(0.75F, -0.75F, -0.75F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case THICK_EYEBROWS -> {
						ms.pushPose();
						ms.translate(-0.1, -0.3, -0.3);
						ms.scale(0.3F, -0.3F, -0.3F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
						ms.popPose();
						ms.translate(0.1, -0.3, -0.3);
						ms.mulPose(VecHelper.rotateY(180F));
						ms.scale(0.3F, -0.3F, -0.3F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case TINY_POTATO_MASK -> {
						ms.translate(0, -0.3, -0.3);
						ms.scale(0.6F, -0.6F, -0.6F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case QUESTGIVER_MARK -> {
						ms.translate(0, -0.8, -0.2);
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case THINKING_HAND -> {
						ms.translate(-0.1, 0, -0.3);
						ms.mulPose(VecHelper.rotateZ(-15F));
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					default -> {
					}
				}
			} else { // body cosmetics
				bipedModel.body.translateAndRotate(ms);
				switch (variant) {
					case BLACK_BOWTIE -> {
						ms.translate(0, 0.1, -0.13);
						ms.scale(0.6F, -0.6F, -0.6F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case BLACK_TIE, PUFFY_SCARF -> {
						ms.translate(0, 0.25, -0.15);
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case WITCH_PIN -> {
						ms.translate(-0.1, 0.15, -0.15);
						ms.scale(0.2F, -0.2F, -0.2F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case DEVIL_TAIL -> {
						ms.translate(0, 0.55, 0.2);
						ms.mulPose(VecHelper.rotateY(-90F));
						ms.scale(0.6F, -0.6F, -0.6F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case KAMUI_EYE -> { // DON'T LOSE YOUR WAAAAAAAAY
						ms.pushPose();
						ms.translate(0.4, 0.1, -0.2);
						ms.scale(0.5F, -0.5F, -0.5F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
						ms.popPose();
						ms.translate(-0.4, 0.1, -0.2);
						ms.mulPose(VecHelper.rotateY(180F));
						ms.scale(0.5F, -0.5F, -0.5F);
						AccessoryRenderHelper.submitCenteredTintedIcon(StandaloneBlockModels.KAMUI_EYE, ms, collector, light, 0xFF00004C);
					}
					case FOUR_LEAF_CLOVER -> {
						ms.translate(0.1, 0.1, -0.13);
						ms.scale(0.3F, -0.3F, -0.3F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case BOTANIST_EMBLEM -> {
						ms.translate(0F, 0.375, -0.13);
						ms.scale(0.3F, -0.3F, -0.3F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					case LUSITANIC_SHIELD -> {
						ms.translate(0F, 0.35, 0.13);
						ms.mulPose(VecHelper.rotateZ(8F));
						ms.mulPose(VecHelper.rotateY(180F));
						ms.scale(0.6F, -0.6F, -0.6F);
						item.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
					default -> {
					}
				}
			}
		}
	}

}
