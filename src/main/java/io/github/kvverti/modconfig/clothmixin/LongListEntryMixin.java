package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.facade.TextFieldOptionFacade;
import me.shedaniel.clothconfig2.gui.entries.LongListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextFieldListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(LongListEntry.class)
public abstract class LongListEntryMixin extends TextFieldListEntry<Long> implements TextFieldOptionFacade {

    @Shadow(remap = false)
    private long minimum;

    @Shadow(remap = false)
    private long maximum;

    @Final
    @Shadow(remap = false)
    private Consumer<Long> saveConsumer;

    @Shadow(remap = false)
    public abstract Long getValue();

    private LongListEntryMixin() {
        super(null, null, null, () -> null);
    }

    @Override
    public int modcfg_getMaxLength() {
        // 19 digits plus sign
        return 20;
    }

    @Override
    public Predicate<String> modcfg_getTextPredicate() {
        return value -> value.matches("[+-]?\\d*");
    }

    @Override
    public Predicate<String> modcfg_getValidator() {
        long min = this.minimum;
        long max = this.maximum;
        return value -> {
            try {
                long i = Long.parseLong(value);
                return i >= min && i <= max;
            } catch(NumberFormatException e) {
                return false;
            }
        };
    }

    @Override
    public boolean modcfg_isShort() {
        return true;
    }

    @Override
    public Text modcfg_getOptionName() {
        return this.getFieldName();
    }

    @Override
    public String modcfg_getValue() {
        return this.getValue().toString();
    }

    @Override
    public Consumer<String> modcfg_getSaveHandler() {
        Consumer<Long> s = this.saveConsumer;
        return value -> s.accept(Long.parseLong(value));
    }
}
