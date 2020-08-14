package io.github.kvverti.modconfig.data;

import java.util.Locale;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class TextUtil {

    public static Locale getCurrentLocale() {
        String localeStr = MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode();
        return Locale.forLanguageTag(localeStr.replace("_", "-"));
    }

    public static String localize(Text text) {
        if(text instanceof TranslatableText) {
            return I18n.translate(((TranslatableText)text).getKey());
        } else {
            return text.asString();
        }
    }
}
