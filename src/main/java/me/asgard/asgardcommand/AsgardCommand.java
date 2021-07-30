package me.asgard.asgardcommand;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.asgard.asgardcommand.listener.BaseListener;
import me.asgard.asgardcommand.listener.CommandListener;
import me.asgard.asgardcommand.pay.Pay;
import me.asgard.asgardcommand.pay.PayType;
import me.asgard.asgardcommand.pay.impl.MoneyPayImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public final class AsgardCommand extends JavaPlugin {

    private static AsgardCommand instance;

    private Map<String, Pay> payMap = Maps.newHashMap();
    private List<BaseListener> baseListenerList = Lists.newArrayList();

    public static AsgardCommand getInstance() {
        return instance;
    }

    public Map<String, Pay> getPayMap() {
        return payMap;
    }

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();

    }

    @Override
    public void onEnable() {
        payMap.put(PayType.MONEY.getName(), new MoneyPayImpl());
        baseListenerList.add(new CommandListener());
        baseListenerList.forEach(BaseListener::registerEvent);
    }

    @Override
    public void onDisable() {
        baseListenerList.clear();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void reload() {
        reloadConfig();
        baseListenerList.forEach(BaseListener::reloadConfig);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName();
        if (!sender.isOp()) return false;
        if (cmd.equalsIgnoreCase("ac")) {
            if (args.length == 0) {
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    reload();
                    sender.sendMessage("§7§l[§f§lAsgard§7§l]§7 AsgardCommand 重载成功");
                }
                return true;
            }
        }
        return false;
    }
}
