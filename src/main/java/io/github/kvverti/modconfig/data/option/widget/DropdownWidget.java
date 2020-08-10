package io.github.kvverti.modconfig.data.option.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class DropdownWidget extends AbstractButtonWidget {

    private final TextRenderer textRenderer;
    private final TextFieldWidget searchBox;
    private final AbstractButtonWidget dropdownButton;

    public DropdownWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text title) {
        super(x, y, width, height, title);
        this.textRenderer = textRenderer;
        this.searchBox = new TextFieldWidget(textRenderer, x, y, width, height, title);
        this.dropdownButton = new ButtonWidget(x, y, width, height, new LiteralText(""), btn -> {
        });
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        final int padding = 2;
        int buttonWidth = (this.height);
        int searchBoxWidth = this.width - buttonWidth - padding;
        int buttonX, searchBoxX;
        if(textRenderer.isRightToLeft()) {
            buttonX = this.x;
            searchBoxX = this.x + buttonWidth + padding;
        } else {
            searchBoxX = this.x;
            buttonX = this.x + searchBoxWidth + padding;
        }
        searchBox.x = searchBoxX;
        searchBox.y = this.y;
        searchBox.setWidth(searchBoxWidth);
        dropdownButton.x = buttonX;
        dropdownButton.y = this.y;
        dropdownButton.setWidth(buttonWidth);
        searchBox.render(matrices, mouseX, mouseY, delta);
        dropdownButton.render(matrices, mouseX, mouseY, delta);
    }
}
