/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric.client;

import net.fabricmc.fabric.api.blockgetter.v2.FabricBlockGetter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModelPart;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.block.IslandType;
import org.violetmoon.botania.client.model.FloatingFlowerModel;

import java.util.function.Predicate;

public final class FabricFloatingFlowerModel extends FloatingFlowerModel implements FabricBlockStateModel {
	private FabricFloatingFlowerModel(Identifier flower, ModelBaker baker) {
		super(flower, baker);
	}

	@Override
	public void emitQuads(QuadEmitter emitter, BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random,
			Predicate<Direction> cullTest) {
		for (var part : parts(island(view, pos))) {
			((FabricBlockStateModelPart) part).emitQuads(emitter, cullTest);
		}
	}

	@Override
	public Object createGeometryKey(BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random) {
		return parts(island(view, pos));
	}

	@Nullable
	private static IslandType island(BlockAndTintGetter view, BlockPos pos) {
		return ((FabricBlockGetter) view).getBlockEntityRenderData(pos) instanceof IslandType type ? type : null;
	}

	public record Unbaked(Identifier flower) implements BlockStateModel.UnbakedRoot {
		@Override
		public BlockStateModel bake(BlockState state, ModelBaker baker) {
			return new FabricFloatingFlowerModel(flower, baker);
		}

		@Override
		public Object visualEqualityGroup(BlockState state) {
			return this;
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			FloatingFlowerModel.resolveDependencies(flower, resolver);
		}
	}
}
