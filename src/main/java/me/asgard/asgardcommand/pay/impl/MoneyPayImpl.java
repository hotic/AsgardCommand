package me.asgard.asgardcommand.pay.impl;

import me.asgard.asgardcommand.pay.Pay;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class MoneyPayImpl implements Pay {

    private static Economy econ = null;

    public MoneyPayImpl() {
        if (setupEconomy()) System.out.println("经济开启成功"); else System.out.println("经济开启失败");
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void takeBalance(String playerName, Double money) {
        if (econ != null) econ.withdrawPlayer(playerName, money);
    }

    @Override
    public void addBalance(String playerName, Double money) {
        if (econ != null) econ.depositPlayer(playerName, money);
    }

    @Override
    public Double getBalance(String playerName) {
        if (econ != null) return econ.getBalance(playerName);
        return 0D;
    }
}
