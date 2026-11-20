/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.fabric.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.fabric.data.FabricDatagenInitializer;

@Mixin(FabricDataGenHelper.class)
public class FabricDataGenHelperMixin {
	@Inject(method = "addConditions(Lcom/google/gson/JsonObject;[Lnet/fabricmc/fabric/api/resource/conditions/v1/ResourceCondition;)V", at = @At("RETURN"))
	private static void addNeoForgeGardenOfGlassCondition(JsonObject json, ResourceCondition[] conditions, CallbackInfo callback) {
		if (conditions == null) {
			return;
		}
		for (ResourceCondition condition : conditions) {
			if (!FabricDatagenInitializer.GOG_NOT_LOADED_CONDITION.equals(condition)) {
				continue;
			}
			var modLoaded = new JsonObject();
			modLoaded.addProperty("type", "neoforge:mod_loaded");
			modLoaded.addProperty("modid", BotaniaAPI.GOG_MODID);
			var notLoaded = new JsonObject();
			notLoaded.addProperty("type", "neoforge:not");
			notLoaded.add("value", modLoaded);
			var forgeConditions = new JsonArray();
			forgeConditions.add(notLoaded);
			json.add("neoforge:conditions", forgeConditions);
			return;
		}
	}
}
