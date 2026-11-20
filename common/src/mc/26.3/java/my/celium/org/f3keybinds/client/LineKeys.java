package my.celium.org.f3keybinds.client;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

/** F3 shortcuts only some Minecraft lines have; the 26.3 copy (one per line in common/src/mc). */
final class LineKeys {
    private LineKeys() {
    }

    /** 26.3 added Toggle Improved Transparency. */
    static void addExtra(BiConsumer<String, Function<Options, KeyMapping>> add) {
        add.accept("improvedTransparency", o -> o.keyDebugSwitchTranslucencyMode);
    }
}
