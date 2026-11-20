/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.SkeletonHorse;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.common.handler.BotaniaSounds;
import org.violetmoon.botania.mixin.AbstractHorseAccessor;

public class EquestrianVirusItem extends Item {
	public EquestrianVirusItem(Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand hand) {
		if (living.isAlive() && living instanceof Horse horse) {
			if (!(player.level() instanceof ServerLevel level)) {
				return InteractionResult.SUCCESS;
			}
			if (horse.isTamed()) {
				AbstractHorse newHorse = stack.is(BotaniaItems.NECRODERMAL_VIRUS)
						? EntityType.ZOMBIE_HORSE.create(level, EntitySpawnReason.CONVERSION)
						: EntityType.SKELETON_HORSE.create(level, EntitySpawnReason.CONVERSION);
				if (newHorse == null) {
					return InteractionResult.PASS;
				}

				SimpleContainer inventory = ((AbstractHorseAccessor) horse).botania_getInventory();
				for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
					ItemStack stored = inventory.getItem(slot);
					if (!stored.isEmpty()) {
						horse.spawnAtLocation(level, stored);
					}
				}
				ItemStack armor = horse.getItemBySlot(EquipmentSlot.BODY);
				if (!armor.isEmpty()) {
					horse.spawnAtLocation(level, armor);
				}
				newHorse.setItemSlot(EquipmentSlot.SADDLE, horse.getItemBySlot(EquipmentSlot.SADDLE));
				horse.discard();

				newHorse.tameWithName(player);
				newHorse.absSnapTo(horse.getX(), horse.getY(), horse.getZ(), horse.getYRot(), horse.getXRot());

				Identifier virusId = BotaniaAPI.botaniaRL("ermergerd_virus");

				AttributeInstance movementSpeed = newHorse.getAttribute(Attributes.MOVEMENT_SPEED);
				movementSpeed.setBaseValue(horse.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue());
				movementSpeed.addPermanentModifier(new AttributeModifier(virusId, movementSpeed.getBaseValue(), AttributeModifier.Operation.ADD_VALUE));

				AttributeInstance health = newHorse.getAttribute(Attributes.MAX_HEALTH);
				health.setBaseValue(horse.getAttribute(Attributes.MAX_HEALTH).getBaseValue());
				health.addPermanentModifier(new AttributeModifier(virusId, health.getBaseValue(), AttributeModifier.Operation.ADD_VALUE));

				AttributeInstance jumpHeight = newHorse.getAttribute(Attributes.JUMP_STRENGTH);
				jumpHeight.setBaseValue(horse.getAttribute(Attributes.JUMP_STRENGTH).getBaseValue());
				jumpHeight.addPermanentModifier(new AttributeModifier(virusId, jumpHeight.getBaseValue() * 0.5, AttributeModifier.Operation.ADD_VALUE));

				newHorse.playSound(BotaniaSounds.VIRUS_INFECT, 1.0F + living.level().getRandom().nextFloat(), living.level().getRandom().nextFloat() * 0.7F + 1.3F);
				newHorse.finalizeSpawn(level, level.getCurrentDifficultyAt(newHorse.blockPosition()), EntitySpawnReason.CONVERSION, null);
				newHorse.setAge(horse.getAge());
				level.addFreshEntity(newHorse);
				newHorse.spawnAnim();

				stack.shrink(1);
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	public static boolean onLivingHurt(LivingEntity entity, DamageSource source) {
		if (entity.isPassenger() && entity.getVehicle() instanceof LivingEntity vehicle) {
			entity = vehicle;
		}

		return (entity instanceof ZombieHorse || entity instanceof SkeletonHorse)
				&& source == entity.damageSources().fall()
				&& ((AbstractHorse) entity).isTamed();
	}
}
