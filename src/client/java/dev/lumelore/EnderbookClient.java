package dev.lumelore;

import dev.lumelore.data.CoordinateEntry;
import dev.lumelore.data.PlayerData;
import dev.lumelore.gui.EnderbookScreen;
import dev.lumelore.network.ModPayloads;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class EnderbookClient implements ClientModInitializer {

	// This data lives on the client. It receives updates from the server player data
	// when necessary.
	public static PlayerData clientPlayerData = new PlayerData();

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// Receive data in client from server
		ClientPlayNetworking.registerGlobalReceiver(ModPayloads.DirtBrokenPayload.ID, ((payload, context) -> {
			context.client().execute(() -> {
				context.client().player.sendMessage(Text.of(String.valueOf(payload.i())));
			});
		}));

		ClientPlayNetworking.registerGlobalReceiver(ModPayloads.PlayerDirtBrokenPayload.ID, ((payload, context) -> {
			context.client().execute(() -> {
				context.client().player.sendMessage(Text.of("Player: " + payload.i()));
				clientPlayerData.dirtBroken = payload.i();
			});
		}));

		// Open the enderbook GUI
		ClientPlayNetworking.registerGlobalReceiver(ModPayloads.OpenEnderbookPayload.ID, ((payload, context) -> {
			context.client().execute(() -> {
				// Open Enderbook screen
				MinecraftClient.getInstance().setScreen(new EnderbookScreen(Text.empty()));
				// Play sound effect
				try {
					context.client().player.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN);
				}
				catch (NullPointerException e) {
					Enderbook.LOGGER.debug(e.getMessage());
				}
			});
		}));

		// Receive coordinate entry
		ClientPlayNetworking.registerGlobalReceiver(ModPayloads.CoordinateEntryPayload.ID, ((payload, context) -> {
			context.client().execute(() -> {
				clientPlayerData.addCoordinateEntry(CoordinateEntry.fromNbt(payload.nbt()));
			});
		}));

		// Receive coordinate entries
		ClientPlayNetworking.registerGlobalReceiver(ModPayloads.CoordinateEntryArrayPayload.ID, ((payload, context) -> {
			context.client().execute(() -> {
				clientPlayerData.addAllCoordinateEntries(CoordinateEntry.coordinateEntriesFromNbt(payload.nbt()));
			});
		}));


	}
}