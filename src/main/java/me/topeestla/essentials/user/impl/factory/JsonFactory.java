package me.topeestla.essentials.user.impl.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.IUserFactory;
import me.topeestla.essentials.api.entity.IUser;
import me.topeestla.essentials.user.entity.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.logging.Level;

public class JsonFactory implements IUserFactory {

    private LiteEssentials essentials;
    private ObjectMapper objectMapper;
    private File userDataFolder;

    @Override
    public void initFactory(LiteEssentials essentials, HikariDataSource dataSource) {
        this.essentials = essentials;
        this.objectMapper = new ObjectMapper();
        this.userDataFolder = new File(essentials.getDataFolder(), "users");

        if (!this.userDataFolder.exists()) {
            this.userDataFolder.mkdirs();
        }
    }

    @Override
    public IUser createUser(Player player) {
        User user = new User(player);

        File userFile = new File(this.userDataFolder, player.getUniqueId().toString() + ".json");

        try {
            objectMapper.writeValue(userFile, user);
        } catch (IOException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to create user", e);
        }

        return user;
    }

    @Override
    public void deleteUser(IUser iUser) {
        File userFile = new File(this.userDataFolder, iUser.getUniqueId().toString() + ".json");
        userFile.delete();
    }

    @Override
    public void updateUser(IUser iUser) {
        File userFile = new File(this.userDataFolder, iUser.getUniqueId().toString() + ".json");
        try {
            objectMapper.writeValue(userFile, iUser);
        } catch (IOException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to update user", e);
        }
    }

    @Override
    public IUser getUser(Player player) {
        try {
            if (!new File(this.userDataFolder, player.getUniqueId().toString() + ".json").exists()) {
                return null;
            }

            IUser user = objectMapper.readValue(new File(this.userDataFolder, player.getUniqueId().toString() + ".json"), User.class);
            user.setPlayer(player);
            user.setLastJoin(new Timestamp(System.currentTimeMillis()));
            return user;
        } catch (IOException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to get user", e);
            return null;
        }
    }

    @Override
    public IUser getUser(String name) {
        OfflinePlayer offlinePlayer = this.essentials.getServer().getOfflinePlayer(name);

        if (offlinePlayer == null) {
            return null;
        }

        return getUser(offlinePlayer.getUniqueId());
    }

    @Override
    public IUser getUser(UUID uuid) {
        try {
            if (!new File(this.userDataFolder, uuid.toString() + ".json").exists()) {
                return null;
            }
            IUser user = objectMapper.readValue(new File(this.userDataFolder, uuid.toString() + ".json"), User.class);
            user.setLastJoin(new Timestamp(System.currentTimeMillis()));
            return user;
        } catch (IOException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to get user", e);
            return null;
        }
    }
}
