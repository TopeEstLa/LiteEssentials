package me.topeestla.essentials.user.impl.cache;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.IUserCache;
import me.topeestla.essentials.api.entity.IUser;
import me.topeestla.essentials.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryCache implements IUserCache {

    private LiteEssentials essentials;
    private List<IUser> users;


    @Override
    public void initCache(LiteEssentials essentials) {
        this.essentials = essentials;
        this.users = new ArrayList<>();
    }

    @Override
    public boolean loadUser(IUser user) {
        if (this.users.contains(user)) {
            return true;
        }

        this.users.add((User) user);
        return false;
    }

    @Override
    public boolean unloadUser(IUser user) {
        if (this.users.contains(user)) {
            this.users.remove(user);
            return true;
        }

        return false;
    }



    @Override
    public IUser getUser(String name) {
        for (IUser user : this.users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public IUser getUser(UUID uuid) {
        for (IUser user : this.users) {
            if (user.getUniqueId().equals(uuid)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public boolean containsUser(IUser user) {
        return this.users.contains(user);
    }

    @Override
    public boolean containsUser(String name) {
        for (IUser user : this.users) {
            if (user.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean containsUser(UUID uuid) {
        for (IUser user : this.users) {
            if (user.getUniqueId().equals(uuid)) {
                return true;
            }
        }

        return false;
    }
}
