package me.topeestla.essentials.user.impl.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.IUserFactory;
import me.topeestla.essentials.api.entity.IUser;
import me.topeestla.essentials.user.entity.User;
import me.topeestla.essentials.utils.SerializedLocation;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class MariaDbFactory implements IUserFactory {

    private LiteEssentials essentials;
    private HikariDataSource dataSource;

    @Override
    public void initFactory(LiteEssentials essentials, HikariDataSource dataSource) {
        this.essentials = essentials;
        this.dataSource = dataSource;


        try (Connection connection = this.dataSource.getConnection()) {
            this.essentials.getDebugLogger().log(Level.WARNING, "Loading database schema");

            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `users`(\n" +
                    "    `id` CHAR(36) NOT NULL PRIMARY KEY,\n" +
                    "    `name` VARCHAR(255) NOT NULL,\n" +
                    "    `last_login` TIMESTAMP NOT NULL,\n" +
                    "    `first_login` TIMESTAMP NOT NULL,\n" +
                    "    `last_location` JSON NOT NULL,\n" +
                    "    `money` BIGINT NOT NULL,\n" +
                    "    `homes` JSON,\n" +
                    "    `cooldown` JSON\n" +
                    ");").executeUpdate();
        } catch (SQLException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to initialize user factory", e);
            this.essentials.getServer().getPluginManager().disablePlugin(this.essentials);
        }
    }

    @Override
    public IUser createUser(Player player) {
        try (Connection connection = this.dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `users` (`id`, `name`, `last_login`, `first_login`, `last_location`, `money`) VALUES (?, ?, ?, ?, ?, ?)");
            ObjectMapper objectMapper = new ObjectMapper();

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(5, objectMapper.writeValueAsString(new SerializedLocation(player.getLocation())));
            preparedStatement.setDouble(6, 0);
            preparedStatement.executeUpdate();


            return new User(player);
        } catch (SQLException | JsonProcessingException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to create user", e);
        }

        return null;
    }

    @Override
    public void deleteUser(IUser iUser) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `users` WHERE `id` = ?");
            preparedStatement.setString(1, iUser.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to delete user", e);
        }
    }

    @Override
    public void updateUser(IUser iUser) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `users` SET `last_login` = ?, `last_location` = ?, `money` = ?, `homes` = ?, `cooldown` = ? WHERE `id` = ?");
            ObjectMapper objectMapper = new ObjectMapper();

            preparedStatement.setTimestamp(1, iUser.getLastLogin());
            preparedStatement.setString(2, objectMapper.writeValueAsString(iUser.getLastLocation()));
            preparedStatement.setDouble(3, iUser.getMoney());


            preparedStatement.setString(4, objectMapper.writeValueAsString(iUser.getHomes()));
            preparedStatement.setString(5, objectMapper.writeValueAsString(iUser.getCooldowns()));

            preparedStatement.setString(6, iUser.getUniqueId().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException | JsonProcessingException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to update user", e);
        }
    }

    @Override
    public IUser getUser(Player player) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `users` WHERE `id` = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ObjectMapper objectMapper = new ObjectMapper();
                TypeReference<Map<String, SerializedLocation>> homeType = new TypeReference<>() {};
                TypeReference<Map<String, Long>> cooldownType = new TypeReference<>() {};

                Map<String, SerializedLocation> homes = objectMapper.readValue(resultSet.getString("homes"), homeType);
                Map<String, Long> cooldowns = objectMapper.readValue(resultSet.getString("cooldown"), cooldownType);

                return new User(player, resultSet.getTimestamp("first_login"), objectMapper.readValue(resultSet.getString("last_location"), SerializedLocation.class), homes, cooldowns, resultSet.getDouble("money"));
            }
        } catch (SQLException | JsonProcessingException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to get user", e);
        }

        return null;
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
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `users` WHERE `id` = ?");
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                ObjectMapper objectMapper = new ObjectMapper();
                TypeReference<Map<String, SerializedLocation>> homeType = new TypeReference<>() {};
                TypeReference<Map<String, Long>> cooldownType = new TypeReference<>() {};

                Map<String, SerializedLocation> homes = objectMapper.readValue(resultSet.getString("homes"), homeType);
                Map<String, Long> cooldowns = objectMapper.readValue(resultSet.getString("cooldown"), cooldownType);

                return new User(resultSet.getString("name"), uuid, resultSet.getString("ip"),resultSet.getTimestamp("first_login"), objectMapper.readValue(resultSet.getString("last_location"), SerializedLocation.class), homes, cooldowns, resultSet.getLong("money"));
            }
        } catch (SQLException | JsonProcessingException e) {
            this.essentials.getLogger().log(Level.WARNING, "Failed to get user", e);
        }

        return null;
    }
}
