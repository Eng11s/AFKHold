package dev.eng11s.mixin;

import dev.eng11s.client.AFKHoldClient;
import dev.eng11s.client.AutoclickerManager;
import dev.eng11s.client.KeybindHandler;
import dev.eng11s.config.AFKHoldConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Shadow
    public Screen screen;

    @Shadow
    public LocalPlayer player;

    @Shadow @Final
    public Options options;

    @Shadow @Final
    public MouseHandler mouseHandler;

    @Unique
    private boolean savedPauseOnLostFocus = true;
    @Unique
    private boolean hasSavedPauseState = false;

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void modifyTick(CallbackInfo info) {
        if (!KeybindHandler.INSTANCE.isRunningIgnorePause()) {
            if (hasSavedPauseState) {
                options.pauseOnLostFocus = savedPauseOnLostFocus;
                hasSavedPauseState = false;
            }
            return;
        }

        if (player == null || !player.isAlive()) {
            KeybindHandler.INSTANCE.disable();
            return;
        }

        if (screen == null) {
            if (AFKHoldClient.unlockMouseKey.isDown()) {
                mouseHandler.grabMouse();
            } else {
                mouseHandler.releaseMouse();
            }
            if (KeybindHandler.INSTANCE.isPaused()) {
                KeybindHandler.INSTANCE.unpause();
            }
        } else {
            if (screen instanceof PauseScreen) {
                if (!hasSavedPauseState) {
                    savedPauseOnLostFocus = options.pauseOnLostFocus;
                    hasSavedPauseState = true;
                }
                options.pauseOnLostFocus = false;
                Minecraft.getInstance().setScreen(null);
                Minecraft.getInstance().mouseHandler.grabMouse();
            }
            if (!KeybindHandler.INSTANCE.isPaused()) {
                KeybindHandler.INSTANCE.pause();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "pauseGame(Z)V", cancellable = true)
    private void modifyOpenPauseMenu(CallbackInfo info) {
        if (KeybindHandler.INSTANCE.isRunning()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "handleKeybinds()V")
    private void modifyHandleInputEvents(CallbackInfo info) {
        if (AFKHoldClient.toggleAfkKey.consumeClick()) {
            Set<KeyMapping> pressedKeybinds = new HashSet<>();
            for (KeyMapping keyMapping : options.keyMappings) {
                if (!keyMapping.isDown()) continue;
                if (keyMapping != AFKHoldClient.toggleAfkKey) {
                    pressedKeybinds.add(keyMapping);
                }
            }
            if (!pressedKeybinds.isEmpty()) {
                KeybindHandler.INSTANCE.enable(pressedKeybinds);
            }
        }

        if (KeybindHandler.INSTANCE.isWasPaused()) {
            KeybindHandler.INSTANCE.getEnabledKeys().forEach(key -> KeyMapping.click(((KeyBindingAccessor) key).getKey()));
            KeybindHandler.INSTANCE.clearWasPaused();
        } else {
            boolean autoclicker = AFKHoldConfig.get().autoclickerMode;
            boolean doClick = autoclicker && AutoclickerManager.INSTANCE.shouldClick();
            for (KeyMapping key : KeybindHandler.INSTANCE.getEnabledKeys()) {
                if (autoclicker && AutoclickerManager.isMouseButton(key)) {
                    if (doClick) {
                        key.setDown(true);
                        KeyMapping.click(((KeyBindingAccessor) key).getKey());
                    } else {
                        key.setDown(false);
                    }
                } else {
                    key.setDown(true);
                }
            }
        }
    }
}
