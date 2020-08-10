package io.github.kvverti.modconfig.data.facade;

import java.util.List;

import io.github.kvverti.modconfig.data.option.DropdownModOption;

import net.minecraft.text.Text;

public interface DropdownFacade<T> extends OptionFacade<T> {

    List<T> modcfg_getSelections();

    @Override
    default DropdownModOption<T> modcfg_createOption(Text modName, Text categoryName) {
        return new DropdownModOption<>(modName, categoryName, this);
    }
}
