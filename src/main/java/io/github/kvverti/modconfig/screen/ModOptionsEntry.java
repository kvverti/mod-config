package io.github.kvverti.modconfig.screen;

import javax.annotation.Nullable;

import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;

/**
 * An entry is composed of a left side button and an optional right side button.
 */
class ModOptionsEntry extends AlwaysSelectedEntryListWidget.Entry<ModOptionsEntry> {

    private final ButtonWidget button1;
    @Nullable
    private final ButtonWidget button2;
    @Nullable
    private ButtonWidget focused;

    ModOptionsEntry(ButtonWidget button1, @Nullable ButtonWidget button2) {
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
