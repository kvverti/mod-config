package io.github.kvverti.modconfig.data.option;

import java.util.function.Consumer;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;

public class CycleOption<T> extends ModOption {

    private T state;
    private Consumer<T> saveHandler;

    protected CycleOption(Text modName, Text categoryName, Text optionName, T initialState, Consumer<T> saveHandler) {
        super(modName, categoryName, optionName);
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        return null;
    }
}
