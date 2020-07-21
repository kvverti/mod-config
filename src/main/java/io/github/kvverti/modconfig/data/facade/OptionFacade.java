package io.github.kvverti.modconfig.data.facade;

import java.util.function.Consumer;

import net.minecraft.text.Text;

/**
 * A facade onto a third party option. These are separate from the ModConfig option hierarchy.
 *
 * @param <T> The type of value the option holds.
 */
public interface OptionFacade<T> {

    Text modcfg_getOptionName();

    T modcfg_getValue();

    Consumer<T> modcfg_getSaveHandler();
}
