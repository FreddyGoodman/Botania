/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.neoforge.client;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.client.model.standalone.UnbakedStandaloneModel;

import org.violetmoon.botania.api.block.FloatingFlowerProvider;
import org.violetmoon.botania.client.model.FloatingFlowerModel;
import org.violetmoon.botania.common.block.flower.FloatingFlowerBaseBlock;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class NeoForgeFloatingFlowerModel extends FloatingFlowerModel implements DynamicBlockStateModel {
	private static final Map<Identifier, StandaloneModelKey<NeoForgeFloatingFlowerModel>> MODELS = new ConcurrentHashMap<>();

	private NeoForgeFloatingFlowerModel(Identifier flower, ModelBaker baker) {
		super(flower, baker);
	}

	@Override
	public void collectParts(BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		var data = view.getBlockEntity(pos) instanceof FloatingFlowerProvider flower ? flower.getFloatingData() : null;
		parts.addAll(parts(data != null ? data.getIslandType() : null));
	}

	public static void register(ModelEvent.RegisterStandalone event) {
		BuiltInRegistries.BLOCK.stream().filter(FloatingFlowerBaseBlock.class::isInstance).forEach(block -> {
			Identifier id = BuiltInRegistries.BLOCK.getKey(block);
			event.register(MODELS.computeIfAbsent(id, key -> new StandaloneModelKey<>(key::toString)), new Unbaked(id.withPrefix("block/")));
		});
	}

	public static void apply(ModelEvent.ModifyBakingResult event) {
		var result = event.getBakingResult();
		result.blockStateModels().replaceAll((state, model) -> {
			if (state.getBlock() instanceof FloatingFlowerBaseBlock) {
				var key = MODELS.get(BuiltInRegistries.BLOCK.getKey(state.getBlock()));
				var replacement = key != null ? result.standaloneModels().get(key) : null;
				return replacement != null ? replacement : model;
			}
			return model;
		});
	}

	private record Unbaked(Identifier flower) implements UnbakedStandaloneModel<NeoForgeFloatingFlowerModel> {
		@Override
		public NeoForgeFloatingFlowerModel bake(ModelBaker baker, ModelDebugName name) {
			return new NeoForgeFloatingFlowerModel(flower, baker);
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			FloatingFlowerModel.resolveDependencies(flower, resolver);
		}
	}
}
