package tsg.main;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Utils {

	Location stafflocation;
	Location playerlocation;

	boolean useInventory;

	public void setup() {
		stafflocation = new Location(
				Bukkit.getWorld(TSGControl.instance.config.getString("Settings.stafflocation.world")),
				TSGControl.instance.config.getInt("Settings.stafflocation.x"),
				TSGControl.instance.config.getInt("Settings.stafflocation.y"),
				TSGControl.instance.config.getInt("Settings.stafflocation.z"));
		playerlocation = new Location(
				Bukkit.getWorld(TSGControl.instance.config.getString("Settings.playerlocation.world")),
				TSGControl.instance.config.getInt("Settings.playerlocation.x"),
				TSGControl.instance.config.getInt("Settings.playerlocation.y"),
				TSGControl.instance.config.getInt("Settings.playerlocation.z"));
		useInventory = TSGControl.instance.config.getBoolean("inventory.enabled");
	}

	public void saveList(File f) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		for (UUID id : TSGControl.instance.controlMap.keySet()) {
			config.set(id.toString(), TSGControl.instance.controlMap.get(id));
		}
		try {
			config.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadList(File f) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		for (String s : config.getKeys(false)) {
			TSGControl.instance.controlMap.put(UUID.fromString(s), UUID.fromString(config.getString(s)));
		}
	}

	public void startControl(UUID staff, UUID p) {
		Player staffer = null;
		Player player = null;
		try {
			staffer = Bukkit.getPlayer(staff);
			staffer.teleport(stafflocation);
		} catch (NullPointerException e) {
		}
		try {
			player = Bukkit.getPlayer(p);
			player.teleport(playerlocation);
		} catch (NullPointerException e) {

		}
		try {
			for (String m : TSGControl.instance.messages.STAFF_CONTROL_STARTED) {
				staffer.sendMessage(m.replaceAll("%player%", player.getName()).replaceAll("&", "§")
						.replaceAll("%prefix%", TSGControl.instance.messages.PREFIX));
			}
		} catch (NullPointerException e) {
		}
		try {
			for (String m : TSGControl.instance.messages.PLAYER_CONTROL_STARTED) {
				player.sendMessage(m.replaceAll("%staff%", staffer.getName().replaceAll("&", "§").replaceAll("%prefix%",
						TSGControl.instance.messages.PREFIX)));
			}
		} catch (NullPointerException e) {
		}
		TSGControl.instance.controlMap.put(staff, p);
	}

	public void endControl(UUID staff) {
		Player staffer = null;
		try {
			staffer = Bukkit.getPlayer(staff);
		} catch (NullPointerException e) {
		}
		if (TSGControl.instance.controlMap.containsKey(staff)) {
			staffer.sendMessage(TSGControl.instance.messages.NOT_IN_A_CONTROL_SESSION);
			return;
		}
		try {
			for (String m : TSGControl.instance.messages.PLAYER_CONTROL_FINISHED) {
				Bukkit.getPlayer(TSGControl.instance.controlMap.get(staff)).sendMessage(m);
			}
		} catch (NullPointerException e) {
		}
		try {
			for (String m : TSGControl.instance.messages.STAFF_CONTROL_FINISHED) {
				staffer.sendMessage(m);
			}
		} catch (NullPointerException e) {
		}
		TSGControl.instance.controlMap.remove(staff);
		if (useInventory) {
			try {
				staffer.openInventory(TSGControl.instance.finishinventory.getFinishControlInventory());
			} catch (NullPointerException e) {
			}
		}
	}

	public String staffMessageFormat(String messages, UUID p) {
		return TSGControl.instance.messages.STAFF_MESSAGES_FORMAT.replaceAll("%player%", Bukkit.getPlayer(p).getName())
				.replaceAll("%messages%", messages);
	}

	public String playerMessageFormat(String messages, UUID p) {
		return TSGControl.instance.messages.PLAYER_MESSAGES_FORMAT.replaceAll("%staff%", Bukkit.getPlayer(p).getName())
				.replaceAll("%messages%", messages);
	}

	@SuppressWarnings("rawtypes")
	public UUID getKeyFromValue(Map hm, UUID value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return (UUID) o;
			}
		}
		return null;
	}

	public UUID getPlayerUUID(String name) {
		if (Bukkit.getPlayer(name) != null) {
			return Bukkit.getPlayer(name).getUniqueId();
		}
		for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			if (player.getName().equals(name)) {
				return player.getUniqueId();
			}
		}
		return null;
	}
}
