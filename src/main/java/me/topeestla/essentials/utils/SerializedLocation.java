package me.topeestla.essentials.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Serializable;

public class SerializedLocation implements Serializable {

    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public SerializedLocation() {
    }

    public SerializedLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public String getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    @JsonIgnore
    public Location getLocation() {
        return new Location(
                Bukkit.getWorld(world),
                x,
                y,
                z,
                yaw,
                pitch
        );
    }
}
