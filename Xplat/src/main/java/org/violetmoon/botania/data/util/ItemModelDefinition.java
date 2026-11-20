/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.data.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class ItemModelDefinition {
	public static JsonObject create(Identifier base, @Nullable JsonElement overrides, int tintLayers) {
		List<Override> choices = new ArrayList<>();
		if (overrides != null) {
			for (JsonElement entry : overrides.getAsJsonArray()) {
				JsonObject override = entry.getAsJsonObject();
				Map<String, Float> predicates = new LinkedHashMap<>();
				override.getAsJsonObject("predicate").asMap().forEach((id, threshold) -> predicates.put(id, threshold.getAsFloat()));
				choices.add(new Override(Identifier.parse(override.get("model").getAsString()), predicates));
			}
		}
		JsonObject item = new JsonObject();
		item.add("model", dispatch(base, choices, tintLayers));
		return item;
	}

	private static JsonObject dispatch(Identifier base, List<Override> choices, int tintLayers) {
		if (choices.isEmpty()) {
			return model(base, tintLayers);
		}
		if (choices.getLast().predicates.isEmpty()) {
			return model(choices.getLast().model, tintLayers);
		}

		String property = choices.getLast().predicates.keySet().iterator().next();
		var thresholds = new TreeSet<Float>();
		List<Override> fallback = new ArrayList<>();
		for (Override choice : choices) {
			Float threshold = choice.predicates.get(property);
			if (threshold == null) {
				fallback.add(choice);
			} else {
				thresholds.add(threshold);
			}
		}

		// An old override won when all its thresholds matched and it appeared last in the list.
		JsonArray entries = new JsonArray();
		for (float threshold : thresholds) {
			List<Override> matching = new ArrayList<>();
			for (Override choice : choices) {
				Float minimum = choice.predicates.get(property);
				if (minimum == null) {
					matching.add(choice);
				} else if (minimum <= threshold) {
					Map<String, Float> remaining = new LinkedHashMap<>(choice.predicates);
					remaining.remove(property);
					matching.add(new Override(choice.model, remaining));
				}
			}
			JsonObject entry = new JsonObject();
			entry.addProperty("threshold", threshold);
			entry.add("model", dispatch(base, matching, tintLayers));
			entries.add(entry);
		}

		JsonObject dispatch = new JsonObject();
		dispatch.addProperty("type", "minecraft:range_dispatch");
		dispatch.addProperty("property", "botania:item_property");
		dispatch.addProperty("predicate", property);
		dispatch.add("fallback", dispatch(base, fallback, tintLayers));
		dispatch.add("entries", entries);
		return dispatch;
	}

	private static JsonObject model(Identifier id, int tintLayers) {
		JsonObject model = new JsonObject();
		model.addProperty("type", "minecraft:model");
		model.addProperty("model", id.toString());
		if (tintLayers > 0) {
			JsonArray tints = new JsonArray();
			for (int layer = 0; layer < tintLayers; layer++) {
				JsonObject tint = new JsonObject();
				tint.addProperty("type", "botania:item_tint");
				tint.addProperty("layer", layer);
				tints.add(tint);
			}
			model.add("tints", tints);
		}
		return model;
	}

	private record Override(Identifier model, Map<String, Float> predicates) {
	}

	private ItemModelDefinition() {}
}
