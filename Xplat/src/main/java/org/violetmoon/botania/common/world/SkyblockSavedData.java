/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.world;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.xplat.BotaniaConfig;

import java.util.List;
import java.util.UUID;

public class SkyblockSavedData extends SavedData {
	private static final Codec<SkyblockSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Island.CODEC.listOf().fieldOf("islands").forGetter(SkyblockSavedData::getIslands),
			Spiral.CODEC.fieldOf("spiral").forGetter(data -> data.spiral)
	).apply(instance, SkyblockSavedData::new));
	private static final SavedDataType<SkyblockSavedData> TYPE = new SavedDataType<>(
			BotaniaAPI.botaniaRL("gog_skyblock_islands"), SkyblockSavedData::new, CODEC, DataFixTypes.LEVEL);

	/** The offset is chosen to put islands under default settings in the center of a chunk region. */
	private static final int OFFSET = 1;

	public final BiMap<IslandPos, UUID> skyblocks;
	private final Spiral spiral;

	public SkyblockSavedData() {
		this(List.of(), new Spiral());
	}

	private SkyblockSavedData(List<Island> islands, Spiral spiral) {
		this.skyblocks = HashBiMap.create();
		for (Island island : islands) {
			skyblocks.put(island.pos(), island.owner());
		}
		this.spiral = spiral;
	}

	public static SkyblockSavedData get(ServerLevel world) {
		return world.getDataStorage().computeIfAbsent(TYPE);
	}

	public IslandPos getSpawn() {
		if (skyblocks.containsValue(Util.NIL_UUID)) {
			return skyblocks.inverse().get(Util.NIL_UUID);
		}
		IslandPos pos = new IslandPos(OFFSET, OFFSET);
		skyblocks.put(pos, Util.NIL_UUID);
		setDirty();
		return pos;
	}

	public IslandPos create(UUID playerId) {
		int scale = BotaniaConfig.common().gogIslandScaleMultiplier();
		IslandPos islandPos;
		do {
			int[] pos = spiral.next();
			islandPos = new IslandPos(pos[0] * scale + OFFSET, pos[1] * scale + OFFSET);
		} while (skyblocks.containsKey(islandPos));

		skyblocks.put(islandPos, playerId);
		setDirty();
		return islandPos;
	}

	private List<Island> getIslands() {
		return skyblocks.entrySet().stream().map(entry -> new Island(entry.getKey(), entry.getValue())).toList();
	}

	private record Island(IslandPos pos, UUID owner) {
		private static final Codec<Island> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				IslandPos.CODEC.fieldOf("position").forGetter(Island::pos),
				UUIDUtil.CODEC.fieldOf("owner").forGetter(Island::owner)
		).apply(instance, Island::new));
	}

	// Adapted from https://stackoverflow.com/questions/398299/looping-in-a-spiral
	private static class Spiral {
		private static final Codec<Spiral> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.fieldOf("x").forGetter(spiral -> spiral.x),
				Codec.INT.fieldOf("y").forGetter(spiral -> spiral.y),
				Codec.INT.fieldOf("dx").forGetter(spiral -> spiral.dx),
				Codec.INT.fieldOf("dy").forGetter(spiral -> spiral.dy)
		).apply(instance, Spiral::new));

		private int x = 0;
		private int y = 0;
		private int dx = 0;
		private int dy = -1;

		Spiral() {}

		Spiral(int x, int y, int dx, int dy) {
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
		}

		int[] next() {
			if (x == y || x < 0 && x == -y || x > 0 && x == 1 - y) {
				int t = dx;
				dx = -dy;
				dy = t;
			}
			x += dx;
			y += dy;
			return new int[] { x, y };
		}
	}
}
