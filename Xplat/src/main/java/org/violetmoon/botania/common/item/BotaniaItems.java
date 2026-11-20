/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.cauldron.CauldronInteractions;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;

import org.violetmoon.botania.api.item.AncientWillContainer;
import org.violetmoon.botania.api.item.PhantomInkable;
import org.violetmoon.botania.api.state.enums.CraftyCratePattern;
import org.violetmoon.botania.client.gui.bag.ColoredContentsPouchMenu;
import org.violetmoon.botania.client.gui.box.TrinketCaseMenu;
import org.violetmoon.botania.client.gui.enderhand.HandOfEnderMenu;
import org.violetmoon.botania.common.BotaniaStats;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.mana.ManaPoolBlock;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.component.ManaRepair;
import org.violetmoon.botania.common.helper.ColorHelper;
import org.violetmoon.botania.common.item.brew.BaseBrewItem;
import org.violetmoon.botania.common.item.brew.FlaskItem;
import org.violetmoon.botania.common.item.brew.IncenseStickItem;
import org.violetmoon.botania.common.item.brew.VialItem;
import org.violetmoon.botania.common.item.equipment.armor.elementium.*;
import org.violetmoon.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import org.violetmoon.botania.common.item.equipment.armor.manasteel.ManasteelHelmItem;
import org.violetmoon.botania.common.item.equipment.armor.manaweave.ManaweaveArmorItem;
import org.violetmoon.botania.common.item.equipment.armor.manaweave.ManaweaveHelmItem;
import org.violetmoon.botania.common.item.equipment.armor.terrasteel.TerrasteelArmorItem;
import org.violetmoon.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import org.violetmoon.botania.common.item.equipment.bauble.*;
import org.violetmoon.botania.common.item.equipment.tool.SoulscribeItem;
import org.violetmoon.botania.common.item.equipment.tool.StarcallerItem;
import org.violetmoon.botania.common.item.equipment.tool.ThundercallerItem;
import org.violetmoon.botania.common.item.equipment.tool.VitreousPickaxeItem;
import org.violetmoon.botania.common.item.equipment.tool.bow.CrystalBowItem;
import org.violetmoon.botania.common.item.equipment.tool.elementium.*;
import org.violetmoon.botania.common.item.equipment.tool.manasteel.*;
import org.violetmoon.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import org.violetmoon.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import org.violetmoon.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import org.violetmoon.botania.common.item.lens.*;
import org.violetmoon.botania.common.item.material.*;
import org.violetmoon.botania.common.item.record.BotaniaJukeboxSongs;
import org.violetmoon.botania.common.item.relic.*;
import org.violetmoon.botania.common.item.rod.*;
import org.violetmoon.botania.common.lib.BotaniaTags;
import org.violetmoon.botania.common.lib.LibItemNames;
import org.violetmoon.botania.mixin.CauldronInteractionDispatcherAccessor;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaItems {
	private static final Map<String, Item> ALL = new LinkedHashMap<>(); // Preserve insertion order

	public static final LexicaBotaniaItem LEXICA_BOTANIA = make(LibItemNames.LEXICA_BOTANIA,
			props -> new LexicaBotaniaItem(props.stacksTo(1)));
	public static final Item WAND_OF_THE_FOREST = make(LibItemNames.WAND_OF_THE_FOREST,
			props -> new WandOfTheForestItem(ChatFormatting.DARK_GREEN, props.stacksTo(1)
					.component(BotaniaDataComponents.WAND_BIND_MODE, Unit.INSTANCE)));
	public static final Item WAND_OF_THE_ELVEN_FOREST = make(LibItemNames.WAND_OF_THE_ELVEN_FOREST,
			props -> new WandOfTheForestItem(ChatFormatting.LIGHT_PURPLE, props.stacksTo(1)
					.component(BotaniaDataComponents.WAND_BIND_MODE, Unit.INSTANCE)));
	public static final Item FLORAL_OBEDIENCE_STICK = make(LibItemNames.FLORAL_OBEDIENCE_STICK,
			props -> new FloralObedienceStickItem(props.stacksTo(1)));
	public static final Item FLORAL_FERTILIZER = make(LibItemNames.FLORAL_FERTILIZER,
			FloralFertilizerItem::new);

	// mystical petals
	public static final Item WHITE_MYSTICAL_PETAL = makePetal(DyeColor.WHITE);
	public static final Item LIGHT_GRAY_MYSTICAL_PETAL = makePetal(DyeColor.LIGHT_GRAY);
	public static final Item GRAY_MYSTICAL_PETAL = makePetal(DyeColor.GRAY);
	public static final Item BLACK_MYSTICAL_PETAL = makePetal(DyeColor.BLACK);
	public static final Item BROWN_MYSTICAL_PETAL = makePetal(DyeColor.BROWN);
	public static final Item RED_MYSTICAL_PETAL = makePetal(DyeColor.RED);
	public static final Item ORANGE_MYSTICAL_PETAL = makePetal(DyeColor.ORANGE);
	public static final Item YELLOW_MYSTICAL_PETAL = makePetal(DyeColor.YELLOW);
	public static final Item LIME_MYSTICAL_PETAL = makePetal(DyeColor.LIME);
	public static final Item GREEN_MYSTICAL_PETAL = makePetal(DyeColor.GREEN);
	public static final Item CYAN_MYSTICAL_PETAL = makePetal(DyeColor.CYAN);
	public static final Item LIGHT_BLUE_MYSTICAL_PETAL = makePetal(DyeColor.LIGHT_BLUE);
	public static final Item BLUE_MYSTICAL_PETAL = makePetal(DyeColor.BLUE);
	public static final Item PURPLE_MYSTICAL_PETAL = makePetal(DyeColor.PURPLE);
	public static final Item MAGENTA_MYSTICAL_PETAL = makePetal(DyeColor.MAGENTA);
	public static final Item PINK_MYSTICAL_PETAL = makePetal(DyeColor.PINK);

	public static final Item MANASTEEL_INGOT = make(LibItemNames.MANASTEEL_INGOT, Item::new);
	public static final Item MANA_PEARL = make(LibItemNames.MANA_PEARL, Item::new);
	public static final Item MANA_DIAMOND = make(LibItemNames.MANA_DIAMOND, Item::new);
	public static final Item LIVINGWOOD_TWIG = make(LibItemNames.LIVINGWOOD_TWIG, Item::new);
	public static final Item TERRASTEEL_INGOT = make(LibItemNames.TERRASTEEL_INGOT,
			props -> new GaiaRitualSacrificeItem(props
					.rarity(Rarity.UNCOMMON), false));
	public static final Item GAIA_SPIRIT = make(LibItemNames.GAIA_SPIRIT,
			props -> new Item(props
					.rarity(Rarity.RARE)));
	public static final Item REDSTONE_ROOT = make(LibItemNames.REDSTONE_ROOT, Item::new);
	public static final Item ELEMENTIUM_INGOT = make(LibItemNames.ELEMENTIUM_INGOT, Item::new);
	public static final Item PIXIE_DUST = make(LibItemNames.PIXIE_DUST, Item::new);
	public static final Item DRAGONSTONE = make(LibItemNames.DRAGONSTONE, Item::new);
	public static final Item RED_STRING = make(LibItemNames.RED_STRING, Item::new);
	public static final Item DREAMWOOD_TWIG = make(LibItemNames.DREAMWOOD_TWIG, Item::new);
	public static final Item GAIA_INGOT = make(LibItemNames.GAIA_INGOT,
			props -> new GaiaRitualSacrificeItem(props.rarity(Rarity.RARE), true));
	public static final Item PURE_ENDER_ESSENCE = make(LibItemNames.PURE_ENDER_ESSENCE, ThrowableEnderEssenceItem::new);
	public static final Item DILUTED_ENDER_ESSENCE = make(LibItemNames.DILUTED_ENDER_ESSENCE, Item::new);
	public static final Item MANA_INFUSED_STRING = make(LibItemNames.MANA_STRING, Item::new);
	public static final Item MANASTEEL_NUGGET = make(LibItemNames.MANASTEEL_NUGGET, Item::new);
	public static final Item TERRASTEEL_NUGGET = make(LibItemNames.TERRASTEEL_NUGGET,
			props -> new Item(props.rarity(Rarity.UNCOMMON)));
	public static final Item ELEMENTIUM_NUGGET = make(LibItemNames.ELEMENTIUM_NUGGET, Item::new);
	public static final Item LIVING_ROOT = make(LibItemNames.LIVING_ROOT, BoneMealItem::new);
	public static final Item PEBBLE = make(LibItemNames.PEBBLE, Item::new);
	public static final Item MANAWEAVE_CLOTH = make(LibItemNames.MANAWEAVE_CLOTH, Item::new);
	public static final Item MANA_POWDER = make(LibItemNames.MANA_POWDER, Item::new);

	// quartz variants
	public static final Item DARK_QUARTZ = make(LibItemNames.SMOKEY_QUARTZ, Item::new);
	public static final Item MANA_QUARTZ = make(LibItemNames.MANA_QUARTZ, Item::new);
	public static final Item BLAZE_QUARTZ = make(LibItemNames.BLAZE_QUARTZ, Item::new);
	public static final Item LAVENDER_QUARTZ = make(LibItemNames.LAVENDER_QUARTZ, Item::new);
	public static final Item RED_QUARTZ = make(LibItemNames.RED_QUARTZ, Item::new);
	public static final Item ELVEN_QUARTZ = make(LibItemNames.ELVEN_QUARTZ, Item::new);
	public static final Item SUNNY_QUARTZ = make(LibItemNames.SUNNY_QUARTZ, Item::new);

	// lenses
	public static final Item MANA_LENS = make(LibItemNames.MANA_LENS,
			props -> new LensItem(props.stacksTo(16), new Lens(), LensItem.PROP_NONE));
	public static final Item VELOCITY_LENS = make(LibItemNames.VELOCITY_LENS,
			props -> new LensItem(props.stacksTo(16), new VelocityLens(), LensItem.PROP_NONE));
	public static final Item POTENCY_LENS = make(LibItemNames.POTENCY_LENS,
			props -> new LensItem(props.stacksTo(16), new PotencyLens(), LensItem.PROP_POWER));
	public static final Item RESISTANCE_LENS = make(LibItemNames.RESISTANCE_LENS,
			props -> new LensItem(props.stacksTo(16), new ResistanceLens(), LensItem.PROP_NONE));
	public static final Item EFFICIENCY_LENS = make(LibItemNames.EFFICIENCY_LENS,
			props -> new LensItem(props.stacksTo(16), new EfficiencyLens(), LensItem.PROP_NONE));
	public static final Item BOUNCE_LENS = make(LibItemNames.BOUNCE_LENS,
			props -> new LensItem(props.stacksTo(16), new BounceLens(), LensItem.PROP_TOUCH));
	public static final Item GRAVITY_LENS = make(LibItemNames.GRAVITY_LENS,
			props -> new LensItem(props.stacksTo(16), new GravityLens(), LensItem.PROP_ORIENTATION));
	public static final Item BORE_LENS = make(LibItemNames.BORE_LENS,
			props -> new LensItem(props.stacksTo(16), new BoreLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item DAMAGING_LENS = make(LibItemNames.DAMAGING_LENS,
			props -> new LensItem(props.stacksTo(16), new DamagingLens(), LensItem.PROP_DAMAGE));
	public static final Item PHANTOM_LENS = make(LibItemNames.PHANTOM_LENS,
			props -> new LensItem(props.stacksTo(16), new PhantomLens(), LensItem.PROP_TOUCH));
	public static final Item MAGNETIZING_LENS = make(LibItemNames.MAGNETIZING_LENS,
			props -> new LensItem(props.stacksTo(16), new MagnetizingLens(), LensItem.PROP_ORIENTATION));
	public static final Item ENTROPIC_LENS = make(LibItemNames.ENTROPIC_LENS,
			props -> new LensItem(props.stacksTo(16), new EntropicLens(),
					LensItem.PROP_DAMAGE | LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item INFLUENCE_LENS = make(LibItemNames.INFLUENCE_LENS,
			props -> new LensItem(props.stacksTo(16), new InfluenceLens(), LensItem.PROP_NONE));
	public static final Item WEIGHT_LENS = make(LibItemNames.WEIGHT_LENS,
			props -> new LensItem(props.stacksTo(16), new WeightLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item PAINTSLINGER_LENS = make(LibItemNames.PAINTSLINGER_LENS,
			props -> new LensItem(props.stacksTo(16), new PaintslingerLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item KINDLE_LENS = make(LibItemNames.KINDLE_LENS,
			props -> new LensItem(props.stacksTo(16), new KindleLens(),
					LensItem.PROP_DAMAGE | LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item FORCE_LENS = make(LibItemNames.FORCE_LENS,
			props -> new LensItem(props.stacksTo(16), new ForceLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item FLASH_LENS = make(LibItemNames.FLASH_LENS,
			props -> new LensItem(props.stacksTo(16), new FlashLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item WARP_LENS = make(LibItemNames.WARP_LENS,
			props -> new LensItem(props.stacksTo(16), new WarpLens(), LensItem.PROP_NONE));
	public static final Item REDIRECTIVE_LENS = make(LibItemNames.REDIRECTIVE_LENS,
			props -> new LensItem(props.stacksTo(16), new RedirectiveLens(), LensItem.PROP_TOUCH | LensItem.PROP_INTERACTION));
	public static final Item CELEBRATORY_LENS = make(LibItemNames.CELEBRATORY_LENS,
			props -> new LensItem(props.stacksTo(16), new CelebratoryLens(), LensItem.PROP_TOUCH));
	public static final Item FLARE_LENS = make(LibItemNames.FLARE_LENS,
			props -> new LensItem(props.stacksTo(16), new FlareLens(), LensItem.PROP_CONTROL));
	public static final Item MESSENGER_LENS = make(LibItemNames.MESSENGER_LENS,
			props -> new LensItem(props.stacksTo(16), new MessengerLens(), LensItem.PROP_POWER));
	public static final Item TRIPWIRE_LENS = make(LibItemNames.TRIPWIRE_LENS,
			props -> new LensItem(props.stacksTo(16), new TripwireLens(), LensItem.PROP_CONTROL));
	public static final Item STORM_LENS = make(LibItemNames.LENS_STORM,
			props -> new LensItem(props.stacksTo(16).rarity(Rarity.EPIC), new StormLens(), LensItem.PROP_NONE));

	// runes
	public static final Item RUNE_OF_WATER = make(LibItemNames.RUNE_OF_WATER, RuneItem::new);
	public static final Item RUNE_OF_FIRE = make(LibItemNames.RUNE_OF_FIRE, RuneItem::new);
	public static final Item RUNE_OF_EARTH = make(LibItemNames.RUNE_OF_EARTH, RuneItem::new);
	public static final Item RUNE_OF_AIR = make(LibItemNames.RUNE_OF_AIR, RuneItem::new);
	public static final Item RUNE_OF_SPRING = make(LibItemNames.RUNE_OF_SPRING, RuneItem::new);
	public static final Item RUNE_OF_SUMMER = make(LibItemNames.RUNE_OF_SUMMER, RuneItem::new);
	public static final Item RUNE_OF_AUTUMN = make(LibItemNames.RUNE_OF_AUTUMN, RuneItem::new);
	public static final Item RUNE_OF_WINTER = make(LibItemNames.RUNE_OF_WINTER, RuneItem::new);
	public static final Item RUNE_OF_MANA = make(LibItemNames.RUNE_OF_MANA, RuneItem::new);
	public static final Item RUNE_OF_LUST = make(LibItemNames.RUNE_OF_LUST, RuneItem::new);
	public static final Item RUNE_OF_GLUTTONY = make(LibItemNames.RUNE_OF_GLUTTONY, RuneItem::new);
	public static final Item RUNE_OF_GREED = make(LibItemNames.RUNE_OF_GREED, RuneItem::new);
	public static final Item RUNE_OF_SLOTH = make(LibItemNames.RUNE_OF_SLOTH, RuneItem::new);
	public static final Item RUNE_OF_WRATH = make(LibItemNames.RUNE_OF_WRATH, RuneItem::new);
	public static final Item RUNE_OF_ENVY = make(LibItemNames.RUNE_OF_ENVY, RuneItem::new);
	public static final Item RUNE_OF_PRIDE = make(LibItemNames.RUNE_OF_PRIDE, RuneItem::new);

	// seeds
	public static final Item PASTURE_SEEDS = make(LibItemNames.PASTURE_SEEDS,
			props -> new GrassSeedsItem(Blocks.GRASS_BLOCK, 0x006600, props));
	public static final Item BOREAL_SEEDS = make(LibItemNames.BOREAL_SEEDS,
			props -> new GrassSeedsItem(Blocks.PODZOL, 0x805E00, props));
	public static final Item INFESTATION_SPORES = make(LibItemNames.INFESTATION_SPORES,
			props -> new GrassSeedsItem(Blocks.MYCELIUM, 0x5E0054, props));
	public static final Item DRY_SEEDS = make(LibItemNames.DRY_PASTURE_SEEDS,
			props -> new GrassSeedsItem(BotaniaBlocks.DRY_GRASS_BLOCK, 0x66800D, props));
	public static final Item GOLDEN_SEEDS = make(LibItemNames.GOLDEN_PASTURE_SEEDS,
			props -> new GrassSeedsItem(BotaniaBlocks.GOLDEN_GRASS_BLOCK, 0xBFB300, props));
	public static final Item VIVID_SEEDS = make(LibItemNames.VIVID_PASTURE_SEEDS,
			props -> new GrassSeedsItem(BotaniaBlocks.VIVID_GRASS_BLOCK, 0x00801A, props));
	public static final Item SCORCHED_SEEDS = make(LibItemNames.SCORCHED_PASTURE_SEEDS,
			props -> new GrassSeedsItem(BotaniaBlocks.SCORCHED_GRASS_BLOCK, 0xBF0000, props));
	public static final Item INFUSED_SEEDS = make(LibItemNames.INFUSED_PASTURE_SEEDS,
			props -> new GrassSeedsItem(BotaniaBlocks.INFUSED_GRASS_BLOCK, 0x008C8C, props));
	public static final Item MUTATED_SEEDS = make(LibItemNames.MUTATED_PASTURE_SEEDS,
			props -> new GrassSeedsItem(BotaniaBlocks.MUTATED_GRASS_BLOCK, 0x661A66, props));

	// Rods
	public static final Item ROD_OF_THE_LANDS = make(LibItemNames.ROD_OF_THE_LANDS,
			props -> new LandsRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_HIGHLANDS = make(LibItemNames.ROD_OF_THE_HIGHLANDS,
			props -> new HighlandsRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_TERRA_FIRMA = make(LibItemNames.ROD_OF_THE_TERRA_FIRMA,
			props -> new TerraFirmaRodItem(props.stacksTo(1)
					.rarity(Rarity.UNCOMMON)));
	public static final Item ROD_OF_THE_DEPTHS = make(LibItemNames.ROD_OF_THE_DEPTHS,
			props -> new DepthsRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_SEAS = make(LibItemNames.ROD_OF_THE_SEAS,
			props -> new SeasRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_SKIES = make(LibItemNames.ROD_OF_THE_SKIES,
			props -> new SkiesRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_HELLS = make(LibItemNames.ROD_OF_THE_HELLS,
			props -> new HellsRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_PLENTIFUL_MANTLE = make(LibItemNames.ROD_OF_THE_PLENTIFUL_MANTLE,
			props -> new PlentifulMantleRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_MOLTEN_CORE = make(LibItemNames.ROD_OF_THE_MOLTEN_CORE,
			props -> new MoltenCoreRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_SHIFTING_CRUST = make(LibItemNames.ROD_OF_THE_SHIFTING_CRUST,
			props -> new ShiftingCrustRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_BIFROST = make(LibItemNames.ROD_OF_THE_BIFROST,
			props -> new BifrostRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_SHADED_MESA = make(LibItemNames.ROD_OF_THE_SHADED_MESA,
			props -> new ShadedMesaRodItem(props.stacksTo(1)));
	public static final Item ROD_OF_THE_UNSTABLE_RESERVOIR = make(LibItemNames.ROD_OF_THE_UNSTABLE_RESERVOIR,
			props -> new UnstableReservoirRodItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));

	// Equipment
	public static final int MANASTEEL_DURABILITY_FACTOR = 18;
	public static final int MANA_PER_DAMAGE_METAL_ARMOR = 70;
	public static final int MANA_PER_DAMAGE_MANAWEAVE_ARMOR = 30;
	public static final int MANA_PER_DAMAGE_MANASTEEL_TOOL = 60;
	public static final int MANA_PER_DAMAGE_ELEMENTIUM_TOOL = 60;
	public static final int MANA_PER_DAMAGE_TERRASTEEL_TOOL = 100;
	public static final int MANA_PER_DAMAGE_UPGRADED_SWORD = 120;
	public static final int MANA_PER_DAMAGE_VITREOUS_TOOL = 160;
	public static final int MANA_PER_DAMAGE_SHEARS = 30;
	public static final int MANA_PER_DAMAGE_BOW = 40;
	public static final float ATTACK_SPEED_HOE = -1;
	public static final float ATTACK_DAMAGE_PICKAXE = 1;
	public static final float ATTACK_SPEED_PICKAXE = -2.8f;
	public static final float ATTACK_DAMAGE_SHOVEL = 1.5f;
	public static final float ATTACK_SPEED_SHOVEL = -3;
	public static final int ATTACK_DAMAGE_SWORD = 3;
	public static final float ATTACK_SPEED_SWORD = -2.4f;
	public static final Item MANASTEEL_HELMET = make(LibItemNames.MANASTEEL_HELMET,
			props -> new ManasteelHelmItem(ArmorType.HELMET, BotaniaArmorMaterials.MANASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item MANASTEEL_CHESTPLATE = make(LibItemNames.MANASTEEL_CHESTPLATE,
			props -> new ManasteelArmorItem(ArmorType.CHESTPLATE, BotaniaArmorMaterials.MANASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item MANASTEEL_LEGGINGS = make(LibItemNames.MANASTEEL_LEGGINGS,
			props -> new ManasteelArmorItem(ArmorType.LEGGINGS, BotaniaArmorMaterials.MANASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item MANASTEEL_BOOTS = make(LibItemNames.MANASTEEL_BOOTS,
			props -> new ManasteelArmorItem(ArmorType.BOOTS, BotaniaArmorMaterials.MANASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item MANASTEEL_PICKAXE = make(LibItemNames.MANASTEEL_PICKAXE,
			props -> new ManasteelPickaxeItem(manaRepairable(props, MANA_PER_DAMAGE_MANASTEEL_TOOL)
					.pickaxe(BotaniaItemTiers.MANASTEEL, ATTACK_DAMAGE_PICKAXE, ATTACK_SPEED_PICKAXE)));
	public static final Item MANASTEEL_SHOVEL = make(LibItemNames.MANASTEEL_SHOVEL,
			props -> new ManasteelShovelItem(BotaniaItemTiers.MANASTEEL, ATTACK_DAMAGE_SHOVEL, ATTACK_SPEED_SHOVEL,
					manaRepairable(props, MANA_PER_DAMAGE_MANASTEEL_TOOL)));
	public static final Item MANASTEEL_AXE = make(LibItemNames.MANASTEEL_AXE,
			props -> new ManasteelAxeItem(BotaniaItemTiers.MANASTEEL, 6, -3.1f,
					manaRepairable(props, MANA_PER_DAMAGE_MANASTEEL_TOOL)));
	public static final Item MANASTEEL_HOE = make(LibItemNames.MANASTEEL_HOE,
			props -> new ManasteelHoeItem(BotaniaItemTiers.MANASTEEL, -BotaniaItemTiers.MANASTEEL.attackDamageBonus(), ATTACK_SPEED_HOE,
					manaRepairable(props, MANA_PER_DAMAGE_MANASTEEL_TOOL)));
	public static final Item MANASTEEL_SWORD = make(LibItemNames.MANASTEEL_SWORD,
			props -> new Item(manaRepairable(props, MANA_PER_DAMAGE_MANASTEEL_TOOL)
					.sword(BotaniaItemTiers.MANASTEEL, ATTACK_DAMAGE_SWORD, ATTACK_SPEED_SWORD)));
	public static final int SHEARS_DURABILITY = 238;
	public static final Item MANASTEEL_SHEARS = make(LibItemNames.MANASTEEL_SHEARS,
			props -> new ManasteelShearsItem(manaRepairable(props, MANA_PER_DAMAGE_SHEARS)
					.durability(SHEARS_DURABILITY * BotaniaItemTiers.MANASTEEL.durability() / ToolMaterial.IRON.durability())
					.component(DataComponents.TOOL, ShearsItem.createToolProperties())));
	public static final int ELEMENTIUM_DURABILITY_FACTOR = 18;
	public static final Item ELEMENTIUM_HELMET = make(LibItemNames.ELEMENTIUM_HELMET,
			props -> new ElementiumHelmItem(ArmorType.HELMET, BotaniaArmorMaterials.ELEMENTIUM, 0.11,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item ELEMENTIUM_CHESTPLATE = make(LibItemNames.ELEMENTIUM_CHESTPLATE,
			props -> new ElementiumArmorItem(ArmorType.CHESTPLATE, BotaniaArmorMaterials.ELEMENTIUM, 0.17,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item ELEMENTIUM_LEGGINGS = make(LibItemNames.ELEMENTIUM_LEGGINGS,
			props -> new ElementiumArmorItem(ArmorType.LEGGINGS, BotaniaArmorMaterials.ELEMENTIUM, 0.15,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item ELEMENTIUM_BOOTS = make(LibItemNames.ELEMENTIUM_BOOTS,
			props -> new ElementiumArmorItem(ArmorType.BOOTS, BotaniaArmorMaterials.ELEMENTIUM, 0.09,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)));
	public static final Item ELEMENTIUM_PICKAXE = make(LibItemNames.ELEMENTIUM_PICKAXE,
			props -> new ElementiumPickaxeItem(manaRepairable(props, MANA_PER_DAMAGE_ELEMENTIUM_TOOL)
					.pickaxe(BotaniaItemTiers.ELEMENTIUM, ATTACK_DAMAGE_PICKAXE, ATTACK_SPEED_PICKAXE)));
	public static final Item ELEMENTIUM_SHOVEL = make(LibItemNames.ELEMENTIUM_SHOVEL,
			props -> new ElementiumShovelItem(BotaniaItemTiers.ELEMENTIUM, ATTACK_DAMAGE_SHOVEL, ATTACK_SPEED_SHOVEL,
					manaRepairable(props, MANA_PER_DAMAGE_ELEMENTIUM_TOOL)));
	public static final Item ELEMENTIUM_AXE = make(LibItemNames.ELEMENTIUM_AXE,
			props -> new ElementiumAxeItem(BotaniaItemTiers.ELEMENTIUM, 6, -3.1f,
					manaRepairable(props, MANA_PER_DAMAGE_ELEMENTIUM_TOOL)));
	public static final Item ELEMENTIUM_HOE = make(LibItemNames.ELEMENTIUM_HOE,
			props -> new ElementiumHoeItem(BotaniaItemTiers.ELEMENTIUM, -BotaniaItemTiers.ELEMENTIUM.attackDamageBonus(), ATTACK_SPEED_HOE,
					manaRepairable(props, MANA_PER_DAMAGE_ELEMENTIUM_TOOL)));
	public static final Item ELEMENTIUM_SWORD = make(LibItemNames.ELEMENTIUM_SWORD,
			props -> new ElementiumSwordItem(BotaniaItemTiers.ELEMENTIUM, ATTACK_DAMAGE_SWORD, ATTACK_SPEED_SWORD,
					manaRepairable(props, MANA_PER_DAMAGE_ELEMENTIUM_TOOL)));
	public static final Item ELEMENTIUM_SHEARS = make(LibItemNames.ELEMENTIUM_SHEARS,
			props -> new ElementiumShearsItem(manaRepairable(props, MANA_PER_DAMAGE_SHEARS)
					.durability(SHEARS_DURABILITY * BotaniaItemTiers.ELEMENTIUM.durability() / ToolMaterial.IRON.durability())
					.component(DataComponents.TOOL, ShearsItem.createToolProperties())));
	public static final int TERRASTEEL_DURABILITY_FACTOR = 34;
	public static final Item TERRASTEEL_HELMET = make(LibItemNames.TERRASTEEL_HELMET,
			props -> new TerrasteelHelmItem(ArmorType.HELMET, BotaniaArmorMaterials.TERRASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)
							.fireResistant()
							.rarity(Rarity.UNCOMMON)));
	public static final Item TERRASTEEL_CHESTPLATE = make(LibItemNames.TERRASTEEL_CHESTPLATE,
			props -> new TerrasteelArmorItem(ArmorType.CHESTPLATE, BotaniaArmorMaterials.TERRASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)
							.fireResistant()
							.rarity(Rarity.UNCOMMON)));
	public static final Item TERRASTEEL_LEGGINGS = make(LibItemNames.TERRASTEEL_LEGGINGS,
			props -> new TerrasteelArmorItem(ArmorType.LEGGINGS, BotaniaArmorMaterials.TERRASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)
							.fireResistant()
							.rarity(Rarity.UNCOMMON)));
	public static final Item TERRASTEEL_BOOTS = make(LibItemNames.TERRASTEEL_BOOTS,
			props -> new TerrasteelArmorItem(ArmorType.BOOTS, BotaniaArmorMaterials.TERRASTEEL,
					manaRepairable(props, MANA_PER_DAMAGE_METAL_ARMOR)
							.fireResistant()
							.rarity(Rarity.UNCOMMON)));
	public static final Item TERRA_SHATTERER = make(LibItemNames.TERRA_SHATTERER,
			props -> new TerraShattererItem(manaRepairable(props, MANA_PER_DAMAGE_TERRASTEEL_TOOL)
					.fireResistant()
					.rarity(Rarity.UNCOMMON)
					.component(BotaniaDataComponents.MAX_MANA, TerraShattererItem.MAX_MANA)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)
					.pickaxe(BotaniaItemTiers.TERRASTEEL, ATTACK_DAMAGE_PICKAXE, ATTACK_SPEED_PICKAXE)));
	public static final Item TERRA_TRUNCATOR = make(LibItemNames.TERRA_TRUNCATOR,
			props -> new TerraTruncatorItem(BotaniaItemTiers.TERRASTEEL, 5, -3,
					manaRepairable(props, MANA_PER_DAMAGE_TERRASTEEL_TOOL)
							.fireResistant()
							.rarity(Rarity.UNCOMMON)));
	public static final Item TERRA_BLADE = make(LibItemNames.TERRA_BLADE,
			props -> new TerraBladeItem(manaRepairable(props, MANA_PER_DAMAGE_TERRASTEEL_TOOL)
					.fireResistant()
					.rarity(Rarity.UNCOMMON)
					.sword(BotaniaItemTiers.TERRASTEEL, ATTACK_DAMAGE_SWORD, ATTACK_SPEED_SWORD)));
	public static final Item STARCALLER = make(LibItemNames.STARCALLER,
			props -> new StarcallerItem(manaRepairable(props, MANA_PER_DAMAGE_UPGRADED_SWORD)
					.rarity(Rarity.UNCOMMON)
					.sword(BotaniaItemTiers.TERRASTEEL, ATTACK_DAMAGE_SWORD, ATTACK_SPEED_SWORD)));
	public static final Item THUNDERCALLER = make(LibItemNames.THUNDERCALLER,
			props -> new ThundercallerItem(manaRepairable(props, MANA_PER_DAMAGE_UPGRADED_SWORD)
					.rarity(Rarity.UNCOMMON)
					.sword(BotaniaItemTiers.TERRASTEEL, ATTACK_DAMAGE_SWORD, 1.5f)));
	public static final int MANAWEAVE_DURABILITY_FACTOR = 5;
	public static final Item MANAWEAVE_HELMET = make(LibItemNames.MANAWEAVE_HELMET,
			props -> new ManaweaveHelmItem(ArmorType.HELMET, BotaniaArmorMaterials.MANAWEAVE,
					manaRepairable(props, MANA_PER_DAMAGE_MANAWEAVE_ARMOR)));
	public static final Item MANAWEAVE_CHESTPLATE = make(LibItemNames.MANAWEAVE_CHESTPLATE,
			props -> new ManaweaveArmorItem(ArmorType.CHESTPLATE, BotaniaArmorMaterials.MANAWEAVE,
					manaRepairable(props, MANA_PER_DAMAGE_MANAWEAVE_ARMOR)));
	public static final Item MANAWEAVE_LEGGINGS = make(LibItemNames.MANAWEAVE_LEGGINGS,
			props -> new ManaweaveArmorItem(ArmorType.LEGGINGS, BotaniaArmorMaterials.MANAWEAVE,
					manaRepairable(props, MANA_PER_DAMAGE_MANAWEAVE_ARMOR)));
	public static final Item MANAWEAVE_BOOTS = make(LibItemNames.MANAWEAVE_BOOTS,
			props -> new ManaweaveArmorItem(ArmorType.BOOTS, BotaniaArmorMaterials.MANAWEAVE,
					manaRepairable(props, MANA_PER_DAMAGE_MANAWEAVE_ARMOR)));
	public static final Item SOULSCRIBE = make(LibItemNames.SOULSCRIBE,
			props -> new SoulscribeItem(props.sword(BotaniaItemTiers.MANASTEEL, ATTACK_DAMAGE_SWORD, -1.25f).durability(69)));
	public static final Item VITREOUS_PICKAXE = make(LibItemNames.VITREOUS_PICKAXE,
			props -> new VitreousPickaxeItem(manaRepairable(props, MANA_PER_DAMAGE_VITREOUS_TOOL)
					.pickaxe(BotaniaItemTiers.VITREOUS, ATTACK_DAMAGE_PICKAXE, ATTACK_SPEED_PICKAXE)
					.component(DataComponents.TOOL, BotaniaItemTiers.vitreousTool())));
	public static final int BOW_DURABILITY = 384; // like vanilla
	public static final Item LIVINGWOOD_BOW = make(LibItemNames.LIVINGWOOD_BOW,
			props -> new BowItem(manaRepairable(props, MANA_PER_DAMAGE_BOW)
					.durability(BOW_DURABILITY)));
	public static final Item CRYSTAL_BOW = make(LibItemNames.CRYSTAL_BOW,
			props -> new CrystalBowItem(manaRepairable(props, MANA_PER_DAMAGE_BOW)
					.durability(BOW_DURABILITY)));
	public static final int CHAKRAM_STACK_SIZE = 6;
	public static final Item THORN_CHAKRAM = make(LibItemNames.THORN_CHAKRAM,
			props -> new ThornChakramItem(props
					.stacksTo(CHAKRAM_STACK_SIZE)
					.rarity(Rarity.UNCOMMON)));
	public static final Item FLARE_CHAKRAM = make(LibItemNames.FLARE_CHAKRAM,
			props -> new ThornChakramItem(props
					.stacksTo(CHAKRAM_STACK_SIZE)
					.rarity(Rarity.UNCOMMON)));

	// Misc tools
	public static final Item MANA_TABLET = make(LibItemNames.MANA_TABLET,
			props -> new ManaTabletItem(props.stacksTo(1)
					.component(BotaniaDataComponents.MAX_MANA, ManaTabletItem.DEFAULT_MAX_MANA)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_DRAIN_MANA_TO_POOL, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_ACCEPT_MANA_FROM_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)));
	public static final Item MANA_MIRROR = make(LibItemNames.MANA_MIRROR,
			props -> new ManaMirrorItem(props.stacksTo(1)
					.rarity(Rarity.UNCOMMON)
					.component(BotaniaDataComponents.MAX_MANA, 0)
					.component(BotaniaDataComponents.MANA_BACKLOG, 0)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)));
	public static final Item MANA_BLASTER = make(LibItemNames.MANA_BLASTER,
			props -> new ManaBlasterItem(props.stacksTo(1)));
	public static final Item LENS_CLIP = make(LibItemNames.LENS_CLIP,
			props -> new Item(props.stacksTo(1)));
	public static final HornItem HORN_OF_THE_WILD = make(LibItemNames.HORN_OF_THE_WILD,
			props -> new HornOfTheWildItem(props.stacksTo(1)));
	public static final HornItem HORN_OF_THE_CANOPY = make(LibItemNames.HORN_OF_THE_CANOPY,
			props -> new HornOfTheCanopyItem(props.stacksTo(1)));
	public static final HornItem HORN_OF_THE_COVERING = make(LibItemNames.HORN_OF_THE_COVERING,
			props -> new HornOfTheCoveringItem(props.stacksTo(1)));
	public static final Item VINE_BALL = make(LibItemNames.VINE_BALL,
			VineBallItem::new);
	public static final Item LIVINGWOOD_SLINGSHOT = make(LibItemNames.LIVINGWOOD_SLINGSHOT,
			props -> new LivingwoodSlingshotItem(props.stacksTo(1)));
	public static final Item EXTRAPOLATED_BUCKET = make(LibItemNames.EXTRAPOLATED_BUCKET,
			props -> new ExtrapolatedBucketItem(props.stacksTo(1)));
	public static final Item LIFE_AGGREGATOR = make(LibItemNames.LIFE_AGGREGATOR,
			props -> new LifeAggregatorItem(props.stacksTo(1).rarity(Rarity.RARE)));
	public static final Item HAND_OF_ENDER = make(LibItemNames.HAND_OF_ENDER,
			props -> new EnderHandItem(props.stacksTo(1)));
	public static final Item ASSEMBLY_HALO = make(LibItemNames.ASSEMBLY_HALO,
			props -> new AssemblyHaloItem(props.stacksTo(1)));
	public static final Item MANUFACTORY_HALO = make(LibItemNames.MANUFACTORY_HALO,
			props -> new ManufactoryHaloItem(props.stacksTo(1)
					.component(BotaniaDataComponents.ACTIVE, Unit.INSTANCE)));
	public static final Item SPELLBINDING_CLOTH = make(LibItemNames.SPELLBINDING_CLOTH,
			props -> new SpellbindingClothItem(XplatAbstractions.INSTANCE.noRepairOnNeoForge(props.stacksTo(1)
					.durability(35))));
	public static final FlowerPouchItem FLOWER_POUCH = make(LibItemNames.FLOWER_POUCH,
			props -> new FlowerPouchItem(props.stacksTo(1)
					.component(BotaniaDataComponents.ITEM_TAGS,
							List.of(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS, BotaniaTags.Items.TALL_MYSTICAL_FLOWERS))));
	public static final PetalPouchItem PETAL_POUCH = make(LibItemNames.PETAL_POUCH,
			props -> new PetalPouchItem(props.stacksTo(1)
					.component(BotaniaDataComponents.ACTIVE, Unit.INSTANCE)
					.component(BotaniaDataComponents.ITEM_TAGS,
							List.of(BotaniaTags.Items.PETALS, BotaniaTags.Items.SHIMMERING_MUSHROOMS))
					.component(BotaniaDataComponents.CRAFTABLE_ITEM_TAGS,
							List.of(BotaniaTags.Items.SMALL_MYSTICAL_FLOWERS, BotaniaTags.Items.TALL_MYSTICAL_FLOWERS))));
	public static final Item BLACK_HOLE_TALISMAN = make(LibItemNames.BLACK_HOLE_TALISMAN,
			props -> new BlackHoleTalismanItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item STONE_OF_TEMPERANCE = make(LibItemNames.STONE_OF_TEMPERANCE,
			props -> new StoneOfTemperanceItem(props.stacksTo(1)));
	public static final Item WATER_BOWL = make(LibItemNames.WATER_BOWL,
			props -> new WaterBowlItem(props.stacksTo(1).craftRemainder(Items.BOWL)));
	public static final Item CACOPHONIUM = make(LibItemNames.CACOPHONIUM,
			props -> new CacophoniumItem(props.stacksTo(1)));
	public static final Item SLIME_IN_A_BOTTLE = make(LibItemNames.SLIME_IN_A_BOTTLE,
			props -> new SlimeInABottleItem(props.stacksTo(1)));
	public static final Item WORLDSHAPERS_SEXTANT = make(LibItemNames.WORLDSHAPERS_SEXTANT,
			props -> new SextantItem(props.stacksTo(1)));
	public static final Item WORLDSHAPERS_ASTROLABE = make(LibItemNames.WORLDSHAPERS_ASTROLABE,
			props -> new AstrolabeItem(props.stacksTo(1).rarity(Rarity.RARE)));
	public static final Item TRINKET_CASE = make(LibItemNames.TRINKET_CASE,
			props -> new BaubleBoxItem(props.stacksTo(1)));

	// Baubles / trinkets / curios / etc.
	public static final Item BAND_OF_MANA = make(LibItemNames.BAND_OF_MANA,
			props -> new BandOfManaItem(props.stacksTo(1)
					.component(BotaniaDataComponents.MAX_MANA, BandOfManaItem.DEFAULT_MAX_MANA)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_DRAIN_MANA_TO_POOL, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_ACCEPT_MANA_FROM_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)));
	public static final Item GREATER_BAND_OF_MANA = make(LibItemNames.GREATER_BAND_OF_MANA,
			props -> new BandOfManaItem(props.stacksTo(1)
					.rarity(Rarity.UNCOMMON)
					.fireResistant()
					.component(BotaniaDataComponents.MAX_MANA, BandOfManaItem.DEFAULT_GREATER_MAX_MANA)
					.component(BotaniaDataComponents.CAN_PROVIDE_MANA_TO_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_DRAIN_MANA_TO_POOL, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_ACCEPT_MANA_FROM_ITEMS, Unit.INSTANCE)
					.component(BotaniaDataComponents.CAN_RECEIVE_MANA_FROM_POOL, Unit.INSTANCE)));
	public static final Item BAND_OF_AURA = make(LibItemNames.BAND_OF_AURA,
			props -> new BandOfAuraItem(10, props.stacksTo(1)));
	public static final Item GREATER_BAND_OF_AURA = make(LibItemNames.GREATER_BAND_OF_AURA,
			props -> new BandOfAuraItem(2, props.stacksTo(1)
					.rarity(Rarity.UNCOMMON)
					.fireResistant()));
	public static final Item RING_OF_MAGNETIZATION = make(LibItemNames.RING_OF_MAGNETIZATION,
			props -> new RingOfMagnetizationItem(props.stacksTo(1)
					.component(BotaniaDataComponents.RANGE, RingOfMagnetizationItem.DEFAULT_RANGE)));
	public static final Item GREATER_RING_OF_MAGNETIZATION = make(LibItemNames.GREATER_RING_OF_MAGNETIZATION,
			props -> new RingOfMagnetizationItem(props.stacksTo(1)
					.rarity(Rarity.UNCOMMON)
					.fireResistant()
					.component(BotaniaDataComponents.RANGE, RingOfMagnetizationItem.DEFAULT_GREATER_RANGE)));
	public static final Item RING_OF_CHORDATA = make(LibItemNames.RING_OF_CHORDATA,
			props -> new RingOfChordataItem(props.stacksTo(1)
					.rarity(Rarity.UNCOMMON)));
	public static final Item RING_OF_CORRECTION = make(LibItemNames.RING_OF_CORRECTION,
			props -> new RingOfCorrectionItem(props.stacksTo(1)));
	public static final Item RING_OF_DEXTEROUS_MOTION = make(LibItemNames.RING_OF_DEXTEROUS_MOTION,
			props -> new RingOfDexterousMotionItem(props.stacksTo(1)));
	public static final Item RING_OF_THE_MANTLE = make(LibItemNames.RING_OF_THE_MANTLE,
			props -> new RingOfTheMantleItem(props.stacksTo(1)));
	public static final Item GREAT_FAIRY_RING = make(LibItemNames.GREAT_FAIRY_RING,
			props -> new GreatFairyRingItem(props.stacksTo(1)));
	public static final Item RING_OF_FAR_REACH = make(LibItemNames.RING_OF_FAR_REACH,
			props -> new RingOfFarReachItem(props.stacksTo(1)));
	public static final Item SOJOURNERS_SASH = make(LibItemNames.SOJOURNORS_SASH,
			props -> new SojournersSashItem(props.stacksTo(1)));
	public static final Item GLOBETROTTERS_SASH = make(LibItemNames.GLOBETROTTERS_SASH,
			props -> new GlobetrottersSashItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item PLANESTRIDERS_SASH = make(LibItemNames.PLANESTRIDERS_SASH,
			props -> new PlanestridersSashItem(props.stacksTo(1)));
	public static final Item TECTONIC_GIRDLE = make(LibItemNames.TECTONIC_GIRDLE,
			props -> new TectonicGirdleItem(props.stacksTo(1)));
	public static final Item SNOWFLAKE_PENDANT = make(LibItemNames.SNOWFLAKE_PENDANT,
			props -> new SnowflakePendantItem(props.stacksTo(1)));
	public static final Item PYROCLAST_PENDANT = make(LibItemNames.PYROCLAST_PENDANT,
			props -> new PyroclastPendantItem(props.stacksTo(1)));
	public static final Item CRIMSON_PENDANT = make(LibItemNames.CRIMSON_PENDANT,
			props -> new CrimsonPendantItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item CIRRUS_AMULET = make(LibItemNames.CIRRUS_AMULET,
			props -> new CirrusAmuletItem(props.stacksTo(1)));
	public static final Item NIMBUS_AMULET = make(LibItemNames.NIMBUS_AMULET,
			props -> new NimbusAmuletItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item CLOAK_OF_VIRTUE = make(LibItemNames.CLOAK_OF_VIRTUE,
			props -> new CloakOfVirtueItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item CLOAK_OF_SIN = make(LibItemNames.CLOAK_OF_SIN,
			props -> new CloakOfSinItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item CLOAK_OF_BALANCE = make(LibItemNames.CLOAK_OF_BALANCE,
			props -> new CloakOfBalanceItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item INVISIBILITY_CLOAK = make(LibItemNames.INVISIBILITY_CLOAK,
			props -> new InvisibilityCloakItem(props.stacksTo(1)));
	public static final Item THIRD_EYE = make(LibItemNames.THIRD_EYE,
			props -> new ThirdEyeItem(props.stacksTo(1)));
	public static final Item MANASEER_MONOCLE = make(LibItemNames.MANASEER_MONOCLE,
			props -> new ManaseerMonocleItem(props.stacksTo(1)));
	public static final Item TINY_PLANET = make(LibItemNames.TINY_PLANET,
			props -> new TinyPlanetItem(props.stacksTo(1)));
	public static final Item BENEVOLENT_GODDESS_CHARM = make(LibItemNames.BENEVOLENT_GODDESS_CHARM,
			props -> new BenevolentGoddessCharmItem(props.stacksTo(1)));
	public static final Item CHARM_OF_THE_DIVA = make(LibItemNames.CHARM_OF_THE_DIVA,
			props -> new CharmOfTheDivaItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item THE_SPECTATOR = make(LibItemNames.THE_SPECTATOR,
			props -> new SpectatorItem(props.stacksTo(1)));
	public static final Item FLUGEL_TIARA = make(LibItemNames.FLUGEL_TIARA,
			props -> new FlugelTiaraItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));

	// Misc
	public static final Item BISCUIT_OF_TOTALITY = make(LibItemNames.BISCUIT_OF_TOTALITY,
			props -> new Item(props
					.food(new FoodProperties.Builder()
							.nutrition(0)
							.saturationModifier(0.1F)
							.build(),
							Consumables.defaultFood()
									.onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SATURATION, 20, 0)))
									.build())));
	public static final Item MANA_IN_A_BOTTLE = make(LibItemNames.MANA_IN_A_BOTTLE,
			props -> new BottledManaItem(props.stacksTo(1)
					// Mark as food just to fool foxes into using it
					.food(new FoodProperties.Builder().alwaysEdible().build())
					.component(BotaniaDataComponents.REMAINING_USES, BottledManaItem.SWIGS)));
	public static final Item SHARD_OF_LAPUTA = make(LibItemNames.SHARD_OF_LAPUTA,
			props -> new LaputaShardItem(props.stacksTo(1)
					.component(BotaniaDataComponents.SHARD_LEVEL, 1)
					.rarity(Rarity.RARE)));
	public static final Item NECRODERMAL_VIRUS = make(LibItemNames.NECRODERMAL_VIRUS,
			props -> new EquestrianVirusItem(props
					.rarity(Rarity.UNCOMMON)));
	public static final Item NULLODERMAL_VIRUS = make(LibItemNames.NULLODERMAL_VIRUS,
			props -> new EquestrianVirusItem(props
					.rarity(Rarity.UNCOMMON)));
	public static final Item MANA_SPARK = make(LibItemNames.MANA_SPARK, ManaSparkItem::new);
	public static final Item SPARK_AUGMENT_DISPERSIVE = make(LibItemNames.SPARK_AUGMENT + "_dispersive",
			props -> new Item(props
					.component(BotaniaDataComponents.AUGMENT_ICON, botaniaRL("item/spark_augment_rune_dispersive"))
					.component(BotaniaDataComponents.AUGMENT_ID, (byte) 1)));
	public static final Item SPARK_AUGMENT_DOMINANT = make(LibItemNames.SPARK_AUGMENT + "_dominant",
			props -> new Item(props
					.component(BotaniaDataComponents.AUGMENT_ICON, botaniaRL("item/spark_augment_rune_dominant"))
					.component(BotaniaDataComponents.AUGMENT_ID, (byte) 2)));
	public static final Item SPARK_AUGMENT_RECESSIVE = make(LibItemNames.SPARK_AUGMENT + "_recessive",
			props -> new Item(props
					.component(BotaniaDataComponents.AUGMENT_ICON, botaniaRL("item/spark_augment_rune_recessive"))
					.component(BotaniaDataComponents.AUGMENT_ID, (byte) 3)));
	public static final Item SPARK_AUGMENT_ISOLATED = make(LibItemNames.SPARK_AUGMENT + "_isolated",
			props -> new Item(props
					.component(BotaniaDataComponents.AUGMENT_ICON, botaniaRL("item/spark_augment_rune_isolated"))
					.component(BotaniaDataComponents.AUGMENT_ID, (byte) 4)));
	public static final Item CORPOREA_SPARK = make(LibItemNames.CORPOREA_SPARK,
			CorporeaSparkItem::new);
	public static final Item MASTER_CORPOREA_SPARK = make(LibItemNames.MASTER_CORPOREA_SPARK,
			CorporeaSparkItem::new);
	public static final Item CREATIVE_CORPOREA_SPARK = make(LibItemNames.CREATIVE_CORPOREA_SPARK,
			props -> new CorporeaSparkItem(props
					.rarity(Rarity.EPIC)));
	public static final Item BLACK_LOTUS = make(LibItemNames.BLACK_LOTUS,
			props -> new BlackLotusItem(props
					.rarity(Rarity.UNCOMMON)));
	public static final Item BLACKER_LOTUS = make(LibItemNames.BLACKER_LOTUS,
			props -> new BlackLotusItem(props
					.rarity(Rarity.RARE)));
	public static final Item WORLD_SEED = make(LibItemNames.WORLD_SEED,
			WorldSeedItem::new);
	public static final Item PHANTOM_INK = make(LibItemNames.PHANTOM_INK,
			PhantomInkItem::new);
	public static final Item MANA_POOL_MINECART = make(LibItemNames.MANA_POOL_MINECART,
			props -> new ManaPoolMinecartItem(props.stacksTo(1)));
	public static final Item RESOLUTE_IVY = make(LibItemNames.RESOLUTE_IVY,
			ResoluteIvyItem::new);
	public static final Item CRAFTING_PLACEHOLDER = make(LibItemNames.CRAFTING_PLACEHOLDER,
			SelfReturningItem::new);
	public static final Item CRAFTING_PATTERN_1_1 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "1_1",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_1, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_2_2 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "2_2",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_2, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_1_2 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "1_2",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_2, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_2_1 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "2_1",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_1, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_1_3 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "1_3",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_1_3, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_3_1 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "3_1",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_3_1, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_2_3 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "2_3",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_2_3, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_3_2 = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "3_2",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_3_2, props.stacksTo(1)));
	public static final Item CRAFTING_PATTERN_DONUT = make(LibItemNames.CRAFTING_PATTERN_PREFIX + "donut",
			props -> new CraftingPatternItem(CraftyCratePattern.CRAFTY_DONUT, props.stacksTo(1)));

	// Guardian of Gaia drops
	public static final Item DICE_OF_FATE = make(LibItemNames.DICE_OF_FATE,
			props -> new DiceOfFateItem(props.stacksTo(1)
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item FRUIT_OF_GRISAIA = make(LibItemNames.THE_FRUIT_OF_GRISAIA,
			props -> new FruitOfGrisaiaItem(props.stacksTo(1)
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item KEY_OF_THE_KINGS_LAW = make(LibItemNames.KEY_OF_THE_KINGS_LAW,
			props -> new KeyOfTheKingsLawItem(props.stacksTo(1)
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item EYE_OF_THE_FLUGEL = make(LibItemNames.EYE_OF_THE_FLUGEL,
			props -> new EyeOfTheFlugelItem(props.stacksTo(1)
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item RING_OF_THOR = make(LibItemNames.RING_OF_THOR,
			props -> new RingOfThorItem(props.stacksTo(1)
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item RING_OF_ODIN = make(LibItemNames.RING_OF_ODIN,
			props -> new RingOfOdinItem(props.stacksTo(1)
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item RING_OF_LOKI = make(LibItemNames.RING_OF_LOKI,
			props -> new RingOfLokiItem(props.stacksTo(1)
					.fireResistant()
					.rarity(Rarity.EPIC)));
	public static final Item SCATHED_MUSIC_DISC_1 = make(LibItemNames.SCATHED_MUSIC_DISC_1,
			props -> new Item(props.stacksTo(1)
					.rarity(Rarity.RARE)
					.jukeboxPlayable(BotaniaJukeboxSongs.GAIA_MUSIC_1)));
	public static final Item SCATHED_MUSIC_DISC_2 = make(LibItemNames.SCATHED_MUSIC_DISC_2,
			props -> new Item(props.stacksTo(1)
					.rarity(Rarity.RARE)
					.jukeboxPlayable(BotaniaJukeboxSongs.GAIA_MUSIC_2)));
	public static final Item WILL_OF_AHRIM = make(LibItemNames.ANCIENT_WILL_PREFIX + "ahrim",
			props -> new AncientWillItem(AncientWillContainer.AncientWillType.AHRIM, props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_DHAROK = make(LibItemNames.ANCIENT_WILL_PREFIX + "dharok",
			props -> new AncientWillItem(AncientWillContainer.AncientWillType.DHAROK, props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_GUTHAN = make(LibItemNames.ANCIENT_WILL_PREFIX + "guthan",
			props -> new AncientWillItem(AncientWillContainer.AncientWillType.GUTHAN, props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_TORAG = make(LibItemNames.ANCIENT_WILL_PREFIX + "torag",
			props -> new AncientWillItem(AncientWillContainer.AncientWillType.TORAG, props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_VERAC = make(LibItemNames.ANCIENT_WILL_PREFIX + "verac",
			props -> new AncientWillItem(AncientWillContainer.AncientWillType.VERAC, props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item WILL_OF_KARIL = make(LibItemNames.ANCIENT_WILL_PREFIX + "karil",
			props -> new AncientWillItem(AncientWillContainer.AncientWillType.KARIL, props.stacksTo(1)
					.rarity(Rarity.RARE)));
	public static final Item THE_PINKINATOR = make(LibItemNames.THE_PINKINATOR,
			props -> new PinkinatorItem(props.stacksTo(1)
					.rarity(Rarity.RARE)));

	// Brewing
	public static final Item MANAGLASS_VIAL = make(LibItemNames.MANAGLASS_VIAL,
			VialItem::new);
	public static final Item ALFGLASS_FLASK = make(LibItemNames.ALFGLASS_FLASK,
			FlaskItem::new);
	public static final BaseBrewItem BREW_VIAL = make(LibItemNames.BREW_VIAL,
			props -> new BaseBrewItem(32, () -> MANAGLASS_VIAL, props.stacksTo(1)
					.component(BotaniaDataComponents.MAX_USES, BaseBrewItem.DEFAULT_USES_VIAL)));
	public static final BaseBrewItem BREW_FLASK = make(LibItemNames.BREW_FLASK,
			props -> new BaseBrewItem(24, () -> ALFGLASS_FLASK, props.stacksTo(1)
					.component(BotaniaDataComponents.MAX_USES, BaseBrewItem.DEFAULT_USES_FLASK)));
	public static final Item TAINTED_BLOOD_PENDANT = make(LibItemNames.TAINTED_BLOOD_PENDANT,
			props -> new TaintedBloodPendantItem(props.stacksTo(1)));
	public static final Item INCENSE_STICK = make(LibItemNames.INCENSE_STICK,
			props -> new IncenseStickItem(props.stacksTo(1)));

	// Cosmetics
	public static final Item COSMETIC_BLACK_BOWTIE = make(LibItemNames.COSMETIC_PREFIX + "black_bowtie",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLACK_BOWTIE, props.stacksTo(1)));
	public static final Item COSMETIC_BLACK_TIE = make(LibItemNames.COSMETIC_PREFIX + "black_tie",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLACK_TIE, props.stacksTo(1)));
	public static final Item COSMETIC_RED_GLASSES = make(LibItemNames.COSMETIC_PREFIX + "red_glasses",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.RED_GLASSES, props.stacksTo(1)));
	public static final Item COSMETIC_PUFFY_SCARF = make(LibItemNames.COSMETIC_PREFIX + "puffy_scarf",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.PUFFY_SCARF, props.stacksTo(1)));
	public static final Item COSMETIC_ENGINEER_GOGGLES = make(LibItemNames.COSMETIC_PREFIX + "engineer_goggles",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ENGINEER_GOGGLES, props.stacksTo(1)));
	public static final Item COSMETIC_EYEPATCH = make(LibItemNames.COSMETIC_PREFIX + "eyepatch",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.EYEPATCH, props.stacksTo(1)));
	public static final Item COSMETIC_WICKED_EYEPATCH = make(LibItemNames.COSMETIC_PREFIX + "wicked_eyepatch",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.WICKED_EYEPATCH, props.stacksTo(1)));
	public static final Item COSMETIC_RED_RIBBONS = make(LibItemNames.COSMETIC_PREFIX + "red_ribbons",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.RED_RIBBONS, props.stacksTo(1)));
	public static final Item COSMETIC_PINK_FLOWER_BUD = make(LibItemNames.COSMETIC_PREFIX + "pink_flower_bud",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.PINK_FLOWER_BUD, props.stacksTo(1)));
	public static final Item COSMETIC_POLKA_DOTTED_BOWS = make(LibItemNames.COSMETIC_PREFIX + "polka_dotted_bows",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.POLKA_DOTTED_BOWS, props.stacksTo(1)));
	public static final Item COSMETIC_BLUE_BUTTERFLY = make(LibItemNames.COSMETIC_PREFIX + "blue_butterfly",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BLUE_BUTTERFLY, props.stacksTo(1)));
	public static final Item COSMETIC_CAT_EARS = make(LibItemNames.COSMETIC_PREFIX + "cat_ears",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.CAT_EARS, props.stacksTo(1)));
	public static final Item COSMETIC_WITCH_PIN = make(LibItemNames.COSMETIC_PREFIX + "witch_pin",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.WITCH_PIN, props.stacksTo(1)));
	public static final Item COSMETIC_DEVIL_TAIL = make(LibItemNames.COSMETIC_PREFIX + "devil_tail",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.DEVIL_TAIL, props.stacksTo(1)));
	public static final Item COSMETIC_KAMUI_EYE = make(LibItemNames.COSMETIC_PREFIX + "kamui_eye",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.KAMUI_EYE, props.stacksTo(1)));
	public static final Item COSMETIC_GOOGLY_EYES = make(LibItemNames.COSMETIC_PREFIX + "googly_eyes",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.GOOGLY_EYES, props.stacksTo(1)));
	public static final Item COSMETIC_FOUR_LEAFED_CLOVER = make(LibItemNames.COSMETIC_PREFIX + "four_leaf_clover",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.FOUR_LEAF_CLOVER, props.stacksTo(1)));
	public static final Item COSMETIC_CLOCK_EYE = make(LibItemNames.COSMETIC_PREFIX + "clock_eye",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.CLOCK_EYE, props.stacksTo(1)));
	public static final Item COSMETIC_UNICORN_HORN = make(LibItemNames.COSMETIC_PREFIX + "unicorn_horn",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.UNICORN_HORN, props.stacksTo(1)));
	public static final Item COSMETIC_DEVIL_HORNS = make(LibItemNames.COSMETIC_PREFIX + "devil_horns",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.DEVIL_HORNS, props.stacksTo(1)));
	public static final Item COSMETIC_HYPER_PLUS = make(LibItemNames.COSMETIC_PREFIX + "hyper_plus",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.HYPER_PLUS, props.stacksTo(1)));
	public static final Item COSMETIC_BOTANIST_EMBLEM = make(LibItemNames.COSMETIC_PREFIX + "botanist_emblem",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.BOTANIST_EMBLEM, props.stacksTo(1)));
	public static final Item COSMETIC_ANCIENT_MASK = make(LibItemNames.COSMETIC_PREFIX + "ancient_mask",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ANCIENT_MASK, props.stacksTo(1)));
	public static final Item COSMETIC_EERIE_MASK = make(LibItemNames.COSMETIC_PREFIX + "eerie_mask",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.EERIE_MASK, props.stacksTo(1)));
	public static final Item COSMETIC_ALIEN_ANTENNA = make(LibItemNames.COSMETIC_PREFIX + "alien_antenna",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ALIEN_ANTENNA, props.stacksTo(1)));
	public static final Item COSMETIC_ANAGLYPH_GLASSES = make(LibItemNames.COSMETIC_PREFIX + "anaglyph_glasses",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ANAGLYPH_GLASSES, props.stacksTo(1)));
	public static final Item COSMETIC_ORANGE_SHADES = make(LibItemNames.COSMETIC_PREFIX + "orange_shades",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.ORANGE_SHADES, props.stacksTo(1)));
	public static final Item COSMETIC_GROUCHO_GLASSES = make(LibItemNames.COSMETIC_PREFIX + "groucho_glasses",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.GROUCHO_GLASSES, props.stacksTo(1)));
	public static final Item COSMETIC_THICK_EYEBROWS = make(LibItemNames.COSMETIC_PREFIX + "thick_eyebrows",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.THICK_EYEBROWS, props.stacksTo(1)));
	public static final Item COSMETIC_LUSITANIC_SHIELD = make(LibItemNames.COSMETIC_PREFIX + "lusitanic_shield",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.LUSITANIC_SHIELD, props.stacksTo(1)));
	public static final Item COSMETIC_TINY_POTATO_MASK = make(LibItemNames.COSMETIC_PREFIX + "tiny_potato_mask",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.TINY_POTATO_MASK, props.stacksTo(1)));
	public static final Item COSMETIC_QUESTGIVER_MARK = make(LibItemNames.COSMETIC_PREFIX + "questgiver_mark",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.QUESTGIVER_MARK, props.stacksTo(1)));
	public static final Item COSMETIC_THINKING_HAND = make(LibItemNames.COSMETIC_PREFIX + "thinking_hand",
			props -> new CosmeticBaubleItem(CosmeticBaubleItem.Variant.THINKING_HAND, props.stacksTo(1)));

	// Banner patterns
	public static final Item BANNER_PATTERN_BOTANIA = make("botania_banner_pattern",
			props -> new Item(bannerPattern(props.rarity(Rarity.UNCOMMON), BotaniaTags.BannerPatterns.PATTERN_ITEM_BOTANIA)));
	public static final Item BANNER_PATTERN_MATERIALS = make("materials_banner_pattern",
			props -> new Item(bannerPattern(props, BotaniaTags.BannerPatterns.PATTERN_ITEM_MATERIALS)));
	public static final Item BANNER_PATTERN_SPARK_AUGMENTS = make("spark_augments_banner_pattern",
			props -> new Item(bannerPattern(props, BotaniaTags.BannerPatterns.PATTERN_ITEM_SPARK_AUGMENTS)));
	public static final Item BANNER_PATTERN_TOOLS = make("tools_banner_pattern",
			props -> new Item(bannerPattern(props, BotaniaTags.BannerPatterns.PATTERN_ITEM_TOOLS)));

	private static Item.Properties bannerPattern(Item.Properties properties, TagKey<BannerPattern> patterns) {
		return properties.stacksTo(1).delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS,
				registries -> registries.lookupOrThrow(Registries.BANNER_PATTERN).getOrThrow(patterns));
	}

	public static final MenuType<TrinketCaseMenu> TRINKET_CASE_MENU_TYPE =
			XplatAbstractions.INSTANCE.createMenuType(TrinketCaseMenu::new, ByteBufCodecs.BOOL);
	public static final MenuType<ColoredContentsPouchMenu> COLORED_CONTENTS_POUCH_MENU_TYPE =
			XplatAbstractions.INSTANCE.createMenuType(ColoredContentsPouchMenu::new, ByteBufCodecs.BOOL);
	public static final MenuType<HandOfEnderMenu> HAND_OF_ENDER_MENU_TYPE =
			XplatAbstractions.INSTANCE.createMenuType(HandOfEnderMenu::new, ByteBufCodecs.BOOL);

	public static final CauldronInteraction MANA_POOL_INTERACTION = BotaniaItems::cauldronUndyeManaPool;
	public static final CauldronInteraction MANA_LENS_INTERACTION = BotaniaItems::cauldronUndyeManaLens;
	public static final CauldronInteraction PHANTOM_INK_INTERACTION = BotaniaItems::cauldronRemovePhantomInk;

	private static InteractionResult cauldronUndyeManaPool(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, ItemStack stack) {
		Block block = Block.byItem(stack.getItem());
		if (!(block instanceof ManaPoolBlock poolBlock)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (!level.isClientSide()) {
			ManaPoolBlock undyedPool = ManaPoolBlock.getUndyedBlock(poolBlock);
			ItemStack itemstack = stack.transmuteCopy(undyedPool, 1);
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, itemstack, false));
			player.awardStat(BotaniaStats.MANA_POOLS_CLEANED);
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}
		return InteractionResult.SUCCESS;
	}

	private static InteractionResult cauldronUndyeManaLens(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, ItemStack stack) {
		if (!LensItem.isLensTinted(stack)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (!level.isClientSide()) {
			stack.remove(BotaniaDataComponents.LENS_RAINBOW_TINT);
			stack.remove(BotaniaDataComponents.LENS_TINT);
			player.awardStat(BotaniaStats.MANA_LENSES_CLEANED);
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}
		return InteractionResult.SUCCESS;
	}

	private static InteractionResult cauldronRemovePhantomInk(BlockState state, Level level, BlockPos pos,
			Player player, InteractionHand hand, ItemStack stack) {
		if (!(stack.getItem() instanceof PhantomInkable inkable) || !inkable.hasPhantomInk(stack)) {
			return InteractionResult.TRY_WITH_EMPTY_HAND;
		}
		if (!level.isClientSide()) {
			inkable.setPhantomInk(stack, false);
			player.awardStat(BotaniaStats.PHANTOM_INK_CLEANED);
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}
		return InteractionResult.SUCCESS;
	}

	private static <T extends Item> T make(String name, Function<Item.Properties, T> factory) {
		T item = factory.apply(properties(botaniaRL(name)));
		var old = ALL.put(name, item);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate name: " + name);
		}
		return item;
	}

	private static MysticalPetalItem makePetal(DyeColor dyeColor) {
		return make(dyeColor.getName() + LibItemNames.MYSTICAL_PETAL_SUFFIX,
				props -> new MysticalPetalItem(BotaniaBlocks.getBuriedPetal(dyeColor), dyeColor, props));
	}

	public static Item.Properties properties(Identifier id) {
		return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id));
	}

	private static Item.Properties manaRepairable(Item.Properties properties, int manaPerDamage) {
		return properties.component(BotaniaDataComponents.MANA_REPAIR, new ManaRepair(manaPerDamage, 2 * manaPerDamage));
	}

	public static void registerItems(BiConsumer<Item, Identifier> r) {
		for (var e : ALL.entrySet()) {
			r.accept(e.getValue(), botaniaRL(e.getKey()));
		}
	}

	public static void registerMenuTypes(BiConsumer<MenuType<?>, Identifier> consumer) {
		consumer.accept(TRINKET_CASE_MENU_TYPE, botaniaRL(LibItemNames.TRINKET_CASE));
		consumer.accept(COLORED_CONTENTS_POUCH_MENU_TYPE, botaniaRL(LibItemNames.FLOWER_POUCH));
		consumer.accept(HAND_OF_ENDER_MENU_TYPE, botaniaRL(LibItemNames.HAND_OF_ENDER));
	}

	public static void registerCauldronInteractions() {
		var water = (CauldronInteractionDispatcherAccessor) CauldronInteractions.WATER;
		ColorHelper.supportedColors().forEach(color -> {
			water.botania_put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.MANA_POOL, color).asItem(),
					MANA_POOL_INTERACTION);
			water.botania_put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.CREATIVE_MANA_POOL, color).asItem(),
					MANA_POOL_INTERACTION);
			water.botania_put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.DILUTED_MANA_POOL, color).asItem(),
					MANA_POOL_INTERACTION);
			water.botania_put(BotaniaBlocks.findOptionallyDyedBlock(BotaniaBlocks.FABULOUS_MANA_POOL, color).asItem(),
					MANA_POOL_INTERACTION);
		});

		ALL.values().stream().filter(item -> item instanceof LensItem)
				.forEach(item -> water.botania_put(item, MANA_LENS_INTERACTION));
		ALL.values().stream().filter(item -> item instanceof PhantomInkable)
				.forEach(item -> water.botania_put(item, PHANTOM_INK_INTERACTION));
	}

	public static Item getPetal(DyeColor color) {
		return switch (color) {
			case WHITE -> WHITE_MYSTICAL_PETAL;
			case ORANGE -> ORANGE_MYSTICAL_PETAL;
			case MAGENTA -> MAGENTA_MYSTICAL_PETAL;
			case LIGHT_BLUE -> LIGHT_BLUE_MYSTICAL_PETAL;
			case YELLOW -> YELLOW_MYSTICAL_PETAL;
			case LIME -> LIME_MYSTICAL_PETAL;
			case PINK -> PINK_MYSTICAL_PETAL;
			case GRAY -> GRAY_MYSTICAL_PETAL;
			case LIGHT_GRAY -> LIGHT_GRAY_MYSTICAL_PETAL;
			case CYAN -> CYAN_MYSTICAL_PETAL;
			case PURPLE -> PURPLE_MYSTICAL_PETAL;
			case BLUE -> BLUE_MYSTICAL_PETAL;
			case BROWN -> BROWN_MYSTICAL_PETAL;
			case GREEN -> GREEN_MYSTICAL_PETAL;
			case RED -> RED_MYSTICAL_PETAL;
			case BLACK -> BLACK_MYSTICAL_PETAL;
		};
	}

	public static boolean isNoDespawn(Item item) {
		return item instanceof ManaTabletItem || item instanceof BandOfManaItem || item instanceof TerraShattererItem
				|| item instanceof RelicItem || item instanceof RelicBaubleItem;
	}
}
