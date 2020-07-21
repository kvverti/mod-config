package io.github.kvverti.modconfig.data.option;

import java.util.function.Consumer;

import io.github.kvverti.modconfig.data.facade.IntOptionFacade;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class IntModOption extends ModOption {

    private int state;
    private final int min;
    private final int max;
    private final Consumer<Integer> saveHandler;

    public IntModOption(Text modName, Text categoryName, IntOptionFacade facade) {
        super(modName, categoryName, facade.modcfg_getOptionName());
        this.min = facade.modcfg_getMin();
        this.max = facade.modcfg_getMax();
        this.saveHandler = facade.modcfg_getSaveHandler();
        this.state = facade.modcfg_getValue();
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        return new SliderWidget(0, 0, width, height, getMessageText(), (double)(state - min) / max) {
            @Override
            protected void updateMessage() {
                this.setMessage(getMessageText());
            }

            @Override
            protected void applyValue() {
                double halfStep = 1.0 / (2 * (max - min));
                state = (int)((this.value + halfStep) * max) + min;
                saveHandler.accept(state);
            }

            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                boolean ret = super.keyPressed(keyCode, scanCode, modifiers);
                return ret || keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT;
            }
        };
    }

    private Text getMessageText() {
        return new LiteralText("")
            .append(this.getOptionName())
            .append(": ")
            .append(Integer.toString(state));
    }
}
