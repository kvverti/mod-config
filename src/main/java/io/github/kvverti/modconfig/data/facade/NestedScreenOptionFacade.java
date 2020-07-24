package io.github.kvverti.modconfig.data.facade;

import java.util.function.Consumer;

import io.github.kvverti.modconfig.data.ScreenFactory;
import io.github.kvverti.modconfig.data.option.NestedScreenModOption;

import net.minecraft.text.Text;

public interface NestedScreenOptionFacade extends OptionFacade<ScreenFactory> {

    @Override
    default NestedScreenModOption modcfg_createOption(Text modName, Text categoryName) {
        return new NestedScreenModOption(modName, categoryName, this);
    }

    static NestedScreenOptionFacade makeFacade(Text optionName, ScreenFactory factory) {
        return new NestedScreenOptionFacade() {
            @Override
            public Text modcfg_getOptionName() {
                return optionName;
            }

            @Override
            public ScreenFactory modcfg_getValue() {
                return factory;
            }

            @Override
            public Consumer<ScreenFactory> modcfg_getSaveHandler() {
                return value -> {
                };
            }
        };
    }
}
