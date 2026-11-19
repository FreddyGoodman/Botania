/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.block.block_entity.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import org.violetmoon.botania.api.block.FloatingFlower;
import org.violetmoon.botania.api.block.FloatingFlowerImpl;
import org.violetmoon.botania.api.block.FloatingFlowerProvider;
import org.violetmoon.botania.api.block.IslandType;
import org.violetmoon.botania.common.annotations.SoftImplement;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;

public class FloatingFlowerBlockEntity extends BlockEntity implements FloatingFlowerProvider {
	private static final String TAG_FLOATING_DATA = "floating";
	private final FloatingFlower floatingData = new FloatingFlowerImpl();

	public FloatingFlowerBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.FLOATING_MUNDANE_FLOWER, pos, state);
	}

	@Override
	public FloatingFlower getFloatingData() {
		return floatingData;
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		floatingData.save(output.child(TAG_FLOATING_DATA));
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		IslandType oldType = floatingData.getIslandType();
		input.child(TAG_FLOATING_DATA).ifPresent(floatingData::load);
		if (oldType != floatingData.getIslandType() && level != null && level.isClientSide()) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
		}
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return saveCustomOnly(registries);
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@SoftImplement("RenderDataBlockEntity")
	public Object getRenderData() {
		return floatingData.getIslandType();
	}
}
