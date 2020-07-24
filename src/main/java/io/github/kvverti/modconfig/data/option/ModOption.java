package io.github.kvverti.modconfig.data.option;

import java.util.function.Consumer;

import io.github.kvverti.modconfig.data.facade.OptionFacade;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class ModOption<T> {
    private final Text modName;
    private final Text categoryName;
    private final Text optionName;
    private final Consumer<T> saveHandler;
    private T state;

    protected ModOption(Text modName, Text categoryName, OptionFacade<T> facade) {
        this.modName = modName;
        this.categoryName = categoryName;
        this.optionName = facade.modcfg_getOptionName();
        this.saveHandler = facade.modcfg_getSaveHandler();
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
        if(modName instanceof TranslatableText) {
            return I18n.translate(((TranslatableText)modName).getKey());
        } else {
            return modName.asString();
        }
    }

    public final String getLocalizedCategoryName() {
        if(categoryName instanceof TranslatableText) {
            return I18n.translate(((TranslatableText)categoryName).getKey());
        } else {
            return categoryName.asString();
        }
    }

    public final String getLocalizedOptionName() {
        if(optionName instanceof TranslatableText) {
            return I18n.translate(((TranslatableText)optionName).getKey());
        } else {
            return optionName.asString();
        }
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
}
