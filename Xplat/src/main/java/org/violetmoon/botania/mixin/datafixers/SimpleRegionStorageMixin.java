package org.violetmoon.botania.mixin.datafixers;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.DataFixer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.botania.datafix.ChunkStorageMigration;

@Mixin(SimpleRegionStorage.class)
public class SimpleRegionStorageMixin {
	@WrapOperation(method = "upgradeChunkTag(Lnet/minecraft/nbt/CompoundTag;ILnet/minecraft/nbt/CompoundTag;I)Lnet/minecraft/nbt/CompoundTag;",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/datafix/DataFixTypes;update(Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/nbt/CompoundTag;II)Lnet/minecraft/nbt/CompoundTag;"))
	private CompoundTag botania_upgradeLegacyChunk(DataFixTypes type, DataFixer fixer, CompoundTag data, int oldVersion, int newVersion,
			Operation<CompoundTag> original) {
		CompoundTag updated = original.call(type, fixer, data, oldVersion, newVersion);
		// 3955 is the data version of 1.21.1 and already uses the new Botania block states.
		if (type == DataFixTypes.CHUNK && oldVersion < 3955 && newVersion >= 3955) {
			ChunkStorageMigration.rewriteChunkData(updated);
		}
		return updated;
	}
}
