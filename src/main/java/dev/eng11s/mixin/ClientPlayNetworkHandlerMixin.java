package dev.eng11s.mixin;

import dev.eng11s.client.KeybindHandler;
import dev.eng11s.config.AFKHoldConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {

    private static final byte TOTEM_ACTIVATION_STATUS = 35;

    @Unique
    private float lastServerHealth = Float.MAX_VALUE;

    @Inject(method = "handleSetHealth", at = @At("HEAD"))
    private void onSetHealth(ClientboundSetHealthPacket packet, CallbackInfo ci) {
        float newHealth = packet.getHealth();

        if (lastServerHealth == Float.MAX_VALUE) {
            lastServerHealth = newHealth;
            return;
        }

        if (newHealth < lastServerHealth) {
            if (KeybindHandler.INSTANCE.isRunning() && AFKHoldConfig.get().kickOnDamageWhenAfk) {
                Minecraft client = Minecraft.getInstance();
                if (client.getConnection() != null) {
                    client.getConnection().getConnection().disconnect(
                            Component.literal("§cYou took damage and were disconnected!")
                    );
                }
            }
        }

        lastServerHealth = newHealth;
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void onEntityStatus(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        if (packet.getEventId() == TOTEM_ACTIVATION_STATUS) {
            if (KeybindHandler.INSTANCE.isRunning() && AFKHoldConfig.get().kickOnTotemPopWhenAfk) {
                Minecraft client = Minecraft.getInstance();
                if (client.getConnection() != null) {
                    client.getConnection().getConnection().disconnect(
                            Component.literal("§cYou popped a totem and were disconnected!")
                    );
                }
            }
        }
    }
}
