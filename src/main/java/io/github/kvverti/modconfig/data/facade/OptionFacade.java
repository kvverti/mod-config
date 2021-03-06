package io.github.kvverti.modconfig.data.facade;

import java.util.function.Consumer;
import java.util.function.Function;

import io.github.kvverti.modconfig.data.option.ModOption;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

/**
 * A facade onto a third party option. These are separate from the ModConfig option hierarchy.
 *
 * @param <T> The type of value the option holds.
 */
public interface OptionFacade<T> {

    Text modcfg_getOptionName();

    T modcfg_getValue();

    default Function<T, Text> modcfg_getNameProvider() {
        return value -> new LiteralText(value.toString());
    }

    Consumer<T> modcfg_getSaveHandler();

    ModOption<T> modcfg_createOption(Text modName, Text categoryName);
}
