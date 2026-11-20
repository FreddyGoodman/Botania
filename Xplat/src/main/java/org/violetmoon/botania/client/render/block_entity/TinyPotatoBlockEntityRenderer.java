/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.block_entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.handler.StandaloneBlockModels;
import org.violetmoon.botania.client.model.TinyPotatoModel;
import org.violetmoon.botania.common.block.block_entity.TinyPotatoBlockEntity;
import org.violetmoon.botania.common.handler.ContributorList;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.block.TinyPotatoBlockItem;
import org.violetmoon.botania.common.item.equipment.bauble.FlugelTiaraItem;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.Arrays;
import java.util.List;

public class TinyPotatoBlockEntityRenderer implements BlockEntityRenderer<TinyPotatoBlockEntity, TinyPotatoBlockEntityRenderer.State> {
	private static final BlockDisplayContext CAKE_CONTEXT = BlockDisplayContext.create();
	private static final Direction[] SIDES = Direction.values();
	private final ItemModelResolver itemModels;
	private final BlockModelResolver blockModels;
	private final Font font;

	public TinyPotatoBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		itemModels = ctx.itemModelResolver();
		blockModels = ctx.blockModelResolver();
		font = ctx.font();
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(TinyPotatoBlockEntity potato, State state, float partialTick, Vec3 camera,
			ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(potato, state, partialTick, camera, crumblingOverlay);
		state.potato = potato;
		state.name = potato.name != null ? potato.name.copy() : null;
		state.variant = TinyPotatoModel.variantName(state.name);
		state.enchanted = TinyPotatoBlockItem.isEnchantedName(state.name, null);
		state.facing = potato.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		state.yaw = switch (state.facing) {
			case NORTH -> 0F;
			case EAST -> 90F;
			case WEST -> 270F;
			default -> 180F;
		};
		float jump = potato.jumpTicks;
		if (jump > 0) {
			jump -= partialTick;
		}
		state.jump = (float) Math.sin(jump / 10 * Math.PI);
		boolean hideBody = state.variant.equals("mami") || state.variant.equals("soaryn") || state.variant.equals("eloraam") && jump != 0;
		state.body = hideBody ? List.of() : TinyPotatoModel.collectQuads(TinyPotatoModel.getVariant(state.variant));
		state.partialTick = partialTick;
		state.haloTime = ClientTickHandler.getEntityTicksInGame() + partialTick;

		for (int slot = 0; slot < state.items.length; slot++) {
			var item = state.items[slot];
			var stack = potato.getItemHandler().getItem(slot);
			itemModels.updateForTopItem(item.render, stack, ItemDisplayContext.HEAD, potato.getLevel(), null, 0);
			item.block = stack.getItem() instanceof BlockItem;
			item.potato = stack.getItem() instanceof TinyPotatoBlockItem;
			item.skull = stack.getItem() instanceof BlockItem block && block.getBlock() instanceof AbstractSkullBlock;
			item.offsetChild = item.potato && stack.has(DataComponents.CUSTOM_NAME)
					&& TinyPotatoModel.variantName(stack.getHoverName()).equals("kingdaddydmac");
		}

		var mc = Minecraft.getInstance();
		state.showName = Minecraft.renderNames() && state.name != null && !state.variant.isEmpty() && mc.hitResult instanceof BlockHitResult hit
				&& hit.getType() == BlockHitResult.Type.BLOCK && potato.getBlockPos().equals(hit.getBlockPos());
		if (state.showName) {
			state.label = state.name.getVisualOrderText();
			state.labelWidth = font.width(state.label);
			state.labelBackground = (int) (mc.options.getBackgroundOpacity(0.25F) * 255F) << 24;
		}

		ItemStack props = ItemStack.EMPTY;
		state.cake.clear();
		if (!state.variant.isEmpty()) {
			ContributorList.firstStart();
			props = switch (state.variant) {
				case "martysgames", "marty" -> {
					ItemStack fruit = new ItemStack(BotaniaItems.FRUIT_OF_GRISAIA);
					fruit.set(DataComponents.CUSTOM_NAME, Component.literal("das boot"));
					yield fruit;
				}
				case "kingdaddydmac" -> {
					blockModels.update(state.cake, Blocks.CAKE.defaultBlockState(), CAKE_CONTEXT);
					yield new ItemStack(BotaniaItems.BAND_OF_MANA);
				}
				default -> ContributorList.getFlower(state.variant);
			};
		}
		itemModels.updateForTopItem(state.propItem, props, ItemDisplayContext.HEAD, potato.getLevel(), null, 0);
	}

	@Override
	public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0.5, 0, 0.5);
		pose.mulPose(VecHelper.rotateY(-state.yaw));
		pose.translate(state.jump * 0.05F, Math.abs(state.jump) * 0.2F, 0);
		pose.mulPose(VecHelper.rotateZ(state.jump * 2F));

		if (!state.body.isEmpty()) {
			pose.pushPose();
			pose.translate(-0.5, 0, -0.5);
			collector.submitItem(pose, ItemDisplayContext.NONE, state.lightCoords, OverlayTexture.NO_OVERLAY, 0,
					BlockModelRenderState.EMPTY_TINTS, state.body,
					state.enchanted ? ItemStackRenderState.FoilType.STANDARD : ItemStackRenderState.FoilType.NONE);
			pose.popPose();
		}

		pose.translate(0, 1.5, 0);
		pose.pushPose();
		pose.mulPose(VecHelper.rotateZ(180));
		submitItems(state, pose, collector);
		pose.pushPose();
		ClientXplatAbstractions.INSTANCE.fireRenderTinyPotato(state.potato, state.name, state.partialTick,
				pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
		pose.popPose();
		pose.popPose();

		if (state.showName) {
			pose.mulPose(VecHelper.rotateZ(-state.jump * 2F));
			pose.mulPose(VecHelper.rotateY(state.yaw));
			submitName(state, pose, collector, camera);
		}
		pose.popPose();
	}

	private void submitName(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
		pose.pushPose();
		pose.translate(0, -0.6, 0);
		pose.mulPose(camera.orientation);
		float scale = 0.016666668F * 1.6F;
		pose.scale(-scale, -scale, scale);
		submitLabel(state.label, state.labelWidth, state.labelBackground, state.lightCoords,
				Font.DisplayMode.NORMAL, pose, collector);
		if (state.variant.equals("pahimar") || state.variant.equals("soaryn")) {
			pose.translate(0, 14, 0);
			var label = FormattedCharSequence.forward(state.variant.equals("pahimar") ? "[WIP]" : "(soon)", Style.EMPTY);
			submitLabel(label, font.width(label), state.labelBackground, state.lightCoords,
					Font.DisplayMode.SEE_THROUGH, pose, collector);
		}
		pose.popPose();
	}

	private static void submitLabel(FormattedCharSequence label, int width, int background, int light, Font.DisplayMode mode,
			PoseStack pose, SubmitNodeCollector collector) {
		int x = -width / 2;
		collector.submitText(pose, x, 0, label, false, Font.DisplayMode.SEE_THROUGH, light, 0x20FFFFFF, background, 0);
		collector.submitText(pose, x, 0, label, false, mode, light, 0xFFFFFFFF, 0, 0);
	}

	private void submitItems(State state, PoseStack ms, SubmitNodeCollector collector) {
		int light = state.lightCoords;
		ms.pushPose();
		ms.mulPose(VecHelper.rotateZ(180F));
		ms.translate(0F, -1F, 0F);
		float size = 1F / 3.5F;
		ms.scale(size, size, size);
		for (int slot = 0; slot < state.items.length; slot++) {
			AttachedItem item = state.items[slot];
			if (item.render.isEmpty()) {
				continue;
			}
			Direction side = SIDES[slot];
			if (side.getAxis() != Axis.Y) {
				side = Direction.fromYRot(side.toYRot() - state.facing.toYRot());
			}
			boolean block = item.block;
			boolean mySon = item.potato;
			ms.pushPose();
			switch (side) {
				case UP -> {
					if (mySon) {
						ms.translate(0F, -0.375F, 0.5F);
					} else if (block) {
						ms.translate(0F, 0.3F, 0.5F);
					}
					ms.translate(0F, -0.5F, -0.4F);
				}
				case DOWN -> {
					ms.translate(0F, -2.3F, -0.88F);
					if (mySon) {
						ms.translate(0F, 1.25F, 0.5F);
					} else if (block) {
						ms.translate(0F, 1F, 0.6F);
					}
				}
				case NORTH -> {
					ms.translate(0F, -1.9F, 0.02F);
					if (mySon) {
						ms.translate(0F, 0.2F, 0.57F);
					} else if (block) {
						ms.translate(0F, 1F, 0.6F);
					}
				}
				case SOUTH -> {
					ms.translate(0F, -1.6F, -0.89F);
					if (mySon) {
						ms.translate(0F, -0.59F, 0.26F);
					} else if (block) {
						ms.translate(0F, 1F, 0.5F);
					}
				}
				case EAST -> {
					if (mySon) {
						ms.translate(-0.35F, -0.29F, -0.06F);
					} else if (block) {
						ms.translate(-0.4F, 0.8F, 0F);
					} else {
						ms.mulPose(VecHelper.rotateY(-90F));
					}
					ms.translate(-0.3F, -1.9F, 0.04F);
				}
				case WEST -> {
					if (mySon) {
						ms.translate(0.95F, -0.29F, 0.9F);
						if (item.offsetChild) {
							ms.translate(0.55F, 0, 0);
						}
					} else if (block) {
						ms.translate(1F, 0.8F, 1F);
					} else {
						ms.mulPose(VecHelper.rotateY(-90F));
					}
					ms.translate(-0.3F, -1.9F, -0.92F);
				}
			}

			if (mySon) {
				ms.scale(1.1F, 1.1F, 1.1F);
			} else if (block) {
				ms.scale(0.5F, 0.5F, 0.5F);
			}
			if (block) {
				if (side == Direction.NORTH) {
					ms.mulPose(VecHelper.rotateY(180F));
				}
				if (item.skull) {
					// heads are always rendered the wrong way around for some reason
					ms.mulPose(VecHelper.rotateY(180F));
				}
			}
			item.render.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
			ms.popPose();
		}
		ms.popPose();

		String name = state.variant;
		if (!name.isEmpty()) {
			ms.pushPose();
			ms.translate(0F, 1F, 0F);
			ms.scale(0.25F, 0.25F, 0.25F);
			switch (name) {
				case "phi", "vazkii" -> {
					ms.pushPose();
					ms.translate(-0.08, 0.1, 0.4);
					ms.mulPose(VecHelper.rotateY(90F));
					ms.mulPose(new Quaternionf().rotateAxis(VecHelper.toRadians(20), 1, 0, 1));
					submitProp(ms, collector, light, StandaloneBlockModels.PHI_FLOWER);
					ms.popPose();
					if (name.equals("vazkii")) {
						ms.scale(1.25F, 1.25F, 1.25F);
						ms.mulPose(VecHelper.rotateX(180F));
						ms.mulPose(VecHelper.rotateY(-90F));
						ms.translate(0.2, -1.25, -0.075);
						submitProp(ms, collector, light, StandaloneBlockModels.NERF_BAT);
					}
				}
				case "haighyorkie" -> {
					ms.scale(1.25F, 1.25F, 1.25F);
					ms.mulPose(VecHelper.rotateZ(180F));
					ms.mulPose(VecHelper.rotateY(-90F));
					ms.translate(-0.5F, -1.2F, -0.075F);
					submitProp(ms, collector, light, StandaloneBlockModels.GOLDFISH);
				}
				case "martysgames", "marty" -> {
					ms.scale(0.7F, 0.7F, 0.7F);
					ms.mulPose(VecHelper.rotateZ(180F));
					ms.translate(-0.3F, -2.7F, -1.2F);
					ms.mulPose(VecHelper.rotateZ(15F));
					state.propItem.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
				}
				case "jibril" -> {
					ms.scale(1.5F, 1.5F, 1.5F);
					ms.translate(0F, 0.8F, 0F);
					FlugelTiaraItem.ClientLogic.submitHalo(ms, collector, state.haloTime);
				}
				case "kingdaddydmac" -> {
					ms.scale(0.5F, 0.5F, 0.5F);
					ms.mulPose(VecHelper.rotateZ(180));
					ms.mulPose(VecHelper.rotateY(90));
					ms.pushPose();
					ms.translate(0F, -2.5F, 0.65F);
					state.propItem.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					ms.translate(0F, 0F, -4F);
					state.propItem.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					ms.popPose();
					ms.translate(1.5, -4, -2.5);
					state.cake.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
				}
				default -> {
					if (!state.propItem.isEmpty()) {
						ms.mulPose(VecHelper.rotateX(180));
						ms.mulPose(VecHelper.rotateY(180));
						ms.translate(0, -0.78, -0.5);
						state.propItem.submit(ms, collector, light, OverlayTexture.NO_OVERLAY, 0);
					}
				}
			}
			ms.popPose();
		}
	}

	private static void submitProp(PoseStack pose, SubmitNodeCollector collector, int light, Identifier id) {
		var model = ClientXplatAbstractions.INSTANCE.getBlockModelPart(id);
		collector.submitBlockModel(pose, RenderTypes.translucentMovingBlock(), List.of(model), BlockModelRenderState.EMPTY_TINTS,
				light, OverlayTexture.NO_OVERLAY, 0);
	}

	private static class AttachedItem {
		private final ItemStackRenderState render = new ItemStackRenderState();
		private boolean block;
		private boolean potato;
		private boolean skull;
		private boolean offsetChild;
	}

	public static class State extends BlockEntityRenderState {
		private final AttachedItem[] items = new AttachedItem[SIDES.length];
		private final ItemStackRenderState propItem = new ItemStackRenderState();
		private final BlockModelRenderState cake = new BlockModelRenderState();
		private TinyPotatoBlockEntity potato;
		private Direction facing;
		private @Nullable Component name;
		private String variant;
		private List<BakedQuad> body = List.of();
		private float yaw;
		private float jump;
		private float partialTick;
		private float haloTime;
		private boolean enchanted;
		private boolean showName;
		private FormattedCharSequence label;
		private int labelWidth;
		private int labelBackground;

		public State() {
			Arrays.setAll(items, slot -> new AttachedItem());
		}
	}
}
