/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.block.IslandType;
import org.violetmoon.botania.common.block.flower.FloatingFlowerBaseBlock;
import org.violetmoon.botania.common.item.GrassSeedsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class FloatingFlowerModelProvider implements DataProvider {
	private final PackOutput packOutput;

	public FloatingFlowerModelProvider(PackOutput packOutput) {
		this.packOutput = packOutput;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		List<Tuple<String, JsonElement>> flowerJsons = new ArrayList<>();
		for (Block b : BuiltInRegistries.BLOCK) {
			Identifier id = BuiltInRegistries.BLOCK.getKey(b);
			if (BotaniaAPI.MODID.equals(id.getNamespace()) && b instanceof FloatingFlowerBaseBlock) {
				String name = id.getPath();
				String nonFloat;
				if (name.endsWith("_floating_flower")) {
					nonFloat = name.replace("_floating_flower", "_mystical_flower");
				} else {
					nonFloat = name.replace("floating_", "");
				}

				JsonObject obj = new JsonObject();
				obj.addProperty("parent", botaniaRL("block/" + nonFloat).toString());
				flowerJsons.add(new Tuple<>(name, obj));
			}
		}
		List<Tuple<Identifier, JsonElement>> islandJsons = new ArrayList<>();
		Registry<IslandType> islandTypeRegistry = BotaniaAPI.INSTANCE.getIslandTypeRegistry();
		islandTypeRegistry.stream().forEach(islandType -> {
			Identifier id = islandTypeRegistry.getKey(islandType);
			if (BotaniaAPI.MODID.equals(id.getNamespace())) {
				Identifier name = islandType.islandModel();
				Item item = islandType.item().asItem();
				Identifier top;
				Identifier side;
				if (item instanceof GrassSeedsItem grassSeedsItem) {
					Block seedsBlock = grassSeedsItem.getGrassBlock();
					if (seedsBlock == Blocks.GRASS_BLOCK) {
						top = botaniaRL("block/island_top");
						side = botaniaRL("block/island_side");
					} else {
						top = TextureMapping.getBlockTexture(seedsBlock, "_top").sprite();
						side = TextureMapping.getBlockTexture(seedsBlock, "_side").sprite();
					}
				} else if (item == Items.SNOWBALL) {
					top = TextureMapping.getBlockTexture(Blocks.SNOW).sprite();
					side = TextureMapping.getBlockTexture(Blocks.GRASS_BLOCK, "_snow").sprite();
				} else {
					throw new IllegalStateException("Don't know how to generate island model " + name);
				}

				JsonObject obj = new JsonObject();
				obj.addProperty("parent", "botania:block/shapes/mini_island");
				JsonObject textures = new JsonObject();
				textures.addProperty("top", top.toString());
				textures.addProperty("side", side.toString());
				obj.add("textures", textures);
				islandJsons.add(new Tuple<>(name, obj));
			}
		});
		List<CompletableFuture<?>> output = new ArrayList<>();
		PackOutput.PathProvider blocks = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
		PackOutput.PathProvider items = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
		PackOutput.PathProvider itemDefinitions = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
		for (Tuple<String, JsonElement> pair : flowerJsons) {
			Identifier id = botaniaRL(pair.getA());
			output.add(DataProvider.saveStable(cache, pair.getB(), blocks.json(id)));
			output.add(DataProvider.saveStable(cache, pair.getB(), items.json(id)));
			output.add(DataProvider.saveStable(cache, floatingItemDefinition(id), itemDefinitions.json(id)));
		}
		PackOutput.PathProvider islands = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		for (Tuple<Identifier, JsonElement> pair : islandJsons) {
			output.add(DataProvider.saveStable(cache, pair.getB(), islands.json(pair.getA())));
		}

		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}

	private static JsonObject floatingItemDefinition(Identifier id) {
		JsonObject flower = itemModel(id.withPrefix("item/"));
		JsonArray scale = new JsonArray();
		scale.add(0.5F);
		scale.add(0.5F);
		scale.add(0.5F);
		JsonArray translation = new JsonArray();
		translation.add(0F);
		translation.add(0.2F);
		translation.add(0F);
		JsonArray identityRotation = new JsonArray();
		identityRotation.add(0F);
		identityRotation.add(0F);
		identityRotation.add(0F);
		identityRotation.add(1F);
		JsonObject transform = new JsonObject();
		transform.add("left_rotation", identityRotation);
		transform.add("right_rotation", identityRotation.deepCopy());
		transform.add("scale", scale);
		transform.add("translation", translation);
		flower.add("transformation", transform);

		JsonArray layers = new JsonArray();
		layers.add(flower);
		layers.add(itemModel(IslandType.DEFAULT_ID.withPrefix("block/islands/")));
		JsonObject composite = new JsonObject();
		composite.addProperty("type", "minecraft:composite");
		composite.add("models", layers);
		JsonObject definition = new JsonObject();
		definition.add("model", composite);
		return definition;
	}

	private static JsonObject itemModel(Identifier id) {
		JsonObject model = new JsonObject();
		model.addProperty("type", "minecraft:model");
		model.addProperty("model", id.toString());
		return model;
	}

	@Override
	public String getName() {
		return "Botania floating flower models";
	}
}
