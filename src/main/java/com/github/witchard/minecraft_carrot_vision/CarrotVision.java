package com.github.witchard.minecraft_carrot_vision;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.nio.file.Paths;
import java.util.logging.Logger;

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
    public void onEat(PlayerItemConsumeEvent event) {
        var item = event.getItem();
        if (item.getType() == Material.CARROT) {
            var player = event.getPlayer();
            log.info(player.getDisplayName() + " guzzled up a carrot");
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 60, 0));
        }
    }
}
