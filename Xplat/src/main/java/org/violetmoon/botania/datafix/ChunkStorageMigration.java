package org.violetmoon.botania.datafix;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.IdMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.*;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.Strategy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.botania.api.state.enums.HopperhockFilterType;

import java.util.*;
import java.util.function.Consumer;

public class ChunkStorageMigration {
	private static final ListMultimap<String, BlockEntityMigration> BLOCK_ENTITY_MIGRATIONS;
	static {
		ImmutableListMultimap.Builder<String, BlockEntityMigration> migrationsBuilder = ImmutableListMultimap.builder();
		migrationsBuilder
				.put("botania:mana_pool", be -> {
					Dynamic<Tag> outputting = BlockEntityMigration.removeBoolean(be, "outputting");
					Optional<DyeColor> legacyColor = BlockEntityMigration.removeMaybeColor(be, "color");
					return oldBlockState -> {
						String color = legacyColor.map(
								col -> col.getSerializedName().equals("white") ? "none" : col.getSerializedName()
						).orElse(oldBlockState.properties.get("color") != null
								? ((StringTag) oldBlockState.properties.get("color").getValue()).value()
								: "white"
						);
						Identifier newId = oldBlockState.block.withPath(old -> color.equals("none")
								? old
								: color + "_" + old
						);
						return new FakeBlockstate(newId, ImmutableMap.of("outputting", outputting));
					};
				})
				.put("botania:mana_spreader", be -> {
					Optional<DyeColor> color = BlockEntityMigration.removeMaybeColor(be, "paddingColor");
					// legacy uuid format
					if (be.get("uuidLeast") instanceof LongTag) {
						long least = be.getLongOr("uuidLeast", 0L);
						long most = be.getLongOr("uuidMost", 0L);
						be.remove("uuidLeast");
						be.remove("uuidMost");
						be.store("uuid", UUIDUtil.CODEC, new UUID(most, least));
					}

					// update rotation format
					float xRot = -be.getFloatOr("rotationY", 0F);
					float yRot = 90 - be.getFloatOr("rotationX", 0F);
					be.putFloat("rotationY", yRot);
					be.putFloat("rotationX", xRot);

					return oldBlockState -> {
						Identifier newId = oldBlockState.block.withPath(old -> color.map(dyeColor -> dyeColor.getSerializedName() + "_covered_").orElse("") + old
						);
						return new FakeBlockstate(newId, oldBlockState.properties);
					};
				})
				.put("botania:turntable", be -> {
					be.putString("id", "botania:spreader_turntable");
					Dynamic<Tag> backwards = BlockEntityMigration.removeBoolean(be, "backwards");
					Dynamic<Tag> speed = BlockEntityMigration.removeInt(be, "speed");
					return BlockStateMigration.make(builder -> builder
							.put("backwards", backwards)
							.put("speed", speed)
					);
				})
				.put("botania:incense_plate", be -> {
					Dynamic<Tag> lit = BlockEntityMigration.removeBoolean(be, "burning");
					return BlockStateMigration.make(builder -> builder
							.put("lit", lit)
					);
				})
				.put("botania:corporea_crystal_cube", be -> {
					Dynamic<Tag> locked = BlockEntityMigration.removeBoolean(be, "lock");
					Dynamic<Tag> hidden = BlockEntityMigration.removeBoolean(be, "hideCount");
					return BlockStateMigration.make(builder -> builder
							.put("locked", locked)
							.put("hidden", hidden)
					);
				})
				.put("botania:corporea_retainer", be -> {
					Dynamic<Tag> retainMissing = BlockEntityMigration.removeBoolean(be, "retainMissing");
					return BlockStateMigration.make(builder -> builder
							.put("retain_missing", retainMissing)
					);
				})
				.put("botania:animated_torch", be -> {
					int side = BlockEntityMigration.removeIntAsInt(be, "side");
					Dynamic<Tag> facing = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(switch (side) {
						case 0 -> "north";
						case 1 -> "east";
						case 2 -> "south";
						default -> "west";
					}));
					int torchMode = BlockEntityMigration.removeIntAsInt(be, "torchMode");
					Dynamic<Tag> mode = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(switch (torchMode) {
						case 0 -> "toggle";
						case 1 -> "rotate";
						default -> "random";
					}));
					Dynamic<Tag> triggered = BlockEntityMigration.removeBoolean(be, "rotating");
					return BlockStateMigration.make(builder -> builder
							.put("facing", facing)
							.put("mode", mode)
							.put("triggered", triggered)
					);
				})
				.put("botania:alfheim_portal", be -> {
					int stackCount = BlockEntityMigration.removeIntAsInt(be, "stackCount");
					if (stackCount > 0) {
						ListTag portalStacks = new ListTag();
						for (int i = 0; i < stackCount; i++) {
							portalStacks.add(BlockEntityMigration.removeTag(be, "portalStack" + i));
						}
						be.put("portalStacks", portalStacks);
					}
					return state -> state;
				})
				.put("botania:mana_fluxfield", be -> {
					be.put("energy", BlockEntityMigration.removeTag(be, "mana"));
					return state -> state;
				})
				.put("botania:mini_island", BlockEntityMigration.rename("botania:floating_mundane_flower"))
				.put("botania:altar", BlockEntityMigration.rename("botania:petal_apothecary"))
				.put("botania:bifrost", BlockEntityMigration.rename("botania:bifrost_bridge"))
				.put("botania:enchanter", BlockEntityMigration.rename("botania:mana_enchanter"))
				.put("botania:brewery", BlockEntityMigration.rename("botania:botanical_brewery"))
				.put("botania:terra_plate", BlockEntityMigration.rename("botania:terrestrial_agglomeration_plate"))
				.put("botania:mana_distributor", BlockEntityMigration.rename("botania:mana_splitter"))
				.put("botania:tiny_planet_block", BlockEntityMigration.rename("botania:tiny_planet"))
				.put("botania:spawner_claw", BlockEntityMigration.rename("botania:life_imbuer"))
				.put("botania:prism", BlockEntityMigration.rename("botania:mana_prism"))
				.put("botania:pump", BlockEntityMigration.rename("botania:mana_pump"))
				.put("botania:spark_changer", BlockEntityMigration.rename("botania:spark_tinkerer"))
				.put("botania:bellows", BlockEntityMigration.rename("botania:manatide_bellows"))
				.put("botania:forest_eye", BlockEntityMigration.rename("botania:eye_of_the_ancients"))
				.put("botania:ender_eye_block", BlockEntityMigration.rename("botania:ender_overseer"))
				.put("botania:red_string_container", BlockEntityMigration.rename("botania:red_stringed_container"))
				.put("botania:red_string_dispenser", BlockEntityMigration.rename("botania:red_stringed_dispenser"))
				.put("botania:red_string_fertilizer", BlockEntityMigration.rename("botania:red_stringed_nutrifier"))
				.put("botania:red_string_comparator", BlockEntityMigration.rename("botania:red_stringed_comparator"))
				.put("botania:red_string_relay", BlockEntityMigration.rename("botania:red_stringed_spoofer"))
				.put("botania:red_string_interceptor", BlockEntityMigration.rename("botania:red_stringed_interceptor"))
				.put("botania:hourglass", BlockEntityMigration.rename("botania:hovering_hourglass"))
				.put("botania:light_relay", BlockEntityMigration.rename("botania:luminizer"))
				.put("botania:cell_block", BlockEntityMigration.rename("botania:cellular_block"))
				.put("botania:cocoon", BlockEntityMigration.rename("botania:cocoon_of_caprice"))
				.put("botania:starfield", BlockEntityMigration.rename("botania:starfield_creator"));

		BlockEntityMigration hopperhockMigration = be -> {
			int filterType = BlockEntityMigration.removeIntAsInt(be, "filterType");
			Dynamic<Tag> filterTypeProp = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(switch (filterType) {
				case 0 -> HopperhockFilterType.ACCEPT_IN_FRAME.getSerializedName();
				case 1 -> HopperhockFilterType.ACCEPT_NOT_IN_FRAME.getSerializedName();
				default -> HopperhockFilterType.ACCEPT_ALL.getSerializedName();
			}));
			return BlockStateMigration.make(builder -> builder.put("filter_type", filterTypeProp));
		};
		migrationsBuilder.put("botania:hopperhock", hopperhockMigration);
		migrationsBuilder.put("botania:hopperhock_chibi", hopperhockMigration);

		BlockEntityMigration rannuncarpusMigration = be -> {
			Dynamic<Tag> mode = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(
					be.get("stateSensitive") instanceof ByteTag && !be.getBooleanOr("stateSensitive", false)
							? "state_insensitive"
							: "state_sensitive"
			));
			return BlockStateMigration.make(builder -> builder.put("mode", mode));
		};
		migrationsBuilder.put("botania:rannuncarpus", rannuncarpusMigration);
		migrationsBuilder.put("botania:rannuncarpus_chibi", rannuncarpusMigration);

		migrationsBuilder.put("botania:daffomill", be -> {
			int orientation = BlockEntityMigration.removeIntAsInt(be, "orientation");
			Dynamic<Tag> facing = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(switch (orientation) {
				case 2 -> "north";
				case 3 -> "south";
				case 4 -> "west";
				default -> "east";
			}));
			return BlockStateMigration.make(builder -> builder.put("facing", facing));
		});

		BlockEntityMigration hydroAndThermMigration = be -> {
			Dynamic<Tag> generating = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(
					be.getIntOr("burnTime", 0) > 0 ? "true" : "false"
			));
			Dynamic<Tag> onCooldown = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(
					be.getIntOr("cooldown", 0) > 0 ? "true" : "false"
			));
			return BlockStateMigration.make(builder -> builder
					.put("generating", generating)
					.put("on_cooldown", onCooldown)
			);
		};
		migrationsBuilder.put("botania:hydroangeas", hydroAndThermMigration);
		migrationsBuilder.put("botania:thermalily", hydroAndThermMigration);

		migrationsBuilder.put("botania:munchdew", be -> {
			Dynamic<Tag> generating = BlockEntityMigration.removeBoolean(be, "ateOnce");
			Dynamic<Tag> onCooldown = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(
					be.getIntOr("cooldown", 0) > 0 ? "true" : "false"
			));
			return BlockStateMigration.make(builder -> builder
					.put("generating", generating)
					.put("on_cooldown", onCooldown)
			);
		});

		migrationsBuilder.put("botania:endoflame", be -> {
			Dynamic<Tag> generating = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(
					be.getIntOr("burnTime", 0) > 0 ? "true" : "false"
			));
			return BlockStateMigration.make(builder -> builder
					.put("generating", generating)
			);
		});

		migrationsBuilder.put("botania:gourmaryllis", be -> {
			Dynamic<Tag> generating = new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(
					be.getIntOr("digestingMana", 0) != 0 ? "true" : "false"
			));
			return BlockStateMigration.make(builder -> builder
					.put("generating", generating)
			);
		});

		migrationsBuilder.put("botania:pollidisiac", be -> {
			String mode = BlockEntityMigration.removeStringAsString(be, "mode");
			return BlockStateMigration.make(builder -> builder
					.put("mode", new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(switch (mode) {
						case "feed_adults" -> "adult_animals";
						case "feed_babies" -> "baby_animals";
						default -> "all_animals";
					})))
			);
		});

		BlockEntityMigration bindableFlowerMigration = be -> {
			BlockPos pos = null;
			if (be.get("collectorX") instanceof IntTag) {
				int x = BlockEntityMigration.removeIntAsInt(be, "collectorX");
				int y = BlockEntityMigration.removeIntAsInt(be, "collectorY");
				int z = BlockEntityMigration.removeIntAsInt(be, "collectorZ");
				be.store("binding", BlockPos.CODEC, new BlockPos(x, y, z));
			} else if (be.get("poolX") instanceof IntTag) {
				int x = BlockEntityMigration.removeIntAsInt(be, "poolX");
				int y = BlockEntityMigration.removeIntAsInt(be, "poolY");
				int z = BlockEntityMigration.removeIntAsInt(be, "poolZ");
				pos = new BlockPos(x, y, z);
			} else if (be.contains("binding")) {
				CompoundTag binding = be.getCompoundOrEmpty("binding");
				int x = binding.getIntOr("x", 0);
				int y = binding.getIntOr("y", 0);
				int z = binding.getIntOr("z", 0);
				pos = new BlockPos(x, y, z);
			}
			if (pos != null) {
				be.store("binding", BlockPos.CODEC, pos);
			}
			be.putBoolean("autoBinding", false);

			return state -> state;
		};

		for (String flower : BotaniaDataFixers.GENERATING_FLOWERS) {
			migrationsBuilder.put(flower, bindableFlowerMigration);
		}

		for (String flower : BotaniaDataFixers.MANA_CONSUMING_FUNCTIONAL_FLOWERS) {
			migrationsBuilder.put(flower, bindableFlowerMigration);
		}

		for (Map.Entry<String, String> flower : BotaniaDataFixers.FLOWER_RENAMES.entrySet()) {
			migrationsBuilder.put(flower.getKey(), BlockEntityMigration.rename(flower.getValue()));
		}

		BlockEntityMigration floatingFlowerMigration = be -> {
			if (be.get("islandType") instanceof StringTag) {
				String type = be.getStringOr("islandType", "");
				type = type.equals("MYCEL") ? "MYCELIUM" : type;
				type = "botania:" + type.toLowerCase(Locale.ROOT);
				be.putString("islandType", type);
			}
			return state -> state;
		};

		for (String flower : BotaniaDataFixers.ALL_SPECIAL_FLOWERS) {
			migrationsBuilder.put(flower, floatingFlowerMigration);
		}
		migrationsBuilder.put("botania:mini_island", floatingFlowerMigration);

		for (String recheck : BotaniaDataFixers.REDSTONE_RECHECK) {
			migrationsBuilder.put(recheck, be -> {
				be.putString("redstoneRecheck", "{}");
				return state -> state;
			});
		}

		migrationsBuilder.put("botania:hourglass", be -> {
			boolean enabled = be.getBooleanOr("move", false);
			boolean hasInv = be.get("Items") instanceof ListTag items && !items.isEmpty();
			return BlockStateMigration.make(builder -> builder
					.put("enabled", new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(enabled ? "true" : "false")))
					.put("active", new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(enabled && hasInv ? "true" : "false")))
			);
		});

		BLOCK_ENTITY_MIGRATIONS = migrationsBuilder.build();
	}

	public static void rewriteChunkData(CompoundTag chunkData) {
		ListTag blockEntities = chunkData.getListOrEmpty("block_entities");
		if (blockEntities.isEmpty()) {
			return;
		}
		Map<Byte, Map<BlockPos, BlockStateMigration>> newStates = new HashMap<>();
		for (CompoundTag be : blockEntities.compoundStream().toList()) {
			List<BlockEntityMigration> migrations = BLOCK_ENTITY_MIGRATIONS.get(be.getStringOr("id", ""));
			if (migrations.isEmpty()) {
				continue;
			}
			BlockStateMigration stateMigration = migrations.getFirst().blockStateMigration(be);
			for (BlockEntityMigration migration : migrations.subList(1, migrations.size())) {
				stateMigration = BlockStateMigration.compose(
						stateMigration,
						migration.blockStateMigration(be)
				);
			}
			newStates.computeIfAbsent((byte) Math.floorDiv(be.getIntOr("y", 0), 16), y -> new HashMap<>()).put(
					new BlockPos(
							Mth.positiveModulo(be.getIntOr("x", 0), 16),
							Mth.positiveModulo(be.getIntOr("y", 0), 16),
							Mth.positiveModulo(be.getIntOr("z", 0), 16)
					),
					stateMigration
			);
		}

		ListTag sections = chunkData.getListOrEmpty("sections");
		for (CompoundTag section : sections.compoundStream().toList()) {
			Map<BlockPos, BlockStateMigration> migrations = newStates.get(section.getByteOr("Y", (byte) 0));
			if (migrations == null) {
				continue;
			}
			Tag blockStates = section.get("block_states");
			if (blockStates == null) {
				continue;
			}
			var codec = createBlockStateCodec();
			PalettedContainer<FakeBlockstate> blocks = codec.parse(NbtOps.INSTANCE, blockStates).getOrThrow();
			for (Map.Entry<BlockPos, BlockStateMigration> migration : migrations.entrySet()) {
				int x = migration.getKey().getX(), y = migration.getKey().getY(), z = migration.getKey().getZ();
				blocks.set(x, y, z, migration.getValue().migrateBlockState(blocks.get(x, y, z)));
			}
			Tag tag = codec.encodeStart(NbtOps.INSTANCE, blocks).getOrThrow();
			section.put("block_states", tag);
		}
	}

	public interface BlockEntityMigration {
		BlockStateMigration blockStateMigration(CompoundTag blockEntity);

		static BlockEntityMigration rename(String newName) {
			return be -> {
				be.putString("id", newName);
				return state -> state;
			};
		}

		static Dynamic<Tag> removeBoolean(CompoundTag tag, String key) {
			boolean value = tag.getBooleanOr(key, false);
			tag.remove(key);
			return new Dynamic<>(NbtOps.INSTANCE, StringTag.valueOf(value ? "true" : "false"));
		}

		static Tag removeTag(CompoundTag tag, String key) {
			Tag value = tag.get(key);
			tag.remove(key);
			return value;
		}

		static int removeIntAsInt(CompoundTag tag, String key) {
			int value = tag.getIntOr(key, 0);
			tag.remove(key);
			return value;
		}

		static Dynamic<Tag> removeInt(CompoundTag tag, String key) {
			int value = tag.getIntOr(key, 0);
			tag.remove(key);
			return new Dynamic<>(NbtOps.INSTANCE, IntTag.valueOf(value));
		}

		static String removeStringAsString(CompoundTag tag, String key) {
			String value = tag.getStringOr(key, "");
			tag.remove(key);
			return value;
		}

		static Optional<DyeColor> removeMaybeColor(CompoundTag tag, String key) {
			if (!(tag.get(key) instanceof NumericTag)) {
				return Optional.empty();
			}
			int value = tag.getIntOr(key, 0);
			if (value == -1) {
				return Optional.empty();
			}
			return Optional.of(DyeColor.byId(value));
		}
	}

	public interface BlockStateMigration {
		FakeBlockstate migrateBlockState(FakeBlockstate old);

		static BlockStateMigration make(Consumer<ImmutableMap.Builder<String, Dynamic<?>>> builderConsumer) {
			return blockState -> {
				ImmutableMap.Builder<String, Dynamic<?>> builder = ImmutableMap.<String, Dynamic<?>>builder()
						.putAll(blockState.properties);
				builderConsumer.accept(builder);
				return new FakeBlockstate(blockState.block, builder.build());
			};
		}

		static BlockStateMigration compose(BlockStateMigration first, BlockStateMigration second) {
			return state -> second.migrateBlockState(first.migrateBlockState(state));
		}
	}

	private static Codec<PalettedContainer<FakeBlockstate>> createBlockStateCodec() {
		return PalettedContainer.codecRW(FakeBlockstate.CODEC, Strategy.createForBlockStates(new OnDemandIdProvider<>()),
				new FakeBlockstate(Identifier.parse("minecraft:air"), ImmutableMap.of()));
	}

	private static class OnDemandIdProvider<T> implements IdMap<T> {

		private final HashBiMap<Integer, T> map;
		private int nextId;

		public OnDemandIdProvider() {
			this.map = HashBiMap.create();
			this.nextId = 0;
		}

		@Override
		public int getId(T t) {
			if (map.containsValue(t)) {
				return map.inverse().get(t);
			}
			map.put(nextId, t);
			nextId++;
			return nextId - 1;
		}

		@Override
		public @Nullable T byId(int i) {
			if (map.containsKey(i)) {
				return map.get(i);
			}
			return null;
		}

		@Override
		public int size() {
			// A section can contain a distinct old and replacement state at each of its 4096 positions.
			return 8192;
		}

		@Override
		public @NotNull Iterator<T> iterator() {
			return map.values().iterator();
		}
	}

	public record FakeBlockstate(Identifier block, Map<String, Dynamic<?>> properties) {
		public static Codec<FakeBlockstate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("Name").forGetter(FakeBlockstate::block),
				ExtraCodecs.strictUnboundedMap(Codec.STRING, Codec.PASSTHROUGH)
						.fieldOf("Properties").orElse(ImmutableMap.of())
						.forGetter(FakeBlockstate::properties)
		).apply(instance, FakeBlockstate::new));
	}
}
