package me.justcallmelewis.SystemCore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import me.justcallmelewis.SystemCore.Core;
import net.md_5.bungee.api.ChatColor;

public class PlayerQuitEventListener implements Listener {

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {

		Player player = e.getPlayer();

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();

		DBCollection users = Core.getPlugin().getMongo().getDatabase().getCollection("users");

		DBObject quit = new BasicDBObject("uuid", player.getUniqueId().toString());
		DBObject found = users.findOne(quit);

		if (found == null) {
			BasicDBObject newUser = new BasicDBObject();
			newUser.put("name", player.getName());
			newUser.put("uuid", player.getUniqueId().toString());
			newUser.put("rank", 4);
			newUser.put("home_x", null);
			newUser.put("home_y", null);
			newUser.put("home_z", null);
			newUser.put("firstLoginDate", System.currentTimeMillis());
			newUser.put("lastLoginDate", System.currentTimeMillis());
			users.insert(newUser);
			return;
		}

		BasicDBObject set = new BasicDBObject("$set", quit);
		set.append("$set", new BasicDBObject("lastLoginDate", System.currentTimeMillis()));

		users.update(found, set);

		e.setQuitMessage(board.getEntryTeam(player.getName()).getPrefix() + player.getName() + ChatColor.YELLOW
				+ " has left the server.");

		board.getEntryTeam(player.getName()).removeEntry(player.getName());
	}

}
