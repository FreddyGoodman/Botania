/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric.mixin;

import net.minecraft.core.TypedInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.violetmoon.botania.common.item.BotaniaItems;

@Mixin(TypedInstance.class)
public interface TypedInstanceFabricMixin {
	@Inject(method = "is(Ljava/lang/Object;)Z", at = @At("HEAD"), cancellable = true)
	private void isBotaniaShears(Object item, CallbackInfoReturnable<Boolean> callback) {
		if (item == Items.SHEARS && (Object) this instanceof ItemStack stack
				&& (stack.getItem() == BotaniaItems.MANASTEEL_SHEARS || stack.getItem() == BotaniaItems.ELEMENTIUM_SHEARS)) {
			callback.setReturnValue(true);
		}
	}
}
