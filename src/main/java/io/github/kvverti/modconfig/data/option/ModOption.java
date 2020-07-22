package io.github.kvverti.modconfig.data.option;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class ModOption {
    private final Text modName;
    private final Text categoryName;
    private final Text optionName;

    protected ModOption(Text modName, Text categoryName, Text optionName) {
        this.modName = modName;
        this.categoryName = categoryName;
        this.optionName = optionName;
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

    public abstract AbstractButtonWidget createWidget(Screen containing, int width, int height);

    /**
     * True if the option should take up an entire row, false if the option should take up only half a row.
     */
    public boolean isFullWidth() {
        return false;
    }
}
