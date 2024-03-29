package io.github.kvverti.modconfig.data.option;

import io.github.kvverti.modconfig.data.facade.BooleanOptionFacade;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BooleanModOption extends ModOption<Boolean> {

    public BooleanModOption(Text modName, Text categoryName, BooleanOptionFacade facade) {
        super(modName, categoryName, facade);
    }

    @Override
    public boolean isStoredOptionValid() {
        return true;
    }

    @Override
    public ClickableWidget createWidget(Screen containing, int width, int height) {
        Text optionLabel = this.getMergedMessageText();
        return new ButtonWidget(
            0, 0, width, height, optionLabel,
            btn -> {
                this.saveState(!this.getState());
                btn.setMessage(this.getMergedMessageText());
            }
        );
    }
}
