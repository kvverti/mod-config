package io.github.kvverti.modconfig.data.option;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class BooleanModOption extends ModOption {

    private boolean state;

    public BooleanModOption(Text modName, Text categoryName, Text optionName, boolean initialState) {
        super(modName, categoryName, optionName);
        this.state = initialState;
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        Text optionLabel = getMessageText();
        return new ButtonWidget(
            0, 0, width, height, optionLabel,
            btn -> {
                state ^= true;
                btn.setMessage(getMessageText());
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
