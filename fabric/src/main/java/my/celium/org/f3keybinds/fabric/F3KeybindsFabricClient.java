package my.celium.org.f3keybinds.fabric;

import my.celium.org.f3keybinds.MissingMycel;
import my.celium.org.f3keybinds.client.F3Keys;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;

/** Fabric client entrypoint: the keys and the tick that runs them. */
public final class F3KeybindsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (!FabricLoader.getInstance().isModLoaded("mycel")) {
            ClientTickEvents.END_CLIENT_TICK.register(MissingMycel::tick);
            return;
        }
        F3Keys.keys().forEach(KeyMappingHelper::registerKeyMapping);
        ClientTickEvents.END_CLIENT_TICK.register(F3Keys::tick);
    }
}
