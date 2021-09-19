package io.github.kvverti.modconfig.data.option;

import io.github.kvverti.modconfig.data.ScreenFactory;
import io.github.kvverti.modconfig.data.facade.NestedScreenOptionFacade;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class NestedScreenModOption extends ModOption<ScreenFactory> {

    public NestedScreenModOption(Text modName, Text categoryName, NestedScreenOptionFacade facade) {
        super(modName, categoryName, facade);
    }

    @Override
    public boolean isStoredOptionValid() {
        return true;
    }

    @Override
    public ClickableWidget createWidget(Screen containing, int width, int height) {
        return new ButtonWidget(
            0, 0, width, height, this.getOptionName(),
            btn -> MinecraftClient.getInstance().setScreen(this.getState().create(containing))
        );
    }
}
