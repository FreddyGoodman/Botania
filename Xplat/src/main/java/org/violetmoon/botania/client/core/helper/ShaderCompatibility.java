/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.client.core.helper;

import com.mojang.blaze3d.pipeline.RenderPipeline;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.xplat.BotaniaConfig;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.BooleanSupplier;

public final class ShaderCompatibility {
	private static final String IRIS_API = "net.irisshaders.iris.api.v0.IrisApi";
	private static final BooleanSupplier IRIS_SHADER_PACK_ACTIVE = findIrisShaderPackCheck();

	private ShaderCompatibility() {}

	public static boolean useVanillaPipelines() {
		return !BotaniaConfig.client().useShaders() || IRIS_SHADER_PACK_ACTIVE.getAsBoolean();
	}

	public static boolean registerAdditiveParticles(RenderPipeline... pipelines) {
		if (!XplatAbstractions.INSTANCE.isModLoaded("iris")) {
			return false;
		}
		try {
			Class<?> api = Class.forName(IRIS_API);
			Class<?> programType = Class.forName("net.irisshaders.iris.api.v0.IrisProgram");
			Object particles = programType.getField("PARTICLES_TRANSLUCENT").get(null);
			Object instance = api.getMethod("getInstance").invoke(null);
			var assign = api.getMethod("assignPipeline", RenderPipeline.class, programType);
			for (var pipeline : pipelines) {
				assign.invoke(instance, pipeline, particles);
			}
			return true;
		} catch (ReflectiveOperationException | LinkageError e) {
			BotaniaAPI.LOGGER.warn("Could not register Botania's additive particle pipelines with Iris. Using translucent particles with shader packs.", e);
			return false;
		}
	}

	private static BooleanSupplier findIrisShaderPackCheck() {
		if (!XplatAbstractions.INSTANCE.isModLoaded("iris")) {
			return () -> false;
		}

		try {
			// Bind only the public API so both loaders can run with or without Iris installed.
			Class<?> api = Class.forName(IRIS_API);
			var lookup = MethodHandles.publicLookup();
			Object instance = api.getMethod("getInstance").invoke(null);
			var check = lookup.findVirtual(api, "isShaderPackInUse", MethodType.methodType(boolean.class));
			return MethodHandleProxies.asInterfaceInstance(BooleanSupplier.class, check.bindTo(instance));
		} catch (ReflectiveOperationException | LinkageError e) {
			BotaniaAPI.LOGGER.warn("Could not check the active Iris shader pack. Using vanilla render pipelines.", e);
			return () -> true;
		}
	}
}
