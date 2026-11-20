/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;

import org.violetmoon.botania.client.core.handler.StandaloneBlockModels;
import org.violetmoon.botania.common.handler.ContributorList;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import org.violetmoon.botania.xplat.BotaniaConfig;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.List;
import java.util.Locale;

public final class PlayerDecorationLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
	private static final int FULL_BRIGHT = 0xF000F0;

	public PlayerDecorationLayer(RenderLayerParent<AvatarRenderState, PlayerModel> renderer) {
		super(renderer);
	}

	public static void extract(Avatar avatar, AvatarRenderState avatarState) {
		State state = ((DecorationState) avatarState).botania$getDecorations();
		state.flower.clear();
		state.tablets[0].clear();
		state.tablets[1].clear();
		state.goldfish = false;
		state.willFlame = false;
		if (!(avatar instanceof Player player)) {
			return;
		}

		ContributorList.firstStart();
		var helmet = player.getItemBySlot(EquipmentSlot.HEAD);
		state.willFlame = helmet.getItem() instanceof TerrasteelHelmItem terraHelm
				&& TerrasteelHelmItem.hasAnyWill(helmet) && !terraHelm.hasPhantomInk(helmet);
		if (avatarState.isInvisible) {
			return;
		}

		String name = player.getGameProfile().name();
		state.goldfish = name.equals("haighyorkie");
		var resolver = Minecraft.getInstance().getItemModelResolver();
		if (avatarState.showCape) {
			var flower = ContributorList.getFlower(name.toLowerCase(Locale.ROOT));
			resolver.updateForLiving(state.flower, flower, ItemDisplayContext.NONE, player);
		}

		if (BotaniaConfig.client().renderAccessories()) {
			state.leggings = !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty();
			var inventory = player.getInventory();
			int tablet = 0;
			for (int slot = 0; slot < inventory.getContainerSize() && tablet < state.tablets.length; slot++) {
				var stack = inventory.getItem(slot);
				if (stack.is(BotaniaItems.MANA_TABLET)) {
					resolver.updateForTopItem(state.tablets[tablet++], stack, ItemDisplayContext.NONE, player.level(), null, player.getId());
				}
			}
		}
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector collector, int light, AvatarRenderState avatarState, float yaw, float pitch) {
		State state = ((DecorationState) avatarState).botania$getDecorations();
		if (state.goldfish) {
			var goldfish = ClientXplatAbstractions.INSTANCE.findBlockModelPart(StandaloneBlockModels.GOLDFISH);
			if (goldfish != null) {
				pose.pushPose();
				getParentModel().head.translateAndRotate(pose);
				pose.translate(-0.15F, -0.60F, 0);
				pose.scale(0.4F, -0.4F, -0.4F);
				collector.submitBlockModel(pose, Sheets.translucentItemSheet(), List.of(goldfish),
						BlockModelRenderState.EMPTY_TINTS, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
				pose.popPose();
			}
		}

		if (!state.flower.isEmpty()) {
			pose.pushPose();
			getParentModel().head.translateAndRotate(pose);
			pose.translate(0, -0.75, 0);
			pose.scale(0.5F, -0.5F, -0.5F);
			state.flower.submit(pose, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
			pose.popPose();
		}

		double lateralOffset = state.leggings ? 0.3 : 0.25;
		for (int index = 0; index < state.tablets.length; index++) {
			var tablet = state.tablets[index];
			if (!tablet.isEmpty()) {
				pose.pushPose();
				getParentModel().body.translateAndRotate(pose);
				pose.translate(index == 0 ? -lateralOffset : lateralOffset, 0.65, 0);
				pose.mulPose(VecHelper.rotateY(index == 0 ? 90F : -90F));
				pose.scale(0.375F, -0.375F, -0.375F);
				tablet.submit(pose, collector, FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
				pose.popPose();
			}
		}

		if (state.willFlame) {
			var will = ClientXplatAbstractions.INSTANCE.findBlockModelPart(StandaloneBlockModels.TERRASTEEL_WILL);
			if (will != null) {
				pose.pushPose();
				getParentModel().head.translateAndRotate(pose);
				pose.translate(-0.2, -0.15, -0.3);
				pose.scale(0.4F, -0.4F, -0.4F);
				collector.submitBlockModel(pose, Sheets.cutoutItemSheet(), List.of(will),
						BlockModelRenderState.EMPTY_TINTS, light, OverlayTexture.NO_OVERLAY, 0);
				pose.popPose();
			}
		}
	}

	public interface DecorationState {
		State botania$getDecorations();
	}

	public static final class State {
		private final ItemStackRenderState flower = new ItemStackRenderState();
		private final ItemStackRenderState[] tablets = { new ItemStackRenderState(), new ItemStackRenderState() };
		private boolean leggings;
		private boolean goldfish;
		private boolean willFlame;
	}
}
