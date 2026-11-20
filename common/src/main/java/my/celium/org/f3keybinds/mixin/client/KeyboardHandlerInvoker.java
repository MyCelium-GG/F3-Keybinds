package my.celium.org.f3keybinds.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;

/** Vanilla's F3 + key handling, which normally only runs while F3 is held. */
@Mixin(KeyboardHandler.class)
public interface KeyboardHandlerInvoker {
    @Invoker("handleDebugKeys")
    boolean f3keybinds$handleDebugKeys(KeyEvent event);
}
