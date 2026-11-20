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
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;

import org.violetmoon.botania.client.model.PlatformModels;

import java.util.List;

public class NeoForgePlatformModel extends DelegateBlockStateModel {
	public NeoForgePlatformModel(BlockStateModel originalModel) {
		super(originalModel);
	}

	@Override
	public void collectParts(BlockAndTintGetter view, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		BlockState camouflage = PlatformModels.camouflage(view, pos);
		if (camouflage != null) {
			PlatformModels.model(camouflage).collectParts(view, pos, camouflage, random, parts);
		} else {
			super.collectParts(view, pos, state, random, parts);
		}
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter view, BlockPos pos, BlockState state) {
		BlockState camouflage = PlatformModels.camouflage(view, pos);
		return camouflage != null
				? PlatformModels.model(camouflage).particleMaterial(view, pos, camouflage)
				: super.particleMaterial(view, pos, state);
	}

	@Override
	public int materialFlags(BlockAndTintGetter view, BlockPos pos, BlockState state) {
		BlockState camouflage = PlatformModels.camouflage(view, pos);
		return camouflage != null
				? PlatformModels.model(camouflage).materialFlags(view, pos, camouflage)
				: super.materialFlags(view, pos, state);
	}
}
