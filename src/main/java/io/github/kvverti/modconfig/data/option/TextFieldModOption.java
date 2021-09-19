package io.github.kvverti.modconfig.data.option;

import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.facade.TextFieldOptionFacade;
import io.github.kvverti.modconfig.data.option.widget.ShortTextFieldWidget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TextFieldModOption extends ModOption<String> {

    private final int maxLength;
    private final Predicate<String> textPredicate;
    private final Predicate<String> validator;
    private final boolean isShort;

    public TextFieldModOption(Text modName, Text categoryName, TextFieldOptionFacade facade) {
        super(modName, categoryName, facade);
        this.maxLength = facade.modcfg_getMaxLength();
        this.textPredicate = facade.modcfg_getTextPredicate();
        this.validator = facade.modcfg_getValidator();
        this.isShort = facade.modcfg_isShort();
    }

    @Override
    public boolean isStoredOptionValid() {
        return validator.test(this.getState());
    }

    @Override
    public ClickableWidget createWidget(Screen containing, int width, int height) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        TextFieldWidget widget = new TextFieldWidget(textRenderer, 0, 0, width, height, this.getOptionName());
        widget.setMaxLength(maxLength);
        widget.setTextPredicate(textPredicate);
        widget.setText(this.getState());
        widget.setChangedListener(text -> onTextChanged(widget, text));
        if(isShort) {
            return new ShortTextFieldWidget(textRenderer, widget);
        } else {
            return widget;
        }
    }

    private void onTextChanged(TextFieldWidget widget, String text) {
        this.saveState(text);
        if(validator.test(text)) {
            widget.setEditableColor(0xCCCCCC);
        } else {
            widget.setEditableColor(0xff6655);
        }
    }

    @Override
    public boolean isFullWidth() {
        return !isShort;
    }
}
