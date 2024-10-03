package dev.lumelore.gui;

import dev.lumelore.Enderbook;
import dev.lumelore.EnderbookClient;
import dev.lumelore.data.CoordinateEntry;
import dev.lumelore.gui.widget.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class EnderbookScreen extends Screen {

    // Weapons/Tools: 🗡🪓⛏🏹🎣🔱✂
    // Food: 🍖🧪☠
    // Nature: ☀☽☁⚡❄⛄🔥
    // Misc: ⚓⚐⌂🔔⌚
    // Symbols: ☺☹♡♢♤♧♪♫☮☯⚠❌✔

    // They all have different widths, none are more than 10
    private ArrayList<String> symbols = new ArrayList<>(List.of("☆", "🗡", "🪓", "⛏", "🏹", "🎣", "🔱", "✂",
                                                                "🍖", "🧪", "☠",
                                                                "☀", "☽", "☁", "⚡", "❄", "⛄", "🔥",
                                                                "⚓", "⚐", "⌂", "🔔", "⌚",
                                                                "☺", "☹", "♡", "♢", "♤", "♧", "♪", "♫", "☮", "☯", "⚠", "❌", "✔"));


    // Values pertain to book background
    private int leftEdge = 0;
    private int topEdge = 0;
    private int bgWidth = 194;
    private int bgHeight = 180;
    private int rightEdge = 0;
    private int screenToCornerWidth = 0;
    // Values pertaining to page contents
    private final List<CoordinateWidget> bookContent = Lists.newArrayList();
    private final int pageSize = 12;
    private int pageNumber = 1;
    // This is the entry that is displayed for detailed viewing and editing
    private CoordinateEntry selectedEntry = null;
    // WIDGETS
    // pageDisplay shows the page number in the top right
    private PlainTextWidget pageDisplay;
    private EnderbookPagingWidget nextPageButton;
    private EnderbookPagingWidget prevPageButton;
    // CUD buttons
    private TexturedButtonWidget editButton;
    private TexturedButtonWidget viewButton;
    private TexturedButtonWidget createButton;

    public EnderbookScreen(Text title) {
        super(title);

        // Create the list of widgets to be displayed in the book
        // only once when the screen is created
        generateItemsList();
    }

    @Override
    protected void init() {
        super.init();

        this.leftEdge = (this.width - this.bgWidth) / 2;
        this.topEdge = getPercentHeight(0.05f);
        // this.width - (leftEdge + bgWidth) = width void space
        this.screenToCornerWidth = this.width - (leftEdge + bgWidth);
        // + bgWidth to void space width to get right edge
        this.rightEdge = screenToCornerWidth + bgWidth;


        ButtonWidget closeBtn = ButtonWidget
                .builder(ScreenTexts.DONE, btn->close())
                .size(this.textRenderer.getWidth(ScreenTexts.DONE) + 20, 20)
                .build();
        closeBtn.setPosition(getPercentWidth(0.5f) - (closeBtn.getWidth() / 2), topEdge*2 + bgHeight);

        // CRUD Buttons
        createButton = CRUDButtons.createCreateButton(rightEdge + 10, topEdge, btn -> {});

        viewButton = CRUDButtons.createReadButton(rightEdge + 10, createButton.getY() + 25, btn -> MinecraftClient.getInstance()
                .setScreen(new EnderbookCUDScreen(Text.of("Enderbook View & Edit Screen"), this)));

        editButton = CRUDButtons.createUpdateButton(rightEdge + 10, viewButton.getY() + 25, btn -> MinecraftClient.getInstance()
                .setScreen(new EnderbookCUDScreen(Text.of("Enderbook View & Edit Screen"), this)));



        // Initially update CRUD buttons
        updateCUDButtonWidgets();


        // Turn page button widgets
        nextPageButton = createNextPageWidget();
        prevPageButton = createPrevPageWidget();
        updatePageTurnWidgets();
        // Page number display widget
        pageDisplay = new PlainTextWidget(rightEdge - 35, topEdge + 7, 30, 20, Text.of(Integer.toString(pageNumber)));
        pageDisplay.alignRight();

        // Add other widgets to screen
        this.addDrawableChild(closeBtn);
        this.addDrawableChild(viewButton);
        this.addDrawableChild(editButton);
        this.addDrawableChild(createButton);
        this.addDrawableChild(pageDisplay);
        this.addDrawableChild(nextPageButton);
        this.addDrawableChild(prevPageButton);

        // Draw entries on page
        int pageDrawIndex = 0;
        for (int i = pageSize * (pageNumber - 1); (i < pageSize * pageNumber) && (i < bookContent.size()); i++) {
            bookContent.get(i).setPosition(leftEdge + 15, topEdge + 15 + (12 * pageDrawIndex));
            // Spacing for not favorite-ed entries
            if (bookContent.get(i) instanceof CoordinateEntryContainer && !((CoordinateEntryContainer) bookContent.get(i)).getEntry().isFavorite()) {
                bookContent.get(i).setX(leftEdge + 15 + this.textRenderer.getWidth("☆ "));
            }
            // Add widget to draw
            this.addCoordinateWidgetToDraw(bookContent.get(i));
            pageDrawIndex++;
        }

    }

    // TODO: Make it so the last entry of a page can't be a title
    private void generateItemsList() {
        ArrayList<PressableCoordinateEntryWidget> entries = coordinateEntriesToWidgets();
        while (!entries.isEmpty()) {
            // If not empty ...
            if (!bookContent.isEmpty()) {
               // ... check to see if next item in list should be title or entry
                if ( ( bookContent.getLast() instanceof CoordinateEntryTitleWidget || bookContent.getLast().sameNameAsOther(entries.getFirst()) )
                    && bookContent.size() % pageSize != 0 ) {
                    // Add entry
                    bookContent.add(entries.removeFirst());
                }
                else {
                    // Create Title
                    bookContent.add(new CoordinateEntryTitleWidget(0, 0, 150, 20, Text.of(entries.getFirst().getName())));
                }
            }
            // If empty, the first entry will always be a title
            else {
                bookContent.add(new CoordinateEntryTitleWidget(0, 0, 150, 20, Text.of(entries.getFirst().getName())));
            }
        }
    }

    private ArrayList<PressableCoordinateEntryWidget> coordinateEntriesToWidgets() {
        ArrayList<PressableCoordinateEntryWidget> returnedList = new ArrayList<>(EnderbookClient.clientPlayerData.savedCoordinates.size());
        EnderbookClient.clientPlayerData.savedCoordinates.forEach(entry -> {
            returnedList.add(new PressableCoordinateEntryWidget(0, 0, 150, 10, entry, btn -> setCurrentEntry(entry)));
        });
        return returnedList;
    }

    /**
     * Adds Coordinate Entry widgets to the list of drawables. (It has to cast them to {@code T} in order to do so)
     *
     * @param widget The widget to add to be drawn.
     * @param <T> {@link Element} & {@link Drawable} & {@link Selectable}
     */
    @SuppressWarnings("unchecked")
    private <T extends Element & Drawable & Selectable> void addCoordinateWidgetToDraw(CoordinateWidget widget) {
        if (widget instanceof Element && widget instanceof Drawable && widget instanceof Selectable) {
            this.addDrawableChild((T) widget);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        // This is called every frame. Add stuff here that needs to be updated every frame

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        context.drawTexture(Identifier.of(Enderbook.MOD_ID, "textures/gui/enderbook_gui.png"),
                (this.width - bgWidth) / 2, getPercentHeight(0.05f), 20, 1, bgWidth, bgHeight);

        /*
        if (selectedEntry != null) {
            // Draw dark rect in right void space
            context.fill((rightEdge) + (int)(screenToCornerWidth * 0.1), getPercentHeight(0.05f),
                         (rightEdge) + (int)(screenToCornerWidth * 0.9), getPercentHeight(0.7f),
                       0x4D2E2E2E);
            // Border shadow
            context.drawBorder((rightEdge) + (int)(screenToCornerWidth * 0.1)+1, getPercentHeight(0.05f)+1,
                    (int)(screenToCornerWidth * 0.8), getPercentHeight(0.65f),
                    0xFF2E2E2E);
            // Border
            context.drawBorder((rightEdge) + (int)(screenToCornerWidth * 0.1), getPercentHeight(0.05f),
                               (int)(screenToCornerWidth * 0.8), getPercentHeight(0.65f),
                          0xFFFFFFFF);
        }*/

    }

    private int getPercentHeight(float percent) {
        return (int) (this.height * percent);
    }

    private int getPercentWidth(float percent) {
        return (int) (this.width * percent);
    }

    private int positionHorizontalCenter(int width) {
        return (this.width - width) / 2;
    }

    private void setCurrentEntry(CoordinateEntry entry) {
        this.selectedEntry = entry;
        updateCUDButtonWidgets();
    }

    private void updateCUDButtonWidgets() {
        editButton.active = (selectedEntry != null);
        viewButton.active = (selectedEntry != null);
    }

    /* ==================== *
     * PAGE TURNING METHODS *
     * ==================== */

    private boolean canPageRight() {
        // Check if there is more content for another page
        return bookContent.size() > 12 * pageNumber;
    }

    private boolean canPageLeft() {
        return pageNumber > 1;
    }

    private void pageRight() {
        if (canPageRight()) {
            pageNumber++;
            clearAndInit();
        }
    }

    private void pageLeft() {
        if (canPageLeft()) {
            pageNumber--;
            clearAndInit();
        }
    }

    private EnderbookPagingWidget createNextPageWidget() {
        return new EnderbookPagingWidget(rightEdge - (23*2), topEdge + bgHeight - 26, true, btn -> pageRight());
    }

    private EnderbookPagingWidget createPrevPageWidget() {
        return new EnderbookPagingWidget(leftEdge + 23, topEdge + bgHeight - 26, false, btn -> pageLeft());
    }

    private void updatePageTurnWidgets() {
        // Check to see if there are more pages to the left. If not, hide the
        // prevPageButton, else, assign it to a new instance. (This clears it being selected)
        if (!canPageLeft()) {
            prevPageButton.active = false;
            prevPageButton.visible = false;
        }
        // Check to see if there are more pages to the right. If not, hide the
        // nextPageButton, else, assign it to a new instance. (This clears it being selected)
        if (!canPageRight()) {
            nextPageButton.active = false;
            nextPageButton.visible = false;
        }
    }

}
