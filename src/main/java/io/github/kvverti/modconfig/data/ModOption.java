package io.github.kvverti.modconfig.data;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public abstract class ModOption {
    private final Text optionName;

    protected ModOption(Text optionName) {
        this.optionName = optionName;
    }

    public Text getOptionName() {
        return optionName;
    }

    public String getLocalizedOptionName() {
        if(optionName instanceof TranslatableText) {
            return I18n.translate(((TranslatableText)optionName).getKey());
        } else {
            return optionName.asString();
        }
    }

    public abstract void onInteract(Screen parent);
}
