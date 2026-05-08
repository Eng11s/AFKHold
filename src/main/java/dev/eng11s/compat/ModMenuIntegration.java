package dev.eng11s.compat;

import dev.eng11s.config.AFKHoldConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return AFKHoldConfig::createScreen;
    }
}
