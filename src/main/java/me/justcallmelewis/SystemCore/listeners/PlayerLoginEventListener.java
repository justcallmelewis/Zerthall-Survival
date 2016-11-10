package me.justcallmelewis.SystemCore.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import me.justcallmelewis.SystemCore.Core;

public class PlayerLoginEventListener implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerLoginEvent e) {

		Player player = e.getPlayer();

		DBCollection users = Core.getPlugin().getMongo().getDatabase().getCollection("users");

		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("uuid", player.getUniqueId().toString());
		DBCursor result = users.find(searchQuery);

		if (result.hasNext()) {

			DBObject login = new BasicDBObject("uuid", player.getUniqueId().toString());
			DBObject found = users.findOne(login);

			BasicDBObject set = new BasicDBObject("$set", login);
			set.append("$set", new BasicDBObject("name", player.getName()));

			users.update(found, set);

		}

		if (!result.hasNext()) {

			BasicDBObject newUser = new BasicDBObject();
			newUser.put("name", player.getName());
			newUser.put("uuid", player.getUniqueId().toString());
			newUser.put("rank", 4);
			newUser.put("home_world", null);
			newUser.put("home_x", null);
			newUser.put("home_y", null);
			newUser.put("home_z", null);
			newUser.put("firstLoginDate", System.currentTimeMillis());
			newUser.put("lastLoginDate", System.currentTimeMillis());
			users.insert(newUser);
		}
	}

}
