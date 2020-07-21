package io.github.kvverti.modconfig.data.facade;

import java.util.List;

import io.github.kvverti.modconfig.data.option.CycleModOption;

import net.minecraft.text.Text;

public interface CycleOptionFacade<T> extends OptionFacade<T> {

    List<T> modcfg_getStates();

    List<Text> modcfg_getStateNames();

    @Override
    CycleModOption<T> modcfg_createOption(Text modName, Text categoryName);
}
