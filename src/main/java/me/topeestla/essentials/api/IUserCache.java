package me.topeestla.essentials.api;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.entity.IUser;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IUserCache {

    void initCache(LiteEssentials essentials);

    boolean loadUser(IUser user);

    default CompletableFuture<Boolean> loadUserAsync(IUser user) {
        return CompletableFuture.supplyAsync(() -> loadUser(user));
    }


    boolean unloadUser(IUser user);

    default CompletableFuture<Boolean> unloadUserAsync(IUser user) {
        return CompletableFuture.supplyAsync(() -> unloadUser(user));
    }

    IUser getUser(String name);

    default CompletableFuture<IUser> getUserAsync(String name) {
        return CompletableFuture.supplyAsync(() -> getUser(name));
    }

    IUser getUser(UUID uuid);

    default CompletableFuture<IUser> getUserAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getUser(uuid));
    }

    boolean containsUser(IUser user);

    default CompletableFuture<Boolean> containsUserAsync(IUser user) {
        return CompletableFuture.supplyAsync(() -> containsUser(user));
    }

    boolean containsUser(String name);

    default CompletableFuture<Boolean> containsUserAsync(String name) {
        return CompletableFuture.supplyAsync(() -> containsUser(name));
    }

    boolean containsUser(UUID uuid);

    default CompletableFuture<Boolean> containsUserAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> containsUser(uuid));
    }
}
