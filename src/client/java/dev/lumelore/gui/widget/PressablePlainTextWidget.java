package dev.lumelore.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

@Environment(EnvType.CLIENT)
public class PressablePlainTextWidget extends PressableTextWidget {
    protected final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    private final Text text;
    private final Text hoverText;

    public PressablePlainTextWidget(int x, int y, int width, int height, Text text, PressAction onPress) {
        super(x, y, width, height, text, onPress, MinecraftClient.getInstance().textRenderer);
        this.text = text;
        this.hoverText = Texts.setStyleIfAbsent(text.copy(), Style.EMPTY.withUnderline(true));
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Text text = this.isSelected() ? this.hoverText : this.text;
        context.drawText(textRenderer, text, this.getX(), this.getY(), 0, false);
    }
}
