/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.common.block.PlatformBlock;
import org.violetmoon.botania.common.block.block_entity.PlatformBlockEntity;
import org.violetmoon.botania.common.lib.BotaniaTags;

public final class PlatformModels {
	@Nullable
	public static BlockState camouflage(BlockAndTintGetter level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof PlatformBlockEntity tile ? camouflage(tile.getCamoState()) : null;
	}

	@Nullable
	public static BlockState camouflage(@Nullable BlockState state) {
		return state != null && !(state.getBlock() instanceof PlatformBlock) && !state.is(BotaniaTags.Blocks.UNSUPPORTED_PLATFORM_DISGUISE)
				? state : null;
	}

	public static BlockStateModel model(BlockState state) {
		return Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
	}

	private PlatformModels() {}
}
