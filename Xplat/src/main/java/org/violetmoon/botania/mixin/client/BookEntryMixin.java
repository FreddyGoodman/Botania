/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import org.jetbrains.annotations.UnknownNullability;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.botania.common.handler.DogEnabler;
import org.violetmoon.botania.common.lib.LibItemNames;

import vazkii.patchouli.client.book.BookEntry;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

@Mixin(BookEntry.class)
public class BookEntryMixin {
	@Unique
	private static final Identifier BOTANIA_LEXICA_ID = botaniaRL(LibItemNames.LEXICA_BOTANIA);

	@Unique
	@UnknownNullability
	private Boolean botania_cachedDog;

	@ModifyReturnValue(method = "getEntryColor", at = @At("RETURN"), remap = false)
	private int makeLexicaEntryColorOpaque(int color) {
		// Patchouli parses custom entry colors as RGB, but Minecraft skips text with zero alpha.
		if (ARGB.alpha(color) == 0 && BOTANIA_LEXICA_ID.equals(((BookEntry) (Object) this).getBook().id)) {
			return ARGB.opaque(color);
		}
		return color;
	}

	@WrapOperation(
		method = { "isLocked", "compareTo(Lvazkii/patchouli/client/book/BookEntry;)I" },
		at = @At(value = "FIELD", target = "Lvazkii/patchouli/client/book/BookEntry;locked:Z", opcode = Opcodes.GETFIELD)
	)
	private boolean invertDogLock(BookEntry instance, Operation<Boolean> original) {
		boolean locked = original.call(instance);
		return instance == (Object) this
				? botania_isDog() != locked
				: locked;
	}

	@Unique
	private boolean botania_isDog() {
		if (botania_cachedDog == null) {
			botania_cachedDog = DogEnabler.isDog(((BookEntry) (Object) this).getId());
		}
		return botania_cachedDog;
	}
}
