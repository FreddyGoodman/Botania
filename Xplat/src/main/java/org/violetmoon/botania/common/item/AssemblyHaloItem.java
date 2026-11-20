/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.*;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.client.core.helper.RenderHelper;
import org.violetmoon.botania.client.gui.crafting.AssemblyHaloContainer;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.annotations.SoftImplement;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.component.StoredIds;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.common.helper.DataComponentHelper;
import org.violetmoon.botania.common.helper.PlayerHelper;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.network.clientbound.HaloCraftEffectPacket;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.function.Supplier;

public class AssemblyHaloItem extends Item implements ClientInventoryTick {

	private static final Identifier glowTexture = Identifier.parse(ResourcesLib.MISC_GLOW_GREEN);
	private static final Supplier<ItemStack> craftingTable = Suppliers.memoize(() -> new ItemStack(Blocks.CRAFTING_TABLE));

	public static final int SEGMENTS = 12;

	public AssemblyHaloItem(Properties props) {
		super(props.component(BotaniaDataComponents.STORED_RECIPES, StoredIds.EMPTY));
	}

	@Override
	public InteractionResult use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide()) {
			int segment = getSegmentLookedAt(stack, player);
			RecipeHolder<? extends Recipe<CraftingInput>> recipe = getSavedRecipe(world, stack, segment);

			if (segment == 0) {
				// Pos is never used by workbench, so use origin.
				// But this cannot be the static dummy one in the interface, we have to pass an actual one for the
				// crafting matrix to update properly.
				ContainerLevelAccess wp = ContainerLevelAccess.create(world, BlockPos.ZERO);
				player.openMenu(new SimpleMenuProvider(
						(windowId, playerInv, p) -> new AssemblyHaloContainer(windowId, playerInv, wp),
						stack.getHoverName()));
			} else {
				if (recipe == null) {
					RecipeHolder<? extends Recipe<CraftingInput>> lastRecipe = getLastRecipe(world, stack);
					if (lastRecipe != null) {
						saveRecipe(stack, lastRecipe.id().identifier(), segment);
					}
				} else {
					tryCraft(player, stack, segment, true);
				}
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
		tickInInventory(stack, world, entity, slot);
	}

	@Override
	public void tickInInventory(ItemStack stack, Level world, Entity entity, @Nullable EquipmentSlot slot) {
		if (!(entity instanceof LivingEntity living)) {
			return;
		}

		boolean eqLastTick = wasEquipped(stack);

		boolean equipped = slot == EquipmentSlot.MAINHAND || living.getOffhandItem() == stack;

		if (eqLastTick != equipped) {
			setEquipped(stack, equipped);
		}

		if (!equipped) {
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = segAngles / 2.0F;
			setRotationBase(stack, getCheckingAngle((LivingEntity) entity) - shift);
		}
	}

	private static boolean canCraftHeuristic(Player player, Recipe<CraftingInput> recipe) {
		StackedItemContents contents = new StackedItemContents();
		player.getInventory().fillStackedContents(contents);
		return contents.canCraft(recipe, null);
	}

	void tryCraft(Player player, ItemStack halo, int slot, boolean particles) {
		if (!(player.level() instanceof ServerLevel level)) {
			return;
		}
		RecipeHolder<CraftingRecipe> recipe = getSavedRecipe(level, halo, slot);
		if (recipe == null || !canCraftHeuristic(player, recipe.value())) {
			return;
		}

		// Stage ingredient extraction and output insertion before updating the player inventory.
		Inventory inventory = player.getInventory();
		Inventory staged = new Inventory(player, new EntityEquipment());
		staged.setSelectedSlot(inventory.getSelectedSlot());
		for (int i = 0; i < inventory.getNonEquipmentItems().size(); i++) {
			staged.setItem(i, inventory.getItem(i).copy());
		}
		staged.setItem(Inventory.SLOT_OFFHAND, inventory.getItem(Inventory.SLOT_OFFHAND).copy());

		CraftingInput input = placeRecipe(level, staged, recipe);
		if (!recipe.value().matches(input, level)) {
			return;
		}
		ItemStack output = recipe.value().assemble(input);
		if (output.isEmpty() || !staged.add(output) || !output.isEmpty()) {
			return;
		}

		for (int i = 0; i < inventory.getNonEquipmentItems().size(); i++) {
			ItemStack stagedStack = staged.getItem(i);
			if (!ItemStack.matches(inventory.getItem(i), stagedStack)) {
				inventory.setItem(i, stagedStack);
			}
		}
		ItemStack offhand = staged.getItem(Inventory.SLOT_OFFHAND);
		if (!ItemStack.matches(inventory.getItem(Inventory.SLOT_OFFHAND), offhand)) {
			inventory.setItem(Inventory.SLOT_OFFHAND, offhand);
		}
		inventory.setChanged();
		recipe.value().getRemainingItems(input).forEach(inventory::placeItemBackInInventory);

		if (particles) {
			XplatAbstractions.INSTANCE.sendToTracking(player, new HaloCraftEffectPacket(player.getId()));
		}
	}

	private static CraftingInput placeRecipe(ServerLevel level, Inventory inventory, RecipeHolder<CraftingRecipe> recipe) {
		CraftingMenu menu = new CraftingMenu(-999, inventory);
		var inputSlots = menu.getInputGridSlots();
		CraftingContainer grid = (CraftingContainer) inputSlots.getFirst().container;
		var access = new ServerPlaceRecipe.CraftingMenuAccess<CraftingRecipe>() {
			@Override
			public void fillCraftSlotsStackedContents(StackedItemContents contents) {
				grid.fillStackedContents(contents);
			}

			@Override
			public void clearCraftingContent() {
				grid.clearContent();
			}

			@Override
			public boolean recipeMatches(RecipeHolder<CraftingRecipe> candidate) {
				return candidate.value().matches(grid.asCraftInput(), level);
			}
		};
		ServerPlaceRecipe.placeRecipe(access, grid.getWidth(), grid.getHeight(), inputSlots, inputSlots, inventory, recipe, false, false);
		return grid.asCraftInput();
	}

	@SoftImplement("IItemExtension")
	public boolean onEntitySwing(ItemStack stack, LivingEntity living, InteractionHand hand) {
		int segment = getSegmentLookedAt(stack, living);
		if (segment == 0) {
			return false;
		}

		RecipeHolder<? extends Recipe<CraftingInput>> recipe = getSavedRecipe(living.level(), stack, segment);
		if (recipe != null && living.isShiftKeyDown()) {
			saveRecipe(stack, null, segment);
			return true;
		}

		return false;
	}

	protected static int getSegmentLookedAt(ItemStack stack, LivingEntity living) {
		float yaw = getCheckingAngle(living, getRotationBase(stack));

		int angles = 360;
		int segAngles = angles / SEGMENTS;
		for (int seg = 0; seg < SEGMENTS; seg++) {
			float calcAngle = (float) seg * segAngles;
			if (yaw >= calcAngle && yaw < calcAngle + segAngles) {
				return seg;
			}
		}
		return -1;
	}

	private static float getCheckingAngle(LivingEntity living) {
		return getCheckingAngle(living, 0F);
	}

	// Screw the way minecraft handles rotation
	// Really...
	private static float getCheckingAngle(LivingEntity living, float base) {
		float yaw = Mth.wrapDegrees(living.getYRot()) + 90F;
		int angles = 360;
		int segAngles = angles / SEGMENTS;
		float shift = segAngles / 2f;

		if (yaw < 0) {
			yaw = 180F + (180F + yaw);
		}
		yaw -= 360F - base;
		float angle = 360F - yaw + shift;

		if (angle < 0) {
			angle = 360F + angle;
		}

		return angle;
	}

	@Nullable
	private static RecipeHolder<CraftingRecipe> getSavedRecipe(Level world, ItemStack halo, int segment) {
		if (segment <= 0 || segment >= SEGMENTS) {
			return null;
		}

		var storedRecipes = halo.getOrDefault(BotaniaDataComponents.STORED_RECIPES, StoredIds.EMPTY);
		Identifier id = storedRecipes.getSlot(segment - 1);
		return id != null ? getRecipeHolder(world, id) : null;
	}

	private static void saveRecipe(ItemStack halo, @Nullable Identifier id, int segment) {
		if (segment <= 0 || segment >= SEGMENTS) {
			return;
		}

		var storedRecipes = halo.getOrDefault(BotaniaDataComponents.STORED_RECIPES, StoredIds.EMPTY);
		halo.set(BotaniaDataComponents.STORED_RECIPES, storedRecipes.store(segment - 1, id));
	}

	private static ItemStack getDisplayItem(Level world, ItemStack stack, int segment) {
		if (segment == 0) {
			return craftingTable.get();
		} else if (segment >= SEGMENTS) {
			return ItemStack.EMPTY;
		} else {
			RecipeHolder<? extends Recipe<CraftingInput>> recipeHolder = getSavedRecipe(world, stack, segment);
			if (recipeHolder != null) {
				return getRecipePreview(world, recipeHolder.value());
			} else {
				return ItemStack.EMPTY;
			}
		}
	}

	public static void onItemCrafted(Player player, Container inv) {
		AbstractContainerMenu container = player.containerMenu;

		if (!(container instanceof AssemblyHaloContainer) ||
				!(inv instanceof CraftingContainer cc)) {
			return;
		}

		CraftingInput input = CraftingInput.of(cc.getWidth(), cc.getHeight(), cc.getItems());
		BotaniaRecipeTypes.findRecipe(player.level(), RecipeType.CRAFTING, input).ifPresent(recipe -> {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (!stack.isEmpty() && stack.getItem() instanceof AssemblyHaloItem) {
					rememberLastRecipe(recipe.id().identifier(), stack);
				}
			}
		});
	}

	private static void rememberLastRecipe(Identifier recipeId, ItemStack halo) {
		DataComponentHelper.setOptional(halo, BotaniaDataComponents.LAST_RECIPE_ID, recipeId);
	}

	@Nullable
	private static RecipeHolder<CraftingRecipe> getLastRecipe(Level world, ItemStack halo) {
		Identifier id = halo.get(BotaniaDataComponents.LAST_RECIPE_ID);
		return id != null ? getRecipeHolder(world, id) : null;
	}

	private static @Nullable RecipeHolder<CraftingRecipe> getRecipeHolder(Level world, Identifier id) {
		return BotaniaRecipeTypes.getRecipe(world, id, RecipeType.CRAFTING).orElse(null);
	}

	private static ItemStack getRecipePreview(Level world, Recipe<CraftingInput> recipe) {
		var context = SlotDisplayContext.fromLevel(world);
		return recipe.display().stream()
				.map(display -> display.result().resolveForFirstStack(context))
				.filter(item -> !item.isEmpty())
				.findFirst()
				.orElse(ItemStack.EMPTY);
	}

	private static boolean wasEquipped(ItemStack stack) {
		return stack.has(BotaniaDataComponents.ACTIVE_TRANSIENT);
	}

	private static void setEquipped(ItemStack stack, boolean equipped) {
		DataComponentHelper.setFlag(stack, BotaniaDataComponents.ACTIVE_TRANSIENT, equipped);
	}

	private static float getRotationBase(ItemStack stack) {
		return stack.getOrDefault(BotaniaDataComponents.HALO_ROTATION_BASE, 0f);
	}

	private static void setRotationBase(ItemStack stack, float rotation) {
		stack.set(BotaniaDataComponents.HALO_ROTATION_BASE, rotation);
	}

	public Identifier getGlowResource(ItemStack stack) {
		return glowTexture;
	}

	public static class Rendering {
		public static void submit(Vec3 cameraPosition, float partialTicks, PoseStack ms, SubmitNodeCollector collector) {
			Player player = Minecraft.getInstance().player;
			if (player == null) {
				return;
			}
			ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, AssemblyHaloItem.class);
			if (stack.isEmpty()) {
				return;
			}

			ms.pushPose();

			double posX = player.xo + (player.getX() - player.xo) * partialTicks;
			double posY = player.yo + (player.getY() - player.yo) * partialTicks + player.getEyeHeight();
			double posZ = player.zo + (player.getZ() - player.zo) * partialTicks;

			ms.translate(posX - cameraPosition.x(), posY - cameraPosition.y(), posZ - cameraPosition.z());

			float base = getRotationBase(stack);
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = base - segAngles / 2.0F;

			float u = 1F;
			float v = 0.25F;

			float s = 3F;
			float m = 0.8F;
			float y = v * s * 2;

			int segmentLookedAt = getSegmentLookedAt(stack, player);
			AssemblyHaloItem item = (AssemblyHaloItem) stack.getItem();
			RenderType layer = RenderHelper.getHaloLayer(item.getGlowResource(stack));

			for (int seg = 0; seg < SEGMENTS; seg++) {
				boolean inside = false;
				float rotationAngle = (seg + 0.5F) * segAngles + shift;
				ms.pushPose();
				ms.mulPose(VecHelper.rotateY(rotationAngle));
				ms.translate(s * m, -0.75F, 0F);

				if (segmentLookedAt == seg) {
					inside = true;
				}

				ItemStack slotStack = getDisplayItem(player.level(), stack, seg);
				if (!slotStack.isEmpty()) {
					float scale = seg == 0 ? 0.9F : 0.8F;
					ms.scale(scale, scale, scale);
					ms.mulPose(VecHelper.rotateY(180F));
					ms.translate(seg == 0 ? 0.5F : 0F, seg == 0 ? -0.1F : 0.6F, 0F);

					ms.mulPose(VecHelper.rotateY(90.0F));
					var itemState = new ItemStackRenderState();
					Minecraft.getInstance().getItemModelResolver().updateForTopItem(itemState, slotStack,
							ItemDisplayContext.GUI, player.level(), null, player.getId());
					itemState.submit(ms, collector, 0xF000F0, OverlayTexture.NO_OVERLAY, 0);
				}
				ms.popPose();

				ms.pushPose();
				ms.mulPose(VecHelper.rotateX(180));
				float pulse = Mth.sin((ClientTickHandler.getPlayerTicksInGame() + partialTicks) * 0.2F) * 0.2F + 0.5F;
				float alpha = inside ? pulse + 0.3F : pulse;
				float lowerY = inside ? -y : 0;
				float shade = seg % 2 == 0 ? 0.6F : 1F;
				int firstAngle = seg * segAngles;
				collector.submitCustomGeometry(ms, layer, (transform, buffer) -> {
					var mat = transform.pose();
					for (int i = 0; i < segAngles; i++) {
						float angle = i + firstAngle + shift;
						float x = Mth.cos(angle * Mth.DEG_TO_RAD) * s;
						float z = Mth.sin(angle * Mth.DEG_TO_RAD) * s;
						float nextX = Mth.cos((angle + 1) * Mth.DEG_TO_RAD) * s;
						float nextZ = Mth.sin((angle + 1) * Mth.DEG_TO_RAD) * s;

						buffer.addVertex(mat, x * m, y, z * m).setColor(shade, shade, shade, alpha).setUv(u, v);
						buffer.addVertex(mat, x, lowerY, z).setColor(shade, shade, shade, alpha).setUv(u, 0);
						buffer.addVertex(mat, nextX, lowerY, nextZ).setColor(shade, shade, shade, alpha).setUv(0, 0);
						buffer.addVertex(mat, nextX * m, y, nextZ * m).setColor(shade, shade, shade, alpha).setUv(0, v);
					}
				});
				ms.popPose();
			}
			ms.popPose();
		}

		public static void renderHUD(GuiGraphicsExtractor gui, Player player, ItemStack stack, Window window, Font font) {
			int slot = getSegmentLookedAt(stack, player);

			if (slot == 0) {
				String name = craftingTable.get().getHoverName().getString();
				int l = font.width(name);
				int x = window.getGuiScaledWidth() / 2 - l / 2;
				int y = window.getGuiScaledHeight() / 2 - 65;

				gui.fill(x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
				gui.fill(x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
				gui.item(craftingTable.get(), window.getGuiScaledWidth() / 2 - 8, window.getGuiScaledHeight() / 2 - 52);

				gui.text(font, name, x, y, 0xFFFFFF);
			} else {
				RecipeHolder<? extends Recipe<CraftingInput>> recipe = getSavedRecipe(player.level(), stack, slot);
				Component label;
				boolean setRecipe = false;

				if (recipe == null) {
					label = Component.translatable("botaniamisc.unsetRecipe");
					recipe = getLastRecipe(player.level(), stack);
				} else {
					label = getRecipePreview(player.level(), recipe.value()).getHoverName();
					setRecipe = true;
				}

				renderRecipe(gui, window, font, label, recipe != null ? recipe.value() : null, player, setRecipe);
			}
		}

		private static void renderRecipe(GuiGraphicsExtractor gui, Window window, Font font, Component label, @Nullable Recipe<CraftingInput> recipe, Player player, boolean isSavedRecipe) {
			ItemStack recipeResult;
			if (recipe != null && !(recipeResult = getRecipePreview(player.level(), recipe)).isEmpty()) {
				int x = window.getGuiScaledWidth() / 2 - 45;
				int y = window.getGuiScaledHeight() / 2 - 90;

				gui.fill(x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
				gui.fill(x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

				gui.fill(x + 66, y + 14, x + 92, y + 40, 0x22000000);
				gui.fill(x - 2, y - 2, x + 56, y + 56, 0x22000000);

				int wrap = recipe instanceof ShapedRecipe shaped ? shaped.getWidth() : 3;
				var placement = recipe.placementInfo();
				var slots = placement.slotsToIngredientIndex();
				for (int i = 0; i < slots.size(); i++) {
					int ingredientIndex = slots.getInt(i);
					if (ingredientIndex == PlacementInfo.EMPTY_SLOT) {
						continue;
					}
					var candidates = placement.ingredients().get(ingredientIndex).items().toList();
					if (candidates.isEmpty()) {
						continue;
					}
					ItemStack stack = new ItemStack(candidates.get(ClientTickHandler.getPlayerTicksInGame() / 20 % candidates.size()));
					int xpos = x + i % wrap * 18;
					int ypos = y + i / wrap * 18;
					gui.fill(xpos, ypos, xpos + 16, ypos + 16, 0x22000000);
					gui.item(stack, xpos, ypos);
				}

				gui.item(recipeResult, x + 72, y + 18);
				gui.itemDecorations(font, recipeResult, x + 72, y + 18);

			}

			int yoff = 110;
			if (isSavedRecipe && recipe != null && !canCraftHeuristic(player, recipe)) {
				String warning = ChatFormatting.RED + I18n.get("botaniamisc.cantCraft");
				gui.centeredText(font, warning, window.getGuiScaledWidth() / 2, window.getGuiScaledHeight() / 2 - yoff, 0xFFFFFF);
				yoff += 12;
			}

			gui.centeredText(font, label, window.getGuiScaledWidth() / 2, window.getGuiScaledHeight() / 2 - yoff, 0xFFFFFF);
		}
	}
}
