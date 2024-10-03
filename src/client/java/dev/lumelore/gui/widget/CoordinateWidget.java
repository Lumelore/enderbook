package dev.lumelore.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.widget.Widget;

@Environment(EnvType.CLIENT)
public interface CoordinateWidget extends Widget {

    String getName();

    default boolean sameNameAsOther(CoordinateWidget widget) {
        return widget.getName().equals(getName());
    }
}
