package dev.lumelore.gui.widget;

import dev.lumelore.Enderbook;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

public abstract class CRUDButtons {

    // PENCIL ICONS
    private static final Identifier PEN_DISABLED_FOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/pen/icon_pen_disabled_focused");
    private static final Identifier PEN_DISABLED_UNFOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/pen/icon_pen_disabled_unfocused");
    private static final Identifier PEN_ENABLED_FOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/pen/icon_pen_enabled_focused");
    private static final Identifier PEN_ENABLED_UNFOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/pen/icon_pen_enabled_unfocused");

    // EYEBALL ICONS
    private static final Identifier EYE_DISABLED_FOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/eye/icon_eye_disabled_focused");
    private static final Identifier EYE_DISABLED_UNFOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/eye/icon_eye_disabled_unfocused");
    private static final Identifier EYE_ENABLED_FOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/eye/icon_eye_enabled_focused");
    private static final Identifier EYE_ENABLED_UNFOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/eye/icon_eye_enabled_unfocused");

    // ADD ICONS
    private static final Identifier ADD_DISABLED_FOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/add/icon_add_disabled_focused");
    private static final Identifier ADD_DISABLED_UNFOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/add/icon_add_disabled_unfocused");
    private static final Identifier ADD_ENABLED_FOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/add/icon_add_enabled_focused");
    private static final Identifier ADD_ENABLED_UNFOCUSED = Identifier.of(Enderbook.MOD_ID, "icon/add/icon_add_enabled_unfocused");

    // TODO: Add Delete button

    // Creation methods
    public static TexturedButtonWidget createUpdateButton(int x, int y, ButtonWidget.PressAction onPress) {
        return new TexturedButtonWidget(x, y, 21, 20,
                new ButtonTextures(PEN_ENABLED_UNFOCUSED, PEN_DISABLED_UNFOCUSED, PEN_ENABLED_FOCUSED, PEN_DISABLED_FOCUSED),
                onPress);
    }

    public static TexturedButtonWidget createReadButton(int x, int y, ButtonWidget.PressAction onPress) {
        return new TexturedButtonWidget(x, y, 21, 20,
                new ButtonTextures(EYE_ENABLED_UNFOCUSED, EYE_DISABLED_UNFOCUSED, EYE_ENABLED_FOCUSED, EYE_DISABLED_FOCUSED),
                onPress);
    }

    public static TexturedButtonWidget createCreateButton(int x, int y, ButtonWidget.PressAction onPress) {
        return new TexturedButtonWidget(x, y, 21, 20,
                new ButtonTextures(ADD_ENABLED_UNFOCUSED, ADD_DISABLED_UNFOCUSED, ADD_ENABLED_FOCUSED, ADD_DISABLED_FOCUSED),
                onPress);
    }



}
