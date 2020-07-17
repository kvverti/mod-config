package io.github.kvverti.modconfig.screen;

import net.minecraft.client.MinecraftClient;
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
     * Search text field.
     */
    private TextFieldWidget searchField;

    /**
     * Button for the ModConfig settings screen
     */
    private ButtonWidget modConfigSettings;

    /**
     * Done button.
     */
    private ButtonWidget doneBtn;

    /**
     * Search entries.
     */
    private EntryList entries;

    public ModOptionsScreen(Screen parent) {
        super(new TranslatableText("modconfig.narrator.options.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.searchField = this.addChild(new TextFieldWidget(
            this.textRenderer,
            (this.width / 2) - (ROW_WIDTH / 2),
            20,
            SEARCH_FIELD_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("modconfig.search")));
        this.modConfigSettings = this.addButton(new ButtonWidget(
            this.searchField.x + SEARCH_FIELD_WIDTH + PADDING_H,
            20,
            TOP_BUTTON_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("modconfig.settings"),
            btn -> {
            }
        ));
        this.doneBtn = this.addButton(new ButtonWidget(
            (this.width / 2) - (DONE_BUTTON_WIDTH / 2),
            this.height - 26,
            DONE_BUTTON_WIDTH,
            STANDARD_HEIGHT,
            new TranslatableText("gui.done"),
            btn -> this.onClose()
        ));
        this.entries = this.addChild(new EntryList(this.client, this.width, this.height, 50, this.height - 32, 30));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        entries.render(matrices, mouseX, mouseY, delta);
        this.drawCenteredText(matrices, this.textRenderer, new TranslatableText("modconfig.title"), this.width / 2, 10, 0xffffff);
        searchField.render(matrices, mouseX, mouseY, delta);
        modConfigSettings.render(matrices, mouseX, mouseY, delta);
        doneBtn.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
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
            for(int ii = 0; ii < 11; ii++) {
                this.addEntry(new ModOptionsEntry(new ButtonWidget(
                    30,
                    30,
                    150,
                    STANDARD_HEIGHT,
                    new LiteralText("Minecraft"),
                    btn -> this.client.openScreen(new OptionsScreen(ModOptionsScreen.this, this.client.options))
                ), new ButtonWidget(
                    30,
                    30,
                    150,
                    STANDARD_HEIGHT,
                    new LiteralText("Minecraft"),
                    btn -> this.client.openScreen(new OptionsScreen(ModOptionsScreen.this, this.client.options))
                )));
            }
        }

        @Override
        protected int getScrollbarPositionX() {
            return this.width - 10;
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
                ModOptionsEntry entry = this.getEntry(entryIdx);
                entry.changeFocus(lookForwards);
                this.centerScrollOn(entry);
            } else {
                ModOptionsEntry entry = this.getEntry(entryIdx);
                boolean hasFocus = entry.changeFocus(lookForwards);
                if(!hasFocus) {
                    entryIdx += lookForwards ? 1 : -1;
                    if(entryIdx >= this.getItemCount()) {
                        entryIdx = -1;
                    }
                    if(entryIdx != -1) {
                        ModOptionsEntry entry1 = this.getEntry(entryIdx);
                        entry1.changeFocus(lookForwards);
                        this.centerScrollOn(entry1);
                    }
                }
            }
            return entryIdx != -1;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if(!super.keyPressed(keyCode, scanCode, modifiers)) {
                return entryIdx != -1 && this.getEntry(entryIdx).keyPressed(keyCode, scanCode, modifiers);
            }
            // if the keypress scrolled the selection
            this.setSelected(null);
            return false;
        }
    }
}
