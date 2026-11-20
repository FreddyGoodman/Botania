/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block;

import net.minecraft.core.*;
import net.minecraft.core.dispenser.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.block.PetalApothecary;
import org.violetmoon.botania.api.internal.Colored;
import org.violetmoon.botania.api.internal.OptionallyColored;
import org.violetmoon.botania.api.state.BotaniaStateProperties;
import org.violetmoon.botania.api.state.enums.AlfheimPortalState;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;
import org.violetmoon.botania.common.block.block_entity.flower.generating.HydroangeasBlockEntity;
import org.violetmoon.botania.common.block.corporea.*;
import org.violetmoon.botania.common.block.dispenser.*;
import org.violetmoon.botania.common.block.flower.*;
import org.violetmoon.botania.common.block.mana.*;
import org.violetmoon.botania.common.block.red_string.*;
import org.violetmoon.botania.common.brew.BotaniaMobEffects;
import org.violetmoon.botania.common.helper.ColorHelper;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.block.BlockItemTooltip;
import org.violetmoon.botania.common.item.block.ColoredBlockItem;
import org.violetmoon.botania.common.item.block.DecayableSpecialFlowerBlockItem;
import org.violetmoon.botania.common.item.block.SpecialFlowerBlockItem;
import org.violetmoon.botania.common.item.block.TinyPotatoBlockItem;
import org.violetmoon.botania.common.lib.LibBlockNames;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaBlocks {

	private static final Map<String, Block> ALL = new LinkedHashMap<>(); // Preserve insertion order

	private static final BlockBehaviour.StateArgumentPredicate<EntityType<?>> NO_SPAWN = (state, world, pos, et) -> false;
	private static final BlockBehaviour.StatePredicate NEVER = (state, world, pos) -> false;

	// small mundane flowers
	public static final MysticalFlowerBlock WHITE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.WHITE,
			// [VanillaCopy] Blocks.POPPY
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.noCollision()
					.instabreak()
					.offsetType(BlockBehaviour.OffsetType.XZ)
					.pushReaction(PushReaction.DESTROY)
					.sound(SoundType.GRASS));
	public static final MysticalFlowerBlock ORANGE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock MAGENTA_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock LIGHT_BLUE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock YELLOW_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock LIME_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock PINK_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock GRAY_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock LIGHT_GRAY_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock CYAN_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock PURPLE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock BLUE_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock BROWN_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock GREEN_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock RED_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final MysticalFlowerBlock BLACK_MYSTICAL_FLOWER = makeMysticalFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));

	// glimmering mundane flowers
	public static final GlimmeringFlowerBlock WHITE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER)
					.lightLevel(constInt(15)));
	public static final GlimmeringFlowerBlock ORANGE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock MAGENTA_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock LIGHT_BLUE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock YELLOW_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock LIME_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock PINK_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock GRAY_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock LIGHT_GRAY_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock CYAN_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock PURPLE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock BLUE_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock BROWN_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock GREEN_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock RED_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));
	public static final GlimmeringFlowerBlock BLACK_GLIMMERING_FLOWER = makeGlimmeringFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_GLIMMERING_FLOWER));

	// buried petals
	public static final Block WHITE_BURIED_PETAL = makeBuriedPetal(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER)
					.sound(SoundType.MOSS)
					.lightLevel(constInt(4)));
	public static final Block ORANGE_BURIED_PETAL = makeBuriedPetal(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block MAGENTA_BURIED_PETAL = makeBuriedPetal(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block LIGHT_BLUE_BURIED_PETAL = makeBuriedPetal(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block YELLOW_BURIED_PETAL = makeBuriedPetal(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block LIME_BURIED_PETAL = makeBuriedPetal(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block PINK_BURIED_PETAL = makeBuriedPetal(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block GRAY_BURIED_PETAL = makeBuriedPetal(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block LIGHT_GRAY_BURIED_PETAL = makeBuriedPetal(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block CYAN_BURIED_PETAL = makeBuriedPetal(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block PURPLE_BURIED_PETAL = makeBuriedPetal(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block BLUE_BURIED_PETAL = makeBuriedPetal(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block BROWN_BURIED_PETAL = makeBuriedPetal(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block GREEN_BURIED_PETAL = makeBuriedPetal(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block RED_BURIED_PETAL = makeBuriedPetal(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));
	public static final Block BLACK_BURIED_PETAL = makeBuriedPetal(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_BURIED_PETAL));

	// floating mundane flowers
	public static final Block WHITE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.WHITE,
			BlockBehaviour.Properties.of()
					.mapColor(MapColor.PLANT)
					.strength(0.5f)
					.sound(SoundType.GRAVEL)
					.lightLevel(state -> state.getValue(BotaniaStateProperties.DIMMED) ? 3 : 15)
	);
	public static final Block ORANGE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block MAGENTA_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block LIGHT_BLUE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block YELLOW_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block LIME_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block PINK_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block GRAY_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block LIGHT_GRAY_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block CYAN_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block PURPLE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block BLUE_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block BROWN_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block GREEN_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block RED_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));
	public static final Block BLACK_FLOATING_FLOWER = makeFloatingMundaneFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER));

	// petal blocks
	public static final Block WHITE_PETAL_BLOCK = makePetalBlock(DyeColor.WHITE,
			// Note: map color of properties is set to dye color
			BlockBehaviour.Properties.of()
					.strength(0.4f)
					.sound(SoundType.MOSS));
	public static final Block ORANGE_PETAL_BLOCK = makePetalBlock(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block MAGENTA_PETAL_BLOCK = makePetalBlock(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block LIGHT_BLUE_PETAL_BLOCK = makePetalBlock(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block YELLOW_PETAL_BLOCK = makePetalBlock(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block LIME_PETAL_BLOCK = makePetalBlock(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block PINK_PETAL_BLOCK = makePetalBlock(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block GRAY_PETAL_BLOCK = makePetalBlock(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block LIGHT_GRAY_PETAL_BLOCK = makePetalBlock(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block CYAN_PETAL_BLOCK = makePetalBlock(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block PURPLE_PETAL_BLOCK = makePetalBlock(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block BLUE_PETAL_BLOCK = makePetalBlock(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block BROWN_PETAL_BLOCK = makePetalBlock(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block GREEN_PETAL_BLOCK = makePetalBlock(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block RED_PETAL_BLOCK = makePetalBlock(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));
	public static final Block BLACK_PETAL_BLOCK = makePetalBlock(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_PETAL_BLOCK));

	// shimmering mushrooms
	public static final ShimmeringMushroomBlock WHITE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER)
					.lightLevel(constInt(3))
					.offsetType(BlockBehaviour.OffsetType.NONE)
	);
	public static final ShimmeringMushroomBlock ORANGE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock MAGENTA_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock LIGHT_BLUE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock YELLOW_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock LIME_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock PINK_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock GRAY_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock LIGHT_GRAY_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock CYAN_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock PURPLE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock BLUE_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock BROWN_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock GREEN_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock RED_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));
	public static final ShimmeringMushroomBlock BLACK_SHIMMERING_MUSHROOM = makeShimmeringMushroom(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_SHIMMERING_MUSHROOM));

	// tall mundane flowers
	public static final Block WHITE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.WHITE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER));
	public static final Block ORANGE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.ORANGE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block MAGENTA_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.MAGENTA,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block LIGHT_BLUE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.LIGHT_BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block YELLOW_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.YELLOW,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block LIME_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.LIME,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block PINK_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.PINK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block GRAY_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block LIGHT_GRAY_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.LIGHT_GRAY,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block CYAN_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.CYAN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block PURPLE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.PURPLE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block BLUE_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.BLUE,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block BROWN_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.BROWN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block GREEN_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.GREEN,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block RED_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.RED,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));
	public static final Block BLACK_TALL_MYSTICAL_FLOWER = makeTallMysticalFlower(DyeColor.BLACK,
			BlockBehaviour.Properties.ofFullCopy(WHITE_TALL_MYSTICAL_FLOWER));

	// potted mundane flowers
	public static final Block POTTED_WHITE_MYSTICAL_FLOWER = makePottedMundaneFlower(WHITE_MYSTICAL_FLOWER);
	public static final Block POTTED_ORANGE_MYSTICAL_FLOWER = makePottedMundaneFlower(ORANGE_MYSTICAL_FLOWER);
	public static final Block POTTED_MAGENTA_MYSTICAL_FLOWER = makePottedMundaneFlower(MAGENTA_MYSTICAL_FLOWER);
	public static final Block POTTED_LIGHT_BLUE_MYSTICAL_FLOWER = makePottedMundaneFlower(LIGHT_BLUE_MYSTICAL_FLOWER);
	public static final Block POTTED_YELLOW_MYSTICAL_FLOWER = makePottedMundaneFlower(YELLOW_MYSTICAL_FLOWER);
	public static final Block POTTED_LIME_MYSTICAL_FLOWER = makePottedMundaneFlower(LIME_MYSTICAL_FLOWER);
	public static final Block POTTED_PINK_MYSTICAL_FLOWER = makePottedMundaneFlower(PINK_MYSTICAL_FLOWER);
	public static final Block POTTED_GRAY_MYSTICAL_FLOWER = makePottedMundaneFlower(GRAY_MYSTICAL_FLOWER);
	public static final Block POTTED_LIGHT_GRAY_MYSTICAL_FLOWER = makePottedMundaneFlower(LIGHT_GRAY_MYSTICAL_FLOWER);
	public static final Block POTTED_CYAN_MYSTICAL_FLOWER = makePottedMundaneFlower(CYAN_MYSTICAL_FLOWER);
	public static final Block POTTED_PURPLE_MYSTICAL_FLOWER = makePottedMundaneFlower(PURPLE_MYSTICAL_FLOWER);
	public static final Block POTTED_BLUE_MYSTICAL_FLOWER = makePottedMundaneFlower(BLUE_MYSTICAL_FLOWER);
	public static final Block POTTED_BROWN_MYSTICAL_FLOWER = makePottedMundaneFlower(BROWN_MYSTICAL_FLOWER);
	public static final Block POTTED_GREEN_MYSTICAL_FLOWER = makePottedMundaneFlower(GREEN_MYSTICAL_FLOWER);
	public static final Block POTTED_RED_MYSTICAL_FLOWER = makePottedMundaneFlower(RED_MYSTICAL_FLOWER);
	public static final Block POTTED_BLACK_MYSTICAL_FLOWER = makePottedMundaneFlower(BLACK_MYSTICAL_FLOWER);

	// potted shimmering flowers
	public static final Block POTTED_WHITE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(WHITE_GLIMMERING_FLOWER);
	public static final Block POTTED_ORANGE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(ORANGE_GLIMMERING_FLOWER);
	public static final Block POTTED_MAGENTA_GLIMMERING_FLOWER = makePottedGlimmeringFlower(MAGENTA_GLIMMERING_FLOWER);
	public static final Block POTTED_LIGHT_BLUE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(LIGHT_BLUE_GLIMMERING_FLOWER);
	public static final Block POTTED_YELLOW_GLIMMERING_FLOWER = makePottedGlimmeringFlower(YELLOW_GLIMMERING_FLOWER);
	public static final Block POTTED_LIME_GLIMMERING_FLOWER = makePottedGlimmeringFlower(LIME_GLIMMERING_FLOWER);
	public static final Block POTTED_PINK_GLIMMERING_FLOWER = makePottedGlimmeringFlower(PINK_GLIMMERING_FLOWER);
	public static final Block POTTED_GRAY_GLIMMERING_FLOWER = makePottedGlimmeringFlower(GRAY_GLIMMERING_FLOWER);
	public static final Block POTTED_LIGHT_GRAY_GLIMMERING_FLOWER = makePottedGlimmeringFlower(LIGHT_GRAY_GLIMMERING_FLOWER);
	public static final Block POTTED_CYAN_GLIMMERING_FLOWER = makePottedGlimmeringFlower(CYAN_GLIMMERING_FLOWER);
	public static final Block POTTED_PURPLE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(PURPLE_GLIMMERING_FLOWER);
	public static final Block POTTED_BLUE_GLIMMERING_FLOWER = makePottedGlimmeringFlower(BLUE_GLIMMERING_FLOWER);
	public static final Block POTTED_BROWN_GLIMMERING_FLOWER = makePottedGlimmeringFlower(BROWN_GLIMMERING_FLOWER);
	public static final Block POTTED_GREEN_GLIMMERING_FLOWER = makePottedGlimmeringFlower(GREEN_GLIMMERING_FLOWER);
	public static final Block POTTED_RED_GLIMMERING_FLOWER = makePottedGlimmeringFlower(RED_GLIMMERING_FLOWER);
	public static final Block POTTED_BLACK_GLIMMERING_FLOWER = makePottedGlimmeringFlower(BLACK_GLIMMERING_FLOWER);

	// potted shimmering mushrooms
	public static final Block POTTED_WHITE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(WHITE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_ORANGE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(ORANGE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_MAGENTA_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(MAGENTA_SHIMMERING_MUSHROOM);
	public static final Block POTTED_LIGHT_BLUE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(LIGHT_BLUE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_YELLOW_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(YELLOW_SHIMMERING_MUSHROOM);
	public static final Block POTTED_LIME_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(LIME_SHIMMERING_MUSHROOM);
	public static final Block POTTED_PINK_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(PINK_SHIMMERING_MUSHROOM);
	public static final Block POTTED_GRAY_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(GRAY_SHIMMERING_MUSHROOM);
	public static final Block POTTED_LIGHT_GRAY_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(LIGHT_GRAY_SHIMMERING_MUSHROOM);
	public static final Block POTTED_CYAN_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(CYAN_SHIMMERING_MUSHROOM);
	public static final Block POTTED_PURPLE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(PURPLE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_BLUE_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(BLUE_SHIMMERING_MUSHROOM);
	public static final Block POTTED_BROWN_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(BROWN_SHIMMERING_MUSHROOM);
	public static final Block POTTED_GREEN_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(GREEN_SHIMMERING_MUSHROOM);
	public static final Block POTTED_RED_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(RED_SHIMMERING_MUSHROOM);
	public static final Block POTTED_BLACK_SHIMMERING_MUSHROOM = makePottedShimmeringMushroom(BLACK_SHIMMERING_MUSHROOM);

	// special flowers (regular, floating, and potted; potentially each with petite variants)
	public static final Block PURE_DAISY = make(LibBlockNames.SUBTILE_PUREDAISY,
			key -> new SpecialFlowerBlock(
					BotaniaMobEffects.ABSOLUTION,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.PURE_DAISY));
	public static final Block FLOATING_PURE_DAISY = make(floating(LibBlockNames.SUBTILE_PUREDAISY),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.PURE_DAISY));
	public static final Block POTTED_PURE_DAISY = make(potted(LibBlockNames.SUBTILE_PUREDAISY),
			key -> flowerPot(PURE_DAISY, 0, key));

	public static final Block MANASTAR = make(LibBlockNames.SUBTILE_MANASTAR,
			key -> new ManastarBlock(
					MobEffects.GLOWING,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MANASTAR));
	public static final Block FLOATING_MANASTAR = make(floating(LibBlockNames.SUBTILE_MANASTAR),
			key -> new FloatingManastarBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MANASTAR));
	public static final Block POTTED_MANASTAR = make(potted(LibBlockNames.SUBTILE_MANASTAR),
			key -> flowerPot(MANASTAR, 0, key));

	public static final Block HYDROANGEAS = make(LibBlockNames.SUBTILE_HYDROANGEAS,
			key -> new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.UNLUCK,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HYDROANGEAS));
	public static final Block FLOATING_HYDROANGEAS = make(floating(LibBlockNames.SUBTILE_HYDROANGEAS),
			key -> new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HYDROANGEAS));
	public static final Block POTTED_HYDROANGEAS = make(potted(LibBlockNames.SUBTILE_HYDROANGEAS),
			key -> flowerPot(HYDROANGEAS, 0, key));

	public static final Block ENDOFLAME = make(LibBlockNames.SUBTILE_ENDOFLAME,
			key -> new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.SLOWNESS,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ENDOFLAME));
	public static final Block FLOATING_ENDOFLAME = make(floating(LibBlockNames.SUBTILE_ENDOFLAME),
			key -> new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ENDOFLAME));
	public static final Block POTTED_ENDOFLAME = make(potted(LibBlockNames.SUBTILE_ENDOFLAME),
			key -> flowerPot(ENDOFLAME, 0, key));

	public static final Block THERMALILY = make(LibBlockNames.SUBTILE_THERMALILY,
			key -> new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.FIRE_RESISTANCE,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.THERMALILY));
	public static final Block FLOATING_THERMALILY = make(floating(LibBlockNames.SUBTILE_THERMALILY),
			key -> new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.THERMALILY));
	public static final Block POTTED_THERMALILY = make(potted(LibBlockNames.SUBTILE_THERMALILY),
			key -> flowerPot(THERMALILY, 0, key));

	public static final Block ROSA_ARCANA = make(LibBlockNames.SUBTILE_ARCANE_ROSE,
			key -> new SpecialFlowerBlock(MobEffects.LUCK,
					64,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ROSA_ARCANA));
	public static final Block FLOATING_ROSA_ARCANA = make(floating(LibBlockNames.SUBTILE_ARCANE_ROSE),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ROSA_ARCANA));
	public static final Block POTTED_ROSA_ARCANA = make(potted(LibBlockNames.SUBTILE_ARCANE_ROSE),
			key -> flowerPot(ROSA_ARCANA, 0, key));

	public static final Block MUNCHDEW = make(LibBlockNames.SUBTILE_MUNCHDEW,
			key -> new SlowGeneratingFlowerWithCooldownBlock(
					MobEffects.SLOW_FALLING,
					300,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MUNCHDEW));
	public static final Block FLOATING_MUNCHDEW = make(floating(LibBlockNames.SUBTILE_MUNCHDEW),
			key -> new FloatingSlowGeneratingFlowerWithCooldownBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MUNCHDEW));
	public static final Block POTTED_MUNCHDEW = make(potted(LibBlockNames.SUBTILE_MUNCHDEW),
			key -> flowerPot(MUNCHDEW, 0, key));

	public static final Block ENTROPINNYUM = make(LibBlockNames.SUBTILE_ENTROPINNYUM,
			key -> new SpecialFlowerBlock(
					MobEffects.RESISTANCE,
					72,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ENTROPINNYUM));
	public static final Block FLOATING_ENTROPINNYUM = make(floating(LibBlockNames.SUBTILE_ENTROPINNYUM),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ENTROPINNYUM));
	public static final Block POTTED_ENTROPINNYUM = make(potted(LibBlockNames.SUBTILE_ENTROPINNYUM),
			key -> flowerPot(ENTROPINNYUM, 0, key));

	public static final Block KEKIMURUS = make(LibBlockNames.SUBTILE_KEKIMURUS,
			key -> new SpecialFlowerBlock(MobEffects.SATURATION,
					15,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.KEKIMURUS));
	public static final Block FLOATING_KEKIMURUS = make(floating(LibBlockNames.SUBTILE_KEKIMURUS),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.KEKIMURUS));
	public static final Block POTTED_KEKIMURUS = make(potted(LibBlockNames.SUBTILE_KEKIMURUS),
			key -> flowerPot(KEKIMURUS, 0, key));

	public static final Block GOURMARYLLIS = make(LibBlockNames.SUBTILE_GOURMARYLLIS,
			key -> new SlowGeneratingFlowerBlock(
					MobEffects.HUNGER,
					180,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.GOURMARYLLIS));
	public static final Block FLOATING_GOURMARYLLIS = make(floating(LibBlockNames.SUBTILE_GOURMARYLLIS),
			key -> new FloatingSlowGeneratingFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.GOURMARYLLIS));
	public static final Block POTTED_GOURMARYLLIS = make(potted(LibBlockNames.SUBTILE_GOURMARYLLIS),
			key -> flowerPot(GOURMARYLLIS, 0, key));

	public static final Block NARSLIMMUS = make(LibBlockNames.SUBTILE_NARSLIMMUS,
			key -> new SpecialFlowerBlock(BotaniaMobEffects.FEATHER_FEET,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.NARSLIMMUS));
	public static final Block FLOATING_NARSLIMMUS = make(floating(LibBlockNames.SUBTILE_NARSLIMMUS),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.NARSLIMMUS));
	public static final Block POTTED_NARSLIMMUS = make(potted(LibBlockNames.SUBTILE_NARSLIMMUS),
			key -> flowerPot(NARSLIMMUS, 0, key));

	public static final Block SPECTROLUS = make(LibBlockNames.SUBTILE_SPECTROLUS,
			key -> new SpecialFlowerBlock(
					MobEffects.BLINDNESS,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SPECTROLUS));
	public static final Block FLOATING_SPECTROLUS = make(floating(LibBlockNames.SUBTILE_SPECTROLUS),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SPECTROLUS));
	public static final Block POTTED_SPECTROLUS = make(potted(LibBlockNames.SUBTILE_SPECTROLUS),
			key -> flowerPot(SPECTROLUS, 0, key));

	public static final Block DANDELIFEON = make(LibBlockNames.SUBTILE_DANDELIFEON,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.NAUSEA,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.DANDELIFEON));
	public static final Block FLOATING_DANDELIFEON = make(floating(LibBlockNames.SUBTILE_DANDELIFEON),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.DANDELIFEON));
	public static final Block POTTED_DANDELIFEON = make(potted(LibBlockNames.SUBTILE_DANDELIFEON),
			key -> flowerPot(DANDELIFEON, 0, key));

	public static final Block RAFFLOWSIA = make(LibBlockNames.SUBTILE_RAFFLOWSIA,
			key -> new SpecialFlowerBlock(
					MobEffects.HEALTH_BOOST,
					18,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.RAFFLOWSIA));
	public static final Block FLOATING_RAFFLOWSIA = make(floating(LibBlockNames.SUBTILE_RAFFLOWSIA),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.RAFFLOWSIA));
	public static final Block POTTED_RAFFLOWSIA = make(potted(LibBlockNames.SUBTILE_RAFFLOWSIA),
			key -> flowerPot(RAFFLOWSIA, 0, key));

	public static final Block SHULK_ME_NOT = make(LibBlockNames.SUBTILE_SHULK_ME_NOT,
			key -> new SpecialFlowerBlock(
					MobEffects.LEVITATION,
					72,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SHULK_ME_NOT));
	public static final Block FLOATING_SHULK_ME_NOT = make(floating(LibBlockNames.SUBTILE_SHULK_ME_NOT),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SHULK_ME_NOT));
	public static final Block POTTED_SHULK_ME_NOT = make(potted(LibBlockNames.SUBTILE_SHULK_ME_NOT),
			key -> flowerPot(SHULK_ME_NOT, 0, key));

	public static final Block BELLETHORNE = make(LibBlockNames.SUBTILE_BELLETHORN,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.WITHER,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BELLETHORNE));
	public static final Block BELLETHORNE_PETITE = make(petite(LibBlockNames.SUBTILE_BELLETHORN),
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.WITHER,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BELLETHORNE_PETITE));
	public static final Block FLOATING_BELLETHORNE = make(floating(LibBlockNames.SUBTILE_BELLETHORN),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BELLETHORNE));
	public static final Block FLOATING_BELLETHORNE_PETITE = make(petite(floating(LibBlockNames.SUBTILE_BELLETHORN)),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BELLETHORNE_PETITE));
	public static final Block POTTED_BELLETHORNE = make(potted(LibBlockNames.SUBTILE_BELLETHORN),
			key -> flowerPot(BELLETHORNE, 0, key));
	public static final Block POTTED_BELLETHORNE_PETITE = make(petite(potted(LibBlockNames.SUBTILE_BELLETHORN)),
			key -> flowerPot(BELLETHORNE_PETITE, 0, key));

	public static final Block BERGAMUTE = make(LibBlockNames.SUBTILE_BERGAMUTE,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.BLINDNESS,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BERGAMUTE));
	public static final Block FLOATING_BERGAMUTE = make(floating(LibBlockNames.SUBTILE_BERGAMUTE),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BERGAMUTE));
	public static final Block POTTED_BERGAMUTE = make(potted(LibBlockNames.SUBTILE_BERGAMUTE),
			key -> flowerPot(BERGAMUTE, 0, key));

	public static final Block DREADTHORNE = make(LibBlockNames.SUBTILE_DREADTHORN,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.WITHER,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.DREADTHORN));
	public static final Block FLOATING_DREADTHORNE = make(floating(LibBlockNames.SUBTILE_DREADTHORN),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.DREADTHORN));
	public static final Block POTTED_DREADTHORNE = make(potted(LibBlockNames.SUBTILE_DREADTHORN),
			key -> flowerPot(DREADTHORNE, 0, key));

	public static final Block HEISEI_DREAM = make(LibBlockNames.SUBTILE_HEISEI_DREAM,
			key -> new SpecialFlowerBlock(
					BotaniaMobEffects.SOUL_CROSS,
					300,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HEISEI_DREAM));
	public static final Block FLOATING_HEISEI_DREAM = make(floating(LibBlockNames.SUBTILE_HEISEI_DREAM),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HEISEI_DREAM));
	public static final Block POTTED_HEISEI_DREAM = make(potted(LibBlockNames.SUBTILE_HEISEI_DREAM),
			key -> flowerPot(HEISEI_DREAM, 0, key));

	public static final Block TIGERSEYE = make(LibBlockNames.SUBTILE_TIGERSEYE,
			key -> new SpecialFlowerBlock(
					MobEffects.STRENGTH,
					90,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.TIGERSEYE));
	public static final Block FLOATING_TIGERSEYE = make(floating(LibBlockNames.SUBTILE_TIGERSEYE),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.TIGERSEYE));
	public static final Block POTTED_TIGERSEYE = make(potted(LibBlockNames.SUBTILE_TIGERSEYE),
			key -> flowerPot(TIGERSEYE, 0, key));

	public static final Block JADED_AMARANTHUS = make(LibBlockNames.SUBTILE_JADED_AMARANTHUS,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.INSTANT_HEALTH,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.JADED_AMARANTHUS));
	public static final Block FLOATING_JADED_AMARANTHUS = make(floating(LibBlockNames.SUBTILE_JADED_AMARANTHUS),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.JADED_AMARANTHUS));
	public static final Block POTTED_JADED_AMARANTHUS = make(potted(LibBlockNames.SUBTILE_JADED_AMARANTHUS),
			key -> flowerPot(JADED_AMARANTHUS, 0, key));

	public static final Block ORECHID = make(LibBlockNames.SUBTILE_ORECHID,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.HASTE,
					10,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ORECHID));
	public static final Block FLOATING_ORECHID = make(floating(LibBlockNames.SUBTILE_ORECHID),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ORECHID));
	public static final Block POTTED_ORECHID = make(potted(LibBlockNames.SUBTILE_ORECHID),
			key -> flowerPot(ORECHID, 0, key));

	public static final Block FALLEN_KANADE = make(LibBlockNames.SUBTILE_FALLEN_KANADE,
			key -> new SpecialFlowerBlock(
					MobEffects.REGENERATION,
					90,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.FALLEN_KANADE));
	public static final Block FLOATING_FALLEN_KANADE = make(floating(LibBlockNames.SUBTILE_FALLEN_KANADE),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.FALLEN_KANADE));
	public static final Block POTTED_FALLEN_KANADE = make(potted(LibBlockNames.SUBTILE_FALLEN_KANADE),
			key -> flowerPot(FALLEN_KANADE, 0, key));

	public static final Block EXOFLAME = make(LibBlockNames.SUBTILE_EXOFLAME,
			key -> new SpecialFlowerBlock(MobEffects.SPEED,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.EXOFLAME));
	public static final Block FLOATING_EXOFLAME = make(floating(LibBlockNames.SUBTILE_EXOFLAME),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.EXOFLAME));
	public static final Block POTTED_EXOFLAME = make(potted(LibBlockNames.SUBTILE_EXOFLAME),
			key -> flowerPot(EXOFLAME, 0, key));

	public static final Block AGRICARNATION = make(LibBlockNames.SUBTILE_AGRICARNATION,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.ABSORPTION,
					48,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.AGRICARNATION));
	public static final Block AGRICARNATION_PETITE = make(petite(LibBlockNames.SUBTILE_AGRICARNATION),
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.ABSORPTION,
					48,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.AGRICARNATION_PETITE));
	public static final Block FLOATING_AGRICARNATION = make(floating(LibBlockNames.SUBTILE_AGRICARNATION),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.AGRICARNATION));
	public static final Block FLOATING_AGRICARNATION_PETITE = make(petite(floating(LibBlockNames.SUBTILE_AGRICARNATION)),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.AGRICARNATION_PETITE));
	public static final Block POTTED_AGRICARNATION = make(potted(LibBlockNames.SUBTILE_AGRICARNATION),
			key -> flowerPot(AGRICARNATION, 0, key));
	public static final Block POTTED_AGRICARNATION_PETITE = make(petite(potted(LibBlockNames.SUBTILE_AGRICARNATION)),
			key -> flowerPot(AGRICARNATION_PETITE, 0, key));

	public static final Block HOPPERHOCK = make(LibBlockNames.SUBTILE_HOPPERHOCK,
			key -> new HopperhockBlock(MobEffects.SPEED,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HOPPERHOCK));
	public static final Block HOPPERHOCK_PETITE = make(petite(LibBlockNames.SUBTILE_HOPPERHOCK),
			key -> new HopperhockBlock(
					MobEffects.SPEED,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HOPPERHOCK_PETITE));
	public static final Block FLOATING_HOPPERHOCK = make(floating(LibBlockNames.SUBTILE_HOPPERHOCK),
			key -> new FloatingHopperhockBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HOPPERHOCK));
	public static final Block FLOATING_HOPPERHOCK_PETITE = make(petite(floating(LibBlockNames.SUBTILE_HOPPERHOCK)),
			key -> new FloatingHopperhockBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HOPPERHOCK_PETITE));
	public static final Block POTTED_HOPPERHOCK = make(potted(LibBlockNames.SUBTILE_HOPPERHOCK),
			key -> flowerPot(HOPPERHOCK, 0, key));
	public static final Block POTTED_HOPPERHOCK_PETITE = make(petite(potted(LibBlockNames.SUBTILE_HOPPERHOCK)),
			key -> flowerPot(HOPPERHOCK_PETITE, 0, key));

	public static final Block TANGLEBERRIE = make(LibBlockNames.SUBTILE_TANGLEBERRIE,
			key -> new SpecialFlowerBlock(
					BotaniaMobEffects.BLOODTHIRST,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.TANGLEBERRIE));
	public static final Block TANGLEBERRIE_PETITE = make(petite(LibBlockNames.SUBTILE_TANGLEBERRIE),
			key -> new SpecialFlowerBlock(
					BotaniaMobEffects.BLOODTHIRST,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.TANGLEBERRIE_PETITE));
	public static final Block FLOATING_TANGLEBERRIE = make(floating(LibBlockNames.SUBTILE_TANGLEBERRIE),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.TANGLEBERRIE));
	public static final Block FLOATING_TANGLEBERRIE_PETITE = make(petite(floating(LibBlockNames.SUBTILE_TANGLEBERRIE)),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.TANGLEBERRIE_PETITE));
	public static final Block POTTED_TANGLEBERRIE = make(potted(LibBlockNames.SUBTILE_TANGLEBERRIE),
			key -> flowerPot(TANGLEBERRIE, 0, key));
	public static final Block POTTED_TANGLEBERRIE_PETITE = make(petite(potted(LibBlockNames.SUBTILE_TANGLEBERRIE)),
			key -> flowerPot(TANGLEBERRIE_PETITE, 0, key));

	public static final Block JIYUULIA = make(LibBlockNames.SUBTILE_JIYUULIA,
			key -> new SpecialFlowerBlock(
					BotaniaMobEffects.EMPTINESS,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.JIYUULIA));
	public static final Block JIYUULIA_PETITE = make(petite(LibBlockNames.SUBTILE_JIYUULIA),
			key -> new SpecialFlowerBlock(
					BotaniaMobEffects.EMPTINESS,
					120,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.JIYUULIA_PETITE));
	public static final Block FLOATING_JIYUULIA = make(floating(LibBlockNames.SUBTILE_JIYUULIA),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.JIYUULIA));
	public static final Block FLOATING_JIYUULIA_PETITE = make(petite(floating(LibBlockNames.SUBTILE_JIYUULIA)),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.JIYUULIA_PETITE));
	public static final Block POTTED_JIYULLIA = make(potted(LibBlockNames.SUBTILE_JIYUULIA),
			key -> flowerPot(JIYUULIA, 0, key));
	public static final Block POTTED_JIYUULIA_PETITE = make(petite(potted(LibBlockNames.SUBTILE_JIYUULIA)),
			key -> flowerPot(JIYUULIA_PETITE, 0, key));

	public static final Block RANNUNCARPUS = make(LibBlockNames.SUBTILE_RANNUNCARPUS,
			key -> new RannuncarpusBlock(
					MobEffects.JUMP_BOOST,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.RANNUNCARPUS));
	public static final Block RANNUNCARPUS_PETITE = make(petite(LibBlockNames.SUBTILE_RANNUNCARPUS),
			key -> new RannuncarpusBlock(
					MobEffects.JUMP_BOOST,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.RANNUNCARPUS_PETITE));
	public static final Block FLOATING_RANNUNCARPUS = make(floating(LibBlockNames.SUBTILE_RANNUNCARPUS),
			key -> new FloatingRannuncarpusBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.RANNUNCARPUS));
	public static final Block FLOATING_RANNUNCARPUS_PETITE = make(petite(floating(LibBlockNames.SUBTILE_RANNUNCARPUS)),
			key -> new FloatingRannuncarpusBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.RANNUNCARPUS_PETITE));
	public static final Block POTTED_RANNUNCARPUS = make(potted(LibBlockNames.SUBTILE_RANNUNCARPUS),
			key -> flowerPot(RANNUNCARPUS, 0, key));
	public static final Block POTTED_RANNUNCARPUS_PETITE = make(petite(potted(LibBlockNames.SUBTILE_RANNUNCARPUS)),
			key -> flowerPot(RANNUNCARPUS_PETITE, 0, key));

	public static final Block HYACIDUS = make(LibBlockNames.SUBTILE_HYACIDUS,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.POISON,
					48,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HYACIDUS));
	public static final Block FLOATING_HYACIDUS = make(floating(LibBlockNames.SUBTILE_HYACIDUS),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.HYACIDUS));
	public static final Block POTTED_HYACIDUS = make(potted(LibBlockNames.SUBTILE_HYACIDUS),
			key -> flowerPot(HYACIDUS, 0, key));

	public static final Block POLLIDISIAC = make(LibBlockNames.SUBTILE_POLLIDISIAC,
			key -> new PollidisiacBlock(
					MobEffects.HASTE,
					369,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.POLLIDISIAC));
	public static final Block FLOATING_POLLIDISIAC = make(floating(LibBlockNames.SUBTILE_POLLIDISIAC),
			key -> new FloatingPollidisiacBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.POLLIDISIAC));
	public static final Block POTTED_POLLIDISIAC = make(potted(LibBlockNames.SUBTILE_POLLIDISIAC),
			key -> flowerPot(POLLIDISIAC, 0, key));

	public static final Block CLAYCONIA = make(LibBlockNames.SUBTILE_CLAYCONIA,
			key -> new SpecialFlowerBlock(
					MobEffects.WEAKNESS,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.CLAYCONIA));
	public static final Block CLAYCONIA_PETITE = make(petite(LibBlockNames.SUBTILE_CLAYCONIA),
			key -> new SpecialFlowerBlock(
					MobEffects.WEAKNESS,
					30,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.CLAYCONIA_PETITE));
	public static final Block FLOATING_CLAYCONIA = make(floating(LibBlockNames.SUBTILE_CLAYCONIA),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.CLAYCONIA));
	public static final Block FLOATING_CLAYCONIA_PETITE = make(petite(floating(LibBlockNames.SUBTILE_CLAYCONIA)),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.CLAYCONIA_PETITE));
	public static final Block POTTED_CLAYCONIA = make(potted(LibBlockNames.SUBTILE_CLAYCONIA),
			key -> flowerPot(CLAYCONIA, 0, key));
	public static final Block POTTED_CLAYCONIA_PETITE = make(petite(potted(LibBlockNames.SUBTILE_CLAYCONIA)),
			key -> flowerPot(CLAYCONIA_PETITE, 0, key));

	public static final Block LOONIUM = make(LibBlockNames.SUBTILE_LOONIUM,
			key -> new PoweredSpecialFlowerBlock(
					BotaniaMobEffects.ALLURE,
					900,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.LOONIUM));
	public static final Block FLOATING_LOONIUM = make(floating(LibBlockNames.SUBTILE_LOONIUM),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.LOONIUM));
	public static final Block POTTED_LOONIUM = make(potted(LibBlockNames.SUBTILE_LOONIUM),
			key -> flowerPot(LOONIUM, 0, key));

	public static final Block DAFFOMILL = make(LibBlockNames.SUBTILE_DAFFOMILL,
			key -> new DaffomillBlock(
					MobEffects.LEVITATION,
					6,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.DAFFOMILL));
	public static final Block FLOATING_DAFFOMILL = make(floating(LibBlockNames.SUBTILE_DAFFOMILL),
			key -> new FloatingDaffomillBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.DAFFOMILL));
	public static final Block POTTED_DAFFOMILL = make(potted(LibBlockNames.SUBTILE_DAFFOMILL),
			key -> flowerPot(DAFFOMILL, 0, key));

	public static final Block VINCULOTUS = make(LibBlockNames.SUBTILE_VINCULOTUS,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.NIGHT_VISION,
					900,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.VINCULOTUS));
	public static final Block FLOATING_VINCULOTUS = make(floating(LibBlockNames.SUBTILE_VINCULOTUS),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.VINCULOTUS));
	public static final Block POTTED_VINCULOTUS = make(potted(LibBlockNames.SUBTILE_VINCULOTUS),
			key -> flowerPot(VINCULOTUS, 0, key));

	public static final Block SPECTRANTHEMUM = make(LibBlockNames.SUBTILE_SPECTRANTHEMUM,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.INVISIBILITY,
					360,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SPECTRANTHEMUM));
	public static final Block FLOATING_SPECTRANTHEMUM = make(floating(LibBlockNames.SUBTILE_SPECTRANTHEMUM),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SPECTRANTHEMUM));
	public static final Block POTTED_SPECTRANTHEMUM = make(potted(LibBlockNames.SUBTILE_SPECTRANTHEMUM),
			key -> flowerPot(SPECTRANTHEMUM, 0, key));

	public static final Block MEDUMONE = make(LibBlockNames.SUBTILE_MEDUMONE,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.SLOWNESS,
					3600,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MEDUMONE));
	public static final Block FLOATING_MEDUMONE = make(floating(LibBlockNames.SUBTILE_MEDUMONE),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MEDUMONE));
	public static final Block POTTED_MEDUMONE = make(potted(LibBlockNames.SUBTILE_MEDUMONE),
			key -> flowerPot(MEDUMONE, 0, key));

	public static final Block MARIMORPHOSIS = make(LibBlockNames.SUBTILE_MARIMORPHOSIS,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.MINING_FATIGUE,
					60,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MARIMORPHOSIS));
	public static final Block MARIMORPHOSIS_PETITE = make(petite(LibBlockNames.SUBTILE_MARIMORPHOSIS),
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.MINING_FATIGUE,
					60,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MARIMORPHOSIS_PETITE));
	public static final Block FLOATING_MARIMORPHOSIS = make(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MARIMORPHOSIS));
	public static final Block FLOATING_MARIMORPHOSIS_PETITE = make(petite(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS)),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.MARIMORPHOSIS_PETITE));
	public static final Block POTTED_MARIMORPHOSIS = make(potted(LibBlockNames.SUBTILE_MARIMORPHOSIS),
			key -> flowerPot(MARIMORPHOSIS, 0, key));
	public static final Block POTTED_MARIMORPHOSIS_PETITE = make(petite(potted(LibBlockNames.SUBTILE_MARIMORPHOSIS)),
			key -> flowerPot(MARIMORPHOSIS_PETITE, 0, key));

	public static final Block BUBBELL = make(LibBlockNames.SUBTILE_BUBBELL,
			key -> new SpecialFlowerBlock(
					MobEffects.WATER_BREATHING,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BUBBELL));
	public static final Block BUBBELL_PETITE = make(petite(LibBlockNames.SUBTILE_BUBBELL),
			key -> new SpecialFlowerBlock(MobEffects.WATER_BREATHING,
					240,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BUBBELL_PETITE));
	public static final Block FLOATING_BUBBELL = make(floating(LibBlockNames.SUBTILE_BUBBELL),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BUBBELL));
	public static final Block FLOATING_BUBBELL_PETITE = make(petite(floating(LibBlockNames.SUBTILE_BUBBELL)),
			key -> new FloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.BUBBELL_PETITE));
	public static final Block POTTED_BUBBELL = make(potted(LibBlockNames.SUBTILE_BUBBELL),
			key -> flowerPot(BUBBELL, 0, key));
	public static final Block POTTED_BUBBELL_PETITE = make(petite(potted(LibBlockNames.SUBTILE_BUBBELL)),
			key -> flowerPot(BUBBELL_PETITE, 0, key));

	public static final Block SOLEGNOLIA = make(LibBlockNames.SUBTILE_SOLEGNOLIA,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.INSTANT_DAMAGE,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SOLEGNOLIA));
	public static final Block SOLEGNOLIA_PETITE = make(petite(LibBlockNames.SUBTILE_SOLEGNOLIA),
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.INSTANT_DAMAGE,
					1,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SOLEGNOLIA_PETITE));
	public static final Block FLOATING_SOLEGNOLIA = make(floating(LibBlockNames.SUBTILE_SOLEGNOLIA),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SOLEGNOLIA));
	public static final Block FLOATING_SOLEGNOLIA_PETITE = make(petite(floating(LibBlockNames.SUBTILE_SOLEGNOLIA)),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.SOLEGNOLIA_PETITE));
	public static final Block POTTED_SOLEGNOLIA = make(potted(LibBlockNames.SUBTILE_SOLEGNOLIA),
			key -> flowerPot(SOLEGNOLIA, 0, key));
	public static final Block POTTED_SOLEGNOLIA_PETITE = make(petite(potted(LibBlockNames.SUBTILE_SOLEGNOLIA)),
			key -> flowerPot(SOLEGNOLIA_PETITE, 0, key));

	public static final Block ORECHID_IGNEM = make(LibBlockNames.SUBTILE_ORECHID_IGNEM,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.FIRE_RESISTANCE,
					600,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ORECHID_IGNEM));
	public static final Block FLOATING_ORECHID_IGNEM = make(floating(LibBlockNames.SUBTILE_ORECHID_IGNEM),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.ORECHID_IGNEM));
	public static final Block POTTED_ORECHID_IGNEM = make(potted(LibBlockNames.SUBTILE_ORECHID_IGNEM),
			key -> flowerPot(ORECHID_IGNEM, 0, key));

	public static final Block LABELLIA = make(LibBlockNames.SUBTILE_LABELLIA,
			key -> new PoweredSpecialFlowerBlock(
					MobEffects.FIRE_RESISTANCE,
					600,
					BlockBehaviour.Properties.ofFullCopy(WHITE_MYSTICAL_FLOWER).setId(key),
					() -> BotaniaBlockEntities.LABELLIA));
	public static final Block FLOATING_LABELLIA = make(floating(LibBlockNames.SUBTILE_LABELLIA),
			key -> new PoweredFloatingSpecialFlowerBlock(
					BlockBehaviour.Properties.ofFullCopy(WHITE_FLOATING_FLOWER).setId(key),
					() -> BotaniaBlockEntities.LABELLIA));
	public static final Block POTTED_LABELLIA = make(potted(LibBlockNames.SUBTILE_LABELLIA),
			key -> flowerPot(LABELLIA, 0, key));

	// petal apothecary variants
	public static final Block PETAL_APOTHECARY = makePetalApothecary(LibBlockNames.PETAL_APOTHECARY,
			SoundType.STONE, MapColor.STONE);
	public static final Block PETAL_APOTHECARY_DEEPSLATE = makePetalApothecary("deepslate" + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.DEEPSLATE, MapColor.DEEPSLATE);
	public static final Block PETAL_APOTHECARY_LIVINGROCK = makePetalApothecary("livingrock" + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.STONE, MapColor.TERRACOTTA_WHITE);
	public static final Block PETAL_APOTHECARY_BLACKSTONE = makePetalApothecary("blackstone" + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.STONE, MapColor.COLOR_BLACK);
	public static final Block PETAL_APOTHECARY_NETHER_BRICKS = makePetalApothecary("nether_brick" + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.NETHER_BRICKS, MapColor.NETHER);
	public static final Block PETAL_APOTHECARY_RED_NETHER_BRICKS = makePetalApothecary("red_nether_brick" + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.NETHER_BRICKS, MapColor.NETHER);
	public static final Block PETAL_APOTHECARY_MOSSY = makePetalApothecary("mossy" + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.STONE, MapColor.STONE);
	public static final Block PETAL_APOTHECARY_FUCHSITE = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.TUFF, MapColor.PLANT);
	public static final Block PETAL_APOTHECARY_TALC = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.CALCITE, MapColor.SNOW);
	public static final Block PETAL_APOTHECARY_GNEISS = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.DEEPSLATE_TILES, MapColor.COLOR_LIGHT_GRAY);
	public static final Block PETAL_APOTHECARY_MYCELITE = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.DEEPSLATE_BRICKS, MapColor.CRIMSON_STEM);
	public static final Block PETAL_APOTHECARY_CATACLASITE = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.DEEPSLATE_TILES, MapColor.TERRACOTTA_BROWN);
	public static final Block PETAL_APOTHECARY_SOLITE = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.DEEPSLATE, MapColor.TERRACOTTA_ORANGE);
	public static final Block PETAL_APOTHECARY_LUNITE = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.DEEPSLATE, MapColor.COLOR_BLUE);
	public static final Block PETAL_APOTHECARY_ROSY_TALC = makePetalApothecary(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.PETAL_APOTHECARY_SUFFIX,
			SoundType.CALCITE, MapColor.TERRACOTTA_WHITE);
	public static final Block[] ALL_APOTHECARIES = new Block[] { PETAL_APOTHECARY, PETAL_APOTHECARY_DEEPSLATE,
			PETAL_APOTHECARY_LIVINGROCK, PETAL_APOTHECARY_BLACKSTONE, PETAL_APOTHECARY_NETHER_BRICKS,
			PETAL_APOTHECARY_RED_NETHER_BRICKS, PETAL_APOTHECARY_MOSSY, PETAL_APOTHECARY_FUCHSITE,
			PETAL_APOTHECARY_TALC, PETAL_APOTHECARY_GNEISS, PETAL_APOTHECARY_MYCELITE, PETAL_APOTHECARY_CATACLASITE,
			PETAL_APOTHECARY_SOLITE, PETAL_APOTHECARY_LUNITE, PETAL_APOTHECARY_ROSY_TALC };

	// livingrock blocks
	public static final Block LIVINGROCK = make(LibBlockNames.LIVINGROCK,
			key -> new Block(BlockBehaviour.Properties.of()
					.strength(2, 10)
					.sound(SoundType.STONE)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.mapColor(MapColor.TERRACOTTA_WHITE)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block LIVINGROCK_STAIRS = make(LibBlockNames.LIVINGROCK + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LIVINGROCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block LIVINGROCK_SLAB = make(LibBlockNames.LIVINGROCK + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block LIVINGROCK_WALL = make(LibBlockNames.LIVINGROCK + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block LIVINGROCK_BUTTON = make(LibBlockNames.LIVINGROCK + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.LIVINGROCK_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block LIVINGROCK_PRESSURE_PLATE = make(LibBlockNames.LIVINGROCK + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.LIVINGROCK_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block POLISHED_LIVINGROCK = make(LibBlockNames.POLISHED_PREFIX + LibBlockNames.LIVINGROCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block POLISHED_LIVINGROCK_STAIRS = make(LibBlockNames.POLISHED_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LIVINGROCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(POLISHED_LIVINGROCK).setId(key)));
	public static final Block POLISHED_LIVINGROCK_SLAB = make(LibBlockNames.POLISHED_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_LIVINGROCK).setId(key)));
	public static final Block POLISHED_LIVINGROCK_WALL = make(LibBlockNames.POLISHED_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(POLISHED_LIVINGROCK).setId(key)));
	public static final Block LIVINGROCK_SLATE = make(LibBlockNames.LIVINGROCK + LibBlockNames.SLATE_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block LIVINGROCK_BRICKS = make(LibBlockNames.LIVINGROCK + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block LIVINGROCK_BRICK_STAIRS = make(LibBlockNames.LIVINGROCK + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LIVINGROCK_BRICKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGROCK_BRICKS).setId(key)));
	public static final Block LIVINGROCK_BRICK_SLAB = make(LibBlockNames.LIVINGROCK + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK_BRICKS).setId(key)));
	public static final Block LIVINGROCK_BRICK_WALL = make(LibBlockNames.LIVINGROCK + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK_BRICKS).setId(key)));
	public static final Block MOSSY_LIVINGROCK_BRICKS = make(LibBlockNames.MOSSY_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block MOSSY_LIVINGROCK_BRICK_STAIRS = make(LibBlockNames.MOSSY_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(MOSSY_LIVINGROCK_BRICKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(MOSSY_LIVINGROCK_BRICKS).setId(key)));
	public static final Block MOSSY_LIVINGROCK_BRICK_SLAB = make(LibBlockNames.MOSSY_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_LIVINGROCK_BRICKS).setId(key)));
	public static final Block MOSSY_LIVINGROCK_BRICK_WALL = make(LibBlockNames.MOSSY_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(MOSSY_LIVINGROCK_BRICKS).setId(key)));
	public static final Block CRACKED_LIVINGROCK_BRICKS = make(LibBlockNames.CRACKED_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block CHISELED_LIVINGROCK_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.LIVINGROCK + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));

	// livingwood blocks
	public static final Block LIVINGWOOD_LOG = make(LibBlockNames.LIVINGWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
					.strength(2)
					.sound(SoundType.WOOD)
					.instrument(NoteBlockInstrument.BASS)
					.mapColor(state -> state.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y
							? MapColor.TERRACOTTA_RED
							: MapColor.TERRACOTTA_BROWN)
					.setId(key)));
	public static final Block LIVINGWOOD = make(LibBlockNames.LIVINGWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_BROWN).setId(key)));
	public static final Block LIVINGWOOD_STAIRS = make(LibBlockNames.LIVINGWOOD + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LIVINGWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block LIVINGWOOD_SLAB = make(LibBlockNames.LIVINGWOOD + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block LIVINGWOOD_WALL = make(LibBlockNames.LIVINGWOOD + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block STRIPPED_LIVINGWOOD_LOG = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.LIVINGWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_RED).setId(key)));
	public static final Block STRIPPED_LIVINGWOOD = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.LIVINGWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_LIVINGWOOD_LOG).setId(key)));
	public static final Block STRIPPED_LIVINGWOOD_STAIRS = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.LIVINGWOOD + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(STRIPPED_LIVINGWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block STRIPPED_LIVINGWOOD_SLAB = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.LIVINGWOOD + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block STRIPPED_LIVINGWOOD_WALL = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.LIVINGWOOD + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block GLIMMERING_LIVINGWOOD_LOG = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.LIVINGWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.lightLevel(constInt(12)).setId(key)));
	public static final Block GLIMMERING_LIVINGWOOD = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.LIVINGWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(GLIMMERING_LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_BROWN).setId(key)));
	public static final Block STRIPPED_GLIMMERING_LIVINGWOOD_LOG = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.STRIPPED_PREFIX + LibBlockNames.LIVINGWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_LIVINGWOOD_LOG)
					.lightLevel(b -> 8).setId(key)));
	public static final Block STRIPPED_GLIMMERING_LIVINGWOOD = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.STRIPPED_PREFIX + LibBlockNames.LIVINGWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_GLIMMERING_LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_BROWN).setId(key)));
	public static final Block LIVINGWOOD_PLANKS = make(LibBlockNames.LIVINGWOOD + LibBlockNames.PLANKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.TERRACOTTA_RED).setId(key)));
	public static final Block LIVINGWOOD_PLANK_STAIRS = make(LibBlockNames.LIVINGWOOD + LibBlockNames.PLANK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LIVINGWOOD_PLANKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS).setId(key)));
	public static final Block LIVINGWOOD_PLANK_SLAB = make(LibBlockNames.LIVINGWOOD + LibBlockNames.PLANK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS).setId(key)));
	public static final Block LIVINGWOOD_FENCE = make(LibBlockNames.LIVINGWOOD + LibBlockNames.FENCE_SUFFIX,
			key -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block LIVINGWOOD_FENCE_GATE = make(LibBlockNames.LIVINGWOOD + LibBlockNames.FENCE_GATE_SUFFIX,
			key -> new FenceGateBlock(BotaniaBlockSetTypes.LIVINGWOOD, BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block LIVINGWOOD_DOOR = make(LibBlockNames.LIVINGWOOD + LibBlockNames.DOOR_SUFFIX,
			key -> new DoorBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noOcclusion()
							.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block LIVINGWOOD_TRAPDOOR = make(LibBlockNames.LIVINGWOOD + LibBlockNames.TRAPDOOR_SUFFIX,
			key -> new TrapDoorBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noOcclusion()
							.isValidSpawn(NO_SPAWN).setId(key)));
	public static final Block LIVINGWOOD_PRESSURE_PLATE = make(LibBlockNames.LIVINGWOOD + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noCollision()
							.strength(0.5F)
							.forceSolidOn()
							.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block LIVINGWOOD_BUTTON = make(LibBlockNames.LIVINGWOOD + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.LIVINGWOOD_BLOCK_SET, 30,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noCollision()
							.strength(0.5F)
							.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block LIVINGWOOD_SIGN = make(LibBlockNames.LIVINGWOOD + LibBlockNames.SIGN_SUFFIX,
			key -> new StandingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS)
							.noCollision()
							.strength(1.0f)
							.forceSolidOn().setId(key)));
	public static final Block LIVINGWOOD_WALL_SIGN = make(LibBlockNames.LIVINGWOOD + LibBlockNames.WALL_INFIX + LibBlockNames.SIGN_SUFFIX,
			key -> new WallSignBlock(BotaniaBlockSetTypes.LIVINGWOOD, BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_SIGN).setId(key)));
	public static final Block LIVINGWOOD_HANGING_SIGN = make(LibBlockNames.LIVINGWOOD + LibBlockNames.HANGING_SIGN_SUFFIX,
			key -> new CeilingHangingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_SIGN).setId(key)));
	public static final Block LIVINGWOOD_WALL_HANGING_SIGN = make(LibBlockNames.LIVINGWOOD + LibBlockNames.WALL_INFIX + LibBlockNames.HANGING_SIGN_SUFFIX,
			key -> new WallHangingSignBlock(BotaniaBlockSetTypes.LIVINGWOOD,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_SIGN).setId(key)));
	public static final Block MOSSY_LIVINGWOOD_PLANKS = make(LibBlockNames.MOSSY_PREFIX + LibBlockNames.LIVINGWOOD + LibBlockNames.PLANKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS).setId(key)));
	public static final Block FRAMED_LIVINGWOOD = make(LibBlockNames.FRAMED_PREFIX + LibBlockNames.LIVINGWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS).setId(key)));
	public static final Block PATTERN_FRAMED_LIVINGWOOD = make(LibBlockNames.PATTERN_FRAMED_PREFIX + LibBlockNames.LIVINGWOOD,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_PLANKS).setId(key)));

	// dreamwood blocks
	public static final Block DREAMWOOD_LOG = make(LibBlockNames.DREAMWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD_LOG)
					.mapColor(MapColor.QUARTZ).setId(key)));
	public static final Block DREAMWOOD = make(LibBlockNames.DREAMWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG).setId(key)));
	public static final Block DREAMWOOD_STAIRS = make(LibBlockNames.DREAMWOOD + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(DREAMWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).setId(key)));
	public static final Block DREAMWOOD_SLAB = make(LibBlockNames.DREAMWOOD + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).setId(key)));
	public static final Block DREAMWOOD_WALL = make(LibBlockNames.DREAMWOOD + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).setId(key)));
	public static final Block STRIPPED_DREAMWOOD_LOG = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.DREAMWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG).setId(key)));
	public static final Block STRIPPED_DREAMWOOD = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.DREAMWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG).setId(key)));
	public static final Block STRIPPED_DREAMWOOD_STAIRS = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.DREAMWOOD + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(STRIPPED_DREAMWOOD.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).setId(key)));
	public static final Block STRIPPED_DREAMWOOD_SLAB = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.DREAMWOOD + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).setId(key)));
	public static final Block STRIPPED_DREAMWOOD_WALL = make(LibBlockNames.STRIPPED_PREFIX + LibBlockNames.DREAMWOOD + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).setId(key)));
	public static final Block GLIMMERING_DREAMWOOD_LOG = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.DREAMWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(GLIMMERING_LIVINGWOOD_LOG).mapColor(MapColor.QUARTZ).setId(key)));
	public static final Block GLIMMERING_DREAMWOOD = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.DREAMWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(GLIMMERING_DREAMWOOD_LOG).setId(key)));
	public static final Block STRIPPED_GLIMMERING_DREAMWOOD_LOG = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.STRIPPED_PREFIX + LibBlockNames.DREAMWOOD + LibBlockNames.LOG_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_GLIMMERING_LIVINGWOOD_LOG)
					.mapColor(MapColor.QUARTZ).setId(key)));
	public static final Block STRIPPED_GLIMMERING_DREAMWOOD = make(LibBlockNames.GLIMMERING_PREFIX + LibBlockNames.STRIPPED_PREFIX + LibBlockNames.DREAMWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(STRIPPED_GLIMMERING_DREAMWOOD_LOG).setId(key)));
	public static final Block DREAMWOOD_PLANKS = make(LibBlockNames.DREAMWOOD + LibBlockNames.PLANKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_LOG).setId(key)));
	public static final Block DREAMWOOD_PLANK_STAIRS = make(LibBlockNames.DREAMWOOD + LibBlockNames.PLANK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(DREAMWOOD_PLANKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS).setId(key)));
	public static final Block DREAMWOOD_PLANK_SLAB = make(LibBlockNames.DREAMWOOD + LibBlockNames.PLANK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS).setId(key)));
	public static final Block DREAMWOOD_FENCE = make(LibBlockNames.DREAMWOOD + LibBlockNames.FENCE_SUFFIX,
			key -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS).setId(key)));
	public static final Block DREAMWOOD_FENCE_GATE = make(LibBlockNames.DREAMWOOD + LibBlockNames.FENCE_GATE_SUFFIX,
			key -> new FenceGateBlock(BotaniaBlockSetTypes.DREAMWOOD, BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS).setId(key)));
	public static final Block DREAMWOOD_DOOR = make(LibBlockNames.DREAMWOOD + LibBlockNames.DOOR_SUFFIX,
			key -> new DoorBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noOcclusion()
							.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block DREAMWOOD_TRAPDOOR = make(LibBlockNames.DREAMWOOD + LibBlockNames.TRAPDOOR_SUFFIX,
			key -> new TrapDoorBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noOcclusion()
							.isValidSpawn(NO_SPAWN).setId(key)));
	public static final Block DREAMWOOD_PRESSURE_PLATE = make(LibBlockNames.DREAMWOOD + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noCollision()
							.strength(0.5F)
							.forceSolidOn()
							.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block DREAMWOOD_BUTTON = make(LibBlockNames.DREAMWOOD + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.DREAMWOOD_BLOCK_SET, 30,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noCollision()
							.strength(0.5F)
							.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block DREAMWOOD_SIGN = make(LibBlockNames.DREAMWOOD + LibBlockNames.SIGN_SUFFIX,
			key -> new StandingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS)
							.noCollision()
							.strength(1.0f)
							.forceSolidOn().setId(key)));
	public static final Block DREAMWOOD_WALL_SIGN = make(LibBlockNames.DREAMWOOD + LibBlockNames.WALL_INFIX + LibBlockNames.SIGN_SUFFIX,
			key -> new WallSignBlock(BotaniaBlockSetTypes.DREAMWOOD, BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_SIGN).setId(key)));
	public static final Block DREAMWOOD_HANGING_SIGN = make(LibBlockNames.DREAMWOOD + LibBlockNames.HANGING_SIGN_SUFFIX,
			key -> new CeilingHangingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_SIGN).setId(key)));
	public static final Block DREAMWOOD_WALL_HANGING_SIGN = make(LibBlockNames.DREAMWOOD + LibBlockNames.WALL_INFIX + LibBlockNames.HANGING_SIGN_SUFFIX,
			key -> new WallHangingSignBlock(BotaniaBlockSetTypes.DREAMWOOD,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_SIGN).setId(key)));
	public static final Block MOSSY_DREAMWOOD_PLANKS = make(LibBlockNames.MOSSY_PREFIX + LibBlockNames.DREAMWOOD + LibBlockNames.PLANKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS).setId(key)));
	public static final Block FRAMED_DREAMWOOD = make(LibBlockNames.FRAMED_PREFIX + LibBlockNames.DREAMWOOD,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS).setId(key)));
	public static final Block PATTERN_FRAMED_DREAMWOOD = make(LibBlockNames.PATTERN_FRAMED_PREFIX + LibBlockNames.DREAMWOOD,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(DREAMWOOD_PLANKS).setId(key)));

	// mana spreaders
	public static final ManaSpreaderBlock MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.MANA_SPREADER,
			(color, key) -> new ManaSpreaderBlock(ManaSpreaderBlock.DEFAULT_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER).setId(key)),
			LibBlockNames.COVERED_INFIX);
	public static final ManaSpreaderBlock PULSE_MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.PULSE_MANA_SPREADER,
			(color, key) -> new ManaSpreaderBlock(ManaSpreaderBlock.PULSE_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER).setId(key)),
			LibBlockNames.COVERED_INFIX);
	public static final ManaSpreaderBlock ELVEN_MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.ELVEN_MANA_SPREADER,
			(color, key) -> new ManaSpreaderBlock(ManaSpreaderBlock.ELVEN_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER).setId(key)),
			LibBlockNames.COVERED_INFIX);
	public static final ManaSpreaderBlock GAIA_MANA_SPREADER = makeBlockWithColoredVariants(LibBlockNames.GAIA_MANA_SPREADER,
			(color, key) -> new ManaSpreaderBlock(ManaSpreaderBlock.GAIA_SPREADER_PARAMETERS, color,
					BlockBehaviour.Properties.ofFullCopy(DREAMWOOD).isValidSpawn(NO_SPAWN).isRedstoneConductor(NEVER).setId(key)),
			LibBlockNames.COVERED_INFIX);

	// mana pools
	public static final ManaPoolBlock MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.MANA_POOL,
			(color, key) -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA, false, false,
					ManaPoolBlock.NORMAL_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final ManaPoolBlock CREATIVE_MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.CREATIVE_MANA_POOL,
			(color, key) -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA, false, true,
					ManaPoolBlock.BIG_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final ManaPoolBlock DILUTED_MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.DILUTED_MANA_POOL,
			(color, key) -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA_DILUTED, false, false,
					ManaPoolBlock.SMALL_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final ManaPoolBlock FABULOUS_MANA_POOL = makeBlockWithColoredVariants(LibBlockNames.FABULOUS_MANA_POOL,
			(color, key) -> new ManaPoolBlock(ManaPoolBlock.MAX_MANA, true, false,
					ManaPoolBlock.NORMAL_SHAPE_VARIANT, color, BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block ALCHEMY_CATALYST = make(LibBlockNames.ALCHEMY_CATALYST,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block CONJURATION_CATALYST = make(LibBlockNames.CONJURATION_CATALYST,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));

	// resource blocks
	public static final Block MANASTEEL_BLOCK = make(LibBlockNames.MANASTEEL_BLOCK,
			key -> new Block(BlockBehaviour.Properties.of()
					.strength(3, 10)
					.mapColor(MapColor.LAPIS)
					.sound(SoundType.METAL)
					.instrument(NoteBlockInstrument.IRON_XYLOPHONE)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block TERRASTEEL_BLOCK = make(LibBlockNames.TERRASTEEL_BLOCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.mapColor(MapColor.EMERALD).setId(key)));
	public static final Block ELEMENTIUM_BLOCK = make(LibBlockNames.ELEMENTIUM_BLOCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.mapColor(MapColor.COLOR_PINK).setId(key)));
	public static final Block MANA_DIAMOND_BLOCK = make(LibBlockNames.MANA_DIAMOND_BLOCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.instrument(NoteBlockInstrument.HARP)
					.mapColor(MapColor.DIAMOND).setId(key)));
	public static final Block DRAGONSTONE_BLOCK = make(LibBlockNames.DRAGONSTONE_BLOCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MANA_DIAMOND_BLOCK)
					.mapColor(MapColor.COLOR_PINK).setId(key)));

	// glass blocks
	public static final Block MANAGLASS = make(LibBlockNames.MANAGLASS,
			// [VanillaCopy] Blocks.GLASS
			key -> new TransparentBlock(BlockBehaviour.Properties.of()
					.instrument(NoteBlockInstrument.HAT)
					.strength(0.3F)
					.sound(SoundType.GLASS)
					.noOcclusion()
					.isValidSpawn(NO_SPAWN)
					.isRedstoneConductor(NEVER)
					.isSuffocating(NEVER)
					.isViewBlocking(NEVER)
					// Botania: mana glasses emit a lot of light
					.lightLevel(constInt(15)).setId(key)));
	public static final Block ALFGLASS = make(LibBlockNames.ALFGLASS,
			key -> new TransparentBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS).setId(key)));
	public static final Block BIFROST_BRIDGE = make(LibBlockNames.BIFROST_BRIDGE,
			key -> new BifrostBridgeBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS)
					.destroyTime(-1).setId(key)));
	public static final Block BIFROST = make(LibBlockNames.BIFROST,
			key -> new BifrostBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS).setId(key)));

	// recipe blocks
	public static final Block RUNIC_ALTAR = make(LibBlockNames.RUNIC_ALTAR,
			key -> new RunicAltarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block MANA_ENCHANTER = make(LibBlockNames.MANA_ENCHANTER,
			key -> new ManaEnchanterBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.LAPIS)
					.strength(3, 5)
					.lightLevel(constInt(15))
					.sound(SoundType.STONE).setId(key)));
	public static final Block BOTANICAL_BREWERY = make(LibBlockNames.BOTANICAL_BREWERY,
			key -> new BotanicalBreweryBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block TERRESTRIAL_AGGLOMERATION_PLATE = make(LibBlockNames.TERRESTRIAL_AGGLOMERATION_PLATE,
			key -> new TerrestrialAgglomerationPlateBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.LAPIS)
					.strength(3, 10)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block ELVEN_GATEWAY_CORE = make(LibBlockNames.ELVEN_GATEWAY_CORE,
			key -> new AlfheimPortalBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.strength(10)
					.sound(SoundType.WOOD)
					.lightLevel(state -> state.getValue(
							BotaniaStateProperties.ALFPORTAL_STATE) != AlfheimPortalState.OFF ? 15 : 0)
					.setId(key)));

	// pylons
	public static final Block MANA_PYLON = make(LibBlockNames.MANA_PYLON,
			key -> new ManaPylonBlock(BlockBehaviour.Properties.of()
					.mapColor(DyeColor.LIGHT_BLUE)
					.noOcclusion()
					.strength(5.5F)
					.sound(SoundType.METAL)
					.lightLevel(constInt(7))
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block NATURA_PYLON = make(LibBlockNames.NATURA_PYLON,
			key -> new NaturaPylonBlock(BlockBehaviour.Properties.ofFullCopy(MANA_PYLON)
					.mapColor(MapColor.EMERALD).setId(key)));
	public static final Block GAIA_PYLON = make(LibBlockNames.GAIA_PYLON,
			key -> new GaiaPylonBlock(BlockBehaviour.Properties.ofFullCopy(MANA_PYLON)
					.mapColor(DyeColor.PINK).setId(key)));

	public static final Block MANA_SPLITTER = make(LibBlockNames.MANA_SPLITTER,
			key -> new ManaSplitterBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block MANA_VOID = make(LibBlockNames.MANA_VOID,
			key -> new ManaVoidBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)
					.strength(2, 2000).setId(key)));
	public static final Block MANA_DETECTOR = make(LibBlockNames.MANA_DETECTOR,
			key -> new ManaDetectorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block FORCE_RELAY = make(LibBlockNames.FORCE_RELAY,
			key -> new ForceRelayBlock(BlockBehaviour.Properties.of()
					.strength(2, 10)
					.sound(SoundType.METAL)
					.mapColor(MapColor.COLOR_PURPLE)
					.isValidSpawn(NO_SPAWN).setId(key)));
	public static final Block SPREADER_TURNTABLE = make(LibBlockNames.TURNTABLE,
			key -> new SpreaderTurntableBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block TINY_PLANET = make(LibBlockNames.TINY_PLANET,
			key -> new TinyPlanetBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.DEEPSLATE)
					.strength(20, 100)
					.sound(SoundType.DEEPSLATE)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block DRUM_OF_THE_WILD = make(LibBlockNames.DRUM_OF_THE_WILD,
			key -> new DrumOfTheWildBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block DRUM_OF_THE_GATHERING = make(LibBlockNames.DRUM_OF_THE_GATHERING,
			key -> new DrumOfTheGatheringBlock(BlockBehaviour.Properties.ofFullCopy(DRUM_OF_THE_WILD).setId(key)));
	public static final Block DRUM_OF_THE_CANOPY = make(LibBlockNames.DRUM_OF_THE_CANOPY,
			key -> new DrumOfTheCanopyBlock(BlockBehaviour.Properties.ofFullCopy(DRUM_OF_THE_WILD).setId(key)));
	public static final Block LIFE_IMBUER = make(LibBlockNames.LIFE_IMBUER,
			key -> new LifeImbuerBlock(BlockBehaviour.Properties.of()
					.sound(SoundType.METAL)
					.strength(3)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block MANA_FLUXFIELD = make(LibBlockNames.MANA_FLUXFIELD,
			key -> new PowerGeneratorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block MANA_PRISM = make(LibBlockNames.MANA_PRISM,
			key -> new ManaPrismBlock(BlockBehaviour.Properties.ofFullCopy(ALFGLASS)
					.noCollision().setId(key)));
	public static final Block MANA_PUMP = make(LibBlockNames.MANA_PUMP,
			key -> new ManaPumpBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block SPARK_TINKERER = make(LibBlockNames.SPARK_TINKERER,
			key -> new SparkTinkererBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block MANASTORM_CHARGE = make(LibBlockNames.MANASTORM_CHARGE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.strength(12).setId(key)));
	public static final Block MANATIDE_BELLOWS = make(LibBlockNames.MANATIDE_BELLOWS,
			key -> new BellowsBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));

	public static final Block OPEN_CRATE = make(LibBlockNames.OPEN_CRATE,
			key -> new OpenCrateBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block CRAFTY_CRATE = make(LibBlockNames.CRAFT_CRATE,
			key -> new CraftyCrateBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block EYE_OF_THE_ANCIENTS = make(LibBlockNames.EYE_OF_THE_ANCIENTS,
			key -> new EyeOfTheAncientsBlock(BlockBehaviour.Properties.of()
					.strength(5, 10)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block SOLID_VINE = make(LibBlockNames.SOLID_VINE,
			key -> new SolidVineBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.VINE).setId(key)));
	public static final Block ABSTRUSE_PLATFORM = make(LibBlockNames.PLATFORM_ABSTRUSE,
			key -> new AbstrusePlatformBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.strength(2, 5)
					.isValidSpawn(NO_SPAWN)
					.noOcclusion()
					.isViewBlocking(NEVER)
					.isSuffocating(NEVER).setId(key)));
	public static final Block SPECTRAL_PLATFORM = make(LibBlockNames.PLATFORM_SPECTRAL,
			key -> new SpectralPlatformBlock(BlockBehaviour.Properties.ofFullCopy(ABSTRUSE_PLATFORM).setId(key)));
	public static final Block INFRANGIBLE_PLATFORM = make(LibBlockNames.PLATFORM_INFRANGIBLE,
			key -> new InfrangiblePlatformBlock(BlockBehaviour.Properties.ofFullCopy(ABSTRUSE_PLATFORM)
					.strength(-1, Float.MAX_VALUE)
					.isValidSpawn(NO_SPAWN)
					.noOcclusion().setId(key)));
	public static final Block TINY_POTATO = make(LibBlockNames.TINY_POTATO,
			key -> new TinyPotatoBlock(BlockBehaviour.Properties.of()
					.strength(0.25F)
					.mapColor(DyeColor.PINK).setId(key)));
	public static final Block ENDER_OVERSEER = make(LibBlockNames.ENDER_OVERSEER,
			key -> new EnderOverseerBlock(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK).setId(key)));
	public static final Block RED_STRINGED_CONTAINER = make(LibBlockNames.RED_STRINGED_CONTAINER,
			key -> new RedStringContainerBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block RED_STRINGED_DISPENSER = make(LibBlockNames.RED_STRINGED_DISPENSER,
			key -> new RedStringDispenserBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block RED_STRINGED_NUTRIFIER = make(LibBlockNames.RED_STRINGED_NUTRIFIER,
			key -> new RedStringNutrifierBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block RED_STRINGED_COMPARATOR = make(LibBlockNames.RED_STRINGED_COMPARATOR,
			key -> new RedStringComparatorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block RED_STRINGED_SPOOFER = make(LibBlockNames.RED_STRINGED_SPOOFER,
			key -> new RedStringSpooferBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block RED_STRINGED_INTERCEPTOR = make(LibBlockNames.RED_STRINGED_INTERCEPTOR,
			key -> new RedStringInterceptorBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));

	public static final Block CORPOREA_FUNNEL = make(LibBlockNames.CORPOREA_FUNNEL,
			key -> new CorporeaFunnelBlock(BlockBehaviour.Properties.of()
					.strength(5.5F)
					.mapColor(DyeColor.PURPLE)
					.sound(SoundType.METAL)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block CORPOREA_INTERCEPTOR = make(LibBlockNames.CORPOREA_INTERCEPTOR,
			key -> new CorporeaInterceptorBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL).setId(key)));
	public static final Block CORPOREA_INDEX = make(LibBlockNames.CORPOREA_INDEX,
			key -> new CorporeaIndexBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL)
					.noOcclusion().setId(key)));
	public static final Block CORPOREA_CRYSTAL_CUBE = make(LibBlockNames.CORPOREA_CRYSTAL_CUBE,
			key -> new CorporeaCrystalCubeBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL).setId(key)));
	public static final Block CORPOREA_RETAINER = make(LibBlockNames.CORPOREA_RETAINER,
			key -> new CorporeaRetainerBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL).setId(key)));

	public static final Block CORPOREA_BLOCK = make(LibBlockNames.CORPOREA_BLOCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(CORPOREA_FUNNEL).setId(key)));
	public static final Block CORPOREA_STAIRS = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(CORPOREA_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK).setId(key)));
	public static final Block CORPOREA_SLAB = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK).setId(key)));
	public static final Block CORPOREA_WALL = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK).setId(key)));
	public static final Block CORPOREA_BUTTON = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.CORPOREA_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(DyeColor.PURPLE).setId(key)));
	public static final Block CORPOREA_PRESSURE_PLATE = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.CORPOREA_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(DyeColor.PURPLE).setId(key)));
	public static final Block CORPOREA_BRICKS = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BLOCK).setId(key)));
	public static final Block CORPOREA_BRICK_STAIRS = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(CORPOREA_BRICKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CORPOREA_BRICKS).setId(key)));
	public static final Block CORPOREA_BRICK_SLAB = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BRICKS).setId(key)));
	public static final Block CORPOREA_BRICK_WALL = make(LibBlockNames.CORPOREA_PREFIX + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CORPOREA_BRICKS).setId(key)));

	public static final Block INCENSE_PLATE = make(LibBlockNames.INCENSE_PLATE,
			key -> new IncensePlateBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD)
					.lightLevel(state -> state.getValue(BlockStateProperties.LIT) ? 2 : 0).setId(key)));
	public static final Block HOVERING_HOURGLASS = make(LibBlockNames.HOVERING_HOURGLASS,
			key -> new HoveringHourglassBlock(BlockBehaviour.Properties.of()
					.mapColor(MapColor.GOLD)
					.strength(2)
					.sound(SoundType.METAL).setId(key)));
	public static final Block SPECTRAL_RAIL = make(LibBlockNames.SPECTRAL_RAIL,
			key -> new SpectralRailBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RAIL).setId(key)));
	public static final Block LUMINIZER = make(LibBlockNames.LUMINIZER,
			key -> new LuminizerBlock(BlockBehaviour.Properties.of()
					.noCollision().setId(key)));
	public static final Block DETECTOR_LUMINIZER = make("detector" + LibBlockNames.LUMINIZER_SUFFIX,
			key -> new LuminizerDetectorBlock(BlockBehaviour.Properties.ofFullCopy(LUMINIZER).setId(key)));
	public static final Block FORK_LUMINIZER = make("fork" + LibBlockNames.LUMINIZER_SUFFIX,
			key -> new LuminizerForkBlock(BlockBehaviour.Properties.ofFullCopy(LUMINIZER).setId(key)));
	public static final Block TOGGLE_LUMINIZER = make("toggle" + LibBlockNames.LUMINIZER_SUFFIX,
			key -> new LuminizerToggleBlock(BlockBehaviour.Properties.ofFullCopy(LUMINIZER).setId(key)));
	public static final Block LUMINIZER_LAUNCHER = make(LibBlockNames.LUMINIZER_LAUNCHER,
			key -> new LuminizerLauncherBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block CACOPHONIUM_BLOCK = make(LibBlockNames.CACOPHONIUM,
			key -> new CacophoniumBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NOTE_BLOCK)
					.strength(0.8F).setId(key)));
	public static final Block CELLULAR_BLOCK = make(LibBlockNames.CELLULAR_BLOCK,
			key -> new CellularBlock(BlockBehaviour.Properties.of()
					.pushReaction(PushReaction.DESTROY)
					.sound(SoundType.WOOL).setId(key)));
	public static final Block TERU_TERU_BOZU = make(LibBlockNames.TERU_TERU_BOZU,
			key -> new TeruTeruBozuBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL)
					.instrument(NoteBlockInstrument.GUITAR)
					.mapColor(DyeColor.WHITE).setId(key)));
	public static final Block LIVINGWOOD_AVATAR = make(LibBlockNames.LIVINGWOOD_AVATAR,
			key -> new AvatarBlock(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block FAKE_AIR = make(LibBlockNames.FAKE_AIR,
			key -> new FakeAirBlock(BlockBehaviour.Properties.of()
					.replaceable()
					.noCollision()
					.noLootTable()
					.air()
					.randomTicks().setId(key)));
	public static final Block LIVING_ROOT = make(LibBlockNames.LIVING_ROOTS,
			key -> new Block(BlockBehaviour.Properties.of()
					.strength(1.2F)
					.sound(SoundType.WOOD).setId(key)));
	public static final Block FEL_PUMPKIN = make(LibBlockNames.FEL_PUMPKIN,
			key -> new FelPumpkinBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CARVED_PUMPKIN).setId(key)));
	public static final Block COCOON_OF_CAPRICE = make(LibBlockNames.COCOON_OF_CAPRICE,
			key -> new CocoonBlock(BlockBehaviour.Properties.of()
					.strength(3, 60)
					.sound(SoundType.WOOL).setId(key)));
	public static final Block ANIMATED_TORCH = make(LibBlockNames.ANIMATED_TORCH,
			key -> new AnimatedTorchBlock(BlockBehaviour.Properties.of()
					.noCollision()
					.instabreak()
					.lightLevel(state -> state.getValue(AnimatedTorchBlock.TRIGGERED) ? 0 : 7)
					.sound(SoundType.WOOD)
					.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block STARFIELD_CREATOR = make(LibBlockNames.STARFIELD_CREATOR,
			key -> new StarfieldCreatorBlock(BlockBehaviour.Properties.of()
					.mapColor(DyeColor.PINK)
					.strength(5, 2000)
					.sound(SoundType.METAL).setId(key)));

	public static final Block AZULEJO_0 = make(LibBlockNames.AZULEJO_PREFIX + 0,
			key -> new Block(BlockBehaviour.Properties.of()
					.mapColor(MapColor.LAPIS)
					.strength(2, 5)
					.sound(SoundType.STONE)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops().setId(key)));
	public static final Block AZULEJO_1 = make(LibBlockNames.AZULEJO_PREFIX + 1,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_2 = make(LibBlockNames.AZULEJO_PREFIX + 2,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_3 = make(LibBlockNames.AZULEJO_PREFIX + 3,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_4 = make(LibBlockNames.AZULEJO_PREFIX + 4,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_5 = make(LibBlockNames.AZULEJO_PREFIX + 5,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_6 = make(LibBlockNames.AZULEJO_PREFIX + 6,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_7 = make(LibBlockNames.AZULEJO_PREFIX + 7,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_8 = make(LibBlockNames.AZULEJO_PREFIX + 8,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_9 = make(LibBlockNames.AZULEJO_PREFIX + 9,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_10 = make(LibBlockNames.AZULEJO_PREFIX + 10,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_11 = make(LibBlockNames.AZULEJO_PREFIX + 11,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_12 = make(LibBlockNames.AZULEJO_PREFIX + 12,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_13 = make(LibBlockNames.AZULEJO_PREFIX + 13,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_14 = make(LibBlockNames.AZULEJO_PREFIX + 14,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));
	public static final Block AZULEJO_15 = make(LibBlockNames.AZULEJO_PREFIX + 15,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(AZULEJO_0).setId(key)));

	public static final Block MANA_FLAME = make(LibBlockNames.MANA_FLAME,
			key -> new ManaFlameBlock(BlockBehaviour.Properties.of()
					.pushReaction(PushReaction.DESTROY)
					.sound(SoundType.WOOL)
					.lightLevel(constInt(15))
					.noCollision().setId(key)));
	public static final Block BLAZE_MESH = make(LibBlockNames.BLAZE_BLOCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MANASTEEL_BLOCK)
					.instrument(NoteBlockInstrument.PLING)
					.lightLevel(constInt(15))
					.mapColor(MapColor.GOLD).setId(key)));
	public static final Block GAIA_HEAD = make(LibBlockNames.GAIA_HEAD,
			key -> new GaiaHeadBlock(BlockBehaviour.Properties.of()
					.pushReaction(PushReaction.DESTROY)
					.strength(1).setId(key)));
	public static final Block GAIA_WALL_HEAD_BLOCK = make(LibBlockNames.GAIA_WALL_HEAD,
			key -> new GaiaWallHeadBlock(BlockBehaviour.Properties.ofFullCopy(GAIA_HEAD).setId(key)));

	public static final Block SHIMMERROCK = make(LibBlockNames.SHIMMERROCK,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK).setId(key)));
	public static final Block SHIMMERROCK_STAIRS = make(LibBlockNames.SHIMMERROCK + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SHIMMERROCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SHIMMERROCK).setId(key)));
	public static final Block SHIMMERROCK_SLAB = make(LibBlockNames.SHIMMERROCK + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERROCK).setId(key)));
	public static final Block SHIMMERROCK_WALL = make(LibBlockNames.SHIMMERROCK + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERROCK).setId(key)));
	public static final Block SHIMMERROCK_BUTTON = make(LibBlockNames.SHIMMERROCK + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.SHIMMERROCK_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block SHIMMERROCK_PRESSURE_PLATE = make(LibBlockNames.SHIMMERROCK + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.SHIMMERROCK_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block SHIMMERWOOD_PLANKS = make(LibBlockNames.SHIMMERWOOD + LibBlockNames.PLANKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGWOOD).setId(key)));
	public static final Block SHIMMERWOOD_PLANK_STAIRS = make(LibBlockNames.SHIMMERWOOD + LibBlockNames.PLANK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SHIMMERWOOD_PLANKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS).setId(key)));
	public static final Block SHIMMERWOOD_PLANK_SLAB = make(LibBlockNames.SHIMMERWOOD + LibBlockNames.PLANK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS).setId(key)));
	public static final Block SHIMMERWOOD_FENCE = make(LibBlockNames.SHIMMERWOOD + LibBlockNames.FENCE_SUFFIX,
			key -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS).setId(key)));
	public static final Block SHIMMERWOOD_FENCE_GATE = make(LibBlockNames.SHIMMERWOOD + LibBlockNames.FENCE_GATE_SUFFIX,
			key -> new FenceGateBlock(BotaniaBlockSetTypes.SHIMMERWOOD,
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS).setId(key)));
	public static final Block SHIMMERWOOD_PRESSURE_PLATE = make(LibBlockNames.SHIMMERWOOD + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.SHIMMERWOOD_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)
							.noCollision()
							.strength(0.5F)
							.forceSolidOn()
							.pushReaction(PushReaction.DESTROY).setId(key)));
	public static final Block SHIMMERWOOD_BUTTON = make(LibBlockNames.SHIMMERWOOD + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.SHIMMERWOOD_BLOCK_SET, 30,
					BlockBehaviour.Properties.ofFullCopy(SHIMMERWOOD_PLANKS)
							.noCollision()
							.strength(0.5F)
							.pushReaction(PushReaction.DESTROY).setId(key)));

	// grass blocks
	public static final Block DRY_GRASS_BLOCK = make("dry" + LibBlockNames.ALT_GRASS_SUFFIX,
			key -> new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.TERRACOTTA_LIGHT_GREEN).setId(key)));
	public static final Block GOLDEN_GRASS_BLOCK = make("golden" + LibBlockNames.ALT_GRASS_SUFFIX,
			key -> new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.GOLD).setId(key)));
	public static final Block VIVID_GRASS_BLOCK = make("vivid" + LibBlockNames.ALT_GRASS_SUFFIX,
			key -> new BotaniaGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.PLANT).setId(key)));
	public static final Block SCORCHED_GRASS_BLOCK = make("scorched" + LibBlockNames.ALT_GRASS_SUFFIX,
			key -> new BotaniaScorchedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.NETHER).setId(key)));
	public static final Block INFUSED_GRASS_BLOCK = make("infused" + LibBlockNames.ALT_GRASS_SUFFIX,
			key -> new BotaniaInfusedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.COLOR_CYAN).setId(key)));
	public static final Block MUTATED_GRASS_BLOCK = make("mutated" + LibBlockNames.ALT_GRASS_SUFFIX,
			key -> new BotaniaMutatedGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
					.mapColor(MapColor.WARPED_HYPHAE).setId(key)));

	public static final Block DAYBLOOM_MOTIF = make(LibBlockNames.MOTIF_DAYBLOOM,
			key -> new FlowerMotifBlock(MobEffects.BLINDNESS, 15, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).setId(key), true));
	public static final Block NIGHTSHADE_MOTIF = make(LibBlockNames.MOTIF_NIGHTSHADE,
			key -> new FlowerMotifBlock(MobEffects.POISON, 20, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).setId(key), true));
	public static final Block HYDROANGEAS_MOTIF = make(LibBlockNames.MOTIF_HYDROANGEAS,
			key -> new FlowerMotifBlock(MobEffects.UNLUCK, 10, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).setId(key), false));

	public static final Block POTTED_DAYBLOOM_MOTIF = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_DAYBLOOM,
			key -> flowerPot(DAYBLOOM_MOTIF, 0, key));
	public static final Block POTTED_NIGHTSHADE_MOTIF = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_NIGHTSHADE,
			key -> flowerPot(NIGHTSHADE_MOTIF, 0, key));
	public static final Block POTTED_HYDROANGEAS_MOTIF = make(LibBlockNames.POTTED_PREFIX + LibBlockNames.MOTIF_HYDROANGEAS,
			key -> flowerPot(HYDROANGEAS_MOTIF, 0, key));

	// quartz variants
	public static final Block SMOKEY_QUARTZ_BLOCK = make(LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_BLACK).setId(key)));
	public static final Block SMOKEY_QUARTZ_STAIRS = make(LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOKEY_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOKEY_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOKEY_QUARTZ_SLAB = make(LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOKEY_QUARTZ_BLOCK).setId(key)));
	public static final Block CHISELED_SMOKEY_QUARTZ_BLOCK = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(SMOKEY_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOKEY_QUARTZ_BRICKS = make(LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(SMOKEY_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOKEY_QUARTZ_PILLAR = make(LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.PILLAR_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(SMOKEY_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_SMOKEY_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_BLACK).setId(key)));
	public static final Block SMOOTH_SMOKEY_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOOTH_SMOKEY_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_SMOKEY_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_SMOKEY_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.SMOKEY_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_SMOKEY_QUARTZ_BLOCK).setId(key)));

	public static final Block MANA_QUARTZ_BLOCK = make(LibBlockNames.MANA_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.DIAMOND).setId(key)));
	public static final Block MANA_QUARTZ_STAIRS = make(LibBlockNames.MANA_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(MANA_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOKEY_QUARTZ_BLOCK).setId(key)));
	public static final Block MANA_QUARTZ_SLAB = make(LibBlockNames.MANA_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK).setId(key)));
	public static final Block CHISELED_MANA_QUARTZ_BLOCK = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.MANA_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK).setId(key)));
	public static final Block MANA_QUARTZ_BRICKS = make(LibBlockNames.MANA_QUARTZ + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK).setId(key)));
	public static final Block MANA_QUARTZ_PILLAR = make(LibBlockNames.MANA_QUARTZ + LibBlockNames.PILLAR_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(MANA_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_MANA_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.MANA_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.DIAMOND).setId(key)));
	public static final Block SMOOTH_MANA_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.MANA_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOOTH_MANA_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_MANA_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_MANA_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.MANA_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_MANA_QUARTZ_BLOCK).setId(key)));

	public static final Block BLAZE_QUARTZ_BLOCK = make(LibBlockNames.BLAZE_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.SAND).setId(key)));
	public static final Block BLAZE_QUARTZ_STAIRS = make(LibBlockNames.BLAZE_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(BLAZE_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK).setId(key)));
	public static final Block BLAZE_QUARTZ_SLAB = make(LibBlockNames.BLAZE_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK).setId(key)));
	public static final Block CHISELED_BLAZE_QUARTZ_BLOCK = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.BLAZE_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK).setId(key)));
	public static final Block BLAZE_QUARTZ_BRICKS = make(LibBlockNames.BLAZE_QUARTZ + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK).setId(key)));
	public static final Block BLAZE_QUARTZ_PILLAR = make(LibBlockNames.BLAZE_QUARTZ + LibBlockNames.PILLAR_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(BLAZE_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_BLAZE_QUARTZ_BLOCKS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.BLAZE_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.SAND).setId(key)));
	public static final Block SMOOTH_BLAZE_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.BLAZE_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOOTH_BLAZE_QUARTZ_BLOCKS.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_BLAZE_QUARTZ_BLOCKS).setId(key)));
	public static final Block SMOOTH_BLAZE_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.BLAZE_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_BLAZE_QUARTZ_BLOCKS).setId(key)));

	public static final Block LAVENDER_QUARTZ_BLOCK = make(LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_PINK).setId(key)));
	public static final Block LAVENDER_QUARTZ_STAIRS = make(LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LAVENDER_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK).setId(key)));
	public static final Block LAVENDER_QUARTZ_SLAB = make(LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK).setId(key)));
	public static final Block CHISELED_LAVENDER_QUARTZ_BLOCK = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK).setId(key)));
	public static final Block LAVENDER_QUARTZ_BRICKS = make(LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK).setId(key)));
	public static final Block LAVENDER_QUARTZ_PILLAR = make(LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.PILLAR_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(LAVENDER_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_LAVENDER_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_PINK).setId(key)));
	public static final Block SMOOTH_LAVENDER_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOOTH_LAVENDER_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_LAVENDER_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_LAVENDER_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.LAVENDER_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_LAVENDER_QUARTZ_BLOCK).setId(key)));

	public static final Block RED_QUARTZ_BLOCK = make(LibBlockNames.RED_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block RED_QUARTZ_STAIRS = make(LibBlockNames.RED_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(RED_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK).setId(key)));
	public static final Block RED_QUARTZ_SLAB = make(LibBlockNames.RED_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK).setId(key)));
	public static final Block CHISELED_RED_QUARTZ_BLOCK = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.RED_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK).setId(key)));
	public static final Block RED_QUARTZ_BRICKS = make(LibBlockNames.RED_QUARTZ + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK).setId(key)));
	public static final Block RED_QUARTZ_PILLAR = make(LibBlockNames.RED_QUARTZ + LibBlockNames.PILLAR_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(RED_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_RED_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.RED_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block SMOOTH_RED_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.RED_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOOTH_RED_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_RED_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_RED_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.RED_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_RED_QUARTZ_BLOCK).setId(key)));

	public static final Block ELVEN_QUARTZ_BLOCK = make(LibBlockNames.ELVEN_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_LIGHT_GREEN).setId(key)));
	public static final Block ELVEN_QUARTZ_STAIRS = make(LibBlockNames.ELVEN_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(ELVEN_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK).setId(key)));
	public static final Block ELVEN_QUARTZ_SLAB = make(LibBlockNames.ELVEN_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK).setId(key)));
	public static final Block CHISELED_ELVEN_QUARTZ_BLOCK = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.ELVEN_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK).setId(key)));
	public static final Block ELVEN_QUARTZ_BRICKS = make(LibBlockNames.ELVEN_QUARTZ + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK).setId(key)));
	public static final Block ELVEN_QUARTZ_PILLAR = make(LibBlockNames.ELVEN_QUARTZ + LibBlockNames.PILLAR_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ELVEN_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_ELVEN_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.ELVEN_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_LIGHT_GREEN).setId(key)));
	public static final Block SMOOTH_ELVEN_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.ELVEN_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOOTH_ELVEN_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_ELVEN_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_ELVEN_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.ELVEN_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_ELVEN_QUARTZ_BLOCK).setId(key)));

	public static final Block SUNNY_QUART_BLOCK = make(LibBlockNames.SUNNY_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.QUARTZ_BLOCK)
					.mapColor(MapColor.COLOR_YELLOW).setId(key)));
	public static final Block SUNNY_QUARTZ_STAIRS = make(LibBlockNames.SUNNY_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SUNNY_QUART_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK).setId(key)));
	public static final Block SUNNY_QUARTZ_SLAB = make(LibBlockNames.SUNNY_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK).setId(key)));
	public static final Block CHISELED_SUNNY_QUARTZ_BLOCK = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.SUNNY_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK).setId(key)));
	public static final Block SUNNY_QUARTZ_BRICKS = make(LibBlockNames.SUNNY_QUARTZ + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK).setId(key)));
	public static final Block SUNNY_QUARTZ_PILLAR = make(LibBlockNames.SUNNY_QUARTZ + LibBlockNames.PILLAR_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(SUNNY_QUART_BLOCK).setId(key)));
	public static final Block SMOOTH_SUNNY_QUARTZ_BLOCK = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.SUNNY_QUARTZ + LibBlockNames.BLOCK_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_QUARTZ)
					.mapColor(MapColor.COLOR_YELLOW).setId(key)));
	public static final Block SMOOTH_SUNNY_QUARTZ_STAIRS = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.SUNNY_QUARTZ + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SMOOTH_SUNNY_QUARTZ_BLOCK.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(SMOOTH_SUNNY_QUARTZ_BLOCK).setId(key)));
	public static final Block SMOOTH_SUNNY_QUARTZ_SLAB = make(LibBlockNames.SMOOTH_PREFIX + LibBlockNames.SUNNY_QUARTZ + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SMOOTH_SUNNY_QUARTZ_BLOCK).setId(key)));

	// metamorphic biome blocks
	public static final Block FUCHSITE = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE,
			key -> new Block(BlockBehaviour.Properties.of()
					.strength(1.5F, 10)
					.sound(SoundType.TUFF)
					.instrument(NoteBlockInstrument.BASEDRUM)
					.requiresCorrectToolForDrops()
					.mapColor(MapColor.WARPED_NYLIUM).setId(key)));
	public static final Block FUCHSITE_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(FUCHSITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block FUCHSITE_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block FUCHSITE_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block FUCHSITE_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_FOREST_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.WARPED_NYLIUM).setId(key)));
	public static final Block FUCHSITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_FOREST_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.WARPED_NYLIUM).setId(key)));
	public static final Block COBBLED_FUCHSITE = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block COBBLED_FUCHSITE_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(FUCHSITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block COBBLED_FUCHSITE_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block COBBLED_FUCHSITE_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block FUCHSITE_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block FUCHSITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(FUCHSITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block FUCHSITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block FUCHSITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));
	public static final Block CHISELED_FUCHSITE_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_FUCHSITE
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE).setId(key)));

	public static final Block TALC = make(LibBlockNames.METAMORPHIC_VARIANT_TALC,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.CALCITE)
					.mapColor(MapColor.QUARTZ).setId(key)));
	public static final Block TALC_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block TALC_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block TALC_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block TALC_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_PLAINS_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.QUARTZ).setId(key)));
	public static final Block TALC_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_PLAINS_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.QUARTZ).setId(key)));
	public static final Block COBBLED_TALC = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_TALC,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block COBBLED_TALC_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_TALC
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block COBBLED_TALC_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_TALC
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block COBBLED_TALC_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_TALC
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block TALC_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.BRICKS_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block TALC_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block TALC_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block TALC_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_TALC + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));
	public static final Block CHISELED_TALC_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_TALC
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(TALC).setId(key)));

	public static final Block GNEISS = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE_TILES)
					.mapColor(MapColor.GLOW_LICHEN).setId(key)));
	public static final Block GNEISS_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(GNEISS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block GNEISS_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block GNEISS_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block GNEISS_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_MOUNTAIN_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.GLOW_LICHEN).setId(key)));
	public static final Block GNEISS_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_MOUNTAIN_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.GLOW_LICHEN).setId(key)));
	public static final Block COBBLED_GNEISS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_GNEISS,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block COBBLED_GNEISS_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_GNEISS
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(GNEISS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block COBBLED_GNEISS_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_GNEISS
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block COBBLED_GNEISS_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_GNEISS
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block GNEISS_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block GNEISS_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(GNEISS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block GNEISS_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block GNEISS_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_GNEISS + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));
	public static final Block CHISELED_GNEISS_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_GNEISS
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(GNEISS).setId(key)));

	public static final Block MYCELITE = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE_BRICKS)
					.mapColor(MapColor.TERRACOTTA_PURPLE).setId(key)));
	public static final Block MYCELITE_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(MYCELITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block MYCELITE_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block MYCELITE_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block MYCELITE_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_FUNGAL_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_PURPLE).setId(key)));
	public static final Block MYCELITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_FUNGAL_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_PURPLE).setId(key)));
	public static final Block COBBLED_MYCELITE = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_MYCELITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block COBBLED_MYCELITE_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_MYCELITE
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(MYCELITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block COBBLED_MYCELITE_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_MYCELITE
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block COBBLED_MYCELITE_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_MYCELITE
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block MYCELITE_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block MYCELITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(MYCELITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block MYCELITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block MYCELITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_MYCELITE + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));
	public static final Block CHISELED_MYCELITE_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_MYCELITE
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(MYCELITE).setId(key)));

	public static final Block CATACLASITE = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE_TILES)
					.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).setId(key)));
	public static final Block CATACLASITE_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(CATACLASITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CATACLASITE_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CATACLASITE_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CATACLASITE_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_SWAMP_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).setId(key)));
	public static final Block CATACLASITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_SWAMP_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_LIGHT_GRAY).setId(key)));
	public static final Block COBBLED_CATACLASITE = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block COBBLED_CATACLASITE_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(CATACLASITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block COBBLED_CATACLASITE_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block COBBLED_CATACLASITE_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CATACLASITE_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.BRICKS_SUFFIX,
			key -> new BotaniaDirectionalBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CATACLASITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(CATACLASITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CATACLASITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CATACLASITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));
	public static final Block CHISELED_CATACLASITE_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_CATACLASITE
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new BotaniaDirectionalBlock(BlockBehaviour.Properties.ofFullCopy(CATACLASITE).setId(key)));

	public static final Block SOLITE = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE)
					.mapColor(MapColor.DIRT).setId(key)));
	public static final Block SOLITE_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SOLITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block SOLITE_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block SOLITE_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block SOLITE_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_DESERT_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.DIRT).setId(key)));
	public static final Block SOLITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_DESERT_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.DIRT).setId(key)));
	public static final Block COBBLED_SOLITE = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_SOLITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block COBBLED_SOLITE_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_SOLITE
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SOLITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block COBBLED_SOLITE_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_SOLITE
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block COBBLED_SOLITE_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_SOLITE
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block SOLITE_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block SOLITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(SOLITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block SOLITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block SOLITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_SOLITE + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));
	public static final Block CHISELED_SOLITE_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_SOLITE
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(SOLITE).setId(key)));

	public static final Block LUNITE = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.DEEPSLATE)
					.mapColor(MapColor.TERRACOTTA_BLUE).setId(key)));
	public static final Block LUNITE_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LUNITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block LUNITE_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block LUNITE_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block LUNITE_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_TAIGA_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_BLUE).setId(key)));
	public static final Block LUNITE_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_TAIGA_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_BLUE).setId(key)));
	public static final Block COBBLED_LUNITE = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_LUNITE,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block COBBLED_LUNITE_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_LUNITE
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LUNITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block COBBLED_LUNITE_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_LUNITE
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block COBBLED_LUNITE_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_LUNITE
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block LUNITE_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block LUNITE_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(LUNITE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block LUNITE_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block LUNITE_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_LUNITE + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));
	public static final Block CHISELED_LUNITE_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_LUNITE
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LUNITE).setId(key)));

	public static final Block ROSY_TALC = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(FUCHSITE)
					.sound(SoundType.CALCITE)
					.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block ROSY_TALC_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(ROSY_TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block ROSY_TALC_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block ROSY_TALC_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block ROSY_TALC_BUTTON = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.BUTTON_SUFFIX,
			key -> new ButtonBlock(BotaniaBlockSetTypes.METAMORPHIC_MESA_BLOCK_SET, 20,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)
							.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block ROSY_TALC_PRESSURE_PLATE = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.PRESSURE_PLATE_SUFFIX,
			key -> new PressurePlateBlock(BotaniaBlockSetTypes.METAMORPHIC_MESA_BLOCK_SET,
					BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)
							.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block COBBLED_ROSY_TALC = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(
					ROSY_TALC).setId(key)));
	public static final Block COBBLED_ROSY_TALC_STAIRS = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC
			+ LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(ROSY_TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block COBBLED_ROSY_TALC_SLAB = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC
			+ LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block COBBLED_ROSY_TALC_WALL = make(LibBlockNames.COBBLED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC
			+ LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block ROSY_TALC_BRICKS = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.BRICKS_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block ROSY_TALC_BRICK_STAIRS = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.BRICK_INFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(ROSY_TALC.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block ROSY_TALC_BRICK_SLAB = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.BRICK_INFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block ROSY_TALC_BRICK_WALL = make(LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC + LibBlockNames.BRICK_INFIX + LibBlockNames.WALL_SUFFIX,
			key -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));
	public static final Block CHISELED_ROSY_TALC_BRICKS = make(LibBlockNames.CHISELED_PREFIX + LibBlockNames.METAMORPHIC_VARIANT_ROSY_TALC
			+ LibBlockNames.BRICKS_SUFFIX,
			key -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(ROSY_TALC).setId(key)));

	// pavements
	public static final Block WHITE_PORTUGUESE_PAVEMENT = make(LibBlockNames.PAVEMENT_VARIANT_WHITE + LibBlockNames.PAVEMENT_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(LIVINGROCK)
					.mapColor(MapColor.TERRACOTTA_WHITE).setId(key)));
	public static final Block WHITE_PORTUGUESE_PAVEMENT_STAIRS = make(LibBlockNames.PAVEMENT_VARIANT_WHITE + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(WHITE_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT).setId(key)));
	public static final Block WHITE_PORTUGUESE_PAVEMENT_SLAB = make(LibBlockNames.PAVEMENT_VARIANT_WHITE + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT).setId(key)));

	public static final Block BLACK_PORTUGUESE_PAVEMENT = make(LibBlockNames.PAVEMENT_VARIANT_BLACK + LibBlockNames.PAVEMENT_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.COLOR_GRAY).setId(key)));
	public static final Block BLACK_PORTUGUESE_PAVEMENT_STAIRS = make(LibBlockNames.PAVEMENT_VARIANT_BLACK + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(BLACK_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(BLACK_PORTUGUESE_PAVEMENT).setId(key)));
	public static final Block BLACK_PORTUGUESE_PAVEMENT_SLAB = make(LibBlockNames.PAVEMENT_VARIANT_BLACK + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLACK_PORTUGUESE_PAVEMENT).setId(key)));

	public static final Block BLUE_PORTUGUESE_PAVEMENT = make(LibBlockNames.PAVEMENT_VARIANT_BLUE + LibBlockNames.PAVEMENT_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.COLOR_BLUE).setId(key)));
	public static final Block BLUE_PORTUGUESE_PAVEMENT_STAIRS = make(LibBlockNames.PAVEMENT_VARIANT_BLUE + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(BLUE_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(BLUE_PORTUGUESE_PAVEMENT).setId(key)));
	public static final Block BLUE_PORTUGUESE_PAVEMENT_SLAB = make(LibBlockNames.PAVEMENT_VARIANT_BLUE + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(BLUE_PORTUGUESE_PAVEMENT).setId(key)));

	public static final Block YELLOW_PORTUGUESE_PAVEMENT = make(LibBlockNames.PAVEMENT_VARIANT_YELLOW + LibBlockNames.PAVEMENT_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.TERRACOTTA_YELLOW).setId(key)));
	public static final Block YELLOW_PORTUGUESE_PAVEMENT_STAIRS = make(LibBlockNames.PAVEMENT_VARIANT_YELLOW + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(YELLOW_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(YELLOW_PORTUGUESE_PAVEMENT).setId(key)));
	public static final Block YELLOW_PORTUGUESE_PAVEMENT_SLAB = make(LibBlockNames.PAVEMENT_VARIANT_YELLOW + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(YELLOW_PORTUGUESE_PAVEMENT).setId(key)));

	public static final Block RED_PORTUGUESE_PAVEMENT = make(LibBlockNames.PAVEMENT_VARIANT_RED + LibBlockNames.PAVEMENT_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.TERRACOTTA_RED).setId(key)));
	public static final Block RED_PORTUGUESE_PAVEMENT_STAIRS = make(LibBlockNames.PAVEMENT_VARIANT_RED + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(RED_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(RED_PORTUGUESE_PAVEMENT).setId(key)));
	public static final Block RED_PORTUGUESE_PAVEMENT_SLAB = make(LibBlockNames.PAVEMENT_VARIANT_RED + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(RED_PORTUGUESE_PAVEMENT).setId(key)));

	public static final Block GREEN_PORTUGUESE_PAVEMENT = make(LibBlockNames.PAVEMENT_VARIANT_GREEN + LibBlockNames.PAVEMENT_SUFFIX,
			key -> new Block(BlockBehaviour.Properties.ofFullCopy(WHITE_PORTUGUESE_PAVEMENT)
					.mapColor(MapColor.TERRACOTTA_GREEN).setId(key)));
	public static final Block GREEN_PORTUGUESE_PAVEMENT_STAIRS = make(LibBlockNames.PAVEMENT_VARIANT_GREEN + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIRS_SUFFIX,
			key -> new StairBlock(GREEN_PORTUGUESE_PAVEMENT.defaultBlockState(),
					BlockBehaviour.Properties.ofFullCopy(GREEN_PORTUGUESE_PAVEMENT).setId(key)));
	public static final Block GREEN_PORTUGUESE_PAVEMENT_SLAB = make(LibBlockNames.PAVEMENT_VARIANT_GREEN + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX,
			key -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GREEN_PORTUGUESE_PAVEMENT).setId(key)));

	public static final Block MANAGLASS_PANE = make(LibBlockNames.MANAGLASS + "_pane",
			key -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(MANAGLASS).setId(key)));
	public static final Block ALFGLASS_PANE = make(LibBlockNames.ALFGLASS + "_pane",
			key -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(ALFGLASS).setId(key)));
	public static final Block BIFROST_PANE = make(LibBlockNames.BIFROST + "_pane",
			key -> new IronBarsBlock(BlockBehaviour.Properties.ofFullCopy(BIFROST).setId(key)));

	private static FlowerPotBlock flowerPot(Block block, int lightLevel, ResourceKey<Block> key) {
		BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().setId(key)
				.instabreak()
				.noOcclusion()
				.pushReaction(PushReaction.DESTROY);
		return new FlowerPotBlock(block, lightLevel > 0
				? properties.lightLevel(blockState -> lightLevel)
				: properties);
	}

	private static String floating(String orig) {
		return "floating_" + orig;
	}

	private static String potted(String orig) {
		return "potted_" + orig;
	}

	private static String petite(String orig) {
		return orig + "_petite";
	}

	public static void registerBlocks(BiConsumer<Block, Identifier> r) {
		for (var e : ALL.entrySet()) {
			r.accept(e.getValue(), botaniaRL(e.getKey()));
		}
	}

	private static <T> ToIntFunction<T> constInt(int value) {
		return ignored -> value;
	}

	public static void registerItemBlocks(BiConsumer<Item, Identifier> r) {
		Stream.<Function<DyeColor, Block>>of(
				BotaniaBlocks::getMysticalFlower,
				BotaniaBlocks::getGlimmeringFlower,
				BotaniaBlocks::getFloatingFlower,
				BotaniaBlocks::getPetalBlock,
				BotaniaBlocks::getShimmeringMushroom,
				BotaniaBlocks::getTallMysticalFlower
		).forEach(
				blockGetter -> ColorHelper.supportedColors().map(blockGetter).forEach(
						block -> registerBlockItem(r, block, props -> new ColoredBlockItem(block, ((Colored) block).getColor(), props))
				)
		);

		registerBlockItem(r, PURE_DAISY, props -> new SpecialFlowerBlockItem(PURE_DAISY, props));
		registerBlockItem(r, FLOATING_PURE_DAISY, props -> new SpecialFlowerBlockItem(FLOATING_PURE_DAISY, props));

		registerBlockItem(r, MANASTAR, props -> new SpecialFlowerBlockItem(MANASTAR, props));
		registerBlockItem(r, FLOATING_MANASTAR, props -> new SpecialFlowerBlockItem(FLOATING_MANASTAR, props));

		registerBlockItem(r, HYDROANGEAS, props -> new DecayableSpecialFlowerBlockItem(HYDROANGEAS, HydroangeasBlockEntity.DECAY_TIME, props));
		registerBlockItem(r, FLOATING_HYDROANGEAS, props -> new DecayableSpecialFlowerBlockItem(FLOATING_HYDROANGEAS, HydroangeasBlockEntity.DECAY_TIME, props));

		registerBlockItem(r, ENDOFLAME, props -> new SpecialFlowerBlockItem(ENDOFLAME, props));
		registerBlockItem(r, FLOATING_ENDOFLAME, props -> new SpecialFlowerBlockItem(FLOATING_ENDOFLAME, props));

		registerBlockItem(r, THERMALILY, props -> new SpecialFlowerBlockItem(THERMALILY, props));
		registerBlockItem(r, FLOATING_THERMALILY, props -> new SpecialFlowerBlockItem(FLOATING_THERMALILY, props));

		registerBlockItem(r, ROSA_ARCANA, props -> new SpecialFlowerBlockItem(ROSA_ARCANA, props));
		registerBlockItem(r, FLOATING_ROSA_ARCANA, props -> new SpecialFlowerBlockItem(FLOATING_ROSA_ARCANA, props));

		registerBlockItem(r, MUNCHDEW, props -> new SpecialFlowerBlockItem(MUNCHDEW, props));
		registerBlockItem(r, FLOATING_MUNCHDEW, props -> new SpecialFlowerBlockItem(FLOATING_MUNCHDEW, props));

		registerBlockItem(r, ENTROPINNYUM, props -> new SpecialFlowerBlockItem(ENTROPINNYUM, props));
		registerBlockItem(r, FLOATING_ENTROPINNYUM, props -> new SpecialFlowerBlockItem(FLOATING_ENTROPINNYUM, props));

		registerBlockItem(r, KEKIMURUS, props -> new SpecialFlowerBlockItem(KEKIMURUS, props));
		registerBlockItem(r, FLOATING_KEKIMURUS, props -> new SpecialFlowerBlockItem(FLOATING_KEKIMURUS, props));

		registerBlockItem(r, GOURMARYLLIS, props -> new SpecialFlowerBlockItem(GOURMARYLLIS, props));
		registerBlockItem(r, FLOATING_GOURMARYLLIS, props -> new SpecialFlowerBlockItem(FLOATING_GOURMARYLLIS, props));

		registerBlockItem(r, NARSLIMMUS, props -> new SpecialFlowerBlockItem(NARSLIMMUS, props));
		registerBlockItem(r, FLOATING_NARSLIMMUS, props -> new SpecialFlowerBlockItem(FLOATING_NARSLIMMUS, props));

		registerBlockItem(r, SPECTROLUS, props -> new SpecialFlowerBlockItem(SPECTROLUS, props));
		registerBlockItem(r, FLOATING_SPECTROLUS, props -> new SpecialFlowerBlockItem(FLOATING_SPECTROLUS, props));

		registerBlockItem(r, DANDELIFEON, props -> new SpecialFlowerBlockItem(DANDELIFEON, props.rarity(Rarity.RARE)));
		registerBlockItem(r, FLOATING_DANDELIFEON, props -> new SpecialFlowerBlockItem(FLOATING_DANDELIFEON, props.rarity(Rarity.RARE)));

		registerBlockItem(r, RAFFLOWSIA, props -> new SpecialFlowerBlockItem(RAFFLOWSIA, props));
		registerBlockItem(r, FLOATING_RAFFLOWSIA, props -> new SpecialFlowerBlockItem(FLOATING_RAFFLOWSIA, props));

		registerBlockItem(r, SHULK_ME_NOT, props -> new SpecialFlowerBlockItem(SHULK_ME_NOT, props.rarity(Rarity.RARE)));
		registerBlockItem(r, FLOATING_SHULK_ME_NOT, props -> new SpecialFlowerBlockItem(FLOATING_SHULK_ME_NOT, props.rarity(Rarity.RARE)));

		registerBlockItem(r, BELLETHORNE, props -> new SpecialFlowerBlockItem(BELLETHORNE, props));
		registerBlockItem(r, BELLETHORNE_PETITE, props -> new SpecialFlowerBlockItem(BELLETHORNE_PETITE, props));
		registerBlockItem(r, FLOATING_BELLETHORNE, props -> new SpecialFlowerBlockItem(FLOATING_BELLETHORNE, props));
		registerBlockItem(r, FLOATING_BELLETHORNE_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_BELLETHORNE_PETITE, props));

		registerBlockItem(r, BERGAMUTE, props -> new SpecialFlowerBlockItem(BERGAMUTE, props));
		registerBlockItem(r, FLOATING_BERGAMUTE, props -> new SpecialFlowerBlockItem(FLOATING_BERGAMUTE, props));

		registerBlockItem(r, DREADTHORNE, props -> new SpecialFlowerBlockItem(DREADTHORNE, props));
		registerBlockItem(r, FLOATING_DREADTHORNE, props -> new SpecialFlowerBlockItem(FLOATING_DREADTHORNE, props));

		registerBlockItem(r, HEISEI_DREAM, props -> new SpecialFlowerBlockItem(HEISEI_DREAM, props));
		registerBlockItem(r, FLOATING_HEISEI_DREAM, props -> new SpecialFlowerBlockItem(FLOATING_HEISEI_DREAM, props));

		registerBlockItem(r, TIGERSEYE, props -> new SpecialFlowerBlockItem(TIGERSEYE, props));
		registerBlockItem(r, FLOATING_TIGERSEYE, props -> new SpecialFlowerBlockItem(FLOATING_TIGERSEYE, props));

		registerBlockItem(r, JADED_AMARANTHUS, props -> new SpecialFlowerBlockItem(JADED_AMARANTHUS, props));
		registerBlockItem(r, FLOATING_JADED_AMARANTHUS, props -> new SpecialFlowerBlockItem(FLOATING_JADED_AMARANTHUS, props));

		registerBlockItem(r, ORECHID, props -> new SpecialFlowerBlockItem(ORECHID, props));
		registerBlockItem(r, FLOATING_ORECHID, props -> new SpecialFlowerBlockItem(FLOATING_ORECHID, props));

		registerBlockItem(r, FALLEN_KANADE, props -> new SpecialFlowerBlockItem(FALLEN_KANADE, props));
		registerBlockItem(r, FLOATING_FALLEN_KANADE, props -> new SpecialFlowerBlockItem(FLOATING_FALLEN_KANADE, props));

		registerBlockItem(r, EXOFLAME, props -> new SpecialFlowerBlockItem(EXOFLAME, props));
		registerBlockItem(r, FLOATING_EXOFLAME, props -> new SpecialFlowerBlockItem(FLOATING_EXOFLAME, props));

		registerBlockItem(r, AGRICARNATION, props -> new SpecialFlowerBlockItem(AGRICARNATION, props));
		registerBlockItem(r, AGRICARNATION_PETITE, props -> new SpecialFlowerBlockItem(AGRICARNATION_PETITE, props));
		registerBlockItem(r, FLOATING_AGRICARNATION, props -> new SpecialFlowerBlockItem(FLOATING_AGRICARNATION, props));
		registerBlockItem(r, FLOATING_AGRICARNATION_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_AGRICARNATION_PETITE, props));

		registerBlockItem(r, HOPPERHOCK, props -> new SpecialFlowerBlockItem(HOPPERHOCK, props));
		registerBlockItem(r, HOPPERHOCK_PETITE, props -> new SpecialFlowerBlockItem(HOPPERHOCK_PETITE, props));
		registerBlockItem(r, FLOATING_HOPPERHOCK, props -> new SpecialFlowerBlockItem(FLOATING_HOPPERHOCK, props));
		registerBlockItem(r, FLOATING_HOPPERHOCK_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_HOPPERHOCK_PETITE, props));

		registerBlockItem(r, TANGLEBERRIE, props -> new SpecialFlowerBlockItem(TANGLEBERRIE, props));
		registerBlockItem(r, TANGLEBERRIE_PETITE, props -> new SpecialFlowerBlockItem(TANGLEBERRIE_PETITE, props));
		registerBlockItem(r, FLOATING_TANGLEBERRIE, props -> new SpecialFlowerBlockItem(FLOATING_TANGLEBERRIE, props));
		registerBlockItem(r, FLOATING_TANGLEBERRIE_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_TANGLEBERRIE_PETITE, props));

		registerBlockItem(r, JIYUULIA, props -> new SpecialFlowerBlockItem(JIYUULIA, props));
		registerBlockItem(r, JIYUULIA_PETITE, props -> new SpecialFlowerBlockItem(JIYUULIA_PETITE, props));
		registerBlockItem(r, FLOATING_JIYUULIA, props -> new SpecialFlowerBlockItem(FLOATING_JIYUULIA, props));
		registerBlockItem(r, FLOATING_JIYUULIA_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_JIYUULIA_PETITE, props));

		registerBlockItem(r, RANNUNCARPUS, props -> new SpecialFlowerBlockItem(RANNUNCARPUS, props));
		registerBlockItem(r, RANNUNCARPUS_PETITE, props -> new SpecialFlowerBlockItem(RANNUNCARPUS_PETITE, props));
		registerBlockItem(r, FLOATING_RANNUNCARPUS, props -> new SpecialFlowerBlockItem(FLOATING_RANNUNCARPUS, props));
		registerBlockItem(r, FLOATING_RANNUNCARPUS_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_RANNUNCARPUS_PETITE, props));

		registerBlockItem(r, HYACIDUS, props -> new SpecialFlowerBlockItem(HYACIDUS, props));
		registerBlockItem(r, FLOATING_HYACIDUS, props -> new SpecialFlowerBlockItem(FLOATING_HYACIDUS, props));

		registerBlockItem(r, POLLIDISIAC, props -> new SpecialFlowerBlockItem(POLLIDISIAC, props));
		registerBlockItem(r, FLOATING_POLLIDISIAC, props -> new SpecialFlowerBlockItem(FLOATING_POLLIDISIAC, props));

		registerBlockItem(r, CLAYCONIA, props -> new SpecialFlowerBlockItem(CLAYCONIA, props));
		registerBlockItem(r, CLAYCONIA_PETITE, props -> new SpecialFlowerBlockItem(CLAYCONIA_PETITE, props));
		registerBlockItem(r, FLOATING_CLAYCONIA, props -> new SpecialFlowerBlockItem(FLOATING_CLAYCONIA, props));
		registerBlockItem(r, FLOATING_CLAYCONIA_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_CLAYCONIA_PETITE, props));

		registerBlockItem(r, LOONIUM, props -> new SpecialFlowerBlockItem(LOONIUM, props));
		registerBlockItem(r, FLOATING_LOONIUM, props -> new SpecialFlowerBlockItem(FLOATING_LOONIUM, props));

		registerBlockItem(r, DAFFOMILL, props -> new SpecialFlowerBlockItem(DAFFOMILL, props));
		registerBlockItem(r, FLOATING_DAFFOMILL, props -> new SpecialFlowerBlockItem(FLOATING_DAFFOMILL, props));

		registerBlockItem(r, VINCULOTUS, props -> new SpecialFlowerBlockItem(VINCULOTUS, props));
		registerBlockItem(r, FLOATING_VINCULOTUS, props -> new SpecialFlowerBlockItem(FLOATING_VINCULOTUS, props));

		registerBlockItem(r, SPECTRANTHEMUM, props -> new SpecialFlowerBlockItem(SPECTRANTHEMUM, props));
		registerBlockItem(r, FLOATING_SPECTRANTHEMUM, props -> new SpecialFlowerBlockItem(FLOATING_SPECTRANTHEMUM, props));

		registerBlockItem(r, MEDUMONE, props -> new SpecialFlowerBlockItem(MEDUMONE, props));
		registerBlockItem(r, FLOATING_MEDUMONE, props -> new SpecialFlowerBlockItem(FLOATING_MEDUMONE, props));

		registerBlockItem(r, MARIMORPHOSIS, props -> new SpecialFlowerBlockItem(MARIMORPHOSIS, props));
		registerBlockItem(r, MARIMORPHOSIS_PETITE, props -> new SpecialFlowerBlockItem(MARIMORPHOSIS_PETITE, props));
		registerBlockItem(r, FLOATING_MARIMORPHOSIS, props -> new SpecialFlowerBlockItem(FLOATING_MARIMORPHOSIS, props));
		registerBlockItem(r, FLOATING_MARIMORPHOSIS_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_MARIMORPHOSIS_PETITE, props));

		registerBlockItem(r, BUBBELL, props -> new SpecialFlowerBlockItem(BUBBELL, props));
		registerBlockItem(r, BUBBELL_PETITE, props -> new SpecialFlowerBlockItem(BUBBELL_PETITE, props));
		registerBlockItem(r, FLOATING_BUBBELL, props -> new SpecialFlowerBlockItem(FLOATING_BUBBELL, props));
		registerBlockItem(r, FLOATING_BUBBELL_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_BUBBELL_PETITE, props));

		registerBlockItem(r, SOLEGNOLIA, props -> new SpecialFlowerBlockItem(SOLEGNOLIA, props));
		registerBlockItem(r, SOLEGNOLIA_PETITE, props -> new SpecialFlowerBlockItem(SOLEGNOLIA_PETITE, props));
		registerBlockItem(r, FLOATING_SOLEGNOLIA, props -> new SpecialFlowerBlockItem(FLOATING_SOLEGNOLIA, props));
		registerBlockItem(r, FLOATING_SOLEGNOLIA_PETITE, props -> new SpecialFlowerBlockItem(FLOATING_SOLEGNOLIA_PETITE, props));

		registerBlockItem(r, ORECHID_IGNEM, props -> new SpecialFlowerBlockItem(ORECHID_IGNEM, props));
		registerBlockItem(r, FLOATING_ORECHID_IGNEM, props -> new SpecialFlowerBlockItem(FLOATING_ORECHID_IGNEM, props));

		registerBlockItem(r, LABELLIA, props -> new SpecialFlowerBlockItem(LABELLIA, props));
		registerBlockItem(r, FLOATING_LABELLIA, props -> new SpecialFlowerBlockItem(FLOATING_LABELLIA, props));

		registerBlockItem(r, PETAL_APOTHECARY);
		registerBlockItem(r, PETAL_APOTHECARY_DEEPSLATE);
		registerBlockItem(r, PETAL_APOTHECARY_LIVINGROCK);
		registerBlockItem(r, PETAL_APOTHECARY_BLACKSTONE);
		registerBlockItem(r, PETAL_APOTHECARY_NETHER_BRICKS);
		registerBlockItem(r, PETAL_APOTHECARY_RED_NETHER_BRICKS);
		registerBlockItem(r, PETAL_APOTHECARY_MOSSY);
		registerBlockItem(r, PETAL_APOTHECARY_FUCHSITE);
		registerBlockItem(r, PETAL_APOTHECARY_TALC);
		registerBlockItem(r, PETAL_APOTHECARY_GNEISS);
		registerBlockItem(r, PETAL_APOTHECARY_MYCELITE);
		registerBlockItem(r, PETAL_APOTHECARY_CATACLASITE);
		registerBlockItem(r, PETAL_APOTHECARY_SOLITE);
		registerBlockItem(r, PETAL_APOTHECARY_LUNITE);
		registerBlockItem(r, PETAL_APOTHECARY_ROSY_TALC);

		registerBlockItem(r, LIVINGROCK);
		registerBlockItem(r, LIVINGROCK_STAIRS);
		registerBlockItem(r, LIVINGROCK_SLAB);
		registerBlockItem(r, LIVINGROCK_WALL);
		registerBlockItem(r, LIVINGROCK_BUTTON);
		registerBlockItem(r, LIVINGROCK_PRESSURE_PLATE);
		registerBlockItem(r, POLISHED_LIVINGROCK);
		registerBlockItem(r, POLISHED_LIVINGROCK_STAIRS);
		registerBlockItem(r, POLISHED_LIVINGROCK_SLAB);
		registerBlockItem(r, POLISHED_LIVINGROCK_WALL);
		registerBlockItem(r, LIVINGROCK_SLATE);
		registerBlockItem(r, LIVINGROCK_BRICKS);
		registerBlockItem(r, LIVINGROCK_BRICK_STAIRS);
		registerBlockItem(r, LIVINGROCK_BRICK_SLAB);
		registerBlockItem(r, LIVINGROCK_BRICK_WALL);
		registerBlockItem(r, MOSSY_LIVINGROCK_BRICKS);
		registerBlockItem(r, MOSSY_LIVINGROCK_BRICK_STAIRS);
		registerBlockItem(r, MOSSY_LIVINGROCK_BRICK_SLAB);
		registerBlockItem(r, MOSSY_LIVINGROCK_BRICK_WALL);
		registerBlockItem(r, CRACKED_LIVINGROCK_BRICKS);
		registerBlockItem(r, CHISELED_LIVINGROCK_BRICKS);

		registerBlockItem(r, LIVINGWOOD_LOG);
		registerBlockItem(r, LIVINGWOOD);
		registerBlockItem(r, LIVINGWOOD_STAIRS);
		registerBlockItem(r, LIVINGWOOD_SLAB);
		registerBlockItem(r, LIVINGWOOD_WALL);
		registerBlockItem(r, STRIPPED_LIVINGWOOD_LOG);
		registerBlockItem(r, STRIPPED_LIVINGWOOD);
		registerBlockItem(r, STRIPPED_LIVINGWOOD_STAIRS);
		registerBlockItem(r, STRIPPED_LIVINGWOOD_SLAB);
		registerBlockItem(r, STRIPPED_LIVINGWOOD_WALL);
		registerBlockItem(r, GLIMMERING_LIVINGWOOD_LOG);
		registerBlockItem(r, GLIMMERING_LIVINGWOOD);
		registerBlockItem(r, STRIPPED_GLIMMERING_LIVINGWOOD_LOG);
		registerBlockItem(r, STRIPPED_GLIMMERING_LIVINGWOOD);
		registerBlockItem(r, LIVINGWOOD_PLANKS);
		registerBlockItem(r, LIVINGWOOD_PLANK_STAIRS);
		registerBlockItem(r, LIVINGWOOD_PLANK_SLAB);
		registerBlockItem(r, LIVINGWOOD_FENCE);
		registerBlockItem(r, LIVINGWOOD_FENCE_GATE);
		registerBlockItem(r, LIVINGWOOD_DOOR);
		registerBlockItem(r, LIVINGWOOD_TRAPDOOR);
		registerBlockItem(r, LIVINGWOOD_PRESSURE_PLATE);
		registerBlockItem(r, LIVINGWOOD_BUTTON);
		registerBlockItem(r, LIVINGWOOD_SIGN, props -> new SignItem(
				LIVINGWOOD_SIGN, LIVINGWOOD_WALL_SIGN, props.stacksTo(16)));
		registerBlockItem(r, LIVINGWOOD_HANGING_SIGN, props -> new HangingSignItem(
				LIVINGWOOD_HANGING_SIGN, LIVINGWOOD_WALL_HANGING_SIGN,
				props.stacksTo(16)));
		registerBlockItem(r, MOSSY_LIVINGWOOD_PLANKS);
		registerBlockItem(r, FRAMED_LIVINGWOOD);
		registerBlockItem(r, PATTERN_FRAMED_LIVINGWOOD);

		registerBlockItem(r, DREAMWOOD_LOG);
		registerBlockItem(r, DREAMWOOD);
		registerBlockItem(r, DREAMWOOD_STAIRS);
		registerBlockItem(r, DREAMWOOD_SLAB);
		registerBlockItem(r, DREAMWOOD_WALL);
		registerBlockItem(r, STRIPPED_DREAMWOOD_LOG);
		registerBlockItem(r, STRIPPED_DREAMWOOD);
		registerBlockItem(r, STRIPPED_DREAMWOOD_STAIRS);
		registerBlockItem(r, STRIPPED_DREAMWOOD_SLAB);
		registerBlockItem(r, STRIPPED_DREAMWOOD_WALL);
		registerBlockItem(r, GLIMMERING_DREAMWOOD_LOG);
		registerBlockItem(r, GLIMMERING_DREAMWOOD);
		registerBlockItem(r, STRIPPED_GLIMMERING_DREAMWOOD_LOG);
		registerBlockItem(r, STRIPPED_GLIMMERING_DREAMWOOD);
		registerBlockItem(r, DREAMWOOD_PLANKS);
		registerBlockItem(r, DREAMWOOD_PLANK_STAIRS);
		registerBlockItem(r, DREAMWOOD_PLANK_SLAB);
		registerBlockItem(r, DREAMWOOD_FENCE);
		registerBlockItem(r, DREAMWOOD_FENCE_GATE);
		registerBlockItem(r, DREAMWOOD_DOOR);
		registerBlockItem(r, DREAMWOOD_TRAPDOOR);
		registerBlockItem(r, DREAMWOOD_PRESSURE_PLATE);
		registerBlockItem(r, DREAMWOOD_BUTTON);
		registerBlockItem(r, DREAMWOOD_SIGN, props -> new SignItem(
				DREAMWOOD_SIGN, DREAMWOOD_WALL_SIGN, props.stacksTo(16)));
		registerBlockItem(r, DREAMWOOD_HANGING_SIGN, props -> new HangingSignItem(
				DREAMWOOD_HANGING_SIGN, DREAMWOOD_WALL_HANGING_SIGN,
				props.stacksTo(16)));
		registerBlockItem(r, MOSSY_DREAMWOOD_PLANKS);
		registerBlockItem(r, FRAMED_DREAMWOOD);
		registerBlockItem(r, PATTERN_FRAMED_DREAMWOOD);

		registerBlockItemWithColoredVariants(r, MANA_SPREADER, Rarity.COMMON, LibBlockNames.COVERED_INFIX);
		registerBlockItemWithColoredVariants(r, PULSE_MANA_SPREADER, Rarity.COMMON, LibBlockNames.COVERED_INFIX);
		registerBlockItemWithColoredVariants(r, ELVEN_MANA_SPREADER, Rarity.COMMON, LibBlockNames.COVERED_INFIX);
		registerBlockItemWithColoredVariants(r, GAIA_MANA_SPREADER, Rarity.RARE, LibBlockNames.COVERED_INFIX);
		registerBlockItemWithColoredVariants(r, MANA_POOL, Rarity.COMMON);
		registerBlockItemWithColoredVariants(r, CREATIVE_MANA_POOL, Rarity.EPIC);
		registerBlockItemWithColoredVariants(r, DILUTED_MANA_POOL, Rarity.COMMON);
		registerBlockItemWithColoredVariants(r, FABULOUS_MANA_POOL, Rarity.COMMON);
		registerBlockItem(r, ALCHEMY_CATALYST);
		registerBlockItem(r, CONJURATION_CATALYST);
		registerBlockItem(r, MANASTEEL_BLOCK);
		registerBlockItem(r, TERRASTEEL_BLOCK, props -> new BlockItem(TERRASTEEL_BLOCK, props.rarity(Rarity.UNCOMMON)));
		registerBlockItem(r, ELEMENTIUM_BLOCK);
		registerBlockItem(r, MANA_DIAMOND_BLOCK);
		registerBlockItem(r, DRAGONSTONE_BLOCK);
		registerBlockItem(r, MANAGLASS);
		registerBlockItem(r, ALFGLASS);
		registerBlockItem(r, BIFROST);
		registerBlockItem(r, RUNIC_ALTAR);
		registerBlockItem(r, MANA_ENCHANTER);
		registerBlockItem(r, BOTANICAL_BREWERY);
		registerBlockItem(r, TERRESTRIAL_AGGLOMERATION_PLATE);
		registerBlockItem(r, ELVEN_GATEWAY_CORE, props -> new BlockItem(ELVEN_GATEWAY_CORE, props.rarity(Rarity.UNCOMMON)));

		registerBlockItem(r, MANA_PYLON);
		registerBlockItem(r, NATURA_PYLON, props -> new BlockItem(NATURA_PYLON, props.rarity(Rarity.UNCOMMON)));
		registerBlockItem(r, GAIA_PYLON);
		registerBlockItem(r, MANA_SPLITTER);
		registerBlockItem(r, MANA_VOID);
		registerBlockItem(r, MANA_DETECTOR);
		registerBlockItem(r, FORCE_RELAY);
		registerBlockItem(r, SPREADER_TURNTABLE);
		registerBlockItem(r, TINY_PLANET);
		registerBlockItem(r, DRUM_OF_THE_WILD);
		registerBlockItem(r, DRUM_OF_THE_GATHERING);
		registerBlockItem(r, DRUM_OF_THE_CANOPY);
		registerBlockItem(r, LIFE_IMBUER);
		registerBlockItem(r, MANA_FLUXFIELD);
		registerBlockItem(r, MANA_PRISM);
		registerBlockItem(r, MANA_PUMP);
		registerBlockItem(r, SPARK_TINKERER);
		registerBlockItem(r, MANASTORM_CHARGE, props -> new BlockItem(MANASTORM_CHARGE, props.rarity(Rarity.RARE)));
		registerBlockItem(r, MANATIDE_BELLOWS);
		registerBlockItem(r, OPEN_CRATE);
		registerBlockItem(r, CRAFTY_CRATE);
		registerBlockItem(r, EYE_OF_THE_ANCIENTS);
		registerBlockItem(r, ABSTRUSE_PLATFORM);
		registerBlockItem(r, SPECTRAL_PLATFORM);
		registerBlockItem(r, INFRANGIBLE_PLATFORM, props -> BlockItemTooltip.createItem(INFRANGIBLE_PLATFORM, props.rarity(Rarity.EPIC)));
		registerBlockItem(r, TINY_POTATO, props -> new TinyPotatoBlockItem(TINY_POTATO, props));
		registerBlockItem(r, ENDER_OVERSEER);
		registerBlockItem(r, RED_STRINGED_CONTAINER);
		registerBlockItem(r, RED_STRINGED_DISPENSER);
		registerBlockItem(r, RED_STRINGED_NUTRIFIER);
		registerBlockItem(r, RED_STRINGED_COMPARATOR);
		registerBlockItem(r, RED_STRINGED_SPOOFER);
		registerBlockItem(r, RED_STRINGED_INTERCEPTOR);
		registerBlockItem(r, CORPOREA_FUNNEL);
		registerBlockItem(r, CORPOREA_INTERCEPTOR);
		registerBlockItem(r, CORPOREA_INDEX);
		registerBlockItem(r, CORPOREA_CRYSTAL_CUBE);
		registerBlockItem(r, CORPOREA_RETAINER);
		registerBlockItem(r, CORPOREA_BLOCK);
		registerBlockItem(r, CORPOREA_STAIRS);
		registerBlockItem(r, CORPOREA_SLAB);
		registerBlockItem(r, CORPOREA_WALL);
		registerBlockItem(r, CORPOREA_BUTTON);
		registerBlockItem(r, CORPOREA_PRESSURE_PLATE);
		registerBlockItem(r, CORPOREA_BRICKS);
		registerBlockItem(r, CORPOREA_BRICK_STAIRS);
		registerBlockItem(r, CORPOREA_BRICK_SLAB);
		registerBlockItem(r, CORPOREA_BRICK_WALL);
		registerBlockItem(r, INCENSE_PLATE);
		registerBlockItem(r, HOVERING_HOURGLASS);
		registerBlockItem(r, SPECTRAL_RAIL);
		registerBlockItem(r, LUMINIZER);
		registerBlockItem(r, DETECTOR_LUMINIZER);
		registerBlockItem(r, FORK_LUMINIZER);
		registerBlockItem(r, TOGGLE_LUMINIZER);
		registerBlockItem(r, LUMINIZER_LAUNCHER);
		registerBlockItem(r, CACOPHONIUM_BLOCK);
		registerBlockItem(r, CELLULAR_BLOCK);
		registerBlockItem(r, TERU_TERU_BOZU);
		registerBlockItem(r, LIVINGWOOD_AVATAR);
		registerBlockItem(r, LIVING_ROOT);
		registerBlockItem(r, FEL_PUMPKIN);
		registerBlockItem(r, COCOON_OF_CAPRICE);
		registerBlockItem(r, ANIMATED_TORCH);
		registerBlockItem(r, STARFIELD_CREATOR);
		registerBlockItem(r, AZULEJO_0);
		registerBlockItem(r, AZULEJO_1);
		registerBlockItem(r, AZULEJO_2);
		registerBlockItem(r, AZULEJO_3);
		registerBlockItem(r, AZULEJO_4);
		registerBlockItem(r, AZULEJO_5);
		registerBlockItem(r, AZULEJO_6);
		registerBlockItem(r, AZULEJO_7);
		registerBlockItem(r, AZULEJO_8);
		registerBlockItem(r, AZULEJO_9);
		registerBlockItem(r, AZULEJO_10);
		registerBlockItem(r, AZULEJO_11);
		registerBlockItem(r, AZULEJO_12);
		registerBlockItem(r, AZULEJO_13);
		registerBlockItem(r, AZULEJO_14);
		registerBlockItem(r, AZULEJO_15);
		registerBlockItem(r, BLAZE_MESH);
		registerBlockItem(r, GAIA_HEAD, props -> new StandingAndWallBlockItem(
				GAIA_HEAD, GAIA_WALL_HEAD_BLOCK, Direction.DOWN, props.rarity(Rarity.RARE)));
		registerBlockItem(r, SHIMMERROCK);
		registerBlockItem(r, SHIMMERROCK_STAIRS);
		registerBlockItem(r, SHIMMERROCK_SLAB);
		registerBlockItem(r, SHIMMERROCK_WALL);
		registerBlockItem(r, SHIMMERROCK_BUTTON);
		registerBlockItem(r, SHIMMERROCK_PRESSURE_PLATE);
		registerBlockItem(r, SHIMMERWOOD_PLANKS);
		registerBlockItem(r, SHIMMERWOOD_PLANK_STAIRS);
		registerBlockItem(r, SHIMMERWOOD_PLANK_SLAB);
		registerBlockItem(r, SHIMMERWOOD_FENCE);
		registerBlockItem(r, SHIMMERWOOD_FENCE_GATE);
		registerBlockItem(r, SHIMMERWOOD_BUTTON);
		registerBlockItem(r, SHIMMERWOOD_PRESSURE_PLATE);
		registerBlockItem(r, DRY_GRASS_BLOCK);
		registerBlockItem(r, GOLDEN_GRASS_BLOCK);
		registerBlockItem(r, VIVID_GRASS_BLOCK);
		registerBlockItem(r, SCORCHED_GRASS_BLOCK);
		registerBlockItem(r, INFUSED_GRASS_BLOCK);
		registerBlockItem(r, MUTATED_GRASS_BLOCK);
		registerBlockItem(r, DAYBLOOM_MOTIF);
		registerBlockItem(r, NIGHTSHADE_MOTIF);
		registerBlockItem(r, HYDROANGEAS_MOTIF);

		registerBlockItem(r, SMOKEY_QUARTZ_BLOCK);
		registerBlockItem(r, SMOKEY_QUARTZ_STAIRS);
		registerBlockItem(r, SMOKEY_QUARTZ_SLAB);
		registerBlockItem(r, CHISELED_SMOKEY_QUARTZ_BLOCK);
		registerBlockItem(r, SMOKEY_QUARTZ_BRICKS);
		registerBlockItem(r, SMOKEY_QUARTZ_PILLAR);
		registerBlockItem(r, SMOOTH_SMOKEY_QUARTZ_BLOCK);
		registerBlockItem(r, SMOOTH_SMOKEY_QUARTZ_STAIRS);
		registerBlockItem(r, SMOOTH_SMOKEY_QUARTZ_SLAB);

		registerBlockItem(r, MANA_QUARTZ_BLOCK);
		registerBlockItem(r, MANA_QUARTZ_STAIRS);
		registerBlockItem(r, MANA_QUARTZ_SLAB);
		registerBlockItem(r, CHISELED_MANA_QUARTZ_BLOCK);
		registerBlockItem(r, MANA_QUARTZ_BRICKS);
		registerBlockItem(r, MANA_QUARTZ_PILLAR);
		registerBlockItem(r, SMOOTH_MANA_QUARTZ_BLOCK);
		registerBlockItem(r, SMOOTH_MANA_QUARTZ_STAIRS);
		registerBlockItem(r, SMOOTH_MANA_QUARTZ_SLAB);

		registerBlockItem(r, BLAZE_QUARTZ_BLOCK);
		registerBlockItem(r, BLAZE_QUARTZ_STAIRS);
		registerBlockItem(r, BLAZE_QUARTZ_SLAB);
		registerBlockItem(r, CHISELED_BLAZE_QUARTZ_BLOCK);
		registerBlockItem(r, BLAZE_QUARTZ_BRICKS);
		registerBlockItem(r, BLAZE_QUARTZ_PILLAR);
		registerBlockItem(r, SMOOTH_BLAZE_QUARTZ_BLOCKS);
		registerBlockItem(r, SMOOTH_BLAZE_QUARTZ_STAIRS);
		registerBlockItem(r, SMOOTH_BLAZE_QUARTZ_SLAB);

		registerBlockItem(r, LAVENDER_QUARTZ_BLOCK);
		registerBlockItem(r, LAVENDER_QUARTZ_STAIRS);
		registerBlockItem(r, LAVENDER_QUARTZ_SLAB);
		registerBlockItem(r, CHISELED_LAVENDER_QUARTZ_BLOCK);
		registerBlockItem(r, LAVENDER_QUARTZ_BRICKS);
		registerBlockItem(r, LAVENDER_QUARTZ_PILLAR);
		registerBlockItem(r, SMOOTH_LAVENDER_QUARTZ_BLOCK);
		registerBlockItem(r, SMOOTH_LAVENDER_QUARTZ_STAIRS);
		registerBlockItem(r, SMOOTH_LAVENDER_QUARTZ_SLAB);

		registerBlockItem(r, RED_QUARTZ_BLOCK);
		registerBlockItem(r, RED_QUARTZ_STAIRS);
		registerBlockItem(r, RED_QUARTZ_SLAB);
		registerBlockItem(r, CHISELED_RED_QUARTZ_BLOCK);
		registerBlockItem(r, RED_QUARTZ_BRICKS);
		registerBlockItem(r, RED_QUARTZ_PILLAR);
		registerBlockItem(r, SMOOTH_RED_QUARTZ_BLOCK);
		registerBlockItem(r, SMOOTH_RED_QUARTZ_STAIRS);
		registerBlockItem(r, SMOOTH_RED_QUARTZ_SLAB);

		registerBlockItem(r, ELVEN_QUARTZ_BLOCK);
		registerBlockItem(r, ELVEN_QUARTZ_STAIRS);
		registerBlockItem(r, ELVEN_QUARTZ_SLAB);
		registerBlockItem(r, CHISELED_ELVEN_QUARTZ_BLOCK);
		registerBlockItem(r, ELVEN_QUARTZ_BRICKS);
		registerBlockItem(r, ELVEN_QUARTZ_PILLAR);
		registerBlockItem(r, SMOOTH_ELVEN_QUARTZ_BLOCK);
		registerBlockItem(r, SMOOTH_ELVEN_QUARTZ_STAIRS);
		registerBlockItem(r, SMOOTH_ELVEN_QUARTZ_SLAB);

		registerBlockItem(r, SUNNY_QUART_BLOCK);
		registerBlockItem(r, SUNNY_QUARTZ_STAIRS);
		registerBlockItem(r, SUNNY_QUARTZ_SLAB);
		registerBlockItem(r, CHISELED_SUNNY_QUARTZ_BLOCK);
		registerBlockItem(r, SUNNY_QUARTZ_BRICKS);
		registerBlockItem(r, SUNNY_QUARTZ_PILLAR);
		registerBlockItem(r, SMOOTH_SUNNY_QUARTZ_BLOCK);
		registerBlockItem(r, SMOOTH_SUNNY_QUARTZ_STAIRS);
		registerBlockItem(r, SMOOTH_SUNNY_QUARTZ_SLAB);

		registerBlockItem(r, FUCHSITE);
		registerBlockItem(r, FUCHSITE_STAIRS);
		registerBlockItem(r, FUCHSITE_SLAB);
		registerBlockItem(r, FUCHSITE_WALL);
		registerBlockItem(r, FUCHSITE_BUTTON);
		registerBlockItem(r, FUCHSITE_PRESSURE_PLATE);
		registerBlockItem(r, FUCHSITE_BRICKS);
		registerBlockItem(r, FUCHSITE_BRICK_STAIRS);
		registerBlockItem(r, FUCHSITE_BRICK_SLAB);
		registerBlockItem(r, FUCHSITE_BRICK_WALL);
		registerBlockItem(r, COBBLED_FUCHSITE);
		registerBlockItem(r, COBBLED_FUCHSITE_STAIRS);
		registerBlockItem(r, COBBLED_FUCHSITE_SLAB);
		registerBlockItem(r, COBBLED_FUCHSITE_WALL);
		registerBlockItem(r, CHISELED_FUCHSITE_BRICKS);

		registerBlockItem(r, TALC);
		registerBlockItem(r, TALC_STAIRS);
		registerBlockItem(r, TALC_SLAB);
		registerBlockItem(r, TALC_WALL);
		registerBlockItem(r, TALC_BUTTON);
		registerBlockItem(r, TALC_PRESSURE_PLATE);
		registerBlockItem(r, TALC_BRICKS);
		registerBlockItem(r, TALC_BRICK_STAIRS);
		registerBlockItem(r, TALC_BRICK_SLAB);
		registerBlockItem(r, TALC_BRICK_WALL);
		registerBlockItem(r, COBBLED_TALC);
		registerBlockItem(r, COBBLED_TALC_STAIRS);
		registerBlockItem(r, COBBLED_TALC_SLAB);
		registerBlockItem(r, COBBLED_TALC_WALL);
		registerBlockItem(r, CHISELED_TALC_BRICKS);

		registerBlockItem(r, GNEISS);
		registerBlockItem(r, GNEISS_STAIRS);
		registerBlockItem(r, GNEISS_SLAB);
		registerBlockItem(r, GNEISS_WALL);
		registerBlockItem(r, GNEISS_BUTTON);
		registerBlockItem(r, GNEISS_PRESSURE_PLATE);
		registerBlockItem(r, GNEISS_BRICKS);
		registerBlockItem(r, GNEISS_BRICK_STAIRS);
		registerBlockItem(r, GNEISS_BRICK_SLAB);
		registerBlockItem(r, GNEISS_BRICK_WALL);
		registerBlockItem(r, COBBLED_GNEISS);
		registerBlockItem(r, COBBLED_GNEISS_STAIRS);
		registerBlockItem(r, COBBLED_GNEISS_SLAB);
		registerBlockItem(r, COBBLED_GNEISS_WALL);
		registerBlockItem(r, CHISELED_GNEISS_BRICKS);

		registerBlockItem(r, MYCELITE);
		registerBlockItem(r, MYCELITE_STAIRS);
		registerBlockItem(r, MYCELITE_SLAB);
		registerBlockItem(r, MYCELITE_WALL);
		registerBlockItem(r, MYCELITE_BUTTON);
		registerBlockItem(r, MYCELITE_PRESSURE_PLATE);
		registerBlockItem(r, MYCELITE_BRICKS);
		registerBlockItem(r, MYCELITE_BRICK_STAIRS);
		registerBlockItem(r, MYCELITE_BRICK_SLAB);
		registerBlockItem(r, MYCELITE_BRICK_WALL);
		registerBlockItem(r, COBBLED_MYCELITE);
		registerBlockItem(r, COBBLED_MYCELITE_STAIRS);
		registerBlockItem(r, COBBLED_MYCELITE_SLAB);
		registerBlockItem(r, COBBLED_MYCELITE_WALL);
		registerBlockItem(r, CHISELED_MYCELITE_BRICKS);

		registerBlockItem(r, CATACLASITE);
		registerBlockItem(r, CATACLASITE_STAIRS);
		registerBlockItem(r, CATACLASITE_SLAB);
		registerBlockItem(r, CATACLASITE_WALL);
		registerBlockItem(r, CATACLASITE_BUTTON);
		registerBlockItem(r, CATACLASITE_PRESSURE_PLATE);
		registerBlockItem(r, CATACLASITE_BRICKS);
		registerBlockItem(r, CATACLASITE_BRICK_STAIRS);
		registerBlockItem(r, CATACLASITE_BRICK_SLAB);
		registerBlockItem(r, CATACLASITE_BRICK_WALL);
		registerBlockItem(r, COBBLED_CATACLASITE);
		registerBlockItem(r, COBBLED_CATACLASITE_STAIRS);
		registerBlockItem(r, COBBLED_CATACLASITE_SLAB);
		registerBlockItem(r, COBBLED_CATACLASITE_WALL);
		registerBlockItem(r, CHISELED_CATACLASITE_BRICKS);

		registerBlockItem(r, SOLITE);
		registerBlockItem(r, SOLITE_STAIRS);
		registerBlockItem(r, SOLITE_SLAB);
		registerBlockItem(r, SOLITE_WALL);
		registerBlockItem(r, SOLITE_BUTTON);
		registerBlockItem(r, SOLITE_PRESSURE_PLATE);
		registerBlockItem(r, SOLITE_BRICKS);
		registerBlockItem(r, SOLITE_BRICK_STAIRS);
		registerBlockItem(r, SOLITE_BRICK_SLAB);
		registerBlockItem(r, SOLITE_BRICK_WALL);
		registerBlockItem(r, COBBLED_SOLITE);
		registerBlockItem(r, COBBLED_SOLITE_STAIRS);
		registerBlockItem(r, COBBLED_SOLITE_SLAB);
		registerBlockItem(r, COBBLED_SOLITE_WALL);
		registerBlockItem(r, CHISELED_SOLITE_BRICKS);

		registerBlockItem(r, LUNITE);
		registerBlockItem(r, LUNITE_STAIRS);
		registerBlockItem(r, LUNITE_SLAB);
		registerBlockItem(r, LUNITE_WALL);
		registerBlockItem(r, LUNITE_BUTTON);
		registerBlockItem(r, LUNITE_PRESSURE_PLATE);
		registerBlockItem(r, LUNITE_BRICKS);
		registerBlockItem(r, LUNITE_BRICK_STAIRS);
		registerBlockItem(r, LUNITE_BRICK_SLAB);
		registerBlockItem(r, LUNITE_BRICK_WALL);
		registerBlockItem(r, COBBLED_LUNITE);
		registerBlockItem(r, COBBLED_LUNITE_STAIRS);
		registerBlockItem(r, COBBLED_LUNITE_SLAB);
		registerBlockItem(r, COBBLED_LUNITE_WALL);
		registerBlockItem(r, CHISELED_LUNITE_BRICKS);

		registerBlockItem(r, ROSY_TALC);
		registerBlockItem(r, ROSY_TALC_STAIRS);
		registerBlockItem(r, ROSY_TALC_SLAB);
		registerBlockItem(r, ROSY_TALC_WALL);
		registerBlockItem(r, ROSY_TALC_BUTTON);
		registerBlockItem(r, ROSY_TALC_PRESSURE_PLATE);
		registerBlockItem(r, ROSY_TALC_BRICKS);
		registerBlockItem(r, ROSY_TALC_BRICK_STAIRS);
		registerBlockItem(r, ROSY_TALC_BRICK_SLAB);
		registerBlockItem(r, ROSY_TALC_BRICK_WALL);
		registerBlockItem(r, COBBLED_ROSY_TALC);
		registerBlockItem(r, COBBLED_ROSY_TALC_STAIRS);
		registerBlockItem(r, COBBLED_ROSY_TALC_SLAB);
		registerBlockItem(r, COBBLED_ROSY_TALC_WALL);
		registerBlockItem(r, CHISELED_ROSY_TALC_BRICKS);

		registerBlockItem(r, WHITE_PORTUGUESE_PAVEMENT);
		registerBlockItem(r, WHITE_PORTUGUESE_PAVEMENT_STAIRS);
		registerBlockItem(r, WHITE_PORTUGUESE_PAVEMENT_SLAB);

		registerBlockItem(r, BLACK_PORTUGUESE_PAVEMENT);
		registerBlockItem(r, BLACK_PORTUGUESE_PAVEMENT_SLAB);
		registerBlockItem(r, BLACK_PORTUGUESE_PAVEMENT_STAIRS);

		registerBlockItem(r, BLUE_PORTUGUESE_PAVEMENT);
		registerBlockItem(r, BLUE_PORTUGUESE_PAVEMENT_STAIRS);
		registerBlockItem(r, BLUE_PORTUGUESE_PAVEMENT_SLAB);

		registerBlockItem(r, YELLOW_PORTUGUESE_PAVEMENT);
		registerBlockItem(r, YELLOW_PORTUGUESE_PAVEMENT_STAIRS);
		registerBlockItem(r, YELLOW_PORTUGUESE_PAVEMENT_SLAB);

		registerBlockItem(r, RED_PORTUGUESE_PAVEMENT);
		registerBlockItem(r, RED_PORTUGUESE_PAVEMENT_STAIRS);
		registerBlockItem(r, RED_PORTUGUESE_PAVEMENT_SLAB);

		registerBlockItem(r, GREEN_PORTUGUESE_PAVEMENT);
		registerBlockItem(r, GREEN_PORTUGUESE_PAVEMENT_STAIRS);
		registerBlockItem(r, GREEN_PORTUGUESE_PAVEMENT_SLAB);

		registerBlockItem(r, MANAGLASS_PANE);
		registerBlockItem(r, ALFGLASS_PANE);
		registerBlockItem(r, BIFROST_PANE);
	}

	private static void registerBlockItem(BiConsumer<Item, Identifier> registrar, Block block) {
		registerBlockItem(registrar, block, properties -> BlockItemTooltip.createItem(block, properties));
	}

	private static void registerBlockItem(BiConsumer<Item, Identifier> registrar, Block block,
			Function<Item.Properties, Item> factory) {
		Identifier id = BuiltInRegistries.BLOCK.getKey(block);
		Item.Properties properties = BotaniaItems.properties(id)
				.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures());
		registrar.accept(factory.apply(properties), id);
	}

	private static <T extends Block & OptionallyColored> void registerBlockItemWithColoredVariants(
			BiConsumer<Item, Identifier> r, T baseBlock, Rarity rarity) {
		registerBlockItemWithColoredVariants(r, baseBlock, rarity, "_");
	}

	private static <T extends Block & OptionallyColored> void registerBlockItemWithColoredVariants(
			BiConsumer<Item, Identifier> r, T baseBlock, Rarity rarity, String coloredBlockInfix) {
		Identifier baseId = BuiltInRegistries.BLOCK.getKey(baseBlock);
		registerBlockItem(r, baseBlock, props -> BlockItemTooltip.createItem(baseBlock, props.rarity(rarity)));
		ColorHelper.supportedColors().forEach(color -> {
			Identifier coloredId = baseId.withPrefix(color.getSerializedName() + coloredBlockInfix);
			Block coloredBlock = ALL.get(coloredId.getPath());
			registerBlockItem(r, coloredBlock, props -> BlockItemTooltip.createItem(coloredBlock, props.rarity(rarity)));
		});
	}

	private static MysticalFlowerBlock makeMysticalFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.MYSTICAL_FLOWER_SUFFIX,
				key -> new MysticalFlowerBlock(
						color,
						effectForFlower(color),
						MysticalFlowerBlock.STEW_DURATION,
						BotaniaBlocks::getTallMysticalFlower,
						properties.setId(key)
				)
		);
	}

	private static GlimmeringFlowerBlock makeGlimmeringFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.GLIMMERING_FLOWER_SUFFIX,
				key -> new GlimmeringFlowerBlock(
						color,
						effectForFlower(color),
						GlimmeringFlowerBlock.STEW_DURATION,
						properties.setId(key)
				)
		);
	}

	private static FloatingMundaneFlowerBlock makeFloatingMundaneFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.FLOATING_FLOWER_SUFFIX,
				key -> new FloatingMundaneFlowerBlock(
						color,
						properties.setId(key)
				)
		);
	}

	private static BuriedPetalBlock makeBuriedPetal(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.BURIED_PETAL_SUFFIX,
				key -> new BuriedPetalBlock(
						color,
						BotaniaBlocks::getTallMysticalFlower,
						properties.setId(key)
				)
		);
	}

	private static PetalBlock makePetalBlock(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.PETAL_BLOCK_SUFFIX,
				key -> new PetalBlock(color, properties.mapColor(color).setId(key))
		);
	}

	private static ShimmeringMushroomBlock makeShimmeringMushroom(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.SHIMMERING_MUSHROOM_SUFFIX,
				key -> new ShimmeringMushroomBlock(color, properties.setId(key))
		);
	}

	private static <T extends Block> T make(String name, Function<ResourceKey<Block>, T> factory) {
		T block = factory.apply(ResourceKey.create(Registries.BLOCK, botaniaRL(name)));
		var old = ALL.put(name, block);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate name: " + name);
		}
		return block;
	}

	private static <T extends Block & OptionallyColored> T makeBlockWithColoredVariants(String baseName,
			BiFunction<@Nullable DyeColor, ResourceKey<Block>, T> blockFactory) {
		return makeBlockWithColoredVariants(baseName, blockFactory, "_");
	}

	private static <T extends Block & OptionallyColored> T makeBlockWithColoredVariants(String baseName, BiFunction<@Nullable DyeColor, ResourceKey<Block>, T> blockFactory,
			String coloredBlockInfix) {
		T baseBlock = make(baseName, key -> blockFactory.apply(null, key));
		ColorHelper.supportedColors().forEach(
				color -> make(color.getSerializedName() + coloredBlockInfix + baseName, key -> blockFactory.apply(color, key)));
		return baseBlock;
	}

	private static TallMysticalFlowerBlock makeTallMysticalFlower(DyeColor color, BlockBehaviour.Properties properties) {
		return make(
				color.getSerializedName() + LibBlockNames.TALL_MYSTICAL_FLOWER_SUFFIX,
				key -> new TallMysticalFlowerBlock(color, properties.setId(key))
		);
	}

	private static FlowerPotBlock makePottedMundaneFlower(MysticalFlowerBlock flower) {
		return make(
				LibBlockNames.POTTED_PREFIX + flower.color.getSerializedName() + LibBlockNames.MYSTICAL_FLOWER_SUFFIX,
				key -> flowerPot(flower, 0, key)
		);
	}

	private static FlowerPotBlock makePottedGlimmeringFlower(GlimmeringFlowerBlock flower) {
		return make(
				LibBlockNames.POTTED_PREFIX + flower.color.getSerializedName() + LibBlockNames.GLIMMERING_FLOWER_SUFFIX,
				key -> flowerPot(flower, 15, key)
		);
	}

	private static FlowerPotBlock makePottedShimmeringMushroom(ShimmeringMushroomBlock mushroom) {
		return make(
				LibBlockNames.POTTED_PREFIX + mushroom.color.getSerializedName() + LibBlockNames.SHIMMERING_MUSHROOM_SUFFIX,
				key -> flowerPot(mushroom, 3, key)
		);
	}

	private static PetalApothecaryBlock makePetalApothecary(String name, SoundType soundType, MapColor mapColor) {
		return make(name, key -> new PetalApothecaryBlock(BlockBehaviour.Properties.of()
				.strength(3.5F)
				.instrument(NoteBlockInstrument.BASEDRUM)
				.requiresCorrectToolForDrops()
				.lightLevel(state -> state.getValue(PetalApothecaryBlock.FLUID) == PetalApothecary.State.LAVA ? 15 : 0)
				.sound(soundType)
				.mapColor(mapColor).setId(key))
		);
	}

	public static void addDispenserBehaviours() {
		DispenserBlock.registerBehavior(BotaniaItems.WAND_OF_THE_FOREST, new WandBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.PHANTOM_INK, new PhantomInkBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.WAND_OF_THE_ELVEN_FOREST, new WandBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.FLORAL_OBEDIENCE_STICK, new StickBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.MANA_POOL_MINECART, new ManaPoolMinecartBehavior());
		DispenserBlock.registerBehavior(FEL_PUMPKIN, new FelPumpkinBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.MANA_SPARK, new ManaSparkBehavior());
		DispenserBlock.registerBehavior(
				GAIA_HEAD, new OptionalDispenseItemBehavior() {
					@Override
					protected ItemStack execute(BlockSource blockSource, ItemStack item) {
						setSuccess(EquipmentDispenseItemBehavior.dispenseEquipment(blockSource, item));
						return item;
					}
				});

		DispenseItemBehavior behavior = new CorporeaSparkBehavior();
		DispenserBlock.registerBehavior(BotaniaItems.CORPOREA_SPARK, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.MASTER_CORPOREA_SPARK, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.CREATIVE_CORPOREA_SPARK, behavior);
		DispenserBlock.registerBehavior(BotaniaItems.PURE_ENDER_ESSENCE, new ProjectileDispenseBehavior(BotaniaItems.PURE_ENDER_ESSENCE));
		DispenserBlock.registerBehavior(BotaniaItems.ALFGLASS_FLASK, new EnderEssenceBottlingBehavior());

		behavior = new GrassSeedsBehavior();
		Item[] seedItems = {
				BotaniaItems.PASTURE_SEEDS,
				BotaniaItems.BOREAL_SEEDS,
				BotaniaItems.INFESTATION_SPORES,
				BotaniaItems.DRY_SEEDS,
				BotaniaItems.GOLDEN_SEEDS,
				BotaniaItems.VIVID_SEEDS,
				BotaniaItems.SCORCHED_SEEDS,
				BotaniaItems.INFUSED_SEEDS,
				BotaniaItems.MUTATED_SEEDS,
		};
		for (Item seed : seedItems) {
			DispenserBlock.registerBehavior(seed, behavior);
		}

		DispenserBlock.registerBehavior(BotaniaItems.MANASTEEL_SHEARS, new ShearsDispenseItemBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.ELEMENTIUM_SHEARS, new ShearsDispenseItemBehavior());
		DispenserBlock.registerBehavior(BotaniaItems.VINE_BALL, new ProjectileDispenseBehavior(BotaniaItems.VINE_BALL));

		SeedBehaviors.init();
	}

	public static void addAxeStripping(BiConsumer<Block, Block> registrationMethod) {
		registrationMethod.accept(LIVINGWOOD_LOG, STRIPPED_LIVINGWOOD_LOG);
		registrationMethod.accept(GLIMMERING_LIVINGWOOD_LOG, STRIPPED_GLIMMERING_LIVINGWOOD_LOG);
		registrationMethod.accept(LIVINGWOOD, STRIPPED_LIVINGWOOD);
		registrationMethod.accept(GLIMMERING_LIVINGWOOD, STRIPPED_GLIMMERING_LIVINGWOOD);
		registrationMethod.accept(DREAMWOOD_LOG, STRIPPED_DREAMWOOD_LOG);
		registrationMethod.accept(GLIMMERING_DREAMWOOD_LOG, STRIPPED_GLIMMERING_DREAMWOOD_LOG);
		registrationMethod.accept(DREAMWOOD, STRIPPED_DREAMWOOD);
		registrationMethod.accept(GLIMMERING_DREAMWOOD, STRIPPED_GLIMMERING_DREAMWOOD);

		registrationMethod.accept(LIVINGWOOD_STAIRS, STRIPPED_LIVINGWOOD_STAIRS);
		registrationMethod.accept(LIVINGWOOD_SLAB, STRIPPED_LIVINGWOOD_SLAB);
		registrationMethod.accept(LIVINGWOOD_WALL, STRIPPED_LIVINGWOOD_WALL);
		registrationMethod.accept(DREAMWOOD_STAIRS, STRIPPED_DREAMWOOD_STAIRS);
		registrationMethod.accept(DREAMWOOD_SLAB, STRIPPED_DREAMWOOD_SLAB);
		registrationMethod.accept(DREAMWOOD_WALL, STRIPPED_DREAMWOOD_WALL);
	}

	public static Block getMysticalFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_MYSTICAL_FLOWER;
			case ORANGE -> ORANGE_MYSTICAL_FLOWER;
			case MAGENTA -> MAGENTA_MYSTICAL_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_MYSTICAL_FLOWER;
			case YELLOW -> YELLOW_MYSTICAL_FLOWER;
			case LIME -> LIME_MYSTICAL_FLOWER;
			case PINK -> PINK_MYSTICAL_FLOWER;
			case GRAY -> GRAY_MYSTICAL_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_MYSTICAL_FLOWER;
			case CYAN -> CYAN_MYSTICAL_FLOWER;
			case PURPLE -> PURPLE_MYSTICAL_FLOWER;
			case BLUE -> BLUE_MYSTICAL_FLOWER;
			case BROWN -> BROWN_MYSTICAL_FLOWER;
			case GREEN -> GREEN_MYSTICAL_FLOWER;
			case RED -> RED_MYSTICAL_FLOWER;
			case BLACK -> BLACK_MYSTICAL_FLOWER;
		};
	}

	public static Block getShimmeringMushroom(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_SHIMMERING_MUSHROOM;
			case ORANGE -> ORANGE_SHIMMERING_MUSHROOM;
			case MAGENTA -> MAGENTA_SHIMMERING_MUSHROOM;
			case LIGHT_BLUE -> LIGHT_BLUE_SHIMMERING_MUSHROOM;
			case YELLOW -> YELLOW_SHIMMERING_MUSHROOM;
			case LIME -> LIME_SHIMMERING_MUSHROOM;
			case PINK -> PINK_SHIMMERING_MUSHROOM;
			case GRAY -> GRAY_SHIMMERING_MUSHROOM;
			case LIGHT_GRAY -> LIGHT_GRAY_SHIMMERING_MUSHROOM;
			case CYAN -> CYAN_SHIMMERING_MUSHROOM;
			case PURPLE -> PURPLE_SHIMMERING_MUSHROOM;
			case BLUE -> BLUE_SHIMMERING_MUSHROOM;
			case BROWN -> BROWN_SHIMMERING_MUSHROOM;
			case GREEN -> GREEN_SHIMMERING_MUSHROOM;
			case RED -> RED_SHIMMERING_MUSHROOM;
			case BLACK -> BLACK_SHIMMERING_MUSHROOM;
		};
	}

	public static Block getBuriedPetal(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_BURIED_PETAL;
			case ORANGE -> ORANGE_BURIED_PETAL;
			case MAGENTA -> MAGENTA_BURIED_PETAL;
			case LIGHT_BLUE -> LIGHT_BLUE_BURIED_PETAL;
			case YELLOW -> YELLOW_BURIED_PETAL;
			case LIME -> LIME_BURIED_PETAL;
			case PINK -> PINK_BURIED_PETAL;
			case GRAY -> GRAY_BURIED_PETAL;
			case LIGHT_GRAY -> LIGHT_GRAY_BURIED_PETAL;
			case CYAN -> CYAN_BURIED_PETAL;
			case PURPLE -> PURPLE_BURIED_PETAL;
			case BLUE -> BLUE_BURIED_PETAL;
			case BROWN -> BROWN_BURIED_PETAL;
			case GREEN -> GREEN_BURIED_PETAL;
			case RED -> RED_BURIED_PETAL;
			case BLACK -> BLACK_BURIED_PETAL;
		};
	}

	public static Block getGlimmeringFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_GLIMMERING_FLOWER;
			case ORANGE -> ORANGE_GLIMMERING_FLOWER;
			case MAGENTA -> MAGENTA_GLIMMERING_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_GLIMMERING_FLOWER;
			case YELLOW -> YELLOW_GLIMMERING_FLOWER;
			case LIME -> LIME_GLIMMERING_FLOWER;
			case PINK -> PINK_GLIMMERING_FLOWER;
			case GRAY -> GRAY_GLIMMERING_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_GLIMMERING_FLOWER;
			case CYAN -> CYAN_GLIMMERING_FLOWER;
			case PURPLE -> PURPLE_GLIMMERING_FLOWER;
			case BLUE -> BLUE_GLIMMERING_FLOWER;
			case BROWN -> BROWN_GLIMMERING_FLOWER;
			case GREEN -> GREEN_GLIMMERING_FLOWER;
			case RED -> RED_GLIMMERING_FLOWER;
			case BLACK -> BLACK_GLIMMERING_FLOWER;
		};
	}

	public static Block getFloatingFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_FLOATING_FLOWER;
			case ORANGE -> ORANGE_FLOATING_FLOWER;
			case MAGENTA -> MAGENTA_FLOATING_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_FLOATING_FLOWER;
			case YELLOW -> YELLOW_FLOATING_FLOWER;
			case LIME -> LIME_FLOATING_FLOWER;
			case PINK -> PINK_FLOATING_FLOWER;
			case GRAY -> GRAY_FLOATING_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_FLOATING_FLOWER;
			case CYAN -> CYAN_FLOATING_FLOWER;
			case PURPLE -> PURPLE_FLOATING_FLOWER;
			case BLUE -> BLUE_FLOATING_FLOWER;
			case BROWN -> BROWN_FLOATING_FLOWER;
			case GREEN -> GREEN_FLOATING_FLOWER;
			case RED -> RED_FLOATING_FLOWER;
			case BLACK -> BLACK_FLOATING_FLOWER;
		};
	}

	@Nullable
	public static Block getTallMysticalFlower(TallFlowerGrower grower) {
		return grower instanceof Colored colored ? getTallMysticalFlower(colored.getColor()) : null;
	}

	public static Block getTallMysticalFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_TALL_MYSTICAL_FLOWER;
			case ORANGE -> ORANGE_TALL_MYSTICAL_FLOWER;
			case MAGENTA -> MAGENTA_TALL_MYSTICAL_FLOWER;
			case LIGHT_BLUE -> LIGHT_BLUE_TALL_MYSTICAL_FLOWER;
			case YELLOW -> YELLOW_TALL_MYSTICAL_FLOWER;
			case LIME -> LIME_TALL_MYSTICAL_FLOWER;
			case PINK -> PINK_TALL_MYSTICAL_FLOWER;
			case GRAY -> GRAY_TALL_MYSTICAL_FLOWER;
			case LIGHT_GRAY -> LIGHT_GRAY_TALL_MYSTICAL_FLOWER;
			case CYAN -> CYAN_TALL_MYSTICAL_FLOWER;
			case PURPLE -> PURPLE_TALL_MYSTICAL_FLOWER;
			case BLUE -> BLUE_TALL_MYSTICAL_FLOWER;
			case BROWN -> BROWN_TALL_MYSTICAL_FLOWER;
			case GREEN -> GREEN_TALL_MYSTICAL_FLOWER;
			case RED -> RED_TALL_MYSTICAL_FLOWER;
			case BLACK -> BLACK_TALL_MYSTICAL_FLOWER;
		};
	}

	public static Block getPetalBlock(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_PETAL_BLOCK;
			case ORANGE -> ORANGE_PETAL_BLOCK;
			case MAGENTA -> MAGENTA_PETAL_BLOCK;
			case LIGHT_BLUE -> LIGHT_BLUE_PETAL_BLOCK;
			case YELLOW -> YELLOW_PETAL_BLOCK;
			case LIME -> LIME_PETAL_BLOCK;
			case PINK -> PINK_PETAL_BLOCK;
			case GRAY -> GRAY_PETAL_BLOCK;
			case LIGHT_GRAY -> LIGHT_GRAY_PETAL_BLOCK;
			case CYAN -> CYAN_PETAL_BLOCK;
			case PURPLE -> PURPLE_PETAL_BLOCK;
			case BLUE -> BLUE_PETAL_BLOCK;
			case BROWN -> BROWN_PETAL_BLOCK;
			case GREEN -> GREEN_PETAL_BLOCK;
			case RED -> RED_PETAL_BLOCK;
			case BLACK -> BLACK_PETAL_BLOCK;
		};
	}

	public static Block getPottedFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> POTTED_WHITE_MYSTICAL_FLOWER;
			case ORANGE -> POTTED_ORANGE_MYSTICAL_FLOWER;
			case MAGENTA -> POTTED_MAGENTA_MYSTICAL_FLOWER;
			case LIGHT_BLUE -> POTTED_LIGHT_BLUE_MYSTICAL_FLOWER;
			case YELLOW -> POTTED_YELLOW_MYSTICAL_FLOWER;
			case LIME -> POTTED_LIME_MYSTICAL_FLOWER;
			case PINK -> POTTED_PINK_MYSTICAL_FLOWER;
			case GRAY -> POTTED_GRAY_MYSTICAL_FLOWER;
			case LIGHT_GRAY -> POTTED_LIGHT_GRAY_MYSTICAL_FLOWER;
			case CYAN -> POTTED_CYAN_MYSTICAL_FLOWER;
			case PURPLE -> POTTED_PURPLE_MYSTICAL_FLOWER;
			case BLUE -> POTTED_BLUE_MYSTICAL_FLOWER;
			case BROWN -> POTTED_BROWN_MYSTICAL_FLOWER;
			case GREEN -> POTTED_GREEN_MYSTICAL_FLOWER;
			case RED -> POTTED_RED_MYSTICAL_FLOWER;
			case BLACK -> POTTED_BLACK_MYSTICAL_FLOWER;
		};
	}

	public static Block getPottedShinyFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> POTTED_WHITE_GLIMMERING_FLOWER;
			case ORANGE -> POTTED_ORANGE_GLIMMERING_FLOWER;
			case MAGENTA -> POTTED_MAGENTA_GLIMMERING_FLOWER;
			case LIGHT_BLUE -> POTTED_LIGHT_BLUE_GLIMMERING_FLOWER;
			case YELLOW -> POTTED_YELLOW_GLIMMERING_FLOWER;
			case LIME -> POTTED_LIME_GLIMMERING_FLOWER;
			case PINK -> POTTED_PINK_GLIMMERING_FLOWER;
			case GRAY -> POTTED_GRAY_GLIMMERING_FLOWER;
			case LIGHT_GRAY -> POTTED_LIGHT_GRAY_GLIMMERING_FLOWER;
			case CYAN -> POTTED_CYAN_GLIMMERING_FLOWER;
			case PURPLE -> POTTED_PURPLE_GLIMMERING_FLOWER;
			case BLUE -> POTTED_BLUE_GLIMMERING_FLOWER;
			case BROWN -> POTTED_BROWN_GLIMMERING_FLOWER;
			case GREEN -> POTTED_GREEN_GLIMMERING_FLOWER;
			case RED -> POTTED_RED_GLIMMERING_FLOWER;
			case BLACK -> POTTED_BLACK_GLIMMERING_FLOWER;
		};
	}

	public static Block getPottedMushroom(DyeColor color) {
		return switch (color) {
			case WHITE -> POTTED_WHITE_SHIMMERING_MUSHROOM;
			case ORANGE -> POTTED_ORANGE_SHIMMERING_MUSHROOM;
			case MAGENTA -> POTTED_MAGENTA_SHIMMERING_MUSHROOM;
			case LIGHT_BLUE -> POTTED_LIGHT_BLUE_SHIMMERING_MUSHROOM;
			case YELLOW -> POTTED_YELLOW_SHIMMERING_MUSHROOM;
			case LIME -> POTTED_LIME_SHIMMERING_MUSHROOM;
			case PINK -> POTTED_PINK_SHIMMERING_MUSHROOM;
			case GRAY -> POTTED_GRAY_SHIMMERING_MUSHROOM;
			case LIGHT_GRAY -> POTTED_LIGHT_GRAY_SHIMMERING_MUSHROOM;
			case CYAN -> POTTED_CYAN_SHIMMERING_MUSHROOM;
			case PURPLE -> POTTED_PURPLE_SHIMMERING_MUSHROOM;
			case BLUE -> POTTED_BLUE_SHIMMERING_MUSHROOM;
			case BROWN -> POTTED_BROWN_SHIMMERING_MUSHROOM;
			case GREEN -> POTTED_GREEN_SHIMMERING_MUSHROOM;
			case RED -> POTTED_RED_SHIMMERING_MUSHROOM;
			case BLACK -> POTTED_BLACK_SHIMMERING_MUSHROOM;
		};
	}

	private static Holder<MobEffect> effectForFlower(DyeColor color) {
		return switch (color) {
			case WHITE -> MobEffects.SPEED;
			case ORANGE -> MobEffects.FIRE_RESISTANCE;
			case MAGENTA -> MobEffects.MINING_FATIGUE;
			case LIGHT_BLUE -> MobEffects.JUMP_BOOST;
			case YELLOW -> MobEffects.ABSORPTION;
			case LIME -> MobEffects.POISON;
			case PINK -> MobEffects.REGENERATION;
			case GRAY -> MobEffects.RESISTANCE;
			case LIGHT_GRAY -> MobEffects.WEAKNESS;
			case CYAN -> MobEffects.WATER_BREATHING;
			case PURPLE -> MobEffects.NAUSEA;
			case BLUE -> MobEffects.NIGHT_VISION;
			case BROWN -> MobEffects.WITHER;
			case GREEN -> MobEffects.HUNGER;
			case RED -> MobEffects.STRENGTH;
			case BLACK -> MobEffects.BLINDNESS;
		};
	}

	public static void registerFlowerPotPlants(BiConsumer<Identifier, Supplier<? extends Block>> consumer) {
		registerBlocks((block, resourceLocation) -> {
			if (block instanceof FlowerPotBlock) {
				var id = BuiltInRegistries.BLOCK.getKey(block);
				consumer.accept(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring(LibBlockNames.POTTED_PREFIX.length())), () -> block);
			}
		});
	}

	/**
	 * Applies naming conventions to find a block of the same type in the specified color variant.
	 * The ID of dyed variants is expected to use the dye name as prefix of the undyed block's ID.
	 */
	public static <T extends Block & OptionallyColored> T findOptionallyDyedBlock(T referenceBlock, @Nullable DyeColor targetColor) {
		return findOptionallyDyedBlock(referenceBlock, targetColor, "_");
	}

	/**
	 * Applies naming conventions to find a block of the same type in the specified color variant.
	 * The ID of dyed variants is expected to use the dye name as prefix of the undyed block's ID.
	 * The ID part between the dye name and the base block ID (the color block infix) is often "_", but can be longer,
	 * e.g. it would be "_stained_" for colored glass blocks, if those supported this operation.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Block & OptionallyColored> T findOptionallyDyedBlock(T referenceBlock,
			@Nullable DyeColor targetColor, String coloredBlockInfix) {
		@Nullable
		DyeColor referenceColor = referenceBlock.getOptionalColor().orElse(null);
		if (targetColor == referenceColor) {
			// already is the expected color
			return referenceBlock;
		}
		// at most one of reference color and target color is empty

		Identifier referenceId = BuiltInRegistries.BLOCK.getKey(referenceBlock);
		Identifier targetId = getTargetId(referenceId, referenceColor, targetColor, coloredBlockInfix);

		Block targetBlock = BuiltInRegistries.BLOCK.getValue(targetId);
		if (!referenceBlock.getClass().isInstance(targetBlock)) {
			throw new IllegalArgumentException(
					"No target block for %s in %s (looked for %s, found %s)".formatted(referenceBlock, targetColor, targetId, targetBlock));
		}

		return (T) targetBlock;
	}

	private static String getUndyedReferencePath(Identifier referenceId, @Nullable DyeColor referenceColor, String coloredBlockInfix) {
		if (referenceColor == null) {
			return referenceId.getPath();
		}
		String referenceColorPrefix = referenceColor.getSerializedName() + coloredBlockInfix;
		if (!referenceId.getPath().startsWith(referenceColorPrefix)) {
			throw new IllegalArgumentException(
					"Block ID %s should start with color prefix %s".formatted(referenceId, referenceColorPrefix));
		}
		return referenceId.getPath().substring(referenceColorPrefix.length());
	}

	private static Identifier getTargetId(Identifier referenceId,
			@Nullable DyeColor referenceColor, @Nullable DyeColor targetColor, String coloredBlockInfix) {
		if (targetColor == null) {
			return referenceId.withPath(getUndyedReferencePath(referenceId, referenceColor, coloredBlockInfix));
		}
		if (referenceColor == null) {
			return referenceId.withPrefix(targetColor.getSerializedName() + coloredBlockInfix);
		}
		return referenceId.withPath(targetColor.getSerializedName() + coloredBlockInfix +
				getUndyedReferencePath(referenceId, referenceColor, coloredBlockInfix));
	}

	/**
	 * Applies naming conventions to find a block of the same type in the specified color variant. The block ID is
	 * expected to have the same format across all dye variants, with the dye name appearing once the ID.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Block & Colored> T findDyedBlock(T referenceBlock, DyeColor targetColor) {
		DyeColor referenceColor = referenceBlock.getColor();
		if (referenceColor == targetColor) {
			return referenceBlock;
		}

		Identifier referenceId = BuiltInRegistries.BLOCK.getKey(referenceBlock);
		Identifier targetId = referenceId.withPath(path -> path.replaceFirst(
				"(?<=_|\\b)" + referenceColor.getSerializedName() + "(?=_|\\b)", targetColor.getSerializedName()));

		Block targetBlock = BuiltInRegistries.BLOCK.getValue(targetId);
		if (!referenceBlock.getClass().isInstance(targetBlock)) {
			throw new IllegalArgumentException(
					"No target block for %s in %s (found %s)".formatted(referenceBlock, targetColor, targetBlock));
		}

		return (T) targetBlock;
	}

	@FunctionalInterface
	public interface BCapConsumer<T> {
		void accept(Function<BlockState, T> factory, Block... blocks);
	}

	@FunctionalInterface
	public interface BCapFallbackConsumer<T> {
		void accept(Function<BlockState, @Nullable T> factory);
	}
}
