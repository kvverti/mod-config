package io.github.kvverti.modconfig.mixin;

import java.util.List;

import io.github.kvverti.modconfig.screen.ModOptionsEntry;
import io.github.kvverti.modconfig.screen.ModOptionsEntryList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.widget.EntryListWidget;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin<E extends EntryListWidget.Entry<E>> extends AbstractParentElement {

    @Shadow
    @Final
    private List<E> children;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getEntryAtPosition", at = @At("HEAD"), cancellable = true)
    private void modCfgMouseOverOverlay(double x, double y, CallbackInfoReturnable<E> info) {
        if((Object)this instanceof ModOptionsEntryList) {
            for(E e : this.children) {
                if(((ModOptionsEntry)e).isMouseOverOverlay(x, y)) {
                    info.setReturnValue(e);
                    return;
                }
            }
        }
    }
}
