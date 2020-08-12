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
class ModOptionsEntryList extends AlwaysSelectedEntryListWidget<ModOptionsEntry> {

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
            elem.changeFocus(idx % 2 == 0);
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
        int parity = entry.getFocusColumnParity();
        if(parity != -1) {
            columnParity = parity;
        }
        if(entryIdx != -1) {
            this.setFocused(entry);
            saveScreenState();
            return true;
        } else {
            this.setFocused(null);
            saveScreenState();
            return false;
        }
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
            if(this.getFocused() == null) {
                this.changeFocus(down);
            } else {
                // navigating vertically should not change the parity
                ModOptionsEntry prevFocused = this.getFocused();
                int parity = prevFocused.getFocusColumnParity();
                if(parity == -1) {
                    parity = columnParity;
                }
                boolean done;
                do {
                    changeFocus(down);
                    ModOptionsEntry focused = this.getFocused();
                    if(focused != null) {
                        int entryParity = focused.getFocusColumnParity();
                        done = focused != prevFocused && (entryParity == -1 || entryParity == parity);
                    } else {
                        done = true;
                    }
                } while(!done);
                // must reset the parity afterwards (as changeFocus() updates the parity)
                columnParity = parity;
                saveScreenState();
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
