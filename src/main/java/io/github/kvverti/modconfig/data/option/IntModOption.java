package io.github.kvverti.modconfig.data.option;

import io.github.kvverti.modconfig.data.facade.IntOptionFacade;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class IntModOption extends ModOption<Integer> {

    private final int min;
    private final int max;

    public IntModOption(Text modName, Text categoryName, IntOptionFacade facade) {
        super(modName, categoryName, facade);
        this.min = facade.modcfg_getMin();
        this.max = facade.modcfg_getMax();
    }

    @Override
    public boolean isStoredOptionValid() {
        return this.getState() <= max && this.getState() >= min;
    }

    @Override
    public AbstractButtonWidget createWidget(Screen containing, int width, int height) {
        return new SliderWidget(0, 0, width, height, getMessageText(), (double)(this.getState() - min) / max) {
            @Override
            protected void updateMessage() {
                this.setMessage(getMessageText());
            }

            @Override
            protected void applyValue() {
                double halfStep = 1.0 / (2 * (max - min));
                IntModOption.this.saveState((int)((this.value + halfStep) * max) + min);
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
            .append(Integer.toString(this.getState()));
    }
}
