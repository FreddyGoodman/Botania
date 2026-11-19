/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.equipment.tool.elementium;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.violetmoon.botania.common.handler.PixieHandler;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class ElementiumSwordItem extends Item {
	public ElementiumSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
		super(properties.sword(material, attackDamage, attackSpeed)
				.attributes(createAttributes(material, attackDamage, attackSpeed)));
	}

	private static ItemAttributeModifiers createAttributes(ToolMaterial material, float attackDamage, float attackSpeed) {
		return ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
						attackDamage + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,
						attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(PixieHandler.PIXIE_SPAWN_CHANCE, PixieHandler.makeModifier(botaniaRL("sword_modifier"), 0.05), EquipmentSlotGroup.MAINHAND)
				.build();
	}
}
