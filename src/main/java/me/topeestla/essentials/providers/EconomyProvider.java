package me.topeestla.essentials.providers;

import me.topeestla.essentials.LiteEssentials;
import me.topeestla.essentials.api.entity.IUser;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class EconomyProvider extends AbstractEconomy {

    private final LiteEssentials essentials;

    public EconomyProvider(LiteEssentials essentials) {
        this.essentials = essentials;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "LiteEssentials";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return currencyNameSingular();
    }

    @Override
    public String currencyNameSingular() {
        return this.essentials.getLocaleManager().getLocaleConfig().getString("ECONOMY.CURRENCY", false, null);
    }

    @Override
    public boolean hasAccount(String playerName) {
        return this.essentials.getUserManager().handleDataNeed(playerName) != null;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return this.hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName) {
        return this.essentials.getUserManager().handleDataNeed(playerName).getMoney();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return this.getBalance(playerName);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return this.getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return this.has(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        IUser iUser = this.essentials.getUserManager().handleDataNeed(playerName);
        if (iUser == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        double money = iUser.getMoney();
        if (money >= amount) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Not enough money");
        }

        iUser.removeMoney(amount);

        return new EconomyResponse(amount, money, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return this.withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        IUser iUser = this.essentials.getUserManager().handleDataNeed(playerName);
        if (iUser == null) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "User not found");
        }

        iUser.addMoney(amount);

        return new EconomyResponse(amount, iUser.getMoney(), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return this.depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Banks are not supported");
    }

    @Override
    public List<String> getBanks() {
        return Arrays.asList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    private double getDoubleValue(final BigDecimal value) {
        double amount = value.doubleValue();
        if (new BigDecimal(amount).compareTo(value) > 0) {
            amount = Math.nextAfter(amount, Double.NEGATIVE_INFINITY);
        }
        return amount;
    }
}
