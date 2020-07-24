package io.github.kvverti.modconfig.data.option.widget;

import io.github.kvverti.modconfig.screen.ModOptionsScreen;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;

/**
 * A half-width text field widget, for text field based integers, etc. where the input is expected to be
 * only a few characters.`
 */
public class ShortTextFieldWidget extends AbstractButtonWidget {

    private final TextRenderer textRenderer;
    private final TextFieldWidget textField;

    public ShortTextFieldWidget(TextRenderer textRenderer, TextFieldWidget widget) {
        super(widget.x, widget.y, widget.getWidth(), widget.getHeight(), widget.getMessage());
        this.textRenderer = textRenderer;
        this.textField = widget;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int widgetWidth = this.width / 3;
        int textY = y + (this.height / 2) - (textRenderer.fontHeight / 2);
        textField.setWidth(widgetWidth);
        textField.y = this.y;
        if(textRenderer.isRightToLeft()) {
            int posX = this.x + widgetWidth + ModOptionsScreen.PADDING_H;
            textField.x = this.x;
            textRenderer.drawWithShadow(matrices, this.getMessage(), posX, textY, 0xffffff);
        } else {
            int posX = this.x + (this.width - widgetWidth);
            int labelWidth = textRenderer.getWidth(this.getMessage()) + ModOptionsScreen.PADDING_H;
            textField.x = posX;
            textRenderer.drawWithShadow(matrices, this.getMessage(), posX - labelWidth, textY, 0xffffff);
        }
        textField.render(matrices, mouseX, mouseY, delta);
    }

    private void syncFocus() {
        this.setFocused(textField.isFocused());
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        boolean ret = textField.changeFocus(lookForwards);
        syncFocus();
        return ret;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return textField.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean ret = textField.keyPressed(keyCode, scanCode, modifiers);
        syncFocus();
        return ret;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(textField.isMouseOver(mouseX, mouseY)) {
            boolean ret = textField.mouseClicked(mouseX, mouseY, button);
            syncFocus();
            return ret;
        }
        return false;
    }
}
