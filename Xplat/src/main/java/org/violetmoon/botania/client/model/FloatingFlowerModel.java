/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model;

import com.mojang.math.Transformation;

import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.block.IslandType;
import org.violetmoon.botania.common.block.block_entity.flower.BotaniaIslandTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FloatingFlowerModel implements BlockStateModel {
	private static final ModelState FLOWER_TRANSFORM = new ModelState() {
		@Override
		public Transformation transformation() {
			return new Transformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F), null);
		}
	};

	private final Map<IslandType, List<BlockStateModelPart>> models = new HashMap<>();
	private final BlockStateModelPart flower;
	private final List<BlockStateModelPart> fallback;
	private final int flags;

	protected FloatingFlowerModel(Identifier flowerId, ModelBaker baker) {
		flower = SimpleModelWrapper.bake(baker, flowerId, FLOWER_TRANSFORM);
		int materialFlags = flower.materialFlags();
		for (IslandType island : BotaniaAPI.instance().getIslandTypeRegistry()) {
			BlockStateModelPart islandModel = new Variant(island.islandModel()).bake(baker);
			models.put(island, List.of(flower, islandModel));
			materialFlags |= islandModel.materialFlags();
		}
		fallback = models.getOrDefault(BotaniaIslandTypes.GRASS, List.of(flower));
		flags = materialFlags;
	}

	public static void resolveDependencies(Identifier flower, ResolvableModel.Resolver resolver) {
		resolver.markDependency(flower);
		for (IslandType island : BotaniaAPI.instance().getIslandTypeRegistry()) {
			resolver.markDependency(island.islandModel());
		}
	}

	public List<BlockStateModelPart> parts(@Nullable IslandType island) {
		return models.getOrDefault(island, fallback);
	}

	@Override
	public void collectParts(RandomSource random, List<BlockStateModelPart> parts) {
		parts.addAll(fallback);
	}

	@Override
	public Material.Baked particleMaterial() {
		return flower.particleMaterial();
	}

	@Override
	public int materialFlags() {
		return flags;
	}
}
