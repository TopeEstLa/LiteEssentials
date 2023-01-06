package me.topeestla.essentials.api.entity;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.utils.SerializedLocation;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IUser {

    void sendMessage(LiteEssentials essentials, String path, String[][] format);

    void sendMessages(LiteEssentials essentials, String path, String[][] format);

    void sendMessage(LiteEssentials essentials, Component component);

    void sendMessages(LiteEssentials essentials, List<Component> components);

    void addHome(String name, Location location);

    void removeHome(String name);

    void addCooldown(String name, long duration);

    void addMoney(double amount);

    void removeMoney(double amount);

    void setMoney(double amount);

    void setLastJoin(Timestamp lastJoin);

    void setLastLocation(SerializedLocation serializedLocation);

    void setPlayer(Player player);

    Player getPlayer();

    String getName();

    UUID getUniqueId();

    String getIP();

    Timestamp getLastLogin();

    Timestamp getFirstLogin();

    SerializedLocation getLastLocation();

    Map<String, SerializedLocation> getHomes();

    Set<String> getHomesNames();

    Map<String, SerializedLocation> getHomesByNear(Location location);

    Location getHomeByName(String name);

    Map<String, Long> getCooldowns();

    boolean isInCooldown(String name);

    Long getCooldownTime(String name);

    String getFormattedCooldownTime(String name);

    double getMoney();
}
