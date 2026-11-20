package org.violetmoon.botania.mixin.datafixers;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V3818_3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.SequencedMap;
import java.util.function.Supplier;

@Mixin(V3818_3.class)
public class V3818_3Mixin {
	@Inject(method = "components", at = @At("RETURN"))
	private static void addBotaniaComponents(Schema schema, CallbackInfoReturnable<SequencedMap<String, Supplier<TypeTemplate>>> callback) {
		var components = callback.getReturnValue();
		components.put("botania:placed_item", () -> References.ITEM_NAME.in(schema));
		components.put("botania:attached_lens", () -> References.ITEM_STACK.in(schema));
		components.put("botania:attached_lenses", () -> DSL.list(References.ITEM_STACK.in(schema)));
	}
}
