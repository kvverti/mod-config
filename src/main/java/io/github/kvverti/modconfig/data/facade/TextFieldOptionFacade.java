package io.github.kvverti.modconfig.data.facade;

import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.option.ModOption;
import io.github.kvverti.modconfig.data.option.TextFieldModOption;

import net.minecraft.text.Text;

public interface TextFieldOptionFacade extends OptionFacade<String> {

    int modcfg_getMaxLength();

    Predicate<String> modcfg_getValidator();

    @Override
    default ModOption modcfg_createOption(Text modName, Text categoryName) {
        return new TextFieldModOption(modName, categoryName, this);
    }
}
