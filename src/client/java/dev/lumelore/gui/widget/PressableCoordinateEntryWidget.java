package dev.lumelore.gui.widget;

import dev.lumelore.data.CoordinateEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class PressableCoordinateEntryWidget extends PressablePlainTextWidget implements CoordinateWidget, CoordinateEntryContainer {

    private CoordinateEntry entry;
    private Text text;
    private Text hoverText;

    public PressableCoordinateEntryWidget(int x, int y, int width, int height, CoordinateEntry entry, PressAction onPress) {
        super(x, y, width, height, Text.of(entry.getName()), onPress);
        setEntry(entry);
    }

    @Override
    public CoordinateEntry getEntry() {
        return this.entry;
    }

    @Override
    public void setEntry(CoordinateEntry entry) {
        this.entry = entry;
        // Add star to text if favorite
        this.text = entry.isFavorite()
                ? CoordinateEntry.getStarAsText().copy().append(" ").append(entry.getPositionAsText())
                : entry.getPositionAsText();
        // Add star to text if favorite, but don't underline the star
        this.hoverText = entry.isFavorite()
                ? CoordinateEntry.getStarAsText().copy().append(" ").append(entry.getPositionAsText().copy().setStyle(Style.EMPTY.withUnderline(true)))
                : entry.getPositionAsText().copy().setStyle(Style.EMPTY.withUnderline(true));
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = this.isSelected() ? this.hoverText : this.text;
        context.drawText(super.textRenderer, text, this.getX(), this.getY(), 0, false);
    }


    @Override
    public String getName() {
        return getEntry().getName();
    }
}
