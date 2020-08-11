package io.github.kvverti.modconfig.screen;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class ModOptionsScreen extends Screen {

    public static final int STANDARD_WIDTH = 150;
    public static final int STANDARD_HEIGHT = 20;
    public static final int TOP_BUTTON_WIDTH = 100;
    public static final int DONE_BUTTON_WIDTH = 150;
    public static final int PADDING_H = 5;
    public static final int ROW_WIDTH = 310;
    public static final int SEARCH_FIELD_WIDTH = ROW_WIDTH - (TOP_BUTTON_WIDTH + PADDING_H);
    public static final int LIST_AREA_TOP_OFFSET = 45;
    public static final int LIST_AREA_BOTTOM_OFFSET = 32;

    private final Screen parent;

    /**
     * Search entries.
     */
    private ModOptionsEntryList entries;

    // saved state
    private String searchText = "";
    private double scrollPos = 0.0;
    private int selectedOptionIdx = -1;

    public ModOptionsScreen(Screen parent) {
        super(new TranslatableText("modconfig.narrator.options.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.entries = new ModOptionsEntryList(this, this.client, this.width, this.height, LIST_AREA_TOP_OFFSET, this.height - LIST_AREA_BOTTOM_OFFSET, STANDARD_HEIGHT + PADDING_H);
        TextFieldWidget searchField = this.addButton(new TextFieldWidget(
            this.textRenderer,
            (this.width / 2) - (ROW_WIDTH / 2),
            20,
            SEARCH_FIELD_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("modconfig.search")));
        searchField.setChangedListener(s -> {
            searchText = s;
            entries.search(s);
        });
        searchField.setText(searchText);
        this.entries.setScrollAmount(scrollPos);
        this.addButton(new ButtonWidget(
            searchField.x + SEARCH_FIELD_WIDTH + PADDING_H,
            20,
            TOP_BUTTON_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("modconfig.settings"),
            btn -> {
            }
        ));
        this.addChild(this.entries);
        this.addButton(new ButtonWidget(
            (this.width / 2) - (DONE_BUTTON_WIDTH / 2),
            this.height - 26,
            DONE_BUTTON_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("gui.done"),
            btn -> this.onClose()
        ));
        if(selectedOptionIdx != -1) {
            this.setFocused(entries);
            entries.setFocusedIndex(selectedOptionIdx);
        }
    }

    void setScrollPos(double scrollPos) {
        this.scrollPos = scrollPos;
    }

    void setSelectedOptionIdx(int selectedOptionIdx) {
        this.selectedOptionIdx = selectedOptionIdx;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        entries.render(matrices, mouseX, mouseY, delta);
        this.drawCenteredText(matrices, this.textRenderer, new TranslatableText("modconfig.title"), this.width / 2, 10, 0xffffff);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ESCAPE) {
            // escape the scroll area if possible
            if(this.getFocused() == entries) {
                entries.setFocusedIndex(-1);
                selectedOptionIdx = -1;
                this.setFocused(null);
                this.changeFocus(true);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        this.client.openScreen(this.parent);
    }
}
