package dev.lumelore.gui.widget;

import dev.lumelore.data.CoordinateEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface CoordinateEntryContainer {

    CoordinateEntry getEntry();

    void setEntry(CoordinateEntry entry);

}
