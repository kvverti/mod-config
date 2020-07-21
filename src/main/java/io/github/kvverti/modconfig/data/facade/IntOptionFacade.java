package io.github.kvverti.modconfig.data.facade;

import io.github.kvverti.modconfig.data.option.IntModOption;

import net.minecraft.text.Text;

public interface IntOptionFacade extends OptionFacade<Integer> {

    int modcfg_getMin();

    int modcfg_getMax();

    @Override
    default IntModOption modcfg_createOption(Text modName, Text categoryName) {
        return new IntModOption(modName, categoryName, this);
    }
}
