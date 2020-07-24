package io.github.kvverti.modconfig.data.facade;

import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.option.TextFieldModOption;

import net.minecraft.text.Text;

public interface TextFieldOptionFacade extends OptionFacade<String> {

    int modcfg_getMaxLength();

    Predicate<String> modcfg_getTextPredicate();

    default Predicate<String> modcfg_getValidator() {
        return modcfg_getTextPredicate();
    }

    boolean modcfg_isShort();

    @Override
    default TextFieldModOption modcfg_createOption(Text modName, Text categoryName) {
        return new TextFieldModOption(modName, categoryName, this);
    }
}
