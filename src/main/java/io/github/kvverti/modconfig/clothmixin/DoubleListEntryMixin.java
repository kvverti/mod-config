package io.github.kvverti.modconfig.clothmixin;

import java.util.function.Consumer;
import java.util.function.Predicate;

import io.github.kvverti.modconfig.data.facade.TextFieldOptionFacade;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextFieldListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.text.Text;

@Mixin(DoubleListEntry.class)
public abstract class DoubleListEntryMixin extends TextFieldListEntry<Double> implements TextFieldOptionFacade {

    @Shadow
    private double minimum;

    @Shadow
    private double maximum;

    @Shadow
    private Consumer<Double> saveConsumer;

    @Shadow
    public abstract Double getValue();

    private DoubleListEntryMixin() {
        super(null, null, null, () -> null);
    }

    @Override
    public int modcfg_getMaxLength() {
        // arbitrary but should fit all floating point values
        return 100;
    }

    @Override
    public Predicate<String> modcfg_getTextPredicate() {
        return value -> value.matches("[+-]?(\\d*\\.)?\\d*([eE][+-]?\\d{0,3})?");
    }

    @Override
    public Predicate<String> modcfg_getValidator() {
        double min = this.minimum;
        double max = this.maximum;
        return value -> {
            try {
                double i = Double.parseDouble(value);
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
        Consumer<Double> s = this.saveConsumer;
        return value -> s.accept(Double.parseDouble(value));
    }
}
