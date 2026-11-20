package my.celium.org.f3keybinds;

import my.celium.org.stats.ModStats;

/**
 * Anonymous stats for the FastStats project {@code f3keybinds}, sent through Mycel under its
 * consent: which F3 actions people use from their own keys.
 */
public final class F3KeybindsStats {
    /** Project token from faststats.dev (public, ships in the jar). Empty until the project exists. */
    private static final String TOKEN = "";

    private static volatile ModStats stats;

    private F3KeybindsStats() {
    }

    static void start() {
        if (TOKEN.isEmpty()) {
            return;
        }
        stats = ModStats.builder(F3KeybindsMod.META, TOKEN)
                .bool("enabled", () -> F3KeybindsMod.enabled.get())
                .counters("actions")
                .errors("my.celium.org.f3keybinds.")
                .start();
    }

    /** Counts one use of an F3 action. */
    public static void action(String id) {
        ModStats current = stats;
        if (current != null) {
            current.count("actions", id);
        }
    }
}
