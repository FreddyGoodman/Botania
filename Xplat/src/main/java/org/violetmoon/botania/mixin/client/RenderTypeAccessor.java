/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.mixin.client;

import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RenderType.class)
public interface RenderTypeAccessor {
	@Invoker("create")
	static RenderType botania_create(String name, RenderSetup setup) {
		throw new IllegalStateException();
	}

	@Invoker("create")
	static RenderType.CompositeRenderType botania_create(String string, VertexFormat vertexFormat,
			VertexFormat.Mode mode, int bufSize, boolean hasCrumbling, boolean sortOnUpload,
			RenderType.CompositeState compositeState) {
		throw new IllegalStateException();
	}
}
