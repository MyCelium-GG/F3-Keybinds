package my.celium.org.f3keybinds.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.blaze3d.platform.InputConstants;

import my.celium.org.f3keybinds.F3KeybindsMod;
import my.celium.org.f3keybinds.F3KeybindsStats;
import my.celium.org.f3keybinds.mixin.client.KeyMappingAccessor;
import my.celium.org.f3keybinds.mixin.client.KeyboardHandlerInvoker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.input.KeyEvent;

/**
 * One extra key per F3 shortcut, listed in Controls under Debug and unbound at first. Pressing one
 * runs vanilla's own F3 code for that shortcut, as if F3 and the shortcut's key were pressed
 * together. Client only.
 */
public final class F3Keys {
    /** One F3 shortcut: our key and the vanilla mapping it stands in for. */
    public record Action(String id, KeyMapping key, Function<Options, KeyMapping> vanilla) {
    }

    /** Placeholder keys, tried in order; one no other key mapping uses is picked. */
    private static final int[] PLACEHOLDERS = {
            InputConstants.KEY_F24, InputConstants.KEY_F23, InputConstants.KEY_F22, InputConstants.KEY_F21,
            InputConstants.KEY_F20, InputConstants.KEY_F19
    };

    private static final List<Action> ACTIONS = new ArrayList<>();

    /** True while our own key opens the game mode switcher, so the switcher can show that key. */
    private static boolean openingSwitcher;

    static {
        add("showChunkBorders", o -> o.keyDebugShowChunkBorders);
        add("showHitboxes", o -> o.keyDebugShowHitboxes);
        add("showAdvancedTooltips", o -> o.keyDebugShowAdvancedTooltips);
        add("reloadChunks", o -> o.keyDebugReloadChunk);
        add("reloadResourcePacks", o -> o.keyDebugReloadResourcePacks);
        add("clearChat", o -> o.keyDebugClearChat);
        add("copyLocation", o -> o.keyDebugCopyLocation);
        add("copyData", o -> o.keyDebugCopyRecreateCommand);
        add("gameModeSwitcher", o -> o.keyDebugSwitchGameMode);
        add("cycleSpectator", o -> o.keyDebugSpectate);
        add("debugOptions", o -> o.keyDebugDebugOptions);
        add("focusPause", o -> o.keyDebugFocusPause);
        add("fpsCharts", o -> o.keyDebugFpsCharts);
        add("networkCharts", o -> o.keyDebugNetworkCharts);
        add("profilingChart", o -> o.keyDebugPofilingChart);
        add("lightmapTexture", o -> o.keyDebugLightmapTexture);
        add("profiling", o -> o.keyDebugProfiling);
        add("dumpDynamicTextures", o -> o.keyDebugDumpDynamicTextures);
        add("dumpVersion", o -> o.keyDebugDumpVersion);
        LineKeys.addExtra(F3Keys::add);
    }

    private F3Keys() {
    }

    static void add(String id, Function<Options, KeyMapping> vanilla) {
        KeyMapping key = new KeyMapping("key.f3keybinds." + id, InputConstants.UNKNOWN.getValue(),
                KeyMapping.Category.DEBUG);
        ACTIONS.add(new Action(id, key, vanilla));
    }

    /** Every action, in Controls order. */
    public static List<Action> actions() {
        return Collections.unmodifiableList(ACTIONS);
    }

    /** Every key to register with the loader. */
    public static List<KeyMapping> keys() {
        List<KeyMapping> keys = new ArrayList<>(ACTIONS.size());
        for (Action action : ACTIONS) {
            keys.add(action.key());
        }
        return keys;
    }

    /** Runs every client tick on every loader. Keys only count presses while no screen is open. */
    public static void tick(Minecraft minecraft) {
        if (minecraft == null) {
            return;
        }
        boolean on = minecraft.player != null && F3KeybindsMod.isModEnabled();
        for (Action action : ACTIONS) {
            while (action.key().consumeClick()) {
                if (on) {
                    run(minecraft, action);
                }
            }
        }
    }

    /** The game mode switcher's own key, so the open switcher can react to it. */
    public static KeyMapping gameModeKey() {
        for (Action action : ACTIONS) {
            if (action.id().equals("gameModeSwitcher")) {
                return action.key();
            }
        }
        throw new IllegalStateException("no game mode switcher key");
    }

    /** Runs one F3 shortcut through vanilla's F3 code. */
    static void run(Minecraft minecraft, Action action) {
        KeyboardHandlerInvoker handler = (KeyboardHandlerInvoker) minecraft.keyboardHandler;
        openingSwitcher = action.key() == gameModeKey();
        try {
            pressAs(minecraft, action.vanilla().apply(minecraft.options), handler::f3keybinds$handleDebugKeys);
        } finally {
            openingSwitcher = false;
        }
        F3KeybindsStats.action(action.id());
    }

    /** @return whether the game mode switcher being opened right now comes from our own key */
    public static boolean openingSwitcher() {
        return openingSwitcher;
    }

    /**
     * Sends {@code target} one key press that only {@code vanilla} answers to. The vanilla mapping
     * gets a free placeholder key for the call and its own key back right after, so it works
     * whatever the player bound it to, even nothing or a mouse button, and even if two F3
     * shortcuts share a key.
     */
    public static void pressAs(Minecraft minecraft, KeyMapping vanilla, Consumer<KeyEvent> target) {
        InputConstants.Key placeholder = freePlaceholder(minecraft.options);
        KeyMappingAccessor access = (KeyMappingAccessor) vanilla;
        InputConstants.Key own = access.f3keybinds$key();
        int modifiers = minecraft.hasShiftDown() ? InputConstants.MOD_SHIFT : 0;
        vanilla.setKey(placeholder);
        try {
            target.accept(new KeyEvent(placeholder.getValue(), 0, modifiers));
        } finally {
            vanilla.setKey(own);
        }
    }

    private static InputConstants.Key freePlaceholder(Options options) {
        for (int code : PLACEHOLDERS) {
            InputConstants.Key candidate = InputConstants.getKey(new KeyEvent(code, 0, 0));
            boolean used = false;
            for (KeyMapping mapping : options.keyMappings) {
                if (candidate.equals(((KeyMappingAccessor) mapping).f3keybinds$key())) {
                    used = true;
                    break;
                }
            }
            if (!used) {
                return candidate;
            }
        }
        return InputConstants.getKey(new KeyEvent(PLACEHOLDERS[0], 0, 0));
    }
}
