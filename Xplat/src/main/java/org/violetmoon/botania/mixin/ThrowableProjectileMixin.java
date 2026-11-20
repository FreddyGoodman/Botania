/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;

import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.common.entity.ManaBurstEntity;

@Mixin(ThrowableProjectile.class)
public class ThrowableProjectileMixin {
	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/entity/projectile/ThrowableProjectile;setPos(Lnet/minecraft/world/phys/Vec3;)V"))
	private boolean stopAtHit(ThrowableProjectile projectile, Vec3 position) {
		return !(projectile instanceof ManaBurstEntity);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void moveBurstAfterImpact(CallbackInfo callback) {
		// A burst can cross a block or change direction in its collision callback.
		if ((Object) this instanceof ManaBurstEntity burst) {
			burst.setPos(burst.position().add(burst.getDeltaMovement()));
		}
	}

	@ModifyArg(method = "applyInertia", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;scale(D)Lnet/minecraft/world/phys/Vec3;"))
	private double noDrag(double origScale) {
		// Do not apply drag to bursts
		if ((Object) this instanceof ManaBurstEntity) {
			return 1;
		}
		return origScale;
	}
}
