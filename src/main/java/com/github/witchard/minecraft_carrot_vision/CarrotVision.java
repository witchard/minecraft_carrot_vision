package com.github.witchard.minecraft_carrot_vision;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.event.block.Action;

import java.util.logging.Logger;
import java.util.concurrent.ThreadLocalRandom;

public final class CarrotVision extends JavaPlugin implements Listener {
    private Logger log;

    @Override
    public void onEnable() {
        // Plugin startup logic
        log = getLogger();

        // Register for events
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            var item = event.getItem();
            if (item != null && item.getType() == Material.CARROT) {
                var player = event.getPlayer();
                if (player.getPotionEffect(PotionEffectType.NIGHT_VISION) != null) {
                    // Player already has night vision so quit
                    return;
                }

                if (player.getFoodLevel() == 20) {
                    // Player is full, fake up eating carrot
                    event.setCancelled(true);
                    player.swingMainHand();
                    item.setAmount(item.getAmount() - 1);
                }
                var rand = ThreadLocalRandom.current().nextInt(100);
                var time = 0;
                if (rand < 75) {
                    time = 60;
                } else if (rand < 90) {
                    time = 120;
                } else if (rand < 95) {
                    time = 300;
                } else if (rand < 97) {
                    time = 600;
                } else if (rand < 98) {
                    time = 3600;
                } else {
                    return; // Unlucky, no night vision!
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * time, 0));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent("Night vision granted for " + String.valueOf(time) + "s!"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        var effect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
                        if (effect == null) {
                            cancel();
                        } else {
                            var seconds = effect.getDuration() / 20;
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    new TextComponent("Night vision active for " + String.valueOf(seconds) + "s!"));
                        }
                    }
                }.runTaskTimer(this, 10 * 20L, 10 * 20L);
            }
        }
    }
}
