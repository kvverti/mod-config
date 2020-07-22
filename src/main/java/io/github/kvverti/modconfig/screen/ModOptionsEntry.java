package io.github.kvverti.modconfig.screen;

import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;

/**
 * An entry in the mod config screen.
 */
public abstract class ModOptionsEntry extends AlwaysSelectedEntryListWidget.Entry<ModOptionsEntry> {

    protected ModOptionsEntry() {
    }

    /**
     * The current focused element's column parity. Returns 0 for first, 1 for second, -1 for pass through.
     */
    public int getFocusColumnParity() {
        return -1;
    }

    /**
     * Clears the focus from this entry. Idempotent.
     */
    public void clearFocus() {
    }
}
