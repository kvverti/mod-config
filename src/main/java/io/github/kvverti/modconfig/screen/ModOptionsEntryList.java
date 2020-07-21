package io.github.kvverti.modconfig.screen;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.github.kvverti.modconfig.data.SearchableOptions;
import io.github.kvverti.modconfig.data.option.ModOption;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

/**
 * An entry list for mod options rows.
 */
class ModOptionsEntryList extends AlwaysSelectedEntryListWidget<ModOptionsEntry> {

    private final ModOptionsScreen containingScreen;
    private final SearchableOptions allOptions = new SearchableOptions();

    private int entryIdx = -1;

    public ModOptionsEntryList(ModOptionsScreen containingScreen, MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.containingScreen = containingScreen;
        List<ModOption> mods = allOptions.findMods("");
        addMods(mods, new LiteralText("Mods"));
    }

    public void search(String match) {
        entryIdx = -1;
        this.clearEntries();
        List<ModOption> mods = allOptions.findMods(match);
        addMods(mods, new LiteralText("Mods"));
        if(!match.isEmpty()) {
            Map<String, List<ModOption>> options = allOptions.findOptions(match);
            for(Map.Entry<String, List<ModOption>> entry : options.entrySet()) {
                addMods(entry.getValue(), new LiteralText(entry.getKey()));
            }
        }
    }

    private void addMods(List<ModOption> mods, Text title) {
        if(!mods.isEmpty()) {
            this.addEntry(new LabelModOptionsEntry(this.client.textRenderer, title));
            for(Iterator<ModOption> itr = mods.iterator(); itr.hasNext(); ) {
                AbstractButtonWidget left = itr.next().createWidget(containingScreen, ModOptionsScreen.STANDARD_WIDTH, ModOptionsScreen.STANDARD_HEIGHT);
                AbstractButtonWidget right;
                if(itr.hasNext()) {
                    right = itr.next().createWidget(containingScreen, ModOptionsScreen.STANDARD_WIDTH, ModOptionsScreen.STANDARD_HEIGHT);
                } else {
                    right = null;
                }
                this.addEntry(new SettingsModOptionsEntry(left, right, this.client.textRenderer.isRightToLeft()));
            }
        }
    }

    private void saveScreenState() {
        containingScreen.setScrollPos(this.getScrollAmount());
        if(entryIdx != -1) {
            ModOptionsEntry entry = this.getEntry(entryIdx);
            int parity;
            if(entry instanceof SettingsModOptionsEntry) {
                parity = ((SettingsModOptionsEntry)entry).getFocusParity();
            } else {
                parity = 0;
            }
            containingScreen.setSelectedOptionIdx(2 * entryIdx + parity);
        } else {
            containingScreen.setSelectedOptionIdx(-1);
        }
    }

    public void setFocusedIndex(int idx) {
        if(idx >= 0 && idx < 2 * this.getItemCount()) {
            entryIdx = idx / 2;
            ModOptionsEntry elem = this.getEntry(entryIdx);
            elem.changeFocus(idx % 2 == 0);
        } else {
            if(entryIdx != -1) {
                ModOptionsEntry elem = this.getEntry(entryIdx);
                if(elem instanceof SettingsModOptionsEntry) {
                    ((SettingsModOptionsEntry)elem).clearFocus();
                }
                entryIdx = -1;
            }
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return (this.width / 2) + (ModOptionsScreen.ROW_WIDTH / 2) + ModOptionsScreen.PADDING_H;
    }

    @Override
    public int getRowWidth() {
        return ModOptionsScreen.ROW_WIDTH;
    }

    @Override
    protected void renderBackground(MatrixStack matrixStack) {
        containingScreen.renderBackground(matrixStack);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if(this.getItemCount() == 0) {
            return false;
        }
        if(entryIdx == -1) {
            entryIdx = lookForwards ? 0 : this.getItemCount() - 1;
        }
        ModOptionsEntry entry = this.getEntry(entryIdx);
        boolean hasFocus = entry.changeFocus(lookForwards);
        while(!hasFocus && entryIdx != -1) {
            entryIdx += lookForwards ? 1 : -1;
            if(entryIdx >= this.getItemCount()) {
                entryIdx = -1;
            }
            if(entryIdx != -1) {
                entry = this.getEntry(entryIdx);
                hasFocus = entry.changeFocus(lookForwards);
            }
        }
        this.centerScrollOn(entry);
        saveScreenState();
        return entryIdx != -1;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(entryIdx != -1 && this.getEntry(entryIdx).keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if(keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_UP) {
            // up/down arrows
            boolean down = keyCode == GLFW.GLFW_KEY_DOWN;
            if(entryIdx == -1) {
                this.changeFocus(down);
            } else {
                int parity = ((SettingsModOptionsEntry)this.getEntry(entryIdx)).getFocusParity();
                do {
                    changeFocus(down);
                } while(entryIdx != -1 && ((SettingsModOptionsEntry)this.getEntry(entryIdx)).getFocusParity() != parity);
            }
            return true;
        } else if(keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_LEFT) {
            // left/right arrows (RTL is left-forward, LTR is right-forward)
            boolean forward = keyCode == (this.client.textRenderer.isRightToLeft() ? GLFW.GLFW_KEY_LEFT : GLFW.GLFW_KEY_RIGHT);
            changeFocus(forward);
            return true;
        } else {
            // if the keypress scrolled the selection
            this.setSelected(null);
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        boolean ret = super.mouseScrolled(mouseX, mouseY, amount);
        saveScreenState();
        return ret;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        boolean ret = super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        saveScreenState();
        return ret;
    }
}
