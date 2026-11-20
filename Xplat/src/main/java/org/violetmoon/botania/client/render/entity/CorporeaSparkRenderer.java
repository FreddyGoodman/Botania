/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import org.violetmoon.botania.common.entity.CorporeaSparkEntity;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class CorporeaSparkRenderer extends BaseSparkRenderer<CorporeaSparkEntity> {
	private final TextureAtlasSprite corporeaWorldSprite;
	private final TextureAtlasSprite corporeaMasterWorldSprite;
	private final TextureAtlasSprite corporeaCreativeWorldSprite;

	public CorporeaSparkRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		this.corporeaWorldSprite = getItemSprite(botaniaRL("item/corporea_spark"));
		this.corporeaMasterWorldSprite = getItemSprite(botaniaRL("item/master_corporea_spark"));
		this.corporeaCreativeWorldSprite = getItemSprite(botaniaRL("item/creative_corporea_spark"));
	}

	@Override
	public TextureAtlasSprite getBaseIcon(CorporeaSparkEntity entity) {
		if (entity.isCreative()) {
			return this.corporeaCreativeWorldSprite;
		} else if (entity.isMaster()) {
			return this.corporeaMasterWorldSprite;
		} else {
			return this.corporeaWorldSprite;
		}
	}

}
