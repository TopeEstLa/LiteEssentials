package me.topeestla.essentials.api;

import com.zaxxer.hikari.HikariDataSource;
import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.entity.IUser;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IUserFactory {

    void initFactory(LiteEssentials essentials, HikariDataSource dataSource);

    IUser createUser(Player player);

    default CompletableFuture<IUser> createUserAsync(Player player) {
        return CompletableFuture.supplyAsync(() -> createUser(player));
    }

    void deleteUser(IUser iUser);

    default CompletableFuture<Void> deleteUserAsync(IUser iUser) {
        return CompletableFuture.runAsync(() -> deleteUser(iUser));
    }

    void updateUser(IUser iUser);

    default CompletableFuture<Void> updateUserAsync(IUser iUser) {
        return CompletableFuture.runAsync(() -> updateUser(iUser));
    }

    IUser getUser(Player player);

    default CompletableFuture<IUser> getUserAsync(Player player) {
        return CompletableFuture.supplyAsync(() -> getUser(player));
    }

    IUser getUser(String name);

    default CompletableFuture<IUser> getUserAsync(String name) {
        return CompletableFuture.supplyAsync(() -> getUser(name));
    }

    IUser getUser(UUID uuid);

    default CompletableFuture<IUser> getUserAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getUser(uuid));
    }
}
