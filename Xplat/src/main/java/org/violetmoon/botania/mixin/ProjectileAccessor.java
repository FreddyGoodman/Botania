/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.projectile.Projectile;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Projectile.class)
public interface ProjectileAccessor {
	@Accessor("leftOwner")
	boolean botania_getLeftOwner();

	@Accessor("owner")
	@Nullable
	EntityReference<Entity> botania_getOwner();

	@Accessor("owner")
	void botania_setOwner(@Nullable EntityReference<Entity> owner);
}
