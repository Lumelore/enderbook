package dev.lumelore.data;

import dev.lumelore.Enderbook;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class StateSaverAndLoader extends PersistentState {

    public Integer totalDirtBroken = 0;
    //TODO: IMPLEMENT SAVING AND LOADING OF COORDINATE DATA
    public HashMap<UUID, PlayerData> players = new HashMap<>();

    /**
     * Gets saved player data for a specific player
     *
     * @param player The player to get data from
     * @return The player's data
     */
    public static PlayerData getPlayerState(LivingEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());

        PlayerData playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());

        return playerState;
    }

    /**
     * Writes NBT data to the player. This is called automatically. To save different data
     * to the world, it needs to be added here.
     *
     * @param nbt Save data
     * @param registryLookup Not used (at least by me)
     * @return The input NBT data with new saved data
     */
    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // Save world data
        nbt.putInt("totalDirtBroken", totalDirtBroken);

        // Save Player data
        NbtCompound AllPlayersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            // Add data to save in playerNbt Here
            playerNbt.putInt("dirtBroken", playerData.dirtBroken);

            // Place playerNbt into AllPlayersNbt
            AllPlayersNbt.put(uuid.toString(), playerNbt);
        });

        // Putting All player nbt into world nbt
        nbt.put("players", AllPlayersNbt);

        return nbt;
    }

    /**
     * This gets saved NBT data and loads it into this class. This method is called by Minecraft.
     *
     * @param nbt Save data
     * @param registryLookup Registry Lookup
     * @return A new StateSaverAndLoader instance
     */
    public static StateSaverAndLoader createFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        // Loading world NBT data
        StateSaverAndLoader state = new StateSaverAndLoader();
        state.totalDirtBroken = nbt.getInt("totalDirtBroken");

        // Loading player specific data
        NbtCompound AllPlayersNbt = nbt.getCompound("players");
        AllPlayersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
            // Saving player data
            playerData.dirtBroken = AllPlayersNbt.getCompound(key).getInt("dirtBroken");

            // Save above player data in persistent hashmap
            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });

        return state;
    }

    // Tells Minecraft what methods to call when loading this data
    private static Type<StateSaverAndLoader> type = new Type<>(StateSaverAndLoader::new, StateSaverAndLoader::createFromNbt, null);

    /**
     * Use to access data saved on the world
     *
     * @param server The server
     * @return A new StateSaverAndLoader instance
     */
    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, Enderbook.MOD_ID);

        // This tells it to save
        state.markDirty();

        return state;
    }

}
