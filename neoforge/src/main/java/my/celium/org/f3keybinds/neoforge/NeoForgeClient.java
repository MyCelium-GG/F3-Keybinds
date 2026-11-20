package my.celium.org.f3keybinds.neoforge;

import my.celium.org.f3keybinds.client.F3Keys;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

/** NeoForge client side: the keys and the tick that runs them. Only loaded on clients. */
final class NeoForgeClient {
    private NeoForgeClient() {
    }

    static void register(IEventBus modBus) {
        modBus.addListener((RegisterKeyMappingsEvent event) -> F3Keys.keys().forEach(event::register));
        NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> F3Keys.tick(Minecraft.getInstance()));
    }
}
