package io.github.kvverti.modconfig.data.facade;

import java.util.List;

import net.minecraft.text.Text;

public interface CycleOptionFacade<T> extends OptionFacade<T> {

    List<T> modcfg_getStates();

    List<Text> modcfg_getStateNames();
}
