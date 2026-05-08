package dev.eng11s.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;

import java.nio.file.Path;

public class AFKHoldConfig {

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("afkhold.json");

    public static final ConfigClassHandler<AFKHoldConfig> HANDLER = ConfigClassHandler.createBuilder(AFKHoldConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_PATH)
                    .build())
            .build();

    @SerialEntry
    public boolean muteWhenAfk = false;
    @SerialEntry
    public boolean kickOnDamageWhenAfk = false;
    @SerialEntry
    public boolean kickOnTotemPopWhenAfk = false;

    public static AFKHoldConfig get() {
        return HANDLER.instance();
    }

    public static void load() {
        HANDLER.load();
    }

    public static void save() {
        HANDLER.save();
    }

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("afkhold.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("afkhold.config.category.general"))
                        .tooltip(Component.translatable("afkhold.config.category.general.tooltip"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("afkhold.config.group.settings"))
                                .description(OptionDescription.of(Component.translatable("afkhold.config.group.settings.description")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("afkhold.config.option.mute_when_afk"))
                                        .description(OptionDescription.of(Component.translatable("afkhold.config.option.mute_when_afk.description")))
                                        .binding(
                                                false,
                                                () -> get().muteWhenAfk,
                                                newVal -> get().muteWhenAfk = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("afkhold.config.option.kick_on_damage"))
                                        .description(OptionDescription.of(Component.translatable("afkhold.config.option.kick_on_damage.description")))
                                        .binding(
                                                false,
                                                () -> get().kickOnDamageWhenAfk,
                                                newVal -> get().kickOnDamageWhenAfk = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("afkhold.config.option.kick_on_totem"))
                                        .description(OptionDescription.of(Component.translatable("afkhold.config.option.kick_on_totem.description")))
                                        .binding(
                                                false,
                                                () -> get().kickOnTotemPopWhenAfk,
                                                newVal -> get().kickOnTotemPopWhenAfk = newVal
                                        )
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .save(AFKHoldConfig::save)
                .build()
                .generateScreen(parent);
    }
}
