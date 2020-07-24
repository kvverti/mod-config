package io.github.kvverti.modconfig.data.option;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.facade.TextFieldOptionFacade;
import io.github.kvverti.modconfig.data.option.widget.ShortTextFieldWidget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TextFieldModOption extends ModOption {

    private String state;
    private final int maxLength;
    private final Predicate<String> validator;
    private final boolean isShort;
    private final Consumer<String> saveHandler;

    public TextFieldModOption(Text modName, Text categoryName, TextFieldOptionFacade facade) {
        super(modName, categoryName, facade.modcfg_getOptionName());
        this.maxLength = facade.modcfg_getMaxLength();
        this.validator = facade.modcfg_getValidator();
        this.isShort = facade.modcfg_isShort();
        this.saveHandler = facade.modcfg_getSaveHandler();
        this.state = facade.modcfg_getValue();
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        TextFieldWidget widget = new TextFieldWidget(textRenderer, 0, 0, width, height, this.getOptionName());
        widget.setMaxLength(maxLength);
        widget.setTextPredicate(validator);
        widget.setText(state);
        widget.setChangedListener(value -> {
            state = value;
            saveHandler.accept(state);
        });
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
