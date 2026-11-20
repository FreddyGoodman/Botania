/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.client.fx.SparkleParticleData;

public class PixieEntity extends Mob {
	private boolean gaia;
	@Nullable
	private LivingEntity summoner;
	private float damage = 0;
	@Nullable
	private MobEffectInstance effect;

	public PixieEntity(EntityType<PixieEntity> type, Level world) {
		super(type, world);
	}

	public PixieEntity(Level world, boolean gaia) {
		this(BotaniaEntities.PIXIE, world);
		this.gaia = gaia;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
		return new ClientboundAddEntityPacket(this, entity, gaia ? 1 : 0);
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		gaia = packet.getData() == 1;
	}

	public boolean isGaia() {
		return gaia;
	}

	public void setProps(LivingEntity target, LivingEntity summoner, float damage) {
		setTarget(target);
		this.summoner = summoner;
		this.damage = damage;
	}

	public void setApplyPotionEffect(MobEffectInstance effect) {
		this.effect = effect;
	}

	@Override
	protected void customServerAiStep(ServerLevel level) {
		LivingEntity target = getTarget();
		if (target != null) {
			double d0 = target.getX() - getX();
			double d1 = target.getY(0.5) - getY();
			double d2 = target.getZ() - getZ();
			double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

			double mod = isGaia() ? 0.1 : 0.45;

			setDeltaMovement(d0 / d3 * mod, d1 / d3 * mod, d2 / d3 * mod);

			if (d3 < 1) {
				if (summoner != null) {
					if (summoner instanceof Player player) {
						target.hurtServer(level, damageSources().playerAttack(player), damage);
					} else {
						target.hurtServer(level, damageSources().mobAttack(summoner), damage);
					}
				} else {
					target.hurtServer(level, damageSources().mobAttack(this), damage);
				}
				if (effect != null && !(target instanceof Player)) {
					target.addEffect(effect);
				}
				discard();
			}
		}

		yBodyRot = (float) Math.atan2(getDeltaMovement().x(), getDeltaMovement().z()) * (-180.0F / (float) Math.PI);
		setYRot(yBodyRot);
	}

	@Override
	public void travel(Vec3 input) {
		if (isLocalInstanceAuthoritative()) {
			float acceleration = 0.02F;
			float drag;
			if (isInWater()) {
				drag = 0.8F;
			} else if (isInLava()) {
				drag = 0.5F;
			} else {
				drag = 0.91F;
				if (onGround()) {
					drag *= level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction();
					acceleration = 0.1F * (0.16277137F / (drag * drag * drag));
				}
			}
			moveRelative(acceleration, input);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(drag));
		}
		calculateEntityAnimation(false);
	}

	@Override
	protected void checkFallDamage(double fallDistance, boolean onGround, BlockState onState, BlockPos pos) {}

	@Override
	public boolean onClimbable() {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
		if (!isGaia() && source.getEntity() == summoner) {
			return true;
		}

		if (isGaia() && !(source.getEntity() instanceof Player)) {
			return true;
		}

		return super.isInvulnerableTo(level, source);
	}

	@Override
	public void baseTick() {
		super.baseTick();

		if (!level().isClientSide()
				&& (getTarget() == null || tickCount > 200)) {
			discard();
		}

		boolean dark = isGaia();
		if (level().isClientSide()) {
			for (int i = 0; i < 4; i++) {
				float r = dark ? 0.1F : 1F;
				float g = dark ? 0.025F : 0.25F;
				float b = dark ? 0.09F : 0.9F;
				SparkleParticleData data = SparkleParticleData.sparkle(0.1F + (float) Math.random() * 0.25F, r, g, b, 12);
				level().addParticle(data, getX() + (Math.random() - 0.5) * 0.25, getY() + 0.5 + (Math.random() - 0.5) * 0.25, getZ() + (Math.random() - 0.5) * 0.25, 0, 0, 0);
			}
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		if (level() != null && level().isClientSide() && !isGaia()) {
			for (int i = 0; i < 12; i++) {
				SparkleParticleData data = SparkleParticleData.sparkle(1F + (float) Math.random() * 0.25F, 1F, 0.25F, 0.9F, 5);
				level().addParticle(data, getX() + (Math.random() - 0.5) * 0.25, getY() + 0.5 + (Math.random() - 0.5) * 0.25, getZ() + (Math.random() - 0.5) * 0.25, 0, 0, 0);
			}
		}
		super.remove(reason);
	}

	@Override
	public boolean isPickable() {
		return isAlive();
	}

	@Override
	public boolean canBeLeashed() {
		return false;
	}
}
