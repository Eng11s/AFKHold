package dev.eng11s.client;

import dev.eng11s.AFKHold;
import dev.eng11s.config.AFKHoldConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;

public class AFKHoldClient implements ClientModInitializer {
    public static KeyMapping toggleAfkKey;
    public static KeyMapping unlockMouseKey;

    @Override
    public void onInitializeClient() {
        AFKHoldConfig.load();

        KeyMapping.Category category = new KeyMapping.Category(Identifier.fromNamespaceAndPath(AFKHold.MOD_ID, "afkhold"));

        toggleAfkKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.afkhold.toggle",
            InputConstants.Type.KEYBOARD,
            InputConstants.getKey("key.keyboard.k").getValue(),
            category
        ));

        unlockMouseKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.afkhold.mouse",
            InputConstants.Type.KEYBOARD,
            InputConstants.getKey("key.keyboard.left.alt").getValue(),
            category
        ));
    }
}
