/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.world.level.block.SkullBlock;

import org.joml.Vector3fc;
import org.violetmoon.botania.client.render.block_entity.GaiaHeadBlockEntityRenderer;

import java.util.function.Consumer;
import java.util.function.Function;

public class GaiaHeadSpecialRenderer implements NoDataSpecialModelRenderer {
	private final Function<SkullBlock.Type, SkullModelBase> models;

	private GaiaHeadSpecialRenderer(EntityModelSet models) {
		this.models = GaiaHeadBlockEntityRenderer.models(models);
	}

	@Override
	public void submit(PoseStack pose, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outline) {
		var type = GaiaHeadBlockEntityRenderer.getViewType();
		SkullBlockRenderer.submitSkull(0, pose, collector, light, models.apply(type),
				GaiaHeadBlockEntityRenderer.getRenderType(type), outline, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> consumer) {
		var type = GaiaHeadBlockEntityRenderer.getViewType();
		models.apply(type).root().getExtentsForGui(new PoseStack(), consumer);
	}

	public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
		public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

		@Override
		public GaiaHeadSpecialRenderer bake(BakingContext context) {
			return new GaiaHeadSpecialRenderer(context.entityModelSet());
		}

		@Override
		public MapCodec<Unbaked> type() {
			return CODEC;
		}
	}
}
