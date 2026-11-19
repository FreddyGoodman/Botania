/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.armor.terrasteel;

import com.google.common.base.Suppliers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.annotations.SoftImplement;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;

import java.util.List;
import java.util.function.Supplier;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class TerrasteelArmorItem extends ManasteelArmorItem {

	public TerrasteelArmorItem(ArmorType type, ArmorMaterial material, Properties properties) {
		super(type, material, properties, defaultModifiers -> defaultModifiers.withModifierAdded(
				Attributes.KNOCKBACK_RESISTANCE,
				new AttributeModifier(botaniaRL("terrasteel_modifier." + type.getName()),
						material.defense().getOrDefault(type, 0) / 20.0, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.bySlot(type.getSlot())));
	}

	@Override
	public Identifier getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return Identifier.parse(ResourcesLib.MODEL_TERRASTEEL_NEW);
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
			new ItemStack(BotaniaItems.TERRASTEEL_HELMET),
			new ItemStack(BotaniaItems.TERRASTEEL_CHESTPLATE),
			new ItemStack(BotaniaItems.TERRASTEEL_LEGGINGS),
			new ItemStack(BotaniaItems.TERRASTEEL_BOOTS)
	});

	@Override
	public ItemStack[] getArmorSetStacks() {
		return armorSet.get();
	}

	@Override
	public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
		if (player == null) {
			return false;
		}

		ItemStack stack = player.getItemBySlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		return switch (slot) {
			case HEAD -> stack.is(BotaniaItems.TERRASTEEL_HELMET);
			case CHEST -> stack.is(BotaniaItems.TERRASTEEL_CHESTPLATE);
			case LEGS -> stack.is(BotaniaItems.TERRASTEEL_LEGGINGS);
			case FEET -> stack.is(BotaniaItems.TERRASTEEL_BOOTS);
			default -> false;
		};

	}

	@Override
	public MutableComponent getArmorSetName() {
		return Component.translatable("botania.armorset.terrasteel.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		list.add(Component.translatable("botania.armorset.terrasteel.desc0").withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("botania.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("botania.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
	}

	@SoftImplement("IItemExtension")
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}
}
