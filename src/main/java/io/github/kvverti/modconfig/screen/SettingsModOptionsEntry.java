package io.github.kvverti.modconfig.screen;

import javax.annotation.Nullable;

import io.github.kvverti.modconfig.data.option.widget.OverlayRenderable;
import io.github.kvverti.modconfig.iface.ClearFocus;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;

/**
 * An settings entry is composed of a left side button and an optional right side button.
 */
class SettingsModOptionsEntry extends ModOptionsEntry {

    private final ClickableWidget button1;
    @Nullable
    private final ClickableWidget button2;
    private final boolean rtl;
    @Nullable
    private ClickableWidget focused;

    SettingsModOptionsEntry(ClickableWidget button1, @Nullable ClickableWidget button2, boolean rtl) {
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
            if(button1.changeFocus(lookForwards)) {
                return true;
            } else {
                if(this.button2 != null && lookForwards) {
                    focused = button2;
                    return button2.changeFocus(lookForwards);
                } else {
                    focused = null;
                    return false;
                }
            }
        } else if(focused == button2) {
            if(button2.changeFocus(lookForwards)) {
                return true;
            } else {
                if(!lookForwards) {
                    focused = button1;
                    return button1.changeFocus(lookForwards);
                } else {
                    focused = null;
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public int getFocusColumnParity() {
        return (focused == null || focused == button1) ? 0 : 1;
    }

    @Override
    public boolean setFocusedColumn(int col) {
        clearFocus();
        if(col == 0) {
            focused = button1;
        } else if(col == 1) {
            focused = button2;
        }
        if(focused != null) {
            return focused.changeFocus(true);
        }
        return false;
    }

    @Override
    public void clearFocus() {
        if(focused != null) {
            ((ClearFocus)focused).modcfg_clearFocus();
        }
        focused = null;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return focused != null && focused.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return focused != null && focused.charTyped(chr, keyCode);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.button1.isMouseOver(mouseX, mouseY)) {
            boolean ret = this.button1.mouseClicked(mouseX, mouseY, button);
            if(ret) {
                clearFocus();
                focused = button1;
            }
            return ret;
        } else if(this.button2 != null && this.button2.isMouseOver(mouseX, mouseY)) {
            boolean ret = this.button2.mouseClicked(mouseX, mouseY, button);
            if(ret) {
                clearFocus();
                focused = button2;
            }
            return ret;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(focused != null) {
            return focused.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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

    @Override
    public void renderOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(button1 instanceof OverlayRenderable) {
            ((OverlayRenderable)button1).renderOverlay(matrices, mouseX, mouseY, delta);
        }
        if(button2 instanceof OverlayRenderable) {
            ((OverlayRenderable)button2).renderOverlay(matrices, mouseX, mouseY, delta);
        }
    }
}
