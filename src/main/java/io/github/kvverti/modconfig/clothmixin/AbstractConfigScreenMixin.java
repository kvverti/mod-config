package io.github.kvverti.modconfig.clothmixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import io.github.kvverti.modconfig.data.facade.ConfigFacade;
import io.github.kvverti.modconfig.data.facade.OptionFacade;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Mixin(AbstractConfigScreen.class)
public abstract class AbstractConfigScreenMixin extends Screen implements ConfigFacade {

    @Shadow(remap = false)
    @Nullable
    private Runnable savingRunnable;

    @Shadow(remap = false)
    public abstract Map<Text, List<AbstractConfigEntry<?>>> getCategorizedEntries();

    private AbstractConfigScreenMixin() {
        super(null);
    }

    @Override
    public Map<Text, List<OptionFacade<?>>> modcfg_getOptionsByCategory() {
        Map<Text, List<OptionFacade<?>>> res = new HashMap<>();
        for(Map.Entry<Text, List<AbstractConfigEntry<?>>> entry : this.getCategorizedEntries().entrySet()) {
            List<OptionFacade<?>> facades = new ArrayList<>();
            for(AbstractConfigEntry<?> configEntry : entry.getValue()) {
                if(configEntry instanceof OptionFacade<?>) {
                    facades.add((OptionFacade<?>)configEntry);
                }
            }
            res.put(entry.getKey(), facades);
        }
        return res;
    }

    @Override
    public Runnable modcfg_persistCallback() {
        return this.savingRunnable != null ? this.savingRunnable : () -> {
        };
    }
}
