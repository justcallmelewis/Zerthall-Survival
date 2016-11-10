package me.justcallmelewis.SystemCore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import me.justcallmelewis.SystemCore.Core;

public class PlayerJoinEventListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {

		Player p = e.getPlayer();

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();

		getRank(p.getUniqueId().toString(), board, p.getName());
		System.out.println("Adding " + p.getName() + " to team " + board.getEntryTeam(p.getName()).getName());

		p.setScoreboard(board);

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.getScoreboard();
		}

		e.setJoinMessage(
				board.getPlayerTeam(p).getPrefix() + p.getName() + ChatColor.YELLOW + " has joined the server.");

	}

	private void getRank(String uuid, Scoreboard board, String player) {

		DBCollection users = Core.getPlugin().getMongo().getDatabase().getCollection("users");

		BasicDBObject search = new BasicDBObject();
		search.put("uuid", uuid);
		DBCursor result = users.find(search);

		DBObject obj = result.next();

		int rankCheck = (Integer) obj.get("rank");

		switch (rankCheck) {

		case 1:
			board.getTeam("A-Owner").addEntry(player);
			break;
		case 2:
			board.getTeam("B-Mod").addEntry(player);
			break;
		case 3:
			board.getTeam("C-Vip").addEntry(player);
			break;
		case 4:
			board.getTeam("D-Regular").addEntry(player);
			break;
		}

	}

}
