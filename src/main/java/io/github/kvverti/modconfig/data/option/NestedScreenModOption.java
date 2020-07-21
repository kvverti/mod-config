package io.github.kvverti.modconfig.data.option;

import io.github.kvverti.modconfig.data.ScreenFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class NestedScreenModOption extends ModOption {

    private final ScreenFactory factory;

    public NestedScreenModOption(Text modName, Text categoryName, ScreenFactory factory) {
        super(modName, categoryName, categoryName);
        this.factory = factory;
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        return new ButtonWidget(
            0, 0, width, height, this.getOptionName(),
            btn -> MinecraftClient.getInstance().openScreen(factory.create(containing))
        );
    }
}
