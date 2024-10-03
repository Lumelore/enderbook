package dev.lumelore.data;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerData {

    public int dirtBroken = 0;

    public ArrayList<CoordinateEntry> savedCoordinates = new ArrayList<>();

    public void sortCoordinateEntries() {
        savedCoordinates.sort((name1, name2) -> name1.getName().compareToIgnoreCase(name2.getName()));
    }

    public void removeCoordinateEntry(UUID uuid) {
        savedCoordinates.removeIf(entry -> entry.getUuid().equals(uuid));
    }

    public void addCoordinateEntry(CoordinateEntry entry) {
        savedCoordinates.add(entry);
        sortCoordinateEntries();
    }

    public void addAllCoordinateEntries(ArrayList<CoordinateEntry> entries) {
        savedCoordinates.addAll(entries);
        sortCoordinateEntries();
    }

}
