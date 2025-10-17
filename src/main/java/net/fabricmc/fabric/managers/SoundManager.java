package net.fabricmc.fabric.managers;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import net.fabricmc.fabric.ClientMain;
import net.fabricmc.fabric.api.astral.EventHandler;
import net.fabricmc.fabric.api.astral.events.AttackEntityEvent;
import net.fabricmc.fabric.api.astral.events.ItemUseEvent;
import net.fabricmc.fabric.managers.ModuleManager;
import net.fabricmc.fabric.systems.module.impl.Client.Sounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;

public class SoundManager {
    private final Random random = new Random();

    public void playSound(String soundName, int times) {
        String path = "/assets/tulip/sounds/" + soundName + ".wav";
        try {
            InputStream audioSrc = this.getClass().getResourceAsStream(path);
            if (audioSrc == null) {
                // empty if block
            }
            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(times - 1);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    try {
                        clip.close();
                        audioStream.close();
                        bufferedIn.close();
                        audioSrc.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void randomPlay(String[] soundNames, int times) {
        String randomSound = soundNames[this.random.nextInt(soundNames.length)];
        this.playSound(randomSound, times);
    }

    @EventHandler
    public void onItemUse(ItemUseEvent.Pre event) {
        if (Sounds.onItemUse.isEnabled() && ModuleManager.INSTANCE.getModuleByClass(Sounds.class).isEnabled()) {
            if (ClientMain.mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
                this.playSound("double", 1);
            } else {
                this.playSound("click", 1);
            }
        }
    }

    @EventHandler
    public void attack(AttackEntityEvent event) {
        if (Sounds.onAttack.isEnabled() && ModuleManager.INSTANCE.getModuleByClass(Sounds.class).isEnabled()) {
            if (ClientMain.mc.player.getMainHandStack().getItem() instanceof AxeItem) {
                this.randomPlay(new String[]{"axe1", "axe2"}, 1);
            } else if (event.getTarget() instanceof PlayerEntity) {
                this.randomPlay(new String[]{"sword1", "sword2", "sword3", "sword4"}, 1);
            }
        }
    }
}
