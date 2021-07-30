package me.asgard.asgardcommand.listener;

import me.asgard.asgardcommand.config.Config;
import org.bukkit.event.Listener;

public interface BaseListener extends Config, Listener {
    void registerEvent();
}
