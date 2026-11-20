package my.celium.org.f3keybinds.fabric;

import my.celium.org.f3keybinds.F3KeybindsMod;
import my.celium.org.f3keybinds.MissingMycel;
import my.celium.org.f3keybinds.ModGate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/** Fabric entrypoint. Everything else is client side. */
public final class F3KeybindsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        if (!FabricLoader.getInstance().isModLoaded("mycel")) {
            MissingMycel.logOnce();
            return;
        }
        ModGate.open();
        F3KeybindsMod.init();
    }
}
