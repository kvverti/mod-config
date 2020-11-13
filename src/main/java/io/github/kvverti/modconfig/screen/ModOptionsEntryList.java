package io.github.kvverti.modconfig.screen;

import java.util.ArrayList;
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
public class ModOptionsEntryList extends AlwaysSelectedEntryListWidget<ModOptionsEntry> {

    private final ModOptionsScreen containingScreen;
    private final SearchableOptions allOptions = new SearchableOptions();

    // zero for first column, one for second column
    private int columnParity = 0;

    public ModOptionsEntryList(ModOptionsScreen containingScreen, MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.containingScreen = containingScreen;
        List<ModOption<?>> mods = allOptions.findMods("");
        addMods(mods, new LiteralText("Mods"));
    }

    public void save() {
        allOptions.saveOptions();
    }

    public void search(String match) {
        this.setFocused(null);
        this.clearEntries();
        List<ModOption<?>> mods = allOptions.findMods(match);
        addMods(mods, new LiteralText("Mods"));
        if(!match.isEmpty()) {
            Map<String, List<ModOption<?>>> options = allOptions.findOptions(match);
            for(Map.Entry<String, List<ModOption<?>>> entry : options.entrySet()) {
                addMods(entry.getValue(), new LiteralText(entry.getKey()));
            }
        }
    }

    private void addMods(List<ModOption<?>> mods, Text title) {
        if(!mods.isEmpty()) {
            this.addEntry(new LabelModOptionsEntry(this.client.textRenderer, title));
            for(Iterator<ModOption<?>> itr = mods.iterator(); itr.hasNext(); ) {
                ModOption<?> option = itr.next();
                AbstractButtonWidget left = option.createWidget(containingScreen, ModOptionsScreen.STANDARD_WIDTH, ModOptionsScreen.STANDARD_HEIGHT);
                if(option.isFullWidth()) {
                    Text label = option.getOptionName();
                    this.addEntry(new WideSettingModOptionsEntry(label, left, this.client.textRenderer.isRightToLeft()));
                } else {
                    AbstractButtonWidget right;
                    List<ModOptionsEntry> wideOptions = new ArrayList<>();
                    option = null;
                    // save wide options for after the second non-wide option; this prevents empty spaces in non-wide entries
                    while(itr.hasNext() && (option = itr.next()).isFullWidth()) {
                        Text label = option.getOptionName();
                        AbstractButtonWidget widget = option.createWidget(containingScreen, ModOptionsScreen.STANDARD_WIDTH, ModOptionsScreen.STANDARD_HEIGHT);
                        wideOptions.add(new WideSettingModOptionsEntry(label, widget, this.client.textRenderer.isRightToLeft()));
                        option = null;
                    }
                    if(option != null) {
                        right = option.createWidget(containingScreen, ModOptionsScreen.STANDARD_WIDTH, ModOptionsScreen.STANDARD_HEIGHT);
                    } else {
                        right = null;
                    }
                    this.addEntry(new SettingsModOptionsEntry(left, right, this.client.textRenderer.isRightToLeft()));
                    for(ModOptionsEntry wideOption : wideOptions) {
                        this.addEntry(wideOption);
                    }
                }
            }
        }
    }

    private void saveScreenState() {
        containingScreen.setScrollPos(this.getScrollAmount());
        ModOptionsEntry focused = this.getFocused();
        if(focused != null) {
            containingScreen.setSelectedOptionIdx(2 * this.children().indexOf(focused) + columnParity);
        } else {
            containingScreen.setSelectedOptionIdx(-1);
        }
    }

    public void setFocusedIndex(int idx) {
        if(idx >= 0 && idx < 2 * this.getItemCount()) {
            columnParity = idx % 2;
            ModOptionsEntry elem = this.getEntry(idx / 2);
            elem.setFocusedColumn(idx % 2);
            this.setFocused(elem);
        } else {
            ModOptionsEntry elem = this.getFocused();
            if(elem != null) {
                elem.clearFocus();
                this.setFocused(null);
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
    protected void renderList(MatrixStack matrixStack, int x, int j, int mouseX, int mouseY, float delta) {
        super.renderList(matrixStack, x, j, mouseX, mouseY, delta);
        for(int i = 0; i < this.getItemCount(); i++) {
            int top = this.getRowTop(i);
            int bottom = top + this.itemHeight;
            if(bottom >= this.top && top <= this.bottom) {
                this.getEntry(i).renderOverlay(matrixStack, mouseX, mouseY, delta);
            }
        }
    }

    private int shiftEntryIdx(int idx, boolean forward) {
        int res = idx + (forward ? 1 : -1);
        if(res >= this.getItemCount()) {
            res = -1;
        }
        return res;
    }

    private void focusOnEntry(int idx) {
        if(idx != -1) {
            ModOptionsEntry entry = this.getEntry(idx);
            this.setFocused(entry);
            this.centerScrollOn(entry);
        } else {
            this.setFocused(null);
        }
        saveScreenState();
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if(this.getItemCount() == 0) {
            return false;
        }
        ModOptionsEntry entry = this.getFocused();
        int entryIdx;
        if(entry == null) {
            entryIdx = lookForwards ? 0 : this.getItemCount() - 1;
            entry = this.getEntry(entryIdx);
        } else {
            entryIdx = this.children().indexOf(entry);
        }
        boolean hasFocus = entry.changeFocus(lookForwards);
        while(!hasFocus && entryIdx != -1) {
            entryIdx = shiftEntryIdx(entryIdx, lookForwards);
            if(entryIdx != -1) {
                entry = this.getEntry(entryIdx);
                hasFocus = entry.changeFocus(lookForwards);
            }
        }
        int parity = entry.getFocusColumnParity();
        if(parity != -1) {
            columnParity = parity;
        }
        focusOnEntry(entryIdx);
        return entryIdx != -1;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return this.getFocused() != null && this.getFocused().charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if(keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_UP) {
            // up/down arrows
            boolean down = keyCode == GLFW.GLFW_KEY_DOWN;
            // navigating vertically should not change the parity
            ModOptionsEntry oldEntry = this.getFocused();
            int entryIdx;
            if(oldEntry == null) {
                entryIdx = down ? -1 : this.getItemCount();
            } else {
                entryIdx = this.children().indexOf(oldEntry);
                oldEntry.clearFocus();
            }
            boolean done;
            do {
                entryIdx = shiftEntryIdx(entryIdx, down);
                if(entryIdx != -1) {
                    done = this.getEntry(entryIdx).setFocusedColumn(columnParity);
                } else {
                    done = true;
                }
            } while(!done);
            focusOnEntry(entryIdx);
            return true;
        } else if(keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_LEFT) {
            // left/right arrows (RTL is left-forward, LTR is right-forward)
            boolean forward = keyCode == (this.client.textRenderer.isRightToLeft() ? GLFW.GLFW_KEY_LEFT : GLFW.GLFW_KEY_RIGHT);
            int entryIdx;
            int col;
            if(this.getFocused() == null) {
                entryIdx = forward ? -1 : this.getItemCount();
                col = forward ? 1 : 0;
            } else {
                ModOptionsEntry entry = this.getFocused();
                entryIdx = this.children().indexOf(entry);
                col = columnParity;
                entry.clearFocus();
            }
            boolean done;
            do {
                if(forward == (col == 1)) {
                    entryIdx = shiftEntryIdx(entryIdx, forward);
                }
                col ^= 1;
                if(entryIdx != -1) {
                    done = this.getEntry(entryIdx).setFocusedColumn(col);
                } else {
                    done = true;
                }
            } while(!done);
            focusOnEntry(entryIdx);
            columnParity = col;
            return true;
        } else {
            // if the keypress scrolled the selection
            this.setSelected(null);
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ModOptionsEntry oldFocused = this.getFocused();
        boolean ret = super.mouseClicked(mouseX, mouseY, button);
        ModOptionsEntry focused = this.getFocused();
        if(ret && focused != null) {
            if(oldFocused != null && oldFocused != focused) {
                oldFocused.clearFocus();
            }
            int parity = focused.getFocusColumnParity();
            if(parity != -1) {
                columnParity = parity;
            }
        }
        return ret;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(this.getFocused() != null && this.getFocused().mouseScrolled(mouseX, mouseY, amount)) {
            return true;
        }
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
