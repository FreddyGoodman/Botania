package org.violetmoon.botania.mixin.datafixers;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;

import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.filefix.FileFixerUpper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.datafix.BotaniaDataAttachmentFixer;
import org.violetmoon.botania.datafix.BotaniaDataFixers;
import org.violetmoon.botania.datafix.BotaniaEntityDataFix;
import org.violetmoon.botania.datafix.V3807_5;

import java.util.Map;
import java.util.function.UnaryOperator;

@Mixin(DataFixers.class)
public class DataFixersMixin {
	@Shadow
	private static UnaryOperator<String> createRenamer(Map<String, String> renameMap) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	@Inject(
		method = "addFixers",
		slice = @Slice(from = @At(value = "CONSTANT", args = "intValue=3808", ordinal = 0)),
		at = @At(
			value = "INVOKE", ordinal = 0,
			target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;"
		)
	)
	private static void injectDataFixers3807_5(DataFixerBuilder builder, FileFixerUpper.Builder fileFixer, CallbackInfo ci) {
		Schema v3807_5 = builder.addSchema(3807, 5, V3807_5::new);
		builder.addFixer(new BotaniaEntityDataFix(v3807_5));
	}

	@ModifyExpressionValue(
		method = "addFixers",
		slice = @Slice(from = @At(value = "CONSTANT", args = "intValue=3945")),
		at = @At(value = "INVOKE", ordinal = 0,
			target = "Lcom/mojang/datafixers/DataFixerBuilder;addSchema(ILjava/util/function/BiFunction;)Lcom/mojang/datafixers/schemas/Schema;")
	)
	private static Schema injectDataFixers_3945(Schema v3945, @Local(argsOnly = true) DataFixerBuilder builder) {
		builder.addFixer(BlockRenameFix.create(v3945, "Botania block renames", createRenamer(BotaniaDataFixers.RENAMED_BLOCKS)));
		builder.addFixer(ItemRenameFix.create(v3945, "Botania block item renames", createRenamer(BotaniaDataFixers.RENAMED_BLOCKS)));
		builder.addFixer(ItemRenameFix.create(v3945, "Botania item renames", createRenamer(BotaniaDataFixers.RENAMED_ITEMS)));
		builder.addFixer(new BotaniaDataAttachmentFixer(v3945));
		return v3945;
	}
}
