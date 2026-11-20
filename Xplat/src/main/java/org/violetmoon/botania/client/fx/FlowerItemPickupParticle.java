/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.client.fx;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class FlowerItemPickupParticle extends ItemPickupParticle {
	public FlowerItemPickupParticle(EntityRenderDispatcher dispatcher, ClientLevel level, Entity item, BlockPos target, boolean onFire) {
		super(level, extract(dispatcher, item, onFire), item, item.getDeltaMovement());
		Vec3 position = target.getCenter().add(level.getBlockState(target).getOffset(target));
		targetXOld = targetX = position.x();
		targetYOld = targetY = position.y();
		targetZOld = targetZ = position.z();
	}

	private static EntityRenderState extract(EntityRenderDispatcher dispatcher, Entity item, boolean onFire) {
		var state = dispatcher.extractEntity(item, 1F);
		state.displayFireAnimation |= onFire;
		return state;
	}

	@Override
	public void tick() {
		if (++life == LIFE_TIME) {
			remove();
		}
	}
}
