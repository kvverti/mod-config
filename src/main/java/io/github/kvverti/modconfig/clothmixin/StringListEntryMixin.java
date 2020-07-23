package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.facade.TextFieldOptionFacade;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextFieldListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(StringListEntry.class)
public abstract class StringListEntryMixin extends TextFieldListEntry<String> implements TextFieldOptionFacade {

    @Shadow
    public abstract String getValue();

    @Shadow
    private Consumer<String> saveConsumer;

    private StringListEntryMixin() {
        super(null, null, null, () -> null);
    }

    @Override
    public int modcfg_getMaxLength() {
        return 999999;
    }

    @Override
    public Predicate<String> modcfg_getValidator() {
        return value -> true;
    }

    @Override
    public Text modcfg_getOptionName() {
        return this.getFieldName();
    }

    @Override
    public String modcfg_getValue() {
        return this.getValue();
    }

    @Override
    public Consumer<String> modcfg_getSaveHandler() {
        return this.saveConsumer;
    }
}
