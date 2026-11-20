/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.model;

import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.violetmoon.botania.api.mana.BasicLensItem;
import org.violetmoon.botania.client.render.ColorHandler;
import org.violetmoon.botania.common.helper.VecHelper;
import org.violetmoon.botania.common.item.ManaBlasterItem;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ManaBlasterItemModel(Gun open, Gun clipped, Map<Item, Lens> lenses, Matrix4fc transform) implements ItemModel {
	private static final ModelState LENS_TRANSFORM = new ModelState() {
		@Override
		public Transformation transformation() {
			return new Transformation(new Vector3f(-0.4F, 0.2F, 0), VecHelper.rotateY(90), new Vector3f(0.625F), null);
		}
	};

	@Override
	public void update(ItemStackRenderState state, ItemStack stack, ItemModelResolver resolver, ItemDisplayContext context,
			@Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		Gun gun = ManaBlasterItem.hasClip(stack) ? clipped : open;
		gun.model.update(state, stack, resolver, context, level, owner, seed);

		ItemStack lensStack = ManaBlasterItem.getLens(stack);
		Lens lens = lenses.get(lensStack.getItem());
		if (lens == null) {
			return;
		}
		int tint = ColorHandler.getItemColor(lensStack, 0);
		state.appendModelIdentityElement(lens);
		state.appendModelIdentityElement(tint);
		if (lens.quads.hasMaterialFlag(BakedQuad.FLAG_ANIMATED)) {
			state.setAnimated();
		}

		var layer = state.newLayer();
		gun.properties.applyToLayer(layer, context);
		layer.setLocalTransform(transform);
		layer.setExtents(() -> lens.extents);
		layer.tintLayers().add(tint);
		layer.prepareQuadList().addAll(lens.quads.getAll());
		if (stack.hasFoil()) {
			layer.setFoilType(ItemStackRenderState.FoilType.STANDARD);
		}
	}

	public record Gun(ItemModel model, ModelRenderProperties properties) {}
	public record Lens(QuadCollection quads, Vector3fc[] extents) {}

	public record Unbaked(Identifier base, Identifier clip) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Identifier.CODEC.fieldOf("base").forGetter(Unbaked::base),
				Identifier.CODEC.fieldOf("clip").forGetter(Unbaked::clip)
		).apply(instance, Unbaked::new));

		@Override
		public MapCodec<Unbaked> type() {
			return CODEC;
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			resolver.markDependency(base);
			resolver.markDependency(clip);
			for (Item lens : BuiltInRegistries.ITEM) {
				if (lens instanceof BasicLensItem) {
					resolver.markDependency(ModelLocationUtils.getModelLocation(lens));
				}
			}
		}

		@Override
		public ItemModel bake(BakingContext context, Matrix4fc transform) {
			var baker = context.blockModelBaker();
			Map<Item, Lens> lenses = new IdentityHashMap<>();
			for (Item item : BuiltInRegistries.ITEM) {
				if (item instanceof BasicLensItem) {
					var model = baker.getModel(ModelLocationUtils.getModelLocation(item));
					var quads = model.bakeTopGeometry(model.getTopTextureSlots(), baker, LENS_TRANSFORM);
					lenses.put(item, new Lens(quads, CuboidItemModelWrapper.computeExtents(quads.getAll())));
				}
			}
			return new ManaBlasterItemModel(bakeGun(context, transform, base), bakeGun(context, transform, clip), Map.copyOf(lenses), transform);
		}

		private static Gun bakeGun(BakingContext context, Matrix4fc transform, Identifier id) {
			var wrapped = new CuboidItemModelWrapper.Unbaked(id, Optional.empty(),
					List.of(new BotaniaItemTint(0), new BotaniaItemTint(1), new BotaniaItemTint(2)));
			var baker = context.blockModelBaker();
			var model = baker.getModel(id);
			var properties = ModelRenderProperties.fromResolvedModel(baker, model, model.getTopTextureSlots());
			return new Gun(wrapped.bake(context, transform), properties);
		}
	}
}
