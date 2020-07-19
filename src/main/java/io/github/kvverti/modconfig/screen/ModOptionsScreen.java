package io.github.kvverti.modconfig.screen;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class ModOptionsScreen extends Screen {

    private static final int STANDARD_HEIGHT = 20;
    private static final int TOP_BUTTON_WIDTH = 100;
    private static final int DONE_BUTTON_WIDTH = 150;
    private static final int PADDING_H = 5;
    private static final int ROW_WIDTH = 310;
    private static final int SEARCH_FIELD_WIDTH = ROW_WIDTH - (TOP_BUTTON_WIDTH + PADDING_H);

    private final Screen parent;

    /**
     * Search entries.
     */
    private EntryList entries;

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
        TextFieldWidget searchField = this.addButton(new TextFieldWidget(
            this.textRenderer,
            (this.width / 2) - (ROW_WIDTH / 2),
            20,
            SEARCH_FIELD_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("modconfig.search")));
        searchField.setChangedListener(s -> searchText = s);
        searchField.setText(searchText);
        this.addButton(new ButtonWidget(
            searchField.x + SEARCH_FIELD_WIDTH + PADDING_H,
            20,
            TOP_BUTTON_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("modconfig.settings"),
            btn -> {
            }
        ));
        this.entries = this.addChild(new EntryList(this.client, this.width, this.height, 45, this.height - 32, STANDARD_HEIGHT + PADDING_H));
        this.entries.setScrollAmount(scrollPos);
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
                ModOptionsScreen.this.setFocused(null);
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

    /**
     * An entry list for mod options rows.
     */
    private class EntryList extends AlwaysSelectedEntryListWidget<ModOptionsEntry> {

        private int entryIdx = -1;

        public EntryList(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
            super(minecraftClient, width, height, top, bottom, itemHeight);
            this.addEntry(new LabelModOptionsEntry(ModOptionsScreen.this.textRenderer, new LiteralText("Label 1")));
            for(int ii = 0; ii < 5; ii++) {
                this.addEntry(new SettingsModOptionsEntry(new ButtonWidget(
                    30,
                    30,
                    150,
                    STANDARD_HEIGHT,
                    new LiteralText("Minecraft"),
                    btn -> tmpOpenOptionsScreen()
                ), new ButtonWidget(
                    30,
                    30,
                    150,
                    STANDARD_HEIGHT,
                    new LiteralText("Minecraft"),
                    btn -> tmpOpenOptionsScreen()
                ), ModOptionsScreen.this.textRenderer.isRightToLeft()));
            }
            this.addEntry(new LabelModOptionsEntry(ModOptionsScreen.this.textRenderer, new LiteralText("Label 2")));
            for(int ii = 0; ii < 3; ii++) {
                this.addEntry(new SettingsModOptionsEntry(new ButtonWidget(
                    30,
                    30,
                    150,
                    STANDARD_HEIGHT,
                    new LiteralText("Minecraft"),
                    btn -> tmpOpenOptionsScreen()
                ), new ButtonWidget(
                    30,
                    30,
                    150,
                    STANDARD_HEIGHT,
                    new LiteralText("Minecraft"),
                    btn -> tmpOpenOptionsScreen()
                ), ModOptionsScreen.this.textRenderer.isRightToLeft()));
            }
            this.addEntry(new SettingsModOptionsEntry(new ButtonWidget(
                30,
                30,
                150,
                STANDARD_HEIGHT,
                new LiteralText("Minecraft"),
                btn -> tmpOpenOptionsScreen()
            ), null, ModOptionsScreen.this.textRenderer.isRightToLeft()));
        }

        private void tmpOpenOptionsScreen() {
            ModOptionsScreen.this.scrollPos = this.getScrollAmount();
            if(entryIdx != -1) {
                ModOptionsEntry entry = this.getEntry(entryIdx);
                int parity;
                if(entry instanceof SettingsModOptionsEntry) {
                    parity = ((SettingsModOptionsEntry)entry).getFocusParity();
                } else {
                    parity = 0;
                }
                ModOptionsScreen.this.selectedOptionIdx = 2 * entryIdx + parity;
            } else {
                ModOptionsScreen.this.selectedOptionIdx = -1;
            }
            this.client.openScreen(new OptionsScreen(ModOptionsScreen.this, this.client.options));
        }

        public void setFocusedIndex(int idx) {
            if(idx >= 0 && idx < 2 * this.getItemCount()) {
                entryIdx = idx / 2;
                ModOptionsEntry elem = this.getEntry(entryIdx);
                elem.changeFocus(idx % 2 == 0);
            } else {
                if(entryIdx != -1) {
                    ModOptionsEntry elem = this.getEntry(entryIdx);
                    if(elem instanceof SettingsModOptionsEntry) {
                        ((SettingsModOptionsEntry)elem).clearFocus();
                    }
                    entryIdx = -1;
                }
            }
        }

        @Override
        protected int getScrollbarPositionX() {
            return (this.width / 2) + (ROW_WIDTH / 2) + PADDING_H;
        }

        @Override
        public int getRowWidth() {
            return ROW_WIDTH;
        }

        @Override
        protected void renderBackground(MatrixStack matrixStack) {
            ModOptionsScreen.this.renderBackground(matrixStack);
        }

        @Override
        public boolean changeFocus(boolean lookForwards) {
            if(entryIdx == -1) {
                entryIdx = lookForwards ? 0 : this.getItemCount() - 1;
            }
            ModOptionsEntry entry = this.getEntry(entryIdx);
            boolean hasFocus = entry.changeFocus(lookForwards);
            while(!hasFocus && entryIdx != -1) {
                entryIdx += lookForwards ? 1 : -1;
                if(entryIdx >= this.getItemCount()) {
                    entryIdx = -1;
                }
                if(entryIdx != -1) {
                    entry = this.getEntry(entryIdx);
                    hasFocus = entry.changeFocus(lookForwards);
                }
            }
            this.centerScrollOn(entry);
            return entryIdx != -1;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if(keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_UP) {
                // up/down arrows
                boolean down = keyCode == GLFW.GLFW_KEY_DOWN;
                if(entryIdx == -1 || changeFocus(down)) {
                    changeFocus(down);
                }
                return true;
            } else if(keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_LEFT) {
                // left/right arrows (RTL is left-forward, LTR is right-forward)
                boolean forward = keyCode == (ModOptionsScreen.this.textRenderer.isRightToLeft() ? GLFW.GLFW_KEY_LEFT : GLFW.GLFW_KEY_RIGHT);
                changeFocus(forward);
                return true;
            } else if(!super.keyPressed(keyCode, scanCode, modifiers)) {
                return entryIdx != -1 && this.getEntry(entryIdx).keyPressed(keyCode, scanCode, modifiers);
            } else {
                // if the keypress scrolled the selection
                this.setSelected(null);
                return false;
            }
        }
    }
}
