package io.github.kvverti.modconfig.screen;

import io.github.kvverti.modconfig.data.option.widget.OverlayRenderable;
import io.github.kvverti.modconfig.iface.ClearFocus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * And entry containing a single setting that takes up the entire width.
 */
public class WideSettingModOptionsEntry extends ModOptionsEntry {

    private final Text label;
    private final ClickableWidget widget;
    private final boolean rtl;

    public WideSettingModOptionsEntry(Text label, ClickableWidget widget, boolean rtl) {
        this.label = label;
        this.widget = widget;
        this.rtl = rtl;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        final int spacingH = 10;
        int halfWidth = (entryWidth - spacingH) / 2;
        int widgetWidth = halfWidth + spacingH + (halfWidth / 3);
        widget.setWidth(widgetWidth);
        widget.y = y;
        int textY = y + (entryHeight / 2) - (textRenderer.fontHeight / 2);
        if(rtl) {
            int posX = x + widgetWidth;
            widget.x = x;
            widget.drawTextWithShadow(matrices, textRenderer, label, posX + ModOptionsScreen.PADDING_H, textY, 0xffffff);
        } else {
            int posX = x + (entryWidth - widgetWidth);
            int labelWidth = textRenderer.getWidth(label) + ModOptionsScreen.PADDING_H;
            widget.x = posX;
            widget.drawTextWithShadow(matrices, textRenderer, label, posX - labelWidth, textY, 0xffffff);
        }
        widget.render(matrices, mouseX, mouseY, tickDelta);
    }

    @Override
    public void renderOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(widget instanceof OverlayRenderable) {
            ((OverlayRenderable)widget).renderOverlay(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean setFocusedColumn(int col) {
        clearFocus();
        return widget.changeFocus(true);
    }

    @Override
    public void clearFocus() {
        ((ClearFocus)widget).modcfg_clearFocus();
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        return widget.changeFocus(lookForwards);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return widget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return widget.charTyped(chr, keyCode);
    }

    @Override
    public boolean isMouseOverOverlay(double mouseX, double mouseY) {
        if(widget instanceof OverlayRenderable) {
            return widget.isMouseOver(mouseX, mouseY);
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return widget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return widget.mouseScrolled(mouseX, mouseY, amount);
    }
}
