package me.justcallmelewis.SystemCore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import me.justcallmelewis.SystemCore.Core;

public class PlayerChatEventListener implements Listener {

	ScoreboardManager manager = Bukkit.getScoreboardManager();
	Scoreboard board = manager.getMainScoreboard();

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {

		Player p = e.getPlayer();

		DBCollection users = Core.getPlugin().getMongo().getDatabase().getCollection("users");

		BasicDBObject search = new BasicDBObject();
		search.put("uuid", p.getUniqueId().toString());
		DBCursor result = users.find(search);

		BasicDBObject obj = (BasicDBObject) result.next();

		int rank = (Integer) obj.get("rank");

		switch (rank) {

		case 1:
			e.setFormat(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Owner" + ChatColor.GRAY + "]"
					+ board.getEntryTeam(p.getName()).getPrefix() + "%s" + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE
					+ "%s");
			break;
		case 2:
			e.setFormat(ChatColor.GRAY + "[" + ChatColor.DARK_RED + "Mod" + ChatColor.GRAY + "]"
					+ board.getEntryTeam(p.getName()).getPrefix() + "%s" + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE
					+ "%s");
			break;
		default:
			e.setFormat(board.getEntryTeam(p.getName()).getPrefix() + "%s" + ChatColor.DARK_GRAY + " » "
					+ ChatColor.WHITE + "%s");
			break;
		}
	}

}
