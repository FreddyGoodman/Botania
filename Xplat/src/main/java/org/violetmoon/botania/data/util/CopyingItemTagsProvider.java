/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.data.util;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Copies raw block tag entries into item tags after the block provider has generated its contents. */
public abstract class CopyingItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {
	private final CompletableFuture<TagLookup<Block>> blockTags;
	private final List<TagCopy> copies = new ArrayList<>();

	protected CopyingItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
			CompletableFuture<TagLookup<Item>> parentTags, CompletableFuture<TagLookup<Block>> blockTags) {
		super(output, Registries.ITEM, registries, parentTags,
				item -> BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
		this.blockTags = blockTags;
	}

	protected void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
		copies.add(new TagCopy(blockTag, itemTag));
	}

	@Override
	protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
		copies.clear();
		return super.createContentsProvider().thenCombine(blockTags, (registries, blocks) -> {
			for (TagCopy copy : copies) {
				var blockTag = blocks.apply(copy.blockTag()).orElseThrow(
						() -> new IllegalStateException("Can't copy missing block tag " + copy.blockTag().location()));
				var itemTag = getOrCreateRawBuilder(copy.itemTag());
				blockTag.build().forEach(itemTag::add);
			}
			return registries;
		});
	}

	private record TagCopy(TagKey<Block> blockTag, TagKey<Item> itemTag) {}
}
