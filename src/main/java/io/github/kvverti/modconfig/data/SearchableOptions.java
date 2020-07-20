package io.github.kvverti.modconfig.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.text.LiteralText;

/**
 * Stores any found searchable options.
 */
public class SearchableOptions {
    /**
     * Mod options screens.
     */
    private final List<NestedScreenModOption> mods = new ArrayList<>();

    public SearchableOptions() {
        MinecraftClient client = MinecraftClient.getInstance();
        mods.add(new NestedScreenModOption(new LiteralText("Minecraft"), p -> new OptionsScreen(p, client.options)));
        mods.add(new NestedScreenModOption(new LiteralText("Minecraft 2"), p -> new OptionsScreen(p, client.options)));
        mods.add(new NestedScreenModOption(new LiteralText("Tiny Potato"), p -> new OptionsScreen(p, client.options)));
    }

    /**
     * Finds and returns all mods whose names match the given query.
     */
    public List<NestedScreenModOption> findMods(String match) {
        List<NestedScreenModOption> found = new ArrayList<>();
        String localeStr = MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode();
        Locale locale = Locale.forLanguageTag(localeStr.replace("_", "-"));
        match = match.toLowerCase(locale);
        for(NestedScreenModOption option : mods) {
            if(option.getLocalizedOptionName().toLowerCase(locale).contains(match)) {
                found.add(option);
            }
        }
        found.sort(Comparator.comparing(ModOption::getLocalizedOptionName));
        return found;
    }
}
