package io.github.kvverti.modconfig.data.option;

import java.util.List;

import io.github.kvverti.modconfig.data.facade.CycleOptionFacade;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CycleModOption<T> extends ModOption<T> {

    private int stateIdx;
    private final List<T> states;

    public CycleModOption(Text modName, Text categoryName, CycleOptionFacade<T> facade) {
        super(modName, categoryName, facade);
        this.states = facade.modcfg_getStates();
        this.stateIdx = states.indexOf(this.getState());
    }

    @Override
    public boolean isStoredOptionValid() {
        return true;
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        return new ButtonWidget(
            0, 0, width, height, this.getMergedMessageText(),
            btn -> {
                stateIdx = (stateIdx + 1) % states.size();
                this.saveState(states.get(stateIdx));
                btn.setMessage(this.getMergedMessageText());
            }
        );
    }
}
