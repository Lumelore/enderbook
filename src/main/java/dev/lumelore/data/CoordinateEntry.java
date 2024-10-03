package dev.lumelore.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.UUID;

public class CoordinateEntry extends BlockPos {

    private static final char star = '☆';
    private String name;
    // Dimension obtained as follows:
    // player.getWorld().getDimensionEntry().getIdAsString()
    // Convert id into user-friendly name: minecraft:overworld -> Overworld
    private String dimension = "";
    private boolean favorite = false;
    private UUID uuid = UUID.randomUUID();


    public CoordinateEntry(String name, BlockPos blockPos) {
        super(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        this.name = name;
    }

    public CoordinateEntry(String name, int x, int y, int z) {
        super(x, y, z);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDimension() {
        return dimension;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /**
     * Used for piecing together a coordinate entry from NBT data
     *
     * @param uuid The ID of this coordinate entry
     */
    private void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    public NbtCompound toNbt() {
        NbtCompound entry = new NbtCompound();

        entry.putUuid("ID", this.uuid);
        entry.putIntArray("blockPos", new int[] {this.getX(), this.getY(), this.getZ()});
        entry.putString("name", this.name);
        entry.putString("dimension", this.dimension);
        entry.putBoolean("favorite", this.favorite);

        return entry;
    }

    public static CoordinateEntry fromNbt(NbtCompound nbt) {
        // Get data from NBT
        UUID ID = nbt.getUuid("ID");
        int[] blockPos = nbt.getIntArray("blockPos");
        String name = nbt.getString("name");
        String dimension = nbt.getString("dimension");
        boolean fav = nbt.getBoolean("favorite");
        // Put data into object
        CoordinateEntry entry = new CoordinateEntry(name, new BlockPos(blockPos[0], blockPos[1], blockPos[2]));
        entry.setDimension(dimension);
        entry.setFavorite(fav);
        entry.setUuid(ID);
        // Return object
        return entry;
    }

    /**
     * Converts a list of coordinate entries to Nbt. The index of the
     * coordinate entry in the list is used as the key.
     *
     * @return A list of coordinate entries. Their index is the key
     */
    public static NbtCompound coordinateEntriesToNbt(ArrayList<CoordinateEntry> list) {
        NbtCompound listAsNbt = new NbtCompound();
        for (int i = 0; i < list.size(); i++) {
            listAsNbt.put(String.valueOf(i), list.get(i).toNbt());
        }
        return listAsNbt;
    }

    /**
     * Converts a list of type {@link CoordinateEntry} in Nbt format to an {@link ArrayList}
     *
     * @param nbt A list of coordinate entries in Nbt format
     * @return list of coordinate entries
     */
    public static ArrayList<CoordinateEntry> coordinateEntriesFromNbt(NbtCompound nbt) {
        ArrayList<CoordinateEntry> entries = new ArrayList<>();

        nbt.getKeys().forEach(key -> entries.add(CoordinateEntry.fromNbt((NbtCompound) nbt.get(key))));

        return entries;
    }

    public Text getNameAsText() {
        return Text.literal(getName());
    }

    /**
     * @return The name as {@link Text}. Will have a star prepended if favorite.
     */
    public Text getNameAndFavorite() {
        return favorite ? Text.of(star + " " + getName()) : getNameAsText();
    }

    public Text getPositionAsText() {
        return Text.literal(this.getX() + ", " + this.getY() + ", " + this.getZ());
    }

    public static Text getStarAsText() {
        return Text.literal(Character.toString(star));
    }



}
