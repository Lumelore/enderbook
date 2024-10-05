package dev.lumelore.gui;

import dev.lumelore.data.CoordinateEntry;
import dev.lumelore.gui.widget.PlainTextWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;

public class EnderbookCRUDScreen extends Screen {

    private int mode = InteractionMode.READ.ordinal();
    private CoordinateEntry selectedEntry;
    private Screen parent;

    /*
    // Widgets
    private final PlainTextWidget namePlainText = new PlainTextWidget();
    private final TextFieldWidget nameEditText = new TextFieldWidget();

    private final PlainTextWidget xPlainText = new PlainTextWidget();
    private final TextFieldWidget xEditText = new TextFieldWidget();

    private final PlainTextWidget yPlainText = new PlainTextWidget();
    private final TextFieldWidget yEditText = new TextFieldWidget();

    private final PlainTextWidget zPlainText = new PlainTextWidget();
    private final TextFieldWidget zEditText = new TextFieldWidget();
*/


    protected EnderbookCRUDScreen(Screen parent) {
        super(Text.of("Enderbook View & Edit Screen"));
        this.parent = parent;
        if (parent instanceof EnderbookScreen) {
            this.selectedEntry = ((EnderbookScreen) parent).getSelectedEntry();
        }


    }

    protected EnderbookCRUDScreen(Screen parent, int mode) {
        super(Text.of("Enderbook View & Edit Screen"));
        this.parent = parent;
        this.mode = Math.clamp(mode, 0, InteractionMode.cachedValues.size());
        if (parent instanceof EnderbookScreen) {
            this.selectedEntry = ((EnderbookScreen) parent).getSelectedEntry();
        }
    }

    protected EnderbookCRUDScreen(Screen parent, InteractionMode mode) {
        super(Text.of("Enderbook View & Edit Screen"));
        this.parent = parent;
        this.mode = mode.ordinal();
        if (parent instanceof EnderbookScreen) {
            this.selectedEntry = ((EnderbookScreen) parent).getSelectedEntry();
        }
    }

    @Override
    protected void init() {
        super.init();

        switch (InteractionMode.of(mode)) {
            case InteractionMode.CREATE:

                break;
            case InteractionMode.READ:

                break;
            case InteractionMode.UPDATE:

                break;
            case InteractionMode.DELETE:
        }

    }

    @Override
    public void close() {
        MinecraftClient.getInstance().setScreen(parent);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    public enum InteractionMode {
        CREATE,
        READ,
        UPDATE,
        DELETE;

        static final List<InteractionMode> cachedValues = List.of(values());

        /**
         * Converts an int to an InteractionMode
         * @return The InteractionMode with the ordinal of the input int
         */
        static InteractionMode of(int mode) {
            return InteractionMode.cachedValues.get(Math.clamp(mode, 0, cachedValues.size()));
        }
    }

}
