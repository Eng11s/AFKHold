package dev.eng11s.client;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;

public class SoundManager {
    private boolean muted = false;
    private double oldVolume = 0.0;

    public void mute() {
        if (muted) return;

        Minecraft mc = Minecraft.getInstance();
        oldVolume = (double) mc.options.getSoundSourceVolume(SoundSource.MASTER);
        mc.options.getSoundSourceOptionInstance(SoundSource.MASTER).set(0.0);
        muted = true;
    }

    public void unmute() {
        if (!muted) return;

        Minecraft.getInstance().options.getSoundSourceOptionInstance(SoundSource.MASTER).set(oldVolume);
        muted = false;
    }

    public int getOldVolumePercentage() {
        return (int) Math.round(oldVolume * 100);
    }

    public boolean isMuted() {
        return muted;
    }
}