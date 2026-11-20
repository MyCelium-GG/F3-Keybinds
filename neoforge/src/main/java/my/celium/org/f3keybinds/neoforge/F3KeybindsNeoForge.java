package my.celium.org.f3keybinds.neoforge;

import my.celium.org.client.ClientHooks;
import my.celium.org.f3keybinds.F3KeybindsMod;
import my.celium.org.f3keybinds.MissingMycel;
import my.celium.org.f3keybinds.ModGate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

/** NeoForge entrypoint. Everything else is client side, in {@link NeoForgeClient}. */
@Mod(F3KeybindsMod.ID)
public final class F3KeybindsNeoForge {
    public F3KeybindsNeoForge(IEventBus modBus, @SuppressWarnings("unused") ModContainer container) {
        if (!ModList.get().isLoaded("mycel")) {
            MissingMycel.logOnce();
            if (FMLEnvironment.getDist().isClient()) {
                NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> MissingMycel.tick(Minecraft.getInstance()));
            }
            return;
        }
        ModGate.open();
        F3KeybindsMod.init();
        if (FMLEnvironment.getDist().isClient()) {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
                    () -> (ModContainer ignored, Screen parent) -> ClientHooks.createConfigScreen(parent, F3KeybindsMod.ID));
            NeoForgeClient.register(modBus);
        }
    }
}
