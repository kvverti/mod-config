package io.github.kvverti.modconfig.api;

import javax.annotation.Nullable;

import io.github.kvverti.modconfig.data.ScreenFactory;
import io.github.kvverti.modconfig.data.facade.ConfigFacade;

/**
 * The entry point for ModConfig. For legacy reasons, ModConfig searches for implementations of
 * this interface under the "modmenu" entry point if a given mod does not define an entry for ModConfig's
 * entry point.
 *
 * If both of {@link #getModConfig()} and {@link #getModConfigScreenProvider()} return {@code null}, then
 * the mod is not shown in the ModConfig searchable options screen.
 */
public interface ModConfigProvider {
    /**
     * Returns the configuration data for this mod, or {@code null} if the mod does not wish to provide
     * configuration data.
     */
    @Nullable
    ConfigFacade getModConfig();

    /**
     * Returns a factory for the mod's config screen. If the factory returns {@code null}, ModConfig will
     * generate a screen based on the config, if any.
     *
     * <p>The default factory always returns {@code null}.
     */
    default ScreenFactory getModConfigScreenProvider() {
        return parent -> null;
    }
}
