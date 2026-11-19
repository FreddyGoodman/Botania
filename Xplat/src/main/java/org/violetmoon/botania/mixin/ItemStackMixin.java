/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.common.item.equipment.tool.elementium.ElementiumAxeItem;
import org.violetmoon.botania.api.mana.ManaItemHandler;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.component.ManaRepair;

@Mixin(ItemStack.class)
public class ItemStackMixin {

	/**
	 * Self-repair for mana-repaired items.
	 */
	@Inject(method = "inventoryTick", at = @At("HEAD"))
	private void tickSelfRepair(Level level, Entity entity, EquipmentSlot slot, CallbackInfo ci) {

		ItemStack self = (ItemStack) (Object) this;

		if (level.isClientSide() || !(entity instanceof Player player)) {
			return;
		}

		ManaRepair manaRepair = self.get(BotaniaDataComponents.MANA_REPAIR);
		if (manaRepair == null || !self.isDamaged() || !ManaItemHandler.instance()
				.requestManaExactForTool(self, player, manaRepair.manaPerRepairedDurability(), true)) {
			return;
		}

		self.setDamageValue(self.getDamageValue() - 1);
	}

	@ModifyExpressionValue(method = "postHurtEnemy", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/item/component/Weapon;itemDamagePerAttack()I"))
	private int botania_elementiumAxeDurabilityCost(int original) {
		return ((ItemStack) (Object) this).getItem() instanceof ElementiumAxeItem ? 1 : original;
	}
}
