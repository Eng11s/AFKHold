package dev.eng11s.client;

import dev.eng11s.config.AFKHoldConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import static net.minecraft.ChatFormatting.*;

public class KeybindHandler {
    public static final KeybindHandler INSTANCE = new KeybindHandler();
    private final SoundManager soundManager = new SoundManager();
    private boolean running = false;
    private boolean paused = false;
    private boolean wasPaused = false;
    private final Set<KeyMapping> enabledKeys = new LinkedHashSet<>();

    public boolean isRunning() {
        return running && !paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isRunningIgnorePause() {
        return running;
    }

    public boolean isWasPaused() {
        return wasPaused;
    }

    public void clearWasPaused() {
        wasPaused = false;
    }

    public Set<KeyMapping> getEnabledKeys() {
        return Collections.unmodifiableSet(enabledKeys);
    }

    public void enable(Set<KeyMapping> keys) {
        running = true;
        enabledKeys.addAll(keys);
        if (AFKHoldConfig.get().muteWhenAfk) {
            soundManager.mute();
        }
        Minecraft.getInstance().mouseHandler.releaseMouse();
    }

    public String[] getMessage() {
        if (Minecraft.getInstance().player == null) {
            return new String[0];
        }

        KeyMapping[] keys = enabledKeys.toArray(new KeyMapping[0]);
        StringBuilder str = new StringBuilder();
        str.append(WHITE).append("Holding down ");
        for (int i = 0; i < keys.length; i++) {
            String keyName = keys[i].getTranslatedKeyMessage().getString().toUpperCase();
            int size = keys.length;
            if (i < size - 2) {
                str.append(AQUA).append(keyName).append(WHITE).append(", ");
            } else if (i == size - 2) {
                str.append(AQUA).append(keyName);
            } else {
                if (size > 1) str.append(WHITE).append(" and ");
                str.append(AQUA).append(keyName);
            }
        }

        String[] msg = new String[4];
        msg[0] = str.toString();
        msg[1] = soundManager.isMuted()
                ? (WHITE + "Sound is " + GRAY + "muted" + WHITE)
                : (WHITE + "Sound is " + GREEN + "not muted" + WHITE);
        msg[2] = "";
        msg[3] = WHITE + "Press " + RED + "ESCAPE" + WHITE + " to exit";
        return msg;
    }

    public void disable() {
        enabledKeys.forEach(key -> key.setDown(false));
        enabledKeys.clear();
        if (AFKHoldConfig.get().muteWhenAfk) {
            soundManager.unmute();
        }
        running = false;
        unpause();
        wasPaused = false;
        if (Minecraft.getInstance().screen == null) Minecraft.getInstance().mouseHandler.grabMouse();
    }

    public void pause() {
        paused = true;
        wasPaused = true;
    }

    public void unpause() {
        paused = false;
    }
}
