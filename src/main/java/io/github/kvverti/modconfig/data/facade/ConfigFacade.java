package io.github.kvverti.modconfig.data.facade;

import java.util.List;
import java.util.Map;

import net.minecraft.text.Text;

public interface ConfigFacade {

    Map<Text, List<OptionFacade<?>>> modcfg_getOptionsByCategory();
}
