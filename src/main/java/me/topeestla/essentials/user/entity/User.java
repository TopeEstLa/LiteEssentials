package me.topeestla.essentials.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.entity.IUser;
import me.topeestla.essentials.utils.SerializedLocation;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.*;

public class User implements IUser {

    @JsonIgnore
    private Player player;

    private String name;
    private UUID uniqueId;
    private String ip;

    private Timestamp lastLogin;
    private Timestamp firstLogin;

    private SerializedLocation lastLocation;

    private Map<String, SerializedLocation> homes;
    private Map<String, Long> cooldowns;

    private double money;

    public User() {
    }

    public User(Player player) {
        this.player = player;

        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
        this.ip = player.getAddress().getAddress().getHostAddress();

        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.firstLogin = new Timestamp(System.currentTimeMillis());

        this.lastLocation = new SerializedLocation(player.getLocation());

        this.homes = new HashMap<>();
        this.cooldowns = new HashMap<>();

        this.money = 0;
    }

    public User(Player player, Timestamp firstLogin, SerializedLocation lastLocation, Map<String, SerializedLocation> homes, Map<String, Long> cooldowns, double money) {
        this.player = player;

        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
        this.ip = player.getAddress().getAddress().getHostAddress();

        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.firstLogin = firstLogin;

        this.lastLocation = lastLocation;

        this.homes = homes;
        this.cooldowns = cooldowns;

        this.money = money;
    }

    public User(String name, UUID uniqueId, String ip, Timestamp firstLogin, SerializedLocation lastLocation, Map<String, SerializedLocation> homes, Map<String, Long> cooldowns, double money) {
        this.player = null;
        this.name = name;
        this.uniqueId = uniqueId;
        this.ip = ip;
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.firstLogin = firstLogin;
        this.lastLocation = lastLocation;
        this.homes = homes;
        this.cooldowns = cooldowns;
        this.money = money;
    }

    @Override
    public void addHome(String name, Location location) {
        this.homes.put(name, new SerializedLocation(location));
    }

    @Override
    public void removeHome(String name) {
        this.homes.remove(name);
    }

    @Override
    public void addCooldown(String name, long duration) {
        this.cooldowns.put(name, (duration * 1000) + System.currentTimeMillis());
    }

    @Override
    public void addMoney(double amount) {
        this.money += amount;
    }

    @Override
    public void removeMoney(double amount) {
        this.money -= amount;
    }

    @Override
    public void setMoney(double amount) {
        this.money = amount;
    }

    @Override
    public void setLastJoin(Timestamp lastJoin) {
        this.lastLogin = lastJoin;
    }

    @Override
    public void setLastLocation(SerializedLocation serializedLocation) {
        this.lastLocation = serializedLocation;
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @JsonIgnore
    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getIP() {
        return this.ip;
    }

    @Override
    public Timestamp getLastLogin() {
        return this.lastLogin;
    }

    @Override
    public Timestamp getFirstLogin() {
        return this.firstLogin;
    }

    @Override
    public SerializedLocation getLastLocation() {
        return this.lastLocation;
    }

    @Override
    public Map<String, SerializedLocation> getHomes() {
        return this.homes;
    }

    @Override
    public Set<String> getHomesNames() {
        return this.homes.keySet();
    }

    @Override
    public Map<String, SerializedLocation> getHomesByNear(Location location) {
        return null;
    }

    @Override
    public Location getHomeByName(String name) {
        return null;
    }

    @Override
    public Map<String, Long> getCooldowns() {
        return this.cooldowns;
    }

    @Override
    public boolean isInCooldown(String name) {
        if (this.cooldowns.containsKey(name)) {
            return this.cooldowns.get(name) > System.currentTimeMillis();
        }

        return false;
    }

    @Override
    public Long getCooldownTime(String name) {
        if (this.cooldowns.containsKey(name)) {
            return this.cooldowns.get(name) - System.currentTimeMillis() / 1000;
        }

        return null;
    }

    @Override
    public String getFormattedCooldownTime(String name) {
        return DurationFormatUtils.formatDuration(getCooldownTime(name) * 1000, "H'h 'm'm 's's'");
    }

    @Override
    public double getMoney() {
        return this.money;
    }

    @Override
    public void sendMessage(LiteEssentials essentials, String path, String[][] format) {
        this.sendMessage(essentials, essentials.getLocaleManager().getLocaleConfig().getChatComponent(path, true, format));
    }

    @Override
    public void sendMessages(LiteEssentials essentials, String path, String[][] format) {
        for (Component component : essentials.getLocaleManager().getLocaleConfig().getChatComponentList(path, true, format)) {
            this.sendMessage(essentials, component);
        }
    }

    @Override
    public void sendMessage(LiteEssentials essentials, Component component) {
        essentials.getAudiences().player(this.player).sendMessage(component);
    }

    @Override
    public void sendMessages(LiteEssentials essentials, List<Component> components) {
        for (Component component : components) {
            sendMessage(essentials, component);
        }
    }
}
