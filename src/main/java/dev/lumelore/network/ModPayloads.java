package dev.lumelore.network;

import dev.lumelore.Enderbook;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public abstract class ModPayloads {

    /*
     * Making Payloads:
     * 1. Create Identifier for payload
     * 2. Create record for payload
     * 3. Register payloads (done in registerModPayloads() method)
     * 4. Set up listener on the client
    */

    public static final Identifier DIRT_BROKEN = Identifier.of(Enderbook.MOD_ID, "dirt_broken");
    public static final Identifier PLAYER_DIRT_BROKEN = Identifier.of(Enderbook.MOD_ID, "player_dirt_broken");
    public static final Identifier OPEN_ENDERBOOK = Identifier.of(Enderbook.MOD_ID, "open_enderbook");
    public static final Identifier COORDINATE_ENTRY = Identifier.of(Enderbook.MOD_ID, "coordinate_entry");
    public static final Identifier COORDINATE_ENTRY_ARRAY = Identifier.of(Enderbook.MOD_ID, "coordinate_entry_array");

    // Make custom payload of data to send. int i is the data to send, can be any data-type
    public record DirtBrokenPayload(int i) implements CustomPayload {
        // Create ID (It needs an Identifier)
        public static CustomPayload.Id<DirtBrokenPayload> ID = new CustomPayload.Id<>(DIRT_BROKEN);
        // This is the data to be passed with the payload
        public static final PacketCodec<RegistryByteBuf, DirtBrokenPayload> CODEC = PacketCodec
                .tuple(PacketCodecs.INTEGER, DirtBrokenPayload::i, DirtBrokenPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public record PlayerDirtBrokenPayload(int i) implements CustomPayload {
        // Create ID (It needs an Identifier)
        public static CustomPayload.Id<PlayerDirtBrokenPayload> ID = new CustomPayload.Id<>(PLAYER_DIRT_BROKEN);
        // This is the data to be passed with the payload
        public static final PacketCodec<RegistryByteBuf, PlayerDirtBrokenPayload> CODEC = PacketCodec
                .tuple(PacketCodecs.INTEGER, PlayerDirtBrokenPayload::i, PlayerDirtBrokenPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /**
     * Send to tell the client to open the Enderbook GUI. The sent boolean does nothing, and
     * is only present because some data is required to be sent.
     *
     * @param b Does nothing
     */
    public record OpenEnderbookPayload(boolean b) implements CustomPayload {
        public static CustomPayload.Id<OpenEnderbookPayload> ID = new CustomPayload.Id<>(OPEN_ENDERBOOK);

        public static final PacketCodec<RegistryByteBuf, OpenEnderbookPayload> CODEC = PacketCodec
                .tuple(PacketCodecs.BOOL, OpenEnderbookPayload::b, OpenEnderbookPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /**
     * Sends a coordinate entry as Nbt data
     *
     * @param nbt The coordinate data as nbt data
     */
    public record CoordinateEntryPayload(NbtCompound nbt) implements CustomPayload {
        public static CustomPayload.Id<CoordinateEntryPayload> ID = new CustomPayload.Id<>(COORDINATE_ENTRY);

        public static final PacketCodec<RegistryByteBuf, CoordinateEntryPayload> CODEC = PacketCodec
                .tuple(PacketCodecs.NBT_COMPOUND, CoordinateEntryPayload::nbt, CoordinateEntryPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /**
     * Sends a list of coordinate entries as Nbt data
     *
     * @param nbt The coordinate data as nbt data
     */
    public record CoordinateEntryArrayPayload(NbtCompound nbt) implements CustomPayload {
        public static CustomPayload.Id<CoordinateEntryArrayPayload> ID = new CustomPayload.Id<>(COORDINATE_ENTRY_ARRAY);

        public static final PacketCodec<RegistryByteBuf, CoordinateEntryArrayPayload> CODEC = PacketCodec
                .tuple(PacketCodecs.UNLIMITED_NBT_COMPOUND, CoordinateEntryArrayPayload::nbt, CoordinateEntryArrayPayload::new);

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    public static void registerModPayloads() {
        Enderbook.LOGGER.info("Registering payloads for " + Enderbook.MOD_ID + " mod...");

        PayloadTypeRegistry.playS2C().register(DirtBrokenPayload.ID, DirtBrokenPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerDirtBrokenPayload.ID, PlayerDirtBrokenPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(OpenEnderbookPayload.ID, OpenEnderbookPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CoordinateEntryPayload.ID, CoordinateEntryPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(CoordinateEntryArrayPayload.ID, CoordinateEntryArrayPayload.CODEC);

    }


}
