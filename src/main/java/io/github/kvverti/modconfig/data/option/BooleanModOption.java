package io.github.kvverti.modconfig.data.option;

import java.util.function.Consumer;

import io.github.kvverti.modconfig.data.facade.BooleanOptionFacade;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class BooleanModOption extends ModOption {

    private boolean state;
    private final Consumer<Boolean> saveHandler;

    public BooleanModOption(Text modName, Text categoryName, BooleanOptionFacade facade) {
        super(modName, categoryName, facade.modcfg_getOptionName());
        this.state = facade.modcfg_getValue();
        this.saveHandler = facade.modcfg_getSaveHandler();
    }

    @Override
    public boolean isStoredOptionValid() {
        return true;
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        Text optionLabel = getMessageText();
        return new ButtonWidget(
            0, 0, width, height, optionLabel,
            btn -> {
                state ^= true;
                btn.setMessage(getMessageText());
                saveHandler.accept(state);
            }
        );
    }

    private MutableText getMessageText() {
        return new LiteralText("")
            .append(this.getOptionName())
            .append(": ")
            .append(state ? ScreenTexts.ON : ScreenTexts.OFF);
    }
}
