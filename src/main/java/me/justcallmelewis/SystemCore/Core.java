package me.justcallmelewis.SystemCore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.justcallmelewis.SystemCore.commands.UserCommands;
import me.justcallmelewis.SystemCore.listeners.PlayerChatEventListener;
import me.justcallmelewis.SystemCore.listeners.PlayerJoinEventListener;
import me.justcallmelewis.SystemCore.listeners.PlayerLoginEventListener;
import me.justcallmelewis.SystemCore.listeners.PlayerQuitEventListener;
import me.justcallmelewis.SystemCore.listeners.SignChangeEventListener;
import me.justcallmelewis.SystemCore.mongo.MongoDB;
import me.justcallmelewis.SystemCore.mongo.MongoDBD;

public class Core extends JavaPlugin {

	private static Core plugin;
	private MongoDB mongodb;
	private PluginManager pm;

	public static Core getPlugin() {
		return plugin;
	}

	@Override
	public void onEnable() {

		plugin = this;
		pm = Bukkit.getPluginManager();
		getConfig().options().copyDefaults(true);
		saveConfig();

		MongoDB mdb = new MongoDB(MongoDBD.username, MongoDBD.password, MongoDBD.database, MongoDBD.host,
				MongoDBD.port);
		mdb.setDatabase(MongoDBD.database);
		mongodb = mdb;

		registerCommands();
		regListeners();
		createScoreboard();
	}

	@Override
	public void onDisable() {
		getMongo().closeConnection();
		plugin = null;
	}

	public MongoDB getMongo() {
		return mongodb;
	}

	public PluginManager getPluginManager() {
		return pm;
	}

	void regListeners() {

		getPluginManager().registerEvents(new PlayerLoginEventListener(), this);
		getPluginManager().registerEvents(new PlayerQuitEventListener(), this);
		getPluginManager().registerEvents(new PlayerChatEventListener(), this);
		getPluginManager().registerEvents(new SignChangeEventListener(), this);
		getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
	}

	private void registerCommands() {

		getCommand("sethome").setExecutor(new UserCommands());
		getCommand("homeinfo").setExecutor(new UserCommands());
		getCommand("home").setExecutor(new UserCommands());
	}

	private void createScoreboard() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();

		if (board.getTeam("A-Owner") == null) {
			board.registerNewTeam("A-Owner");
			board.getTeam("A-Owner").setPrefix(ChatColor.GOLD + "");

			board.registerNewTeam("B-Mod");
			board.getTeam("B-Mod").setPrefix(ChatColor.RED + "");

			board.registerNewTeam("C-Vip");
			board.getTeam("C-Vip").setPrefix(ChatColor.GREEN + "");

			board.registerNewTeam("D-Regular");
			board.getTeam("D-Regular").setPrefix(ChatColor.AQUA + "");

			System.out.println("Created Teams");
		} else {
			System.out.println("Teams already exist!");
		}

	}
}
