package io.github.kvverti.modconfig.iface;

public interface ClearFocus {

    /**
     * Nonvirtually bound superclass implementation.
     */
    default void modcfg_super_clearFocus() {
        throw new AssertionError("Must be implemented in superclass");
    }

    void modcfg_clearFocus();
}
