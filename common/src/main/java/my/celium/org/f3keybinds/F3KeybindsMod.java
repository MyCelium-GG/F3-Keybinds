package my.celium.org.f3keybinds;

import my.celium.org.Mycel;
import my.celium.org.config.ConfigBuilder;
import my.celium.org.config.ConfigValue;
import my.celium.org.config.MycelConfig;
import my.celium.org.metadata.ModMetadata;

/**
 * F3 Keybinds: every F3 shortcut (F3 + G chunk borders, F3 + B hitboxes, ...) gets a key of its
 * own in Controls, so it works without holding F3. Made for laptops and small keyboards, where
 * Fn + F3 + G often does nothing.
 *
 * <p>Client side only. The keys run vanilla's own F3 code, so the chat feedback and the server's
 * "reduced debug info" rule work exactly like vanilla. The client part lives in {@code client/}.
 */
public final class F3KeybindsMod {
    public static final String ID = "f3keybinds";

    public static final ModMetadata META = ModMetadata.builder(ID, "F3 Keybinds", "1.0.0")
            .author("MyCelium")
            .website("https://www.curseforge.com/minecraft/mc-mods/f3-keybinds")
            .updateUrl("https://wiki.mycelium-gg.org/updates/f3keybinds.json")
            .build();

    public static ConfigValue<Boolean> enabled;
    public static MycelConfig config;

    private F3KeybindsMod() {
    }

    /** Called once from each loader's entrypoint. */
    public static void init() {
        Mycel.initialize();
        Mycel.registerMod(META);

        ConfigBuilder builder = Mycel.config(META);
        enabled = builder.booleanValue("enabled", true,
                "Master switch for F3 Keybinds. Set the keys in Options > Controls > Key Binds > Debug.");
        config = builder.build();
        F3KeybindsStats.start();
    }

    public static boolean isModEnabled() {
        return Mycel.isEnabled(ID) && enabled != null && enabled.get();
    }
}
