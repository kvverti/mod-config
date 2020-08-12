package io.github.kvverti.modconfig.data.option.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;

import io.github.kvverti.modconfig.iface.ClearFocus;
import io.github.kvverti.modconfig.screen.ModOptionsScreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

/**
 * A searchable dropdown widget.
 */
public class DropdownWidget<T> extends AbstractButtonWidget implements OverlayRenderable, ClearFocus {

    /**
     * Horizontal padding between elements.
     */
    private static final int PADDING_H = 2;

    private final TextRenderer textRenderer;
    private final TextFieldWidget searchBox;
    private final ButtonWidget dropdownButton;
    private final DropdownListWidget dropdown;
    @Nullable
    private AbstractButtonWidget focused;

    public DropdownWidget(TextRenderer textRenderer, List<T> selections, Function<T, Text> nameProvider, int x, int y, int width, int height, Text title) {
        super(x, y, width, height, title);
        this.textRenderer = textRenderer;
        this.searchBox = new TextFieldWidget(textRenderer, x, y, width, height, title);
        this.dropdown = new DropdownListWidget(selections, nameProvider, x, y, width, height, title);
        this.dropdownButton = new ButtonWidget(x, y, width, height, new LiteralText(""), btn -> {
            dropdown.visible ^= true;
            if(focused != null) {
                focused.changeFocus(true);
            }
            dropdown.changeFocus(true);
            focused = dropdown;
        });
        dropdown.visible = false;
        this.focused = null;
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
        int dropdownHeight = getLineHeight(textRenderer) * DropdownListWidget.DISPLAYED_LINE_COUNT;
        if(MinecraftClient.getInstance().currentScreen != null) {
            int areaBottom = MinecraftClient.getInstance().currentScreen.height - ModOptionsScreen.LIST_AREA_BOTTOM_OFFSET;
            if(dropdown.y + dropdownHeight > areaBottom) {
                dropdown.y = searchBox.y - dropdownHeight;
            }
        }
        dropdown.setWidth(searchBoxWidth + DropdownListWidget.SCROLL_BAR_PADDING + DropdownListWidget.SCROLL_BAR_WIDTH);
        dropdown.setHeight(dropdownHeight);
    }

    @Override
    public void renderOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(dropdown.visible) {
            dropdown.render(matrices, mouseX, mouseY, delta);
            // for some reason, rendering a single upward facing drop down causes text
            // on some widgets to render in front of the drop down
            dropdown.renderButton(matrices, mouseX, mouseY, delta);
        }
    }

    /**
     * The height, in pixels, of each line in the drop down menu.
     */
    private static int getLineHeight(TextRenderer textRenderer) {
        return textRenderer.fontHeight * 7 / 4;
    }

    @Override
    public void modcfg_clearFocus() {
        dropdown.reset();
        if(focused != null) {
            ((ClearFocus)focused).modcfg_clearFocus();
        }
        focused = null;
        modcfg_super_clearFocus();
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if(focused != null) {
            focused.changeFocus(lookForwards);
        } else {
            this.setFocused(true);
        }
        if(focused == searchBox) {
            // searchBox -> dropdownButton | prev
            focused = lookForwards ? dropdownButton : null;
        } else if(focused == dropdownButton) {
            // dropdownButton -> next | searchBox
            focused = lookForwards ? null : searchBox;
        } else if(focused == dropdown) {
            // dropdown -> searchBox
            focused = searchBox;
        } else {
            // prev/next -> this
            // dropdown is never open at this stage
            focused = lookForwards ? searchBox : dropdownButton;
        }
        if(focused == null) {
            modcfg_clearFocus();
            return false;
        } else {
            focused.changeFocus(lookForwards);
            this.onFocusedChanged(true);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(focused != null) {
            return focused.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        if(focused == dropdown) {
            // prevent switching to the search bar when the drop down button is pressed using SPACE
            if(chr == ' ') {
                return false;
            }
            if(focused != null) {
                focused.changeFocus(true);
            }
            focused = searchBox;
            focused.changeFocus(true);
        }
        return searchBox.charTyped(chr, keyCode);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(dropdownButton.isMouseOver(mouseX, mouseY)) {
            return dropdownButton.mouseClicked(mouseX, mouseY, button);
        } else if(searchBox.isMouseOver(mouseX, mouseY)) {
            if(focused != null) {
                ((ClearFocus)focused).modcfg_clearFocus();
            }
            focused = searchBox;
            this.setFocused(true);
            return searchBox.mouseClicked(mouseX, mouseY, button);
        } else if(dropdown.visible && dropdown.isMouseOver(mouseX, mouseY)) {
            return dropdown.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    /**
     * The actial drop down menu widget.
     */
    private class DropdownListWidget extends AbstractButtonWidget {

        private static final int SCROLL_BAR_WIDTH = 5;
        private static final int SCROLL_BAR_PADDING = 4;
        private static final int SCROLL_BAR_HEIGHT = 20;
        private static final int DISPLAYED_LINE_COUNT = 5;

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

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            TextRenderer textRenderer = DropdownWidget.this.textRenderer;
            int lineHeight = getLineHeight(textRenderer);
            int bgx0 = this.x;
            int bgy0 = this.y;
            int bgx1 = bgx0 + this.width - SCROLL_BAR_WIDTH - SCROLL_BAR_PADDING;
            int bgy1 = bgy0 + this.height;
            // render background
            DrawableHelper.fill(matrices, bgx0 - 1, bgy0 - 1, bgx1 + 1, bgy1 + 1, -6250336);
            DrawableHelper.fill(matrices, bgx0, bgy0, bgx1, bgy1, 0xff000000);
            // render selections
            int endIdx = Math.min(selections.size(), scrollIdx + DISPLAYED_LINE_COUNT);
            for(int i = scrollIdx; i < endIdx; i++) {
                int y = this.y + lineHeight * (i - scrollIdx);
                if(i == selectedIndex) {
                    int sx0 = bgx0 - 1;
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    int sy0 = y;
                    int sx1 = bgx1 + 1;
                    int sy1 = y + lineHeight;
                    DrawableHelper.fill(matrices, sx0 - 1, sy0 - 1, sx1 + 1, sy1 + 1, 0xffffffff);
                    DrawableHelper.fill(matrices, sx0, sy0, sx1, sy1, 0xff000000);
                }
                Text name = nameProvider.apply(suggestedSelections.get(i));
                int textX;
                if(textRenderer.isRightToLeft()) {
                    textX = bgx1 - 2 - (int)textRenderer.getTextHandler().getWidth(name);
                } else {
                    textX = bgx0 + 2;
                }
                int textY = y + (lineHeight / 4);
                this.drawTextWithShadow(matrices, textRenderer, name, textX, textY, 0xffffffff);
            }
            // render scroll bar
            float scrollPos = (float)scrollIdx / selections.size();
            int scrollX0;
            if(textRenderer.isRightToLeft()) {
                scrollX0 = bgx0 - SCROLL_BAR_PADDING - SCROLL_BAR_WIDTH;
            } else {
                scrollX0 = bgx1 + SCROLL_BAR_PADDING;
            }
            int scrollX1 = scrollX0 + SCROLL_BAR_WIDTH;
            int scrollY0 = (int)(scrollPos * (bgy1 - SCROLL_BAR_HEIGHT) + (1 - scrollPos) * bgy0);
            int scrollY1 = scrollY0 + SCROLL_BAR_HEIGHT;
            DrawableHelper.fill(matrices, scrollX0, scrollY0, scrollX1, scrollY1, 0xffaaaaaa);
        }

        /**
         * Unselects, unfocues, and hides the drop down.
         */
        public void reset() {
            this.selectedIndex = -1;
            this.scrollIdx = 0;
            this.setFocused(false);
            this.visible = false;
        }
    }
}
