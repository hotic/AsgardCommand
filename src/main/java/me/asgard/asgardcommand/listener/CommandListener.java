package me.asgard.asgardcommand.listener;

import com.google.common.collect.Lists;
import me.asgard.asgardcommand.AsgardCommand;
import me.asgard.asgardcommand.data.Data;
import me.asgard.asgardcommand.entity.*;
import me.asgard.asgardcommand.pay.Pay;
import me.asgard.asgardcommand.pay.PayType;
import me.asgard.asgardcommand.util.Util;
import me.asgard.sacreditem.item.SacredItemManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandListener implements BaseListener {
    private List<Command> commandList = Lists.newArrayList();

    private Pay pay;

    public CommandListener() {
        loadConfig();
        pay = AsgardCommand.getInstance().getPayMap().get(PayType.MONEY.name());
    }

    @Override
    public void loadConfig() {
        FileConfiguration configuration = AsgardCommand.getInstance().getConfig();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("commands");
        commandList = configurationSection
                .getKeys(false)
                .stream()
                .map(s -> {
                    Command command = Command
                            .builder()
                            .key(s)
                            .usePermission(UsePer
                                    .builder()
                                    .per(configurationSection.getString(s + ".use_per.per"))
                                    .message(configurationSection.getString(s + ".use_per.message"))
                                    .build()
                            )
                            .useMoney(UseMoney
                                    .builder()
                                    .consume(configurationSection.getBoolean(s + ".use_money.consume"))
                                    .value(configurationSection.getDouble(s + ".use_money.value"))
                                    .message(configurationSection.getString(s + ".use_money.message"))
                                    .build()
                            )
                            .useItems(Bukkit.getPluginManager().getPlugin("SacredItem") != null ? UseItem
                                    .builder()
                                    .consume(configurationSection.getBoolean(s + ".use_item.consume"))
                                    .message(configurationSection.getString(s + ".use_item.message"))
                                    .items(configurationSection.getStringList(s + ".use_item.item").stream().map(s1 -> Item.builder().itemStack(SacredItemManager.getInstance().getItem(s1.split(":")[0])).number(Integer.valueOf(s1.split(":")[1])).build()).collect(Collectors.toList()))
                                    .build() : null)
                            .useLevel(UseLevel
                                    .builder()
                                    .message(configurationSection.getString(s + ".use_level.message"))
                                    .consume(configurationSection.getBoolean(s + ".use_level.consume"))
                                    .level(configurationSection.getInt(s + ".use_level.value"))
                                    .build()
                            )
                            .cooldown(configurationSection.getInt(s + ".cooldown"))
                            .chance(configurationSection.getInt(s + ".chance"))
                            .sourceCommand(configurationSection.getString(s + ".source_command"))
                            .noArg(configurationSection.getBoolean(s + ".no_arg"))
                            .noMessages(configurationSection.getStringList(s + ".no_messages"))
                            .deCommandParam(configurationSection.getStringList(s + ".decommandp"))
                            .deCommands(configurationSection.getStringList(s + ".decommands"))
                            .commands(configurationSection.getStringList(s + ".commands"))
                            .build();
                            if (command.getChance() == 0) command.setChance(100);
                            return command;
                        }

                )
                .collect(Collectors.toList());
    }

    @Override
    public void reloadConfig() {
        loadConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess (PlayerCommandPreprocessEvent e) {
        String message = e.getMessage().substring(1);
        Optional<Command> first = commandList.stream().filter(command -> (message.length() >= command.getSourceCommand().length()) && Util.convertStr(message.split(" "), 0, command.getSourceCommand().split(" ").length).equalsIgnoreCase(command.getSourceCommand())).findFirst();
        first.ifPresent(command -> {
            e.setCancelled(true);
            Player p = e.getPlayer();
            List <String> result = isTrigger(command, p);
            if (result.isEmpty()) {
                if (isCooldown(p, command.getKey(), command.getCooldown() * 1000, command.getNoMessages().stream().noneMatch("cooldown"::equals))) return;
                if (Objects.nonNull(command.getUseMoney()) && command.getUseMoney().getConsume()) pay.takeBalance(p.getName(), command.getUseMoney().getValue());
                if (Objects.nonNull(command.getUseItems()) && command.getUseItems().getConsume()) command.getUseItems().getItems().forEach(item -> itemTake(p, item.getItemStack(), item.getNumber()));
                if (Objects.nonNull(command.getUseLevel()) && command.getUseLevel().getConsume()) p.setLevel(p.getLevel() - command.getUseLevel().getLevel());
                exeCmds(command.getCommands(), p, command.getNoArg() ? null : Util.convertStr(message.substring(command.getSourceCommand().length()).trim().split(" "), 0));
            } else {
                AtomicReference<String> str = new AtomicReference<>("");
                result.stream().forEachOrdered(s -> {
                    if (StringUtils.isBlank(str.get())) {
                        if (command.getNoMessages().stream().noneMatch(s1 -> s1.equals(s))) {
                            switch (s) {
                                case "use_per":
                                    str.set(command.getUsePermission().getMessage());
                                    break;
                                case "use_money":
                                    str.set(command.getUseMoney().getMessage());
                                    break;
                                case "use_item":
                                    str.set(command
                                            .getUseItems()
                                            .getMessage()
                                            .replace(
                                                    "{items}",
                                                    " §f§l● " + StringUtils
                                                            .strip(command
                                                                            .getUseItems()
                                                                            .getItems()
                                                                            .stream()
//                                                                        .filter(item -> Stream
//                                                                                .of(p.getInventory().getContents())
//                                                                                .anyMatch(itemStack -> Objects.nonNull(itemStack) && itemStack.getItemMeta().equals(item.getItemStack().getItemMeta())))
                                                                            .map(item -> (
                                                                                    StringUtils
                                                                                            .isNotBlank(
                                                                                                    item.getItemStack().getItemMeta().getDisplayName())
                                                                                            ?
                                                                                            (item.getItemStack().getItemMeta().getDisplayName() + "§r §7§l(§f" + ((item.getNumber() - getItemStackNumByPlayerInventory(p, item.getItemStack())) <= 0 ? "已拥有§7§l)§r" : (item.getNumber() - getItemStackNumByPlayerInventory(p, item.getItemStack())) + "个§7§l)§r"))
                                                                                            :
                                                                                            (item.getItemStack().getType().name() + "§r §7§l(§f" + ((item.getNumber() - getItemStackNumByPlayerInventory(p, item.getItemStack())) <= 0 ? "已拥有§7§l)§r" : (item.getNumber() - getItemStackNumByPlayerInventory(p, item.getItemStack())) + "个§7§l)§r"))))
                                                                            .collect(Collectors.toList())
                                                                            .toString(),
                                                                    "[]")
                                                            .replace(",", "\n §f§l●")));
                                    break;
                                case "use_level":
                                    str.set(command.getUseLevel().getMessage());
                                    break;
                                case "chance":
                                    if (Objects.nonNull(command.getUseMoney()) && command.getUseMoney().getConsume()) pay.takeBalance(p.getName(), command.getUseMoney().getValue());
                                    if (Objects.nonNull(command.getUseItems()) && command.getUseItems().getConsume()) command.getUseItems().getItems().forEach(item -> itemTake(p, item.getItemStack(), item.getNumber()));
                                    if (Objects.nonNull(command.getUseLevel()) && command.getUseLevel().getConsume()) p.setLevel(p.getLevel() - command.getUseLevel().getLevel());
                                    str.set("none");
                                    break;
                            }
                            if (StringUtils.isNotBlank(str.get()) && !"none".equals(str.get())){
                                Util.sendMsg(p, str.get());
                            }
                        }
                    }
                });
                if (result.stream().anyMatch(s-> command.getDeCommandParam().stream().anyMatch(s::equals))) {
                    exeCmds(command.getDeCommands(), p, command.getNoArg() ? null : Util.convertStr(message.split(" "), 1));
                }
            }

        });
    }

    private List<String> isTrigger (Command command, Player player) {
        List <String> list = Lists.newArrayList();
        if (Objects.nonNull(command.getUsePermission()) && !player.isOp()) {
            if (Objects.nonNull(command.getUsePermission()) && Objects.nonNull(command.getUsePermission().getPer()) && !player.hasPermission(command.getUsePermission().getPer())) list.add("use_per");
        }
        if (Objects.nonNull(command.getUseMoney())) {
            if (pay.getBalance(player.getName()) < command.getUseMoney().getValue()) list.add("use_money");
        }
        if (Objects.nonNull(command.getUseItems()) && Objects.nonNull(command.getUseItems().getItems()) && !command.getUseItems().getItems().isEmpty()) {
            boolean first = command.getUseItems().getItems().stream().allMatch(item -> getItemStackNumByPlayerInventory(player, item.getItemStack()) >= item.getNumber());
            if (!first) list.add("use_item");
        }
        if (Objects.nonNull(command.getUseLevel())) {
            if (player.getLevel() < command.getUseLevel().getLevel()) list.add("use_level");
        }
        if (new Random().nextInt(100) > command.getChance()) list.add("chance");
        return list;
    }

    private int getItemStackNumByPlayerInventory (Player p, ItemStack itemStack) {
        return Stream.of(p.getInventory().getContents()).filter(itemStack1 -> Objects.nonNull(itemStack1) && itemStack.getItemMeta().equals(itemStack1.getItemMeta())).map(ItemStack::getAmount).mapToInt(integer -> integer).sum();
    }

    private void exeCmds(List<String> cmds, Player player, String arg) {
        AtomicInteger delay = new AtomicInteger();
        cmds.forEach(cmd -> {
            if (cmd.startsWith("delay: ")) {
                delay.addAndGet(NumberConversions.toInt(cmd.substring("delay: ".length())));
            } else {
                Bukkit.getScheduler().runTaskLater(AsgardCommand.getInstance(), () -> {
                    if (player == null) return;
                    if (cmd.split(":").length > 1 && cmd.split(":")[0].equals("{server}")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.split(":")[1].replace("{player}", player.getName()) + (arg == null ? "" : " " + arg));
                    } else {
                        if (player.isOp()) {
                            player.performCommand(cmd.replace("{player}", player.getName()) + (arg == null ? "" : " " + arg));
                        }
                    }
                }, delay.get());
            }
        });
    }

    private void itemTake(Player p, ItemStack itemStack, Integer number) {
        AtomicInteger num = new AtomicInteger(number);
        ItemStack[] contents = Stream.of(p.getInventory().getContents()).map(itemStack1 -> {
            if (Objects.nonNull(itemStack1) && itemStack1.hasItemMeta() && itemStack1.getItemMeta().equals(itemStack.getItemMeta())) {
                if (num.get() != 0) {
                    if (num.get() < itemStack1.getAmount()) {
                        itemStack1.setAmount(itemStack1.getAmount() - num.get());
                        num.set(0);
                    } else if (num.get() == itemStack1.getAmount()) {
                        num.set(0);
                        return null;
                    } else {
                        num.addAndGet(-itemStack1.getAmount());
                        return null;
                    }
                }
            }
            return itemStack1;
        }).toArray(ItemStack[]::new);
        p.getInventory().setContents(contents);
    }

    @Override
    public void registerEvent() {
        Bukkit.getPluginManager().registerEvents(this, AsgardCommand.getInstance());
    }

    private boolean isCooldown(Player p, String key, int s, boolean isShow) {
        long cooldown = Data.get(p.getName(), key);
        if (cooldown + s <= System.currentTimeMillis()) {
            Data.add(p.getName(), key);
            return false;
        } else {
            if (isShow)
            p.sendMessage(MessageFormat.format(Util.colored("&7&l[&f&lAsgard&7&l] &7正在冷却，还需 {0} &7秒..."), String.valueOf((((cooldown + s) - System.currentTimeMillis()) / 1000L) + 1)));
            return true;
        }
    }
}
