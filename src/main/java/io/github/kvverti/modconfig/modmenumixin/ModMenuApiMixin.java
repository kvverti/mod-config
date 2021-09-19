package io.github.kvverti.modconfig.modmenumixin;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.kvverti.modconfig.api.ModConfigProvider;
import io.github.kvverti.modconfig.data.ScreenFactory;
import io.github.kvverti.modconfig.data.facade.ConfigFacade;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModMenuApi.class, remap = false)
public interface ModMenuApiMixin extends ModConfigProvider {
    @Shadow
    ConfigScreenFactory<?> getModConfigScreenFactory();

    @Nullable
    @Override
    default ConfigFacade getModConfig() {
        var factory = this.getModConfigScreenFactory();
        var screen = factory.create(null);
        if(screen instanceof ConfigFacade facade) {
            return facade;
        }
        return null;
    }

    @Nullable
    @Override
    default ScreenFactory getModConfigScreenProvider() {
        return this.getModConfigScreenFactory()::create;
    }
}
