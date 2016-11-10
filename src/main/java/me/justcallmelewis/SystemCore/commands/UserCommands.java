package me.justcallmelewis.SystemCore.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import me.justcallmelewis.SystemCore.Core;

public class UserCommands implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
		Player player = (Player) s;
		if (lbl.equalsIgnoreCase("sethome")) {

			DBCollection users = Core.getPlugin().getMongo().getDatabase().getCollection("users");

			BasicDBObject search = new BasicDBObject();
			search.put("uuid", player.getUniqueId().toString());
			DBCursor result = users.find(search);

			BasicDBObject update = new BasicDBObject();
			update.putAll(result.next());
			update.remove("home_world");
			update.remove("home_x");
			update.remove("home_y");
			update.remove("home_z");
			update.put("home_world", player.getWorld().getName());
			update.put("home_x", player.getLocation().getX());
			update.put("home_y", player.getLocation().getY());
			update.put("home_z", player.getLocation().getZ());

			BasicDBObject fUpdate = new BasicDBObject();
			fUpdate.put("$set", update);

			users.update(search, fUpdate);

			player.sendMessage("§8[§6Home§8] §eYour home has been set.");
			return true;
		}
		if (lbl.equalsIgnoreCase("homeinfo")) {

			DBCollection users = Core.getPlugin().getMongo().getDatabase().getCollection("users");

			BasicDBObject search = new BasicDBObject();
			search.put("uuid", player.getUniqueId().toString());
			DBCursor result = users.find(search);

			BasicDBObject obj = (BasicDBObject) result.next();

			if (obj.get("home_x") == null) {
				player.sendMessage("§8[§6Home§8] §eYour home has not been set!");
				return false;
			}

			Double x = obj.getDouble("home_x");
			Double y = obj.getDouble("home_y");
			Double z = obj.getDouble("home_z");

			player.sendMessage("§8[§6Home§8] §eX:§r " + x + " §8/ §eY:§r " + y + " §8/ §eZ:§r " + z);
		}
		if (lbl.equalsIgnoreCase("home")) {

			DBCollection users = Core.getPlugin().getMongo().getDatabase().getCollection("users");

			BasicDBObject search = new BasicDBObject();
			search.put("uuid", player.getUniqueId().toString());
			DBCursor result = users.find(search);

			BasicDBObject obj = (BasicDBObject) result.next();

			if (obj.get("home_x") == null) {
				player.sendMessage("§8[§6Home§8] §eYour home has not been set!");
				return false;
			}

			World world = Bukkit.getWorld(obj.getString("home_world"));

			World w = (World) world;
			Double x = obj.getDouble("home_x");
			Double y = obj.getDouble("home_y");
			Double z = obj.getDouble("home_z");

			player.teleport(new Location(w, x, y, z));
		}
		return false;
	}

}
