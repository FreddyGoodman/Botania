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
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.model.FabricBlockStateModel;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.model.PlatformModels;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.PlatformBlockEntity;

import java.util.function.Predicate;

public class FabricPlatformModel extends WrapperBlockStateModel {
	public FabricPlatformModel(BlockStateModel original) {
		super(original);
	}

	@Override
	public void emitQuads(QuadEmitter emitter, BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random,
			Predicate<Direction> cullTest) {
		BlockState camouflage = camouflage(view, pos);
		if (camouflage == null) {
			super.emitQuads(emitter, view, pos, state, random, cullTest);
		} else {
			((FabricBlockStateModel) PlatformModels.model(camouflage)).emitQuads(emitter, view, pos, camouflage, random, cullTest);
		}
	}

	@Override
	public Object createGeometryKey(BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random) {
		BlockState camouflage = camouflage(view, pos);
		return camouflage != null
				? ((FabricBlockStateModel) PlatformModels.model(camouflage)).createGeometryKey(view, pos, camouflage, random)
				: super.createGeometryKey(view, pos, state, random);
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter view, BlockPos pos, BlockState state) {
		BlockState camouflage = camouflage(view, pos);
		return camouflage != null
				? ((FabricBlockStateModel) PlatformModels.model(camouflage)).particleMaterial(view, pos, camouflage)
				: super.particleMaterial(view, pos, state);
	}

	@Override
	public int materialFlags(BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random) {
		BlockState camouflage = camouflage(view, pos);
		return camouflage != null
				? ((FabricBlockStateModel) PlatformModels.model(camouflage)).materialFlags(view, pos, camouflage, random)
				: super.materialFlags(view, pos, state, random);
	}

	private static @Nullable BlockState camouflage(BlockAndTintGetter view, BlockPos pos) {
		return ((FabricBlockGetter) view).getBlockEntityRenderData(pos) instanceof PlatformBlockEntity.PlatformData data
				? PlatformModels.camouflage(data.state()) : null;
	}

	public static void registerTints() {
		BlockColorRegistry.register((state, view, pos, tints) -> {
			BlockState camouflage = camouflage(view, pos);
			if (camouflage != null) {
				var factory = BlockColorRegistry.getFactory(camouflage);
				if (factory != null) {
					factory.collect(camouflage, view, pos, tints);
				} else {
					for (var tint : Minecraft.getInstance().getBlockColors().getTintSources(camouflage)) {
						tints.add(tint.colorInWorld(camouflage, view, pos));
					}
				}
			}
		}, BotaniaBlocks.ABSTRUSE_PLATFORM, BotaniaBlocks.SPECTRAL_PLATFORM, BotaniaBlocks.INFRANGIBLE_PLATFORM);
	}
}
