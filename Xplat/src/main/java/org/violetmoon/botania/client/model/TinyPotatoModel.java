/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model;

import com.mojang.serialization.MapCodec;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import org.violetmoon.botania.client.core.proxy.ClientProxy;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.item.block.TinyPotatoBlockItem;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public record TinyPotatoModel(ItemModel plain, ModelRenderProperties properties, Matrix4fc transform) implements ItemModel {

	private static final Pattern ESCAPED = Pattern.compile("[^a-z0-9/._-]");
	private static final Identifier DEFAULT = variantId("default");
	private static final Identifier HALLOWEEN = variantId("halloween");

	public static String variantName(@Nullable Component displayName) {
		var name = new StringBuilder();
		TinyPotatoBlockItem.isEnchantedName(displayName, name);
		return name.toString().toLowerCase(Locale.ROOT);
	}

	public static BlockStateModelPart getVariant(String name) {
		var models = ClientXplatAbstractions.INSTANCE;
		var model = models.findBlockModelPart(variantId(ESCAPED.matcher(name).replaceAll("_")));
		return model != null ? model : models.getBlockModelPart(ClientProxy.dootDoot ? HALLOWEEN : DEFAULT);
	}

	private static Identifier variantId(String name) {
		return botaniaRL(ResourcesLib.PREFIX_TINY_POTATO + "/" + name);
	}

	public static List<BakedQuad> collectQuads(BlockStateModelPart part) {
		if (part instanceof SimpleModelWrapper model) {
			return model.quads().getAll();
		}
		var quads = new ArrayList<>(part.getQuads(null));
		for (Direction face : Direction.values()) {
			quads.addAll(part.getQuads(face));
		}
		return List.copyOf(quads);
	}

	@Override
	public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext context,
			@Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		if (!stack.has(DataComponents.CUSTOM_NAME) && !ClientProxy.dootDoot) {
			plain.update(state, stack, resolver, context, level, owner, seed);
			return;
		}
		var part = getVariant(variantName(stack.getHoverName()));
		var quads = collectQuads(part);
		state.appendModelIdentityElement(this);
		state.appendModelIdentityElement(part);
		if (stack.hasFoil() || (part.materialFlags() & BakedQuad.FLAG_ANIMATED) != 0) {
			state.setAnimated();
		}
		state.appendModelIdentityElement(stack.hasFoil());

		var layer = state.newLayer();
		properties.applyToLayer(layer, context);
		layer.setLocalTransform(transform);
		layer.setParticleMaterial(part.particleMaterial());
		var extents = CuboidItemModelWrapper.computeExtents(quads);
		layer.setExtents(() -> extents);
		layer.prepareQuadList().addAll(quads);
		if (stack.hasFoil()) {
			layer.setFoilType(ItemStackRenderState.FoilType.STANDARD);
		}
	}

	public record Unbaked(Identifier model) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> CODEC = Identifier.CODEC.fieldOf("model").xmap(Unbaked::new, Unbaked::model);

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			resolver.markDependency(model);
		}

		@Override
		public ItemModel bake(BakingContext context, Matrix4fc transform) {
			var baker = context.blockModelBaker();
			var resolved = baker.getModel(model);
			var properties = ModelRenderProperties.fromResolvedModel(baker, resolved, resolved.getTopTextureSlots());
			var plain = new CuboidItemModelWrapper.Unbaked(model, Optional.empty(), List.of()).bake(context, transform);
			return new TinyPotatoModel(plain, properties, transform);
		}

		@Override
		public MapCodec<Unbaked> type() {
			return CODEC;
		}
	}
}
