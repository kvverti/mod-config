package io.github.kvverti.modconfig.screen;

import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

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
     * Sets the given column to be focused. Returns whether the column was focused.
     */
    public abstract boolean setFocusedColumn(int col);

    /**
     * Clears the focus from this entry. Idempotent.
     */
    public void clearFocus() {
    }

    public void renderOverlay(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    /**
     * Determines whether the mouse is over the overlay for a widget. Overlays are on the front-most z plane
     * and mask the widgets behind them.
     */
    public boolean isMouseOverOverlay(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public Text getNarration() {
        // todo
        return new LiteralText("");
    }
}
