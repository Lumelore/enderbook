package dev.lumelore.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

@Environment(EnvType.CLIENT)
public class PlainTextWidget extends TextWidget {

    public PlainTextWidget(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message, MinecraftClient.getInstance().textRenderer);
        this.setTextColor(0);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int textWidth = this.getTextRenderer().getWidth(this.getMessage());
        // 0.5f is horizontal alignment
        int adjustedXPos = this.getX() + Math.round(0.5f * (float)(this.getWidth() - textWidth));
        int adjustedYPos = this.getY() + (this.getHeight() - 10) / 2;
        // If text doesn't fit in widget, trim and append ellipsis
        OrderedText niceText = textWidth > this.getWidth() ? this.trim(this.getMessage(), this.getWidth()) : this.getMessage().asOrderedText();
        context.drawText(this.getTextRenderer(), niceText, adjustedXPos, adjustedYPos, this.getTextColor(), false);
    }


    private OrderedText trim(Text text, int width) {
        return Language.getInstance().reorder(StringVisitable.concat(
                this.getTextRenderer().trimToWidth(text, width - this.getTextRenderer().getWidth(ScreenTexts.ELLIPSIS)),
                ScreenTexts.ELLIPSIS));
    }

}
