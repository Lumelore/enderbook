package dev.lumelore.data;

import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.UUID;

public class CoordinateEntryModification {

    // Hold a list of entries to add
    private ArrayList<CoordinateEntry> toAdd = new ArrayList<>();

    // Hold a list of entries to remove by UUID
    private ArrayList<UUID> toRemove = new ArrayList<>();

    // Modifications to existing occur by marking it for removal, and then adding it with the changes applied

    public CoordinateEntryModification() {}

    public void addEntry(CoordinateEntry entry) {
        toAdd.add(entry);
    }

    public void removeEntry(UUID uuidToRemove) {
        toRemove.add(uuidToRemove);
    }

    public void removeEntry(CoordinateEntry entryToRemove) {
        toRemove.add(entryToRemove.getUuid());
    }

    public void addModification(CoordinateEntry entry) {
        toAdd.add(entry);
        toRemove.add(entry.getUuid());
    }

    public NbtCompound toNbt() {
        NbtCompound thisAsNbt = new NbtCompound();

        thisAsNbt.put("toAdd", CoordinateEntry.coordinateEntriesToNbt(toAdd));

        return thisAsNbt;
    }

    /**
     * Converts the {@link ArrayList} of UUIDs to {@link NbtCompound} format.
     *
     * @return ArrayList as NBT
     */
    public NbtCompound uuidListToNbt() {
        NbtCompound listAsNbt = new NbtCompound();
        for (int i = 0; i < toRemove.size(); i++) {
            listAsNbt.putUuid(String.valueOf(i), toRemove.get(i));
        }
        return listAsNbt;
    }

    /**
     * Converts an {@link NbtCompound} representing a UUID list into an {@link ArrayList}
     *
     * @return ArrayList as NBT
     */
    public static ArrayList<UUID> uuidListFromNbt(NbtCompound nbt) {
        ArrayList<UUID> entries = new ArrayList<>();

        nbt.getKeys().forEach(key -> entries.add(nbt.getUuid(key)));

        return entries;
    }


}
