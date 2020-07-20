package io.github.kvverti.modconfig.data;

import java.util.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.options.OptionsScreen;

/**
 * Stores any found searchable options.
 */
public class SearchableOptions {
    /**
     * Mod options screens.
     */
    private final Map<String, ScreenFactory> mods = new HashMap<>();

    public SearchableOptions() {
        MinecraftClient client = MinecraftClient.getInstance();
        mods.put("Minecraft", p -> new OptionsScreen(p, client.options));
        mods.put("Minecraft 2", p -> new OptionsScreen(p, client.options));
        mods.put("Tiny Potato", p -> new OptionsScreen(p, client.options));
    }

    /**
     * Finds and returns all mods whose names match the given query.
     */
    public List<Map.Entry<String, ScreenFactory>> findMods(String match) {
        List<Map.Entry<String, ScreenFactory>> found = new ArrayList<>();
        String localeStr = MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode();
        Locale locale = Locale.forLanguageTag(localeStr.replace("_", "-"));
        match = match.toLowerCase(locale);
        for(Map.Entry<String, ScreenFactory> entry : mods.entrySet()) {
            if(entry.getKey().toLowerCase(locale).contains(match)) {
                found.add(entry);
            }
        }
        found.sort(Map.Entry.comparingByKey());
        return found;
    }
}
