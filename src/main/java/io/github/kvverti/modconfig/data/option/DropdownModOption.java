package io.github.kvverti.modconfig.data.option;

import java.util.List;
import java.util.function.Function;

import io.github.kvverti.modconfig.data.facade.DropdownFacade;
import io.github.kvverti.modconfig.data.option.widget.DropdownWidget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.text.Text;

public class DropdownModOption<T> extends ModOption<T> {

    private final List<T> selections;
    private final Function<T, Text> nameProvider;

    public DropdownModOption(Text modName, Text categoryName, DropdownFacade<T> facade) {
        super(modName, categoryName, facade);
        this.selections = facade.modcfg_getSelections();
        this.nameProvider = facade.modcfg_getNameProvider();
    }

    @Override
    public boolean isStoredOptionValid() {
        return true;
    }

    @Override
    public boolean isFullWidth() {
        return true;
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        return new DropdownWidget<>(
            textRenderer,
            selections,
            nameProvider,
            0,
            0, width,
            height,
            this.getOptionName(),
            this.getState(),
            this::saveState);
    }
}
