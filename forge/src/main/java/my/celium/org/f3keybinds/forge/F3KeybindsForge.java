package my.celium.org.f3keybinds.forge;

import my.celium.org.client.ClientHooks;
import my.celium.org.f3keybinds.F3KeybindsMod;
import my.celium.org.f3keybinds.MissingMycel;
import my.celium.org.f3keybinds.ModGate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

/** Forge entrypoint. Everything else is client side, in {@link ForgeClient}. */
@Mod(F3KeybindsMod.ID)
public final class F3KeybindsForge {
    public F3KeybindsForge(@SuppressWarnings("unused") FMLJavaModLoadingContext context) {
        if (!ModList.isLoaded("mycel")) {
            MissingMycel.logOnce();
            if (FMLEnvironment.dist == Dist.CLIENT) {
                TickEvent.ClientTickEvent.Post.BUS.addListener(event -> MissingMycel.tick(Minecraft.getInstance()));
            }
            return;
        }
        ModGate.open();
        F3KeybindsMod.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (Minecraft minecraft, Screen parent) -> ClientHooks.createConfigScreen(parent, F3KeybindsMod.ID)));
            ForgeClient.register();
        }
    }
}
