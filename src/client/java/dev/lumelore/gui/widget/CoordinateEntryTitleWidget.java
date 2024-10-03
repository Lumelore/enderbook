package dev.lumelore.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

@Environment(EnvType.CLIENT)
public class CoordinateEntryTitleWidget extends PlainTextWidget implements CoordinateWidget {

    private float horizontalBias = 0;

    public CoordinateEntryTitleWidget(int x, int y, int width, int height, Text title) {
        super(x, y, width, height, title);
        this.setTextColor(0);
    }

    public String getName() {
        return this.getMessage().toString();
    }

    public void setHorizontalBias(float bias) {
        this.horizontalBias = bias;
    }


    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int textWidth = this.getTextRenderer().getWidth(this.getMessage());
        // 0.5f is horizontal alignment
        int adjustedXPos = this.getX() + Math.round(horizontalBias * (float)(this.getWidth() - textWidth));
        int adjustedYPos = this.getY() + (this.getHeight() - this.height) / 2;
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
