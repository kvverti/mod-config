package io.github.kvverti.modconfig.data.option;

import java.util.List;
import java.util.function.Consumer;

import io.github.kvverti.modconfig.data.facade.CycleOptionFacade;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class CycleModOption<T> extends ModOption {

    private int stateIdx;
    private final List<T> states;
    private final List<Text> stateNames;
    private final Consumer<T> saveHandler;

    public CycleModOption(Text modName, Text categoryName, CycleOptionFacade<T> facade) {
        super(modName, categoryName, facade.modcfg_getOptionName());
        this.states = facade.modcfg_getStates();
        this.stateNames = facade.modcfg_getStateNames();
        this.saveHandler = facade.modcfg_getSaveHandler();
        this.stateIdx = states.indexOf(facade.modcfg_getValue());
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        return new ButtonWidget(
            0, 0, width, height, getOptionMessage(),
            btn -> {
                stateIdx = (stateIdx + 1) % states.size();
                btn.setMessage(getOptionMessage());
                saveHandler.accept(states.get(stateIdx));
            }
        );
    }

    private MutableText getOptionMessage() {
        return new LiteralText("")
            .append(this.getOptionName())
            .append(": ")
            .append(stateNames.get(stateIdx));
    }
}
