package dev.eng11s.client;

import dev.eng11s.config.AFKHoldConfig;
import dev.eng11s.mixin.KeyBindingAccessor;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class AutoclickerManager {
    public static final AutoclickerManager INSTANCE = new AutoclickerManager();

    private long lastClickTime = 0;

    public boolean shouldClick() {
        long now = System.currentTimeMillis();
        int delay = AFKHoldConfig.get().autoclickerDelay;
        if (now - lastClickTime >= delay) {
            lastClickTime = now;
            return true;
        }
        return false;
    }

    public boolean isAutoclicking() {
        if (!AFKHoldConfig.get().autoclickerMode) return false;
        return KeybindHandler.INSTANCE.getEnabledKeys().stream().anyMatch(AutoclickerManager::isMouseButton);
    }

    public static boolean isMouseButton(KeyMapping key) {
        return ((KeyBindingAccessor) key).getKey().getType() == InputConstants.Type.MOUSE;
    }

    public void reset() {
        lastClickTime = 0;
    }
}
