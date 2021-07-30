package me.asgard.asgardcommand.data;

import me.asgard.asgardcommand.AsgardCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Data {
	
	private static File f;
	private static FileConfiguration data;
	
	static {
		f = new File (AsgardCommand.getInstance().getDataFolder(),"data.yml");
		data = YamlConfiguration.loadConfiguration(f);
	}
	
	public static void add (String playerName, String key) {
		data.set(playerName + ".cooldowns." + key, System.currentTimeMillis());
		save ();
	}
	
	
	public static long get (String playerName, String key) {
		return data.getLong(playerName+".cooldowns."+key);
	}
	
	private static void save () {
		try {
			data.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
