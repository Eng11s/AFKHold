package dev.eng11s.mixin;

import dev.eng11s.client.KeybindHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    @Unique
    private static final int ACTION_PRESS = 1;
    @Unique
    private static final long DOUBLE_TAP_WINDOW_MS = 300;

    @Unique
    private long lastEscapePressTime = 0;

    @Inject(at = @At("HEAD"), method = "keyPress", cancellable = true)
    private void modifyKeyPress(long window, int action, KeyEvent keyEvent, CallbackInfo ci) {
        if (action != ACTION_PRESS) {
            return;
        }

        int key = keyEvent.key();

        if (KeybindHandler.INSTANCE.isRunning()) {
            for (KeyMapping keyMapping : KeybindHandler.INSTANCE.getEnabledKeys()) {
                if (keyMapping.matches(keyEvent)) {
                    ci.cancel();
                    return;
                }
            }
        }

        if (key == GLFW.GLFW_KEY_ESCAPE) {
            handleEscapePress(ci);
        }
    }

    @Unique
    private void handleEscapePress(CallbackInfo ci) {
        KeybindHandler handler = KeybindHandler.INSTANCE;
        Minecraft mc = Minecraft.getInstance();
        boolean hasScreen = mc.screen != null;

        if (handler.isRunning() && !hasScreen) {
            handler.disable();
            ci.cancel();
            return;
        }

        if (handler.isRunningIgnorePause()) {
            if (!hasScreen) {
                handler.disable();
                ci.cancel();
            } else {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastEscapePressTime <= DOUBLE_TAP_WINDOW_MS) {
                    handler.disable();
                    ci.cancel();
                }
                lastEscapePressTime = currentTime;
            }
        }
    }
}
