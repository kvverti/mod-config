package io.github.kvverti.modconfig.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import io.github.kvverti.modconfig.api.ModConfigProvider;
import io.github.kvverti.modconfig.data.facade.ConfigFacade;
import io.github.kvverti.modconfig.data.facade.NestedScreenOptionFacade;
import io.github.kvverti.modconfig.data.facade.OptionFacade;
import io.github.kvverti.modconfig.data.option.ModOption;
import io.github.kvverti.modconfig.data.option.NestedScreenModOption;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

/**
 * Stores any found searchable options.
 */
public class SearchableOptions {
    /**
     * Mod options screens.
     */
    private final List<ModOption<?>> mods = new ArrayList<>();

    /**
     * Individual mod options.
     */
    private final List<ModOption<?>> options = new ArrayList<>();

    /**
     * Per-mod collective persistence callbacks.
     */
    private final List<Runnable> saveCallbacks = new ArrayList<>();

    public SearchableOptions() {
        reloadMods();
    }

    private void reloadMods() {
        mods.clear();
        options.clear();
        saveCallbacks.clear();

        // add vanilla Minecraft config
        var vanillaName = new LiteralText("Minecraft");
        ScreenFactory vanillaFactory = parent -> new OptionsScreen(parent, MinecraftClient.getInstance().options);
        mods.add(new NestedScreenModOption(vanillaName, vanillaName, NestedScreenOptionFacade.makeFacade(vanillaName, vanillaFactory)));

        // add modmenu mod configs
        List<EntrypointContainer<Object>> modMenuMods = FabricLoader.getInstance().getEntrypointContainers("modmenu", Object.class);
        for(EntrypointContainer<Object> container : modMenuMods) {
            if(container.getEntrypoint() instanceof ModConfigProvider provider) {
                var config = provider.getModConfig();
                var factory = provider.getModConfigScreenProvider();
                var screen = factory.create(null);
                var name = new LiteralText(container.getProvider().getMetadata().getName());
                if(config != null) {
                    scrapeOptions(name, config);
                    if(screen == null) {
                        // todo: construct a screen
                    }
                }
                if(screen != null) {
                    mods.add(new NestedScreenModOption(name, name, NestedScreenOptionFacade.makeFacade(name, factory)));
                }
            }
        }
    }

    private void scrapeOptions(Text modName, ConfigFacade facade) {
        saveCallbacks.add(facade.modcfg_persistCallback());
        Map<Text, List<OptionFacade<?>>> widgetsByCategory = facade.modcfg_getOptionsByCategory();
        for(Map.Entry<Text, List<OptionFacade<?>>> entry : widgetsByCategory.entrySet()) {
            Text categoryName = entry.getKey();
            for(OptionFacade<?> configEntry : entry.getValue()) {
                options.add(configEntry.modcfg_createOption(modName, categoryName));
            }
        }
    }

    /**
     * Finds and returns all mods whose names match the given query.
     */
    public List<ModOption<?>> findMods(String match) {
        return findFrom(mods, match);
    }

    public SortedMap<String, List<ModOption<?>>> findOptions(String match) {
        List<ModOption<?>> found = findFrom(options, match);
        SortedMap<String, List<ModOption<?>>> map = new TreeMap<>();
        for(ModOption<?> opt : found) {
            String modName = opt.getLocalizedModName();
            map.computeIfAbsent(modName, k -> new ArrayList<>()).add(opt);
        }
        return map;
    }

    private static List<ModOption<?>> findFrom(List<ModOption<?>> modOptions, String match) {
        List<ModOption<?>> found = new ArrayList<>();
        List<ModOption<?>> foundInCategory = new ArrayList<>();
        List<ModOption<?>> foundInMod = new ArrayList<>();
        Locale locale = TextUtil.getCurrentLocale();
        match = match.toLowerCase(locale);
        for(ModOption<?> option : modOptions) {
            if(option.getLocalizedOptionName().toLowerCase(locale).contains(match)) {
                found.add(option);
            } else if(option.getLocalizedCategoryName().toLowerCase(locale).contains(match)) {
                foundInCategory.add(option);
            } else if(option.getLocalizedModName().toLowerCase(locale).contains(match)) {
                foundInMod.add(option);
            }
        }
        found.sort(Comparator.comparing(ModOption::getLocalizedOptionName));
        foundInCategory.sort(Comparator.comparing(ModOption::getLocalizedCategoryName));
        foundInMod.sort(Comparator.comparing(ModOption::getLocalizedModName));
        found.addAll(foundInCategory);
        found.addAll(foundInMod);
        return found;
    }

    public void saveOptions() {
        for(Runnable callback : saveCallbacks) {
            callback.run();
        }
    }
}
