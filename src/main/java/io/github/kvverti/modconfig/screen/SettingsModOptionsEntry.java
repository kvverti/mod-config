package io.github.kvverti.modconfig.screen;

import javax.annotation.Nullable;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;

/**
 * An settings entry is composed of a left side button and an optional right side button.
 */
class SettingsModOptionsEntry extends ModOptionsEntry {

    private final AbstractButtonWidget button1;
    @Nullable
    private final AbstractButtonWidget button2;
    private final boolean rtl;
    @Nullable
    private AbstractButtonWidget focused;

    SettingsModOptionsEntry(AbstractButtonWidget button1, @Nullable AbstractButtonWidget button2, boolean rtl) {
        this.button1 = button1;
        this.button2 = button2;
        this.rtl = rtl;
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

    public int getFocusParity() {
        return (focused == null || focused == button1) ? 0 : 1;
    }

    public void clearFocus() {
        if(focused != null && focused.isFocused()) {
            focused.changeFocus(true);
        }
        focused = null;
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
        int rightPos = x + (entryWidth / 2) + (spacingH / 2);
        button1.x = rtl ? rightPos : x;
        button1.y = y;
        button1.setWidth(entryWidth / 2 - (spacingH / 2));
        button1.render(matrices, mouseX, mouseY, tickDelta);
        if(button2 != null) {
            button2.x = rtl ? x : rightPos;
            button2.y = y;
            button2.setWidth(entryWidth / 2 - (spacingH / 2));
            button2.render(matrices, mouseX, mouseY, tickDelta);
        }
    }
}
