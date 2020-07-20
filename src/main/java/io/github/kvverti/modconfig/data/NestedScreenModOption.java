package io.github.kvverti.modconfig.data;

import net.minecraft.text.Text;

public class NestedScreenModOption extends ModOption {

    private final ScreenFactory factory;

    public NestedScreenModOption(Text optionName, ScreenFactory factory) {
        super(optionName);
        this.factory = factory;
    }

    public ScreenFactory getFactory() {
        return factory;
    }
}
