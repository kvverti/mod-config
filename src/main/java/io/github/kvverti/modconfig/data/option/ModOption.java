package io.github.kvverti.modconfig.data.option;

import java.util.function.Consumer;
import java.util.function.Function;

import io.github.kvverti.modconfig.data.TextUtil;
import io.github.kvverti.modconfig.data.facade.OptionFacade;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class ModOption<T> {
    private final Text modName;
    private final Text categoryName;
    private final Text optionName;
    private final Consumer<T> saveHandler;
    private final Function<T, Text> nameProvider;
    private T state;

    protected ModOption(Text modName, Text categoryName, OptionFacade<T> facade) {
        this.modName = modName;
        this.categoryName = categoryName;
        this.optionName = facade.modcfg_getOptionName();
        this.saveHandler = facade.modcfg_getSaveHandler();
        this.nameProvider = facade.modcfg_getNameProvider();
        this.state = facade.modcfg_getValue();
    }

    public final Text getModName() {
        return modName;
    }

    public final Text getCategoryName() {
        return categoryName;
    }

    public final Text getOptionName() {
        return optionName;
    }

    public final String getLocalizedModName() {
        return TextUtil.localize(modName);
    }

    public final String getLocalizedCategoryName() {
        return TextUtil.localize(categoryName);
    }

    public final String getLocalizedOptionName() {
        return TextUtil.localize(optionName);
    }

    /**
     * True if the value of this option is valid and is ok to be saved, false if not.
     */
    public abstract boolean isStoredOptionValid();

    public abstract AbstractButtonWidget createWidget(Screen containing, int width, int height);

    /**
     * True if the option should take up an entire row, false if the option should take up only half a row.
     */
    public boolean isFullWidth() {
        return false;
    }

    /**
     * Returns the current state. This state may not be valid.
     */
    protected final T getState() {
        return state;
    }

    /**
     * Sets the current state, and saves it if it is valid.
     */
    protected final void saveState(T state) {
        this.state = state;
        if(isStoredOptionValid()) {
            saveHandler.accept(state);
        }
    }

    final Text getMergedMessageText() {
        MutableText text = LiteralText.EMPTY.copy();
        // fix RTL manually
        if(MinecraftClient.getInstance().textRenderer.isRightToLeft()) {
            return text.append(nameProvider.apply(state))
                .append(": ")
                .append(optionName);
        } else {
            return text.append(optionName)
                .append(": ")
                .append(nameProvider.apply(state));
        }
    }
}
