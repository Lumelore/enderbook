package dev.lumelore;

import dev.lumelore.data.CoordinateEntry;
import dev.lumelore.data.PlayerData;
import dev.lumelore.data.StateSaverAndLoader;
import dev.lumelore.item.ModItems;
import dev.lumelore.network.ModPayloads;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Enderbook implements ModInitializer {

	public static final String pencil = "✎";

	public static final String MOD_ID = "enderbook";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		LOGGER.info("Initializing " + MOD_ID + " mod.");
		ModItems.registerModItems();
		ModPayloads.registerModPayloads();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			PlayerData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());
			playerState.addCoordinateEntry(new CoordinateEntry("Potato", new BlockPos(-29999983, 330, -29999983)));
			playerState.addCoordinateEntry(new CoordinateEntry("Potato", new BlockPos(0, 0, 0)));
			playerState.addCoordinateEntry(new CoordinateEntry("Potato", new BlockPos(12, 1, -13)));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.addCoordinateEntry(new CoordinateEntry("Crunchy", -12, 32, 9872));
			playerState.savedCoordinates.getFirst().setFavorite(true);
			// Send all entries in an array
			ServerPlayNetworking.send(handler.getPlayer(), new ModPayloads.CoordinateEntryArrayPayload(
					CoordinateEntry.coordinateEntriesToNbt(playerState.savedCoordinates)));
		});


		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			if (state.getBlock() == Blocks.DIRT) {
				// Get saved global data and increment it
				StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
				serverState.totalDirtBroken++;

				// Get player specific data
				PlayerData playerData = StateSaverAndLoader.getPlayerState(player);
				playerData.dirtBroken++;
				// Send data to client
				MinecraftServer server = world.getServer();

				ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
				// Send packet to client with data
				server.execute(() -> {
					ServerPlayNetworking.send(playerEntity, new ModPayloads.DirtBrokenPayload(serverState.totalDirtBroken));
					ServerPlayNetworking.send(playerEntity, new ModPayloads.PlayerDirtBrokenPayload(playerData.dirtBroken));
				});
			}
		});


	}
}