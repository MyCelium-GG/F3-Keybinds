package my.celium.org.f3keybinds.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.platform.InputConstants;

import my.celium.org.f3keybinds.ModGate;
import my.celium.org.f3keybinds.client.F3Keys;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/**
 * Vanilla's game mode switcher picks a mode when F3 is let go. Opened with one key there is no F3
 * to let go, so: the same key again moves to the next mode, Enter, Space or a left click picks it.
 * The "Next" hint then names that key instead of F4.
 */
@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSwitcherScreenMixin extends Screen {
    @Shadow
    private void switchToHoveredGameMode() {
    }

    @Unique
    private boolean f3keybinds$oneKey;

    protected GameModeSwitcherScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void f3keybinds$init(CallbackInfo ci) {
        f3keybinds$oneKey = ModGate.on() && F3Keys.openingSwitcher();
    }

    @Redirect(method = "extractRenderState", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/KeyMapping;getTranslatedKeyMessage()Lnet/minecraft/network/chat/Component;"))
    private Component f3keybinds$hintKey(KeyMapping mapping) {
        return f3keybinds$oneKey ? F3Keys.gameModeKey().getTranslatedKeyMessage() : mapping.getTranslatedKeyMessage();
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void f3keybinds$keyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (!ModGate.on()) {
            return;
        }
        if (F3Keys.gameModeKey().matches(event)) {
            Minecraft minecraft = Minecraft.getInstance();
            F3Keys.pressAs(minecraft, minecraft.options.keyDebugSwitchGameMode, this::keyPressed);
            cir.setReturnValue(true);
        } else if (event.key() == InputConstants.KEY_RETURN || event.key() == InputConstants.KEY_NUMPADENTER
                || event.key() == InputConstants.KEY_SPACE) {
            pick();
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void f3keybinds$mouseReleased(MouseButtonEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (ModGate.on() && event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            pick();
            cir.setReturnValue(true);
        }
    }

    private void pick() {
        switchToHoveredGameMode();
        onClose();
    }
}
