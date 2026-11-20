/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.neoforge.integration.curios;

import com.google.common.collect.Multimap;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;

import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.handler.BotaniaSounds;
import org.violetmoon.botania.common.handler.EquipmentHandler;
import org.violetmoon.botania.common.item.ResoluteIvyItem;
import org.violetmoon.botania.common.item.equipment.bauble.BaubleItem;
import org.violetmoon.botania.common.proxy.Proxy;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.common.DropRule;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CurioIntegration extends EquipmentHandler {
	public static void init() {
		NeoForge.EVENT_BUS.addListener(CurioIntegration::keepCurioDrops);
	}

	public static void keepCurioDrops(DropRulesEvent event) { //TODO make this less hacky
		event.addOverride(stack -> {
			if (ResoluteIvyItem.hasIvy(stack)) {
				stack.remove(BotaniaDataComponents.RESOLUTE_IVY);
				return true;
			}
			return false;
		}, DropRule.ALWAYS_KEEP);
	}

	@Override
	protected Container getAllWornItems(LivingEntity living) {
		return CuriosApi.getCuriosInventory(living)
				.map(ICuriosItemHandler::getEquippedCurios)
				.map(handler -> {
					List<ItemStack> list = new ArrayList<>(handler.getSlots());
					for (int i = 0; i < handler.getSlots(); i++) {
						list.add(handler.getStackInSlot(i));
					}
					return new SimpleContainer(list.toArray(ItemStack[]::new));
				})
				.orElseGet(() -> new SimpleContainer(0));
	}

	@Override
	protected ItemStack findItem(Item item, LivingEntity living) {
		return CuriosApi.getCuriosInventory(living)
				.map(i -> i.findFirstCurio(item).map(SlotResult::stack).orElse(ItemStack.EMPTY))
				.orElse(ItemStack.EMPTY);
	}

	@Override
	protected ItemStack findItem(Predicate<ItemStack> pred, LivingEntity living) {
		return CuriosApi.getCuriosInventory(living)
				.map(i -> i.findFirstCurio(pred).map(SlotResult::stack).orElse(ItemStack.EMPTY))
				.orElse(ItemStack.EMPTY);
	}

	public void initCapability(RegisterCapabilitiesEvent event, Item... items) {
		event.registerItem(CuriosCapability.ITEM, (stack, context) -> new Wrapper(stack), items);
	}

	@Override
	public void onInit(Item item) {
		Proxy.INSTANCE.runOnClient(() -> () -> CuriosRendering.register(item));
	}

	@Override
	public boolean isAccessory(ItemStack stack) {
		return super.isAccessory(stack) || stack.getCapability(CuriosCapability.ITEM) != null;
	}

	public static class Wrapper implements ICurio {
		private static final SoundInfo SOUND_INFO = new SoundInfo(BotaniaSounds.EQUIP_BAUBLE, 0.1F, 1.3F);

		private final ItemStack stack;

		Wrapper(ItemStack stack) {
			this.stack = stack;
		}

		private BaubleItem getItem() {
			return (BaubleItem) stack.getItem();
		}

		@Override
		public ItemStack getStack() {
			return stack;
		}

		@Override
		public void curioTick(SlotContext slotContext) {
			getItem().onWornTick(stack, slotContext.entity());
		}

		@Override
		public void onEquip(SlotContext slotContext, ItemStack previousStack) {
			getItem().onEquipped(stack, slotContext.entity());
		}

		@Override
		public void onUnequip(SlotContext slotContext, ItemStack newStack) {
			getItem().onUnequipped(stack, slotContext.entity());
		}

		@Override
		public boolean canEquip(SlotContext slotContext) {
			return getItem().canEquip(stack, slotContext.entity());
		}

		@Override
		public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, Identifier id) {
			return getItem().getEquippedAttributeModifiers(stack, id);
		}

		@Override
		public boolean canSync(SlotContext slotContext) {
			return true;
		}

		@Override
		public SoundInfo getEquipSound(SlotContext slotContext) {
			return SOUND_INFO;
		}

		@Override
		public boolean canEquipFromUse(SlotContext slotContext) {
			return true;
		}
	}

}
