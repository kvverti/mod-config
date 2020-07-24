package io.github.kvverti.modconfig.data.option;

import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.facade.TextFieldOptionFacade;
import io.github.kvverti.modconfig.data.option.widget.ShortTextFieldWidget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TextFieldModOption extends ModOption<String> {

    private final int maxLength;
    private final Predicate<String> validator;
    private final boolean isShort;

    public TextFieldModOption(Text modName, Text categoryName, TextFieldOptionFacade facade) {
        super(modName, categoryName, facade);
        this.maxLength = facade.modcfg_getMaxLength();
        this.validator = facade.modcfg_getValidator();
        this.isShort = facade.modcfg_isShort();
    }

    @Override
    public boolean isStoredOptionValid() {
        return validator.test(this.getState());
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        TextFieldWidget widget = new TextFieldWidget(textRenderer, 0, 0, width, height, this.getOptionName());
        widget.setMaxLength(maxLength);
        widget.setTextPredicate(validator);
        widget.setText(this.getState());
        widget.setChangedListener(this::saveState);
        if(isShort) {
            return new ShortTextFieldWidget(textRenderer, widget);
        } else {
            return widget;
        }
    }

    @Override
    public boolean isFullWidth() {
        return !isShort;
    }
}
