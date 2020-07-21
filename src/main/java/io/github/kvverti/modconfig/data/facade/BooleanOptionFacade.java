package io.github.kvverti.modconfig.data.facade;

import io.github.kvverti.modconfig.data.option.BooleanModOption;

import net.minecraft.text.Text;

/**
 * A third party object that defines a boolean option.
 */
public interface BooleanOptionFacade extends OptionFacade<Boolean> {

    @Override
    default BooleanModOption modcfg_createOption(Text modName, Text categoryName) {
        return new BooleanModOption(modName, categoryName, this);
    }
}