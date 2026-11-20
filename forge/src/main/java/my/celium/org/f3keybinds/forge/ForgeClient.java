package my.celium.org.f3keybinds.forge;

import my.celium.org.f3keybinds.client.F3Keys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;

/** Forge client side: the keys and the tick that runs them. Only loaded on clients. */
final class ForgeClient {
    private ForgeClient() {
    }

    static void register() {
        RegisterKeyMappingsEvent.BUS.addListener(event -> F3Keys.keys().forEach(event::register));
        TickEvent.ClientTickEvent.Post.BUS.addListener(event -> F3Keys.tick(Minecraft.getInstance()));
    }
}
