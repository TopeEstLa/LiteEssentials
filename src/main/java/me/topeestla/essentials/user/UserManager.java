package me.topeestla.essentials.user;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.IUserCache;
import me.topeestla.essentials.api.IUserFactory;
import me.topeestla.essentials.api.entity.IUser;
import me.topeestla.essentials.api.enums.StorageType;
import me.topeestla.essentials.user.impl.cache.MemoryCache;
import me.topeestla.essentials.user.impl.factory.JsonFactory;
import me.topeestla.essentials.user.impl.factory.MariaDbFactory;
import me.topeestla.essentials.utils.SerializedLocation;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Timestamp;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

public class UserManager {

    private final LiteEssentials essentials;
    private IUserFactory userFactory;
    private IUserCache userCache;

    public UserManager(LiteEssentials essentials) {
        this.essentials = essentials;
    }

    public void init() {
        this.initUserFactory();
        this.initUserCache();
    }

    public void handleJoin(Player player) {
        try {
            IUser iUser = this.userFactory.getUserAsync(player).get();
            if (iUser != null) {
                this.essentials.getDebugLogger().log(Level.WARNING, "User " + iUser.getName() + " already exists loading in cache");
                this.userCache.loadUser(iUser);
            } else {
                this.essentials.getDebugLogger().log(Level.WARNING, "User " + player.getName() + " does not exist creating new user");
                iUser = this.userFactory.createUserAsync(player).get();
                this.userCache.loadUser(iUser);
            }
        } catch (InterruptedException | ExecutionException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to handleJoin", e);
        }
    }

    public void handleQuit(Player player) {
        try {
            if (this.userCache.containsUser(player.getName())) {
                this.essentials.getDebugLogger().log(Level.WARNING, "User " + player.getName() + " exists in cache saving");
                IUser iUser = this.userCache.getUser(player.getName());
                iUser.setLastJoin(new Timestamp(System.currentTimeMillis()));
                iUser.setLastLocation(new SerializedLocation(player.getLocation()));
                this.userFactory.updateUserAsync(iUser);
                this.userCache.unloadUser(iUser);
            } else {
                this.essentials.getDebugLogger().log(Level.WARNING, "User " + player.getName() + " does not exist in cache");
            }
        } catch (Exception e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to handleQuit", e);
        }
    }

    public IUser handleDataNeed(String name) {
        try {
            if (this.userCache.containsUser(name)) {
                this.essentials.getDebugLogger().log(Level.WARNING, "User " + name + " is in cache getting");
                return this.userCache.getUser(name);
            } else {
                this.essentials.getDebugLogger().log(Level.WARNING, "User " + name + " does not exist in cache searching into database");
                return this.userFactory.getUser(name);
            }
        } catch (Exception e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to handleDataNeed", e);
            return null;
        }
    }

    private void initUserFactory() {
        this.essentials.getDebugLogger().log(Level.WARNING, "Initializing user factory");

        StorageType storageType = StorageType.valueOf(this.essentials.getConfig().getString("storage.type"));
        this.essentials.getDebugLogger().log(Level.WARNING, "Storage type: " + storageType.name());

        if (storageType.isSql()) {
            this.essentials.getDebugLogger().log(Level.WARNING, "Storage is an Sql database, loading HikariCP");

            this.saveDatabaseProperties();
            HikariConfig hikariConfig = new HikariConfig(this.essentials.getDataFolder().getAbsolutePath() + "/database.properties");
            HikariDataSource dataSource = new HikariDataSource(hikariConfig);

            this.essentials.getDebugLogger().log(Level.WARNING, "Loaded HikariCP");

            switch (storageType) {
                case MARIADB:
                    this.essentials.getDebugLogger().log(Level.WARNING, "Storage is MariaDB, loading MariaDbFactory");

                    this.userFactory = new MariaDbFactory();
                    this.userFactory.initFactory(essentials, dataSource);
                    break;
                case H2:
                    break;
                case POSTGRESQL:
                    break;
            }
        } else {
            this.essentials.getDebugLogger().log(Level.WARNING, "Storage is not an Sql database, loading NoSql");

            switch (storageType) {
                case MONGODB:
                    break;
                case JSON:
                    this.essentials.getDebugLogger().log(Level.WARNING, "Storage is Json, loading JsonFactory");
                    this.userFactory = new JsonFactory();
                    this.userFactory.initFactory(essentials, null);
                    break;
            }
        }

        this.essentials.getDebugLogger().log(Level.WARNING, "Loaded user factory");
    }

    private void initUserCache() {
        this.essentials.getDebugLogger().log(Level.WARNING, "Initializing user cache");

        switch (this.essentials.getConfig().getString("cache.type")) {
            case "MEMORY":
                this.essentials.getDebugLogger().log(Level.WARNING, "Cache is Memory, loading MemoryCache");

                this.userCache = new MemoryCache();
                this.userCache.initCache(essentials);
                break;
            case "JSON":
                break;
        }

        this.essentials.getDebugLogger().log(Level.WARNING, "Loaded user cache");
    }

    private void saveDatabaseProperties() {
        if (!new File(this.essentials.getDataFolder().getAbsolutePath(), "database.properties").exists()) {
            this.essentials.getDebugLogger().log(Level.WARNING, "Database properties file does not exist, creating");
            this.essentials.saveResource("database.properties", false);
            this.essentials.getServer().shutdown();
        }
    }

    public IUserFactory getUserFactory() {
        return userFactory;
    }

    public IUserCache getUserCache() {
        return userCache;
    }
}
