/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package org.violetmoon.botania.common.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import org.violetmoon.botania.common.handler.BotaniaSounds;
import org.violetmoon.botania.common.lib.BotaniaTags;

import java.util.Map;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaArmorMaterials {
	public static final ArmorMaterial MANASTEEL = create("manasteel", BotaniaItems.MANASTEEL_DURABILITY_FACTOR,
			Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 5, ArmorType.CHESTPLATE, 6, ArmorType.HELMET, 2),
			18, BotaniaSounds.EQUIP_MANASTEEL, BotaniaTags.Items.REPAIRS_MANASTEEL_ARMOR, 0);

	public static final ArmorMaterial MANAWEAVE = create("manaweave", BotaniaItems.MANAWEAVE_DURABILITY_FACTOR,
			Map.of(ArmorType.BOOTS, 1, ArmorType.LEGGINGS, 2, ArmorType.CHESTPLATE, 3, ArmorType.HELMET, 1),
			18, BotaniaSounds.EQUIP_MANAWEAVE, BotaniaTags.Items.REPAIRS_MANAWEAVE_ARMOR, 0);

	public static final ArmorMaterial ELEMENTIUM = create("elementium", BotaniaItems.ELEMENTIUM_DURABILITY_FACTOR,
			Map.of(ArmorType.BOOTS, 2, ArmorType.LEGGINGS, 5, ArmorType.CHESTPLATE, 6, ArmorType.HELMET, 2),
			18, BotaniaSounds.EQUIP_ELEMENTIUM, BotaniaTags.Items.REPAIRS_ELEMENTIUM_ARMOR, 0);

	public static final ArmorMaterial TERRASTEEL = create("terrasteel", BotaniaItems.TERRASTEEL_DURABILITY_FACTOR,
			Map.of(ArmorType.BOOTS, 3, ArmorType.LEGGINGS, 6, ArmorType.CHESTPLATE, 8, ArmorType.HELMET, 3),
			26, BotaniaSounds.EQUIP_TERRASTEEL, BotaniaTags.Items.REPAIRS_TERRASTEEL_ARMOR, 3);

	private BotaniaArmorMaterials() {}

	private static ArmorMaterial create(String name, int durability, Map<ArmorType, Integer> defense,
			int enchantmentValue, Holder<SoundEvent> equipSound, TagKey<Item> repairIngredient, float toughness) {
		return new ArmorMaterial(durability, defense, enchantmentValue, equipSound, toughness, 0,
				repairIngredient, ResourceKey.create(EquipmentAssets.ROOT_ID, botaniaRL(name)));
	}
}
