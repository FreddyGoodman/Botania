/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class RandomPatchFeature extends Feature<RandomPatchFeature.Configuration> {
	public RandomPatchFeature() {
		super(Configuration.CODEC);
	}

	@Override
	public boolean place(FeaturePlaceContext<Configuration> context) {
		var config = context.config();
		var random = context.random();
		int horizontal = config.horizontalSpread() + 1;
		int vertical = config.verticalSpread() + 1;
		var position = new BlockPos.MutableBlockPos();
		boolean placed = false;
		for (int attempt = 0; attempt < config.tries(); attempt++) {
			position.setWithOffset(context.origin(),
					random.nextInt(horizontal) - random.nextInt(horizontal),
					random.nextInt(vertical) - random.nextInt(vertical),
					random.nextInt(horizontal) - random.nextInt(horizontal));
			placed |= config.feature().value().place(context.level(), context.chunkGenerator(), random, position);
		}
		return placed;
	}

	public record Configuration(int tries, int horizontalSpread, int verticalSpread, Holder<PlacedFeature> feature)
			implements
				FeatureConfiguration {
		public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ExtraCodecs.POSITIVE_INT.optionalFieldOf("tries", 128).forGetter(Configuration::tries),
				ExtraCodecs.intRange(0, Integer.MAX_VALUE - 1).optionalFieldOf("xz_spread", 7).forGetter(Configuration::horizontalSpread),
				ExtraCodecs.intRange(0, Integer.MAX_VALUE - 1).optionalFieldOf("y_spread", 3).forGetter(Configuration::verticalSpread),
				PlacedFeature.CODEC.fieldOf("feature").forGetter(Configuration::feature)
		).apply(instance, Configuration::new));
	}
}
