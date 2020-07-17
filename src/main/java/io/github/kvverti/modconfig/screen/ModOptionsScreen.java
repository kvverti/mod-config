package io.github.kvverti.modconfig.screen;

import javax.annotation.Nullable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class ModOptionsScreen extends Screen {

    private EntryList buttons;

    public ModOptionsScreen() {
        super(new TranslatableText("modoptions.narrator.options.title"));
    }

    @Override
    protected void init() {
        super.init();
        this.buttons = this.addChild(new EntryList(this.client, this.width, this.height, 32, this.height - 64, 30));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        buttons.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

    /**
     * An entry list for mod options rows.
     */
    private class EntryList extends AlwaysSelectedEntryListWidget<ModOptionsScreen.Entry> {

        private int entryIdx = -1;

        public EntryList(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
            super(minecraftClient, width, height, top, bottom, itemHeight);
            for(int ii = 0; ii < 11; ii++) {
                this.addEntry(new ModOptionsScreen.Entry(new ButtonWidget(
                    30,
                    30,
                    150,
                    20,
                    new LiteralText("Minecraft"),
                    btn -> this.client.openScreen(new OptionsScreen(ModOptionsScreen.this, this.client.options))
                ), new ButtonWidget(
                    30,
                    30,
                    150,
                    20,
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
            return 310;
        }

        @Override
        protected void renderBackground(MatrixStack matrixStack) {
            ModOptionsScreen.this.renderBackground(matrixStack);
        }

        @Override
        public boolean changeFocus(boolean lookForwards) {
            if(entryIdx == -1) {
                entryIdx = lookForwards ? 0 : this.getItemCount() - 1;
                ModOptionsScreen.Entry entry = this.getEntry(entryIdx);
                entry.changeFocus(lookForwards);
                this.centerScrollOn(entry);
            } else {
                ModOptionsScreen.Entry entry = this.getEntry(entryIdx);
                boolean hasFocus = entry.changeFocus(lookForwards);
                if(!hasFocus) {
                    entryIdx += lookForwards ? 1 : -1;
                    if(entryIdx >= this.getItemCount()) {
                        entryIdx = -1;
                    }
                    if(entryIdx != -1) {
                        ModOptionsScreen.Entry entry1 = this.getEntry(entryIdx);
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

    /**
     * An entry is composed of a left side button and an optional right side button.
     */
    private static class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {

        private final ButtonWidget button1;
        @Nullable
        private final ButtonWidget button2;
        @Nullable
        private ButtonWidget focused;

        private Entry(ButtonWidget button1, @Nullable ButtonWidget button2) {
            this.button1 = button1;
            this.button2 = button2;
        }

        @Override
        public boolean changeFocus(boolean lookForwards) {
            if(focused == null) {
                if(!lookForwards && button2 != null) {
                    focused = button2;
                    return button2.changeFocus(lookForwards);
                } else {
                    focused = button1;
                    return button1.changeFocus(lookForwards);
                }
            } else if(focused == button1) {
                button1.changeFocus(lookForwards);
                if(this.button2 != null && lookForwards) {
                    focused = button2;
                    return button2.changeFocus(lookForwards);
                } else {
                    focused = null;
                    return false;
                }
            } else if(focused == button2) {
                button2.changeFocus(lookForwards);
                if(!lookForwards) {
                    focused = button1;
                    return button1.changeFocus(lookForwards);
                } else {
                    focused = null;
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return focused != null && focused.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if(this.button1.isMouseOver(mouseX, mouseY)) {
                return this.button1.mouseClicked(mouseX, mouseY, button);
            } else if(this.button2 != null && this.button2.isMouseOver(mouseX, mouseY)) {
                return this.button2.mouseClicked(mouseX, mouseY, button);
            } else {
                return false;
            }
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int spacingH = 10;
            button1.x = x;
            button1.y = y;
            button1.setWidth(entryWidth / 2 - (spacingH / 2));
            button1.render(matrices, mouseX, mouseY, tickDelta);
            if(button2 != null) {
                button2.x = x + (entryWidth / 2) + (spacingH / 2);
                button2.y = y;
                button2.setWidth(entryWidth / 2 - (spacingH / 2));
                button2.render(matrices, mouseX, mouseY, tickDelta);
            }
        }
    }
}
