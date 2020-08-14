package io.github.kvverti.modconfig.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import io.github.kvverti.modconfig.data.facade.ConfigFacade;
import io.github.kvverti.modconfig.data.facade.NestedScreenOptionFacade;
import io.github.kvverti.modconfig.data.facade.OptionFacade;
import io.github.kvverti.modconfig.data.option.ModOption;
import io.github.kvverti.modconfig.data.option.NestedScreenModOption;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.gui.screen.Screen;
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

    private final List<ModOption<?>> options = new ArrayList<>();

    public SearchableOptions() {
        reloadMods();
    }

    private void reloadMods() {
        mods.clear();
        options.clear();
        List<EntrypointContainer<ModMenuApi>> modMenuMods = FabricLoader.getInstance().getEntrypointContainers("modmenu", ModMenuApi.class);
        for(EntrypointContainer<ModMenuApi> container : modMenuMods) {
            {
                ConfigScreenFactory<?> factory = container.getEntrypoint().getModConfigScreenFactory();
                Screen screen = factory.create(null);
                if(screen != null) {
                    String modName = container.getProvider().getMetadata().getName();
                    Text name = new LiteralText(modName);
                    mods.add(new NestedScreenModOption(name, name, NestedScreenOptionFacade.makeFacade(name, factory::create)));
                    scrapeOptions(name, screen);
                }
            }
            for(Map.Entry<String, ConfigScreenFactory<?>> entry : container.getEntrypoint().getProvidedConfigScreenFactories().entrySet()) {
                String modId = entry.getKey();
                Optional<ModContainer> mod = FabricLoader.getInstance().getModContainer(modId);
                Screen screen = entry.getValue().create(null);
                if(mod.isPresent() && screen != null) {
                    Text name = new LiteralText(mod.get().getMetadata().getName());
                    mods.add(new NestedScreenModOption(name, name, NestedScreenOptionFacade.makeFacade(name, entry.getValue()::create)));
                    scrapeOptions(name, screen);
                }
            }
        }
    }

    private void scrapeOptions(Text modName, Screen screen) {
        if(screen instanceof ConfigFacade) {
            Map<Text, List<OptionFacade<?>>> widgetsByCategory = ((ConfigFacade)screen).modcfg_getOptionsByCategory();
            for(Map.Entry<Text, List<OptionFacade<?>>> entry : widgetsByCategory.entrySet()) {
                Text categoryName = entry.getKey();
                for(OptionFacade<?> configEntry : entry.getValue()) {
                    options.add(configEntry.modcfg_createOption(modName, categoryName));
                }
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
}
