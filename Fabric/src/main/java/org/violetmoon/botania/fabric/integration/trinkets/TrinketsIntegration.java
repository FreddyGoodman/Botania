/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric.integration.trinkets;

import eu.pb4.trinkets.api.TrinketDropRule;
import eu.pb4.trinkets.api.TrinketSlotAccess;
import eu.pb4.trinkets.api.TrinketSlotUtils;
import eu.pb4.trinkets.api.TrinketsApi;
import eu.pb4.trinkets.api.callback.TrinketCallback;
import eu.pb4.trinkets.api.component.TrinketDataComponents;
import eu.pb4.trinkets.api.event.TrinketDropCallback;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.handler.BotaniaSounds;
import org.violetmoon.botania.common.handler.EquipmentHandler;
import org.violetmoon.botania.common.item.ResoluteIvyItem;
import org.violetmoon.botania.common.item.equipment.bauble.BaubleItem;
import org.violetmoon.botania.common.proxy.Proxy;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class TrinketsIntegration extends EquipmentHandler {
	public static void init() {
		TrinketDropCallback.EVENT.register(botaniaRL("resolute_ivy"), TrinketsIntegration::keepAccessoryDrops);
		UseItemCallback.EVENT.register(TrinketsIntegration::useBaubleItem);
	}

	private static TrinketDropRule keepAccessoryDrops(TrinketDropRule oldRule, ItemStack stack, TrinketSlotAccess slot, LivingEntity living) {
		if (ResoluteIvyItem.hasIvy(stack)) {
			stack.remove(BotaniaDataComponents.RESOLUTE_IVY);
			return TrinketDropRule.KEEP;
		}
		return oldRule;
	}

	private static InteractionResult useBaubleItem(Player player, Level level, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!(stack.getItem() instanceof BaubleItem bauble) || !bauble.canEquip(stack, player)) {
			return InteractionResult.PASS;
		}

		for (var inventory : TrinketsApi.getAttachment(player).getInventories().values()) {
			for (int index = 0; index < inventory.getContainerSize(); index++) {
				var slot = inventory.getSlotAccess(index);
				if (slot.get().isEmpty() && TrinketSlotUtils.mayPlace(slot, stack)
						&& inventory.slotType().interactEquipableCheck(stack, slot, player)) {
					if (!level.isClientSide()) {
						if (!slot.set(stack.copyWithCount(1))) {
							continue;
						}
						stack.consume(1, player);
						level.playSound(null, player.getX(), player.getY(), player.getZ(),
								BotaniaSounds.EQUIP_BAUBLE, SoundSource.PLAYERS, 0.1F, 1.3F);
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	protected Container getAllWornItems(LivingEntity living) {
		ItemStack[] stacks = TrinketsApi.getAttachment(living).allEquipped(false).stream()
				.map(TrinketSlotAccess::get).toArray(ItemStack[]::new);
		return new SimpleContainer(stacks);
	}

	@Override
	protected ItemStack findItem(Item item, LivingEntity living) {
		return findItem(stack -> stack.is(item), living);
	}

	@Override
	protected ItemStack findItem(Predicate<ItemStack> predicate, LivingEntity living) {
		return TrinketsApi.getAttachment(living).findFirst(predicate, false)
				.map(TrinketSlotAccess::get).orElse(ItemStack.EMPTY);
	}

	@Override
	public void onInit(Item item) {
		TrinketCallback.setCallback(item, WRAPPER);
		Proxy.INSTANCE.runOnClient(() -> () -> TrinketsRendering.register(item));
	}

	@Override
	public boolean isAccessory(ItemStack stack) {
		return super.isAccessory(stack) || stack.has(TrinketDataComponents.EQUIPMENT)
				|| TrinketCallback.getCallback(stack) != TrinketCallback.DEFAULT;
	}

	private static final TrinketCallback WRAPPER = new TrinketCallback() {
		@Override
		public void tick(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
			if (stack.getItem() instanceof BaubleItem bauble && slot.canApplyEffects(stack)) {
				bauble.onWornTick(stack, entity);
			}
		}

		@Override
		public void onEquip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
			if (stack.getItem() instanceof BaubleItem bauble && !slot.cosmetic()) {
				bauble.onEquipped(stack, entity);
			}
		}

		@Override
		public void onUnequip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
			if (stack.getItem() instanceof BaubleItem bauble && !slot.cosmetic()) {
				bauble.onUnequipped(stack, entity);
			}
		}

		@Override
		public boolean canEquip(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity) {
			return stack.getItem() instanceof BaubleItem bauble && bauble.canEquip(stack, entity);
		}

		@Override
		public void forEachTrinketModifier(ItemStack stack, TrinketSlotAccess slot, LivingEntity entity, Identifier slotId,
				BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
			if (stack.getItem() instanceof BaubleItem bauble) {
				bauble.getEquippedAttributeModifiers(stack, slotId).forEach(consumer);
			}
		}
	};

}
