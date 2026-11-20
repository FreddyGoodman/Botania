/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.brew.Brew;
import org.violetmoon.botania.api.brew.BrewItem;
import org.violetmoon.botania.api.mana.BurstProperties;
import org.violetmoon.botania.api.mana.ManaItem;
import org.violetmoon.botania.client.core.handler.ClientTickHandler;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.mana.ManaPoolBlock;
import org.violetmoon.botania.common.brew.BotaniaBrews;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.item.*;
import org.violetmoon.botania.common.item.equipment.bauble.TaintedBloodPendantItem;
import org.violetmoon.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import org.violetmoon.botania.common.item.lens.LensItem;
import org.violetmoon.botania.common.item.material.MysticalPetalItem;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public final class ColorHandler {

	public static final float MANA_HUE = 0.528F;

	public interface BlockHandlerConsumer {
		void register(List<BlockTintSource> sources, Block... blocks);
	}

	private interface ItemColor {
		int color(ItemStack stack, int layer);
	}

	private record ItemTint(ItemColor color, int layers) {
	}

	public static int getItemTintCount(Item item) {
		ItemTint tint = ItemTints.VALUES.get(item);
		return tint != null ? tint.layers() : 0;
	}

	public static int getItemColor(ItemStack stack, int layer) {
		ItemTint tint = ItemTints.VALUES.get(stack.getItem());
		return tint != null ? tint.color().color(stack, layer) : -1;
	}

	private interface ItemHandlerConsumer {
		void register(int layers, ItemColor handler, ItemLike... items);
	}

	private static class ItemTints {
		private static final Map<Item, ItemTint> VALUES = create();

		private static Map<Item, ItemTint> create() {
			Map<Item, ItemTint> colors = new IdentityHashMap<>();
			registerItems((layers, color, items) -> {
				for (ItemLike item : items) {
					colors.put(item.asItem(), new ItemTint(color, layers));
				}
			});
			return colors;
		}
	}

	private static Block[] getModBlocks(Predicate<Block> blockPredicate) {
		return BuiltInRegistries.BLOCK.stream()
				.filter(blockPredicate)
				.filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(BotaniaAPI.MODID))
				.toArray(Block[]::new);
	}

	public static void submitBlocks(BlockHandlerConsumer blocks) {
		blocks.register(List.of(BlockTintSources.foliage()), BotaniaBlocks.SOLID_VINE);

		BlockTintSource poolColor = new BlockTintSource() {
			@Override
			public int color(BlockState state) {
				return ARGB.opaque(getManaPoolColor((ManaPoolBlock) state.getBlock(), state, null));
			}

			@Override
			public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
				return ARGB.opaque(getManaPoolColor((ManaPoolBlock) state.getBlock(), state, pos));
			}
		};
		blocks.register(List.of(poolColor), getModBlocks(block -> block instanceof ManaPoolBlock pool && (pool.color != null || pool.isFabulous())));
	}

	public static int getManaPoolColor(ManaPoolBlock block, BlockState state, @Nullable BlockPos pos) {
		Optional<Integer> optionalColor = block.getOptionalColor()
				.map(color -> ARGB.srgbLerp(0.8f, 0xFFFFFF, MysticalPetalItem.getPetalLikeColor(color)));
		if (block.isFabulous()) {
			float time = (ClientTickHandler.getEntityTicksInGame() + ClientTickHandler.getEntityPartialTick()) * 0.005F;
			float posOffset = pos != null ? new Random(state.getSeed(pos)).nextFloat() : 0;
			int fabulousColor = Mth.hsvToRgb((time + posOffset) % 1f, 0.4F, 1F);
			return optionalColor.map(color -> ARGB.multiply(fabulousColor, color)).orElse(fabulousColor);
		}
		return optionalColor.orElse(-1);
	}

	private static void registerItems(ItemHandlerConsumer items) {
		items.register(1, (stack, tintIndex) -> tintIndex == 0
				? ARGB.opaque(Mth.hsvToRgb(ClientTickHandler.getUiAnimationTicks() % 180 / 180, 0.25f, 1.0f))
				: -1,
				BotaniaItems.GAIA_SPIRIT, BotaniaItems.GAIA_INGOT
		);

		items.register(1, (stack, tintIndex) -> tintIndex == 0
				? ARGB.opaque(Mth.hsvToRgb(ClientTickHandler.getUiAnimationTicks() % 180 / 180, 0.4f, 0.9f))
				: -1,
				BotaniaBlocks.GAIA_MANA_SPREADER
		);

		items.register(3, (stack, tintIndex) -> switch (tintIndex) {
			case 1 -> ARGB.opaque(MysticalPetalItem.getPetalLikeColor(WandOfTheForestItem.getColor1(stack)));
			case 2 -> ARGB.opaque(MysticalPetalItem.getPetalLikeColor(WandOfTheForestItem.getColor2(stack)));
			default -> -1;
		},
				BotaniaItems.WAND_OF_THE_FOREST, BotaniaItems.WAND_OF_THE_ELVEN_FOREST
		);

		items.register(1, (stack, tintIndex) -> tintIndex == 0
				? poolItemColor((BlockItem) stack.getItem())
				: -1,
				getModBlocks(block -> block instanceof ManaPoolBlock pool && (pool.color != null || pool.isFabulous())));

		items.register(2, (stack, tintIndex) -> {
			if (tintIndex == 1) {
				var manaItem = ManaItem.LOOKUP.find(stack);
				return ARGB.opaque(Mth.hsvToRgb(
						MANA_HUE,
						manaItem != null ? (float) manaItem.getMana() / (float) Math.max(1, manaItem.getMaxMana()) : 0,
						1));
			}
			if (stack.has(BotaniaDataComponents.CREATIVE_MANA)) {
				return 0xFFFFCCCC;
			}
			return -1;
		}, BotaniaItems.MANA_MIRROR, BotaniaItems.MANA_TABLET
		);

		items.register(1, (stack, tintIndex) -> tintIndex == 0
				? ARGB.opaque(Mth.hsvToRgb(
						0.55F,
						((float) stack.getMaxDamage() - (float) stack.getDamageValue()) / (float) stack.getMaxDamage() * 0.5F,
						1F))
				: -1,
				BotaniaItems.SPELLBINDING_CLOTH
		);

		items.register(2, (stack, tintIndex) -> {
			if (tintIndex != 1) {
				return -1;
			}

			Brew brew = ((BrewItem) stack.getItem()).getBrew(stack);
			if (brew == BotaniaBrews.FALLBACK) {
				return stack.getItem() instanceof TaintedBloodPendantItem ? 0xFFC6000E : 0xFF989898;
			}

			float speed = stack.is(BotaniaItems.BREW_FLASK) || stack.is(BotaniaItems.BREW_VIAL) ? 0.1f : 0.2f;
			int add = (int) (Mth.sin(ClientTickHandler.getUiAnimationTicks() * speed) * 24);

			return addToColor(brew.getColor(stack), add);
		}, BotaniaItems.TAINTED_BLOOD_PENDANT, BotaniaItems.INCENSE_STICK, BotaniaItems.BREW_FLASK, BotaniaItems.BREW_VIAL
		);

		items.register(3, (stack, tintIndex) -> {
			ItemStack lens = ManaBlasterItem.getLens(stack);
			if (!lens.isEmpty() && tintIndex == 0) {
				return getItemColor(lens, tintIndex);
			}

			if (tintIndex == 2) {
				var player = Minecraft.getInstance().player;
				int color = 0x20FF20;
				if (player != null) {
					BurstProperties props = ((ManaBlasterItem) stack.getItem()).getBurstProps(player, stack, false, InteractionHand.MAIN_HAND);
					color = props.color;
				} else if (!lens.isEmpty() && lens.getItem() instanceof LensItem item) {
					color = item.getLensColor(lens, null);
				}
				int add = ARGB.as8BitChannel(Mth.sin(ClientTickHandler.getUiAnimationTicks() / 5) * 0.15f);

				return addToColor(color, add);
			} else {
				return -1;
			}
		}, BotaniaItems.MANA_BLASTER
		);

		items.register(2, (stack, tintIndex) -> tintIndex == 1
				? ARGB.opaque(Mth.hsvToRgb(0.75f, 1,
						1.5f - Math.min(1, Mth.sin(ClientTickHandler.getUiAnimationTicks() / 2) * 0.5f + 1.2f)))
				: -1,
				BotaniaItems.SOULSCRIBE
		);

		items.register(2, (stack, tintIndex) -> tintIndex == 1 && TerraShattererItem.isEnabled(stack)
				? ARGB.opaque(Mth.hsvToRgb(0.375f,
						Math.min(1, Mth.sin(ClientTickHandler.getUiAnimationTicks() / 4) * 0.5f + 1f),
						1))
				: -1,
				BotaniaItems.TERRA_SHATTERER
		);

		ItemColor lensHandler = (stack, tintIndex) -> tintIndex == 0
				? ARGB.opaque(((LensItem) stack.getItem()).getLensColor(stack, Minecraft.getInstance().level))
				: -1;
		items.register(1, lensHandler,
				BotaniaItems.MANA_LENS, BotaniaItems.VELOCITY_LENS, BotaniaItems.POTENCY_LENS,
				BotaniaItems.RESISTANCE_LENS, BotaniaItems.EFFICIENCY_LENS, BotaniaItems.BOUNCE_LENS,
				BotaniaItems.GRAVITY_LENS, BotaniaItems.BORE_LENS, BotaniaItems.DAMAGING_LENS,
				BotaniaItems.PHANTOM_LENS, BotaniaItems.MAGNETIZING_LENS, BotaniaItems.ENTROPIC_LENS,
				BotaniaItems.INFLUENCE_LENS, BotaniaItems.WEIGHT_LENS, BotaniaItems.PAINTSLINGER_LENS,
				BotaniaItems.KINDLE_LENS, BotaniaItems.FORCE_LENS, BotaniaItems.FLASH_LENS, BotaniaItems.WARP_LENS,
				BotaniaItems.REDIRECTIVE_LENS, BotaniaItems.CELEBRATORY_LENS, BotaniaItems.FLARE_LENS,
				BotaniaItems.MESSENGER_LENS, BotaniaItems.TRIPWIRE_LENS, BotaniaItems.STORM_LENS
		);
	}

	private static int poolItemColor(BlockItem item) {
		ManaPoolBlock pool = (ManaPoolBlock) item.getBlock();
		return ARGB.opaque(getManaPoolColor(pool, pool.defaultBlockState(), null));
	}

	private static int addToColor(int color, int add) {
		int red = Math.clamp(ARGB.red(color) + add, 0, 255);
		int green = Math.clamp(ARGB.green(color) + add, 0, 255);
		int blue = Math.clamp(ARGB.blue(color) + add, 0, 255);

		return ARGB.color(red, green, blue);
	}

	private ColorHandler() {}

}
