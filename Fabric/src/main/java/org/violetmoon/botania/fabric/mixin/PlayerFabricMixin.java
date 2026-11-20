/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.violetmoon.botania.common.PlayerAccess;
import org.violetmoon.botania.common.block.block_entity.EnderOverseerBlockEntity;
import org.violetmoon.botania.common.handler.EquipmentHandler;
import org.violetmoon.botania.common.handler.PixieHandler;
import org.violetmoon.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import org.violetmoon.botania.common.item.equipment.bauble.*;

@Mixin(Player.class)
public abstract class PlayerFabricMixin extends LivingEntity {

	protected PlayerFabricMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Shadow
	@Final
	Inventory inventory;

	/**
	 * Registers the pixie spawn chance attribute on players
	 */
	@Inject(at = @At("RETURN"), method = "createAttributes")
	private static void addPixieAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
		cir.getReturnValue().add(PixieHandler.PIXIE_SPAWN_CHANCE);
	}

	/**
	 * Performs many reactions when being hit
	 */
	@ModifyArgs(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Avatar;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private void onHurt(Args args) {
		Player self = (Player) (Object) this;
		DamageSource src = args.get(1);
		float amount = args.get(2);
		Container worn = EquipmentHandler.getAllWorn(self);
		for (int i = 0; i < worn.getContainerSize(); i++) {
			ItemStack stack = worn.getItem(i);
			if (stack.getItem() instanceof CloakOfVirtueItem cloak) {
				amount = cloak.onPlayerDamage(self, src, amount);
			}
		}

		// Should really make a separate inject for this, but putting it here works too
		PixieHandler.onDamageTaken(self, src);

		args.set(2, amount);
	}


	@Inject(at = @At("RETURN"), method = "tick")
	private void tickBeltTiara(CallbackInfo ci) {
		Player player = (Player) (Object) this;
		FlugelTiaraItem.updatePlayerFlyStatus(player);
		SojournersSashItem.tickBelt(player);
		if (!player.level().isClientSide()) {
			EnderOverseerBlockEntity.checkLookingAtEnderOverseer(player);
		}
	}

	@ModifyArg(index = 0, method = "causeFallDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Avatar;causeFallDamage(DFLnet/minecraft/world/damagesource/DamageSource;)Z"))
	private double cushionFall(double originalDist) {
		return SojournersSashItem.onPlayerFall((Player) (Object) this, (float) originalDist);
	}

	@ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
	private float onCritMul(float multiplier, Entity target) {
		Player player = (Player) (Object) this;
		if (!player.level().isClientSide() && target instanceof LivingEntity living
				&& TerrasteelHelmItem.hasTerraArmorSet(player)) {
			((PlayerAccess) this).botania$setCritTarget(living);
			return multiplier * TerrasteelHelmItem.getCritDamageMult(player);
		}
		return multiplier;
	}
}
