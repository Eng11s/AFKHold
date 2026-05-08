package dev.eng11s.client;

import dev.eng11s.AFKHold;
import dev.eng11s.config.AFKHoldConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class AFKHoldClient implements ClientModInitializer {
    public static KeyMapping toggleAfkKey;
    public static KeyMapping unlockMouseKey;

    @Override
    public void onInitializeClient() {
        AFKHoldConfig.load();

        KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath(AFKHold.MOD_ID, "afkhold"));

        toggleAfkKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.afkhold.toggle",
            GLFW.GLFW_KEY_K,
            category
        ));

        unlockMouseKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.afkhold.mouse",
            GLFW.GLFW_KEY_LEFT_ALT,
            category
        ));
    }
}
