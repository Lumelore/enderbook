package dev.lumelore.gui.widget;

import dev.lumelore.Enderbook;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnderbookPagingWidget extends ButtonWidget {

    private static final Identifier PAGE_FORWARD_TEXTURE = Identifier.of(Enderbook.MOD_ID, "enderbook_page_forward");
    private static final Identifier PAGE_FORWARD_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/page_forward_highlighted");

    private static final Identifier PAGE_BACKWARD_TEXTURE = Identifier.of(Enderbook.MOD_ID, "enderbook_page_backward");
    private static final Identifier PAGE_BACKWARD_HIGHLIGHTED_TEXTURE = Identifier.ofVanilla("widget/page_backward_highlighted");

    private boolean turnToNext;

    public EnderbookPagingWidget(int x, int y, boolean turnsToNextPage, PressAction onPress) {
        super(x, y, 23, 13, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.turnToNext = turnsToNextPage;
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // This will store which graphic to draw depending on stuff
        Identifier identifier;
        // Use the right facing arrow graphic
        if (this.turnToNext) {
            identifier = this.isSelected() ? PAGE_FORWARD_HIGHLIGHTED_TEXTURE : PAGE_FORWARD_TEXTURE;
        }
        // Else use the left facing arrow graphic
        else {
            identifier = this.isSelected() ? PAGE_BACKWARD_HIGHLIGHTED_TEXTURE : PAGE_BACKWARD_TEXTURE;
        }
        // 23, 13 is the size of the texture
        context.drawGuiTexture(identifier, this.getX(), this.getY(),23, 13);
    }


    public void playDownSound(SoundManager soundManager) {
        // Always play the page turn sound
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F));
    }


}
