/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.violetmoon.botania.common.item.CacophoniumItem;

public class CacophoniumBlockEntity extends BlockEntity {
	private static final String TAG_STACK = "stack";

	public ItemStack stack = ItemStack.EMPTY;

	public CacophoniumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.CACOPHONIUM_BLOCK, pos, state);
	}

	public void annoyDirewolf() {
		CacophoniumItem.playSound(level, stack, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundSource.BLOCKS, 1F);
		if (!level.isClientSide()) {
			float noteColor = level.getRandom().nextInt(25) / 24.0F;
			((ServerLevel) level).sendParticles(ParticleTypes.NOTE, worldPosition.getX() + 0.5, worldPosition.getY() + 1.2, worldPosition.getZ() + 0.5, 0, noteColor, 0, 0, 1);
			level.gameEvent(null, GameEvent.NOTE_BLOCK_PLAY, getBlockPos());
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		output.store(TAG_STACK, ItemStack.OPTIONAL_CODEC, stack);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		stack = input.read(TAG_STACK, ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveCustomOnly(registries);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
