package dev.eng11s;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AFKHold implements ModInitializer {
	public static final String MOD_ID = "afkhold";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("AFKHold has been initialized!");
	}
}
