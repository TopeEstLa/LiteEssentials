package me.topeestla.essentials.user.listener;

import me.topeestla.essentials.LiteEssentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {

    private final LiteEssentials essentials;

    public UserListener(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.essentials.getUserManager().handleJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.essentials.getUserManager().handleQuit(event.getPlayer());
    }
}
