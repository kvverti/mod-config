package io.github.kvverti.modconfig.data.option.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class DropdownWidget<T> extends AbstractButtonWidget implements OverlayRenderable {

    private static final int PADDING_H = 2;

    private final TextRenderer textRenderer;
    private final TextFieldWidget searchBox;
    private final AbstractButtonWidget dropdownButton;
    private final DropdownListWidget dropdown;

    public DropdownWidget(TextRenderer textRenderer, List<T> selections, Function<T, Text> nameProvider, int x, int y, int width, int height, Text title) {
        super(x, y, width, height, title);
        this.textRenderer = textRenderer;
        this.searchBox = new TextFieldWidget(textRenderer, x, y, width, height, title);
        this.dropdownButton = new ButtonWidget(x, y, width, height, new LiteralText(""), btn -> {
        });
        this.dropdown = new DropdownListWidget(selections, nameProvider, x, y, width, height, title);
        dropdown.visible = true;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int buttonWidth = (this.height);
        int searchBoxWidth = this.width - buttonWidth - PADDING_H;
        int buttonX, searchBoxX;
        if(textRenderer.isRightToLeft()) {
            buttonX = this.x;
            searchBoxX = this.x + buttonWidth + PADDING_H;
        } else {
            searchBoxX = this.x;
            buttonX = this.x + searchBoxWidth + PADDING_H;
        }
        searchBox.x = searchBoxX;
        searchBox.y = this.y;
        searchBox.setWidth(searchBoxWidth);
        dropdownButton.x = buttonX;
        dropdownButton.y = this.y;
        dropdownButton.setWidth(buttonWidth);
        searchBox.render(matrices, mouseX, mouseY, delta);
        dropdownButton.render(matrices, mouseX, mouseY, delta);
        dropdown.x = searchBoxX;
        dropdown.y = searchBox.y + searchBox.getHeight() + 1;
        dropdown.setWidth(searchBoxWidth + PADDING_H + DropdownListWidget.SCROLL_BAR_WIDTH);
    }

    @Override
    public void renderOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(dropdown.visible) {
            dropdown.render(matrices, mouseX, mouseY, delta);
        }
    }

    private class DropdownListWidget extends AbstractButtonWidget {

        private static final int SCROLL_BAR_WIDTH = 5;
        private static final int SCROLL_BAR_HEIGHT = 20;

        private final List<T> selections;
        private final Function<T, Text> nameProvider;
        private final List<T> suggestedSelections;
        private int selectedIndex;
        private int scrollIdx;

        public DropdownListWidget(List<T> selections, Function<T, Text> nameProvider, int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
            this.selections = selections;
            this.nameProvider = nameProvider;
            this.suggestedSelections = new ArrayList<>(selections);
            this.selectedIndex = 4;
            this.scrollIdx = 3;
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            final int displayedCount = 6;
            TextRenderer textRenderer = DropdownWidget.this.textRenderer;
            int lineHeight = textRenderer.fontHeight * 7 / 4;
            this.height = lineHeight * displayedCount;
            int bgx0 = this.x;
            int bgy0 = this.y;
            int bgx1 = bgx0 + this.width - SCROLL_BAR_WIDTH - PADDING_H;
            int bgy1 = bgy0 + this.height;
            // render background
            DrawableHelper.fill(matrices, bgx0 - 1, bgy0 - 1, bgx1 + 1, bgy1 + 1, -6250336);
            DrawableHelper.fill(matrices, bgx0, bgy0, bgx1, bgy1, 0xff000000);
            // render selections
            int endIdx = Math.min(selections.size(), scrollIdx + displayedCount);
            for(int i = scrollIdx; i < endIdx; i++) {
                int y = this.y + lineHeight * (i - scrollIdx);
                if(i == selectedIndex) {
                    int sx0 = bgx0 - 1;
                    int sy0 = y;
                    int sx1 = bgx1 + 1;
                    int sy1 = y + lineHeight;
                    DrawableHelper.fill(matrices, sx0 - 1, sy0 - 1, sx1 + 1, sy1 + 1, 0xffffffff);
                    DrawableHelper.fill(matrices, sx0, sy0, sx1, sy1, 0xff000000);
                }
                int textX = this.x + 2;
                int textY = y + (lineHeight / 4);
                this.drawTextWithShadow(matrices, textRenderer, nameProvider.apply(suggestedSelections.get(i)), textX, textY, 0xffffffff);
            }
            // render scroll bar
            float scrollPos = (float)scrollIdx / selections.size();
            int scrollX0 = bgx1 + PADDING_H;
            int scrollY0 = (int)(scrollPos * (bgy1 - SCROLL_BAR_HEIGHT) + (1 - scrollPos) * bgy0);
            int scrollX1 = scrollX0 + SCROLL_BAR_WIDTH;
            int scrollY1 = scrollY0 + SCROLL_BAR_HEIGHT;
            DrawableHelper.fill(matrices, scrollX0, scrollY0, scrollX1, scrollY1, 0xffaaaaaa);
        }
    }
}
