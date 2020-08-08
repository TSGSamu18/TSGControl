package tsg.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import tsg.main.TSGControl;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void chatListener(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String m = e.getMessage();
		if (TSGControl.instance.controlMap.containsKey(p.getUniqueId())) {
			if (m.startsWith("/")) {
				return;
			}
			e.setCancelled(true);
			if (Bukkit.getPlayer(TSGControl.instance.controlMap.get(p.getUniqueId())) == null) {
				p.sendMessage(TSGControl.instance.messages.PLAYER_NOT_ONLINE.replaceAll("%player%",
						Bukkit.getOfflinePlayer(TSGControl.instance.controlMap.get(p.getUniqueId())).getName()));
				return;
			}
			p.sendMessage(TSGControl.instance.utils.playerMessageFormat(m, p.getUniqueId()));
			Bukkit.getPlayer(TSGControl.instance.controlMap.get(p.getUniqueId()))
					.sendMessage(TSGControl.instance.utils.staffMessageFormat(m, p.getUniqueId()));
		} else if (TSGControl.instance.controlMap.containsValue(p.getUniqueId())) {
			e.setCancelled(true);
			if (Bukkit.getPlayer(
					TSGControl.instance.utils.getKeyFromValue(TSGControl.instance.controlMap, e.getPlayer().getUniqueId())) == null) {
				p.sendMessage(TSGControl.instance.messages.STAFF_NOT_ONLINE.replaceAll("%staff%",
						Bukkit.getOfflinePlayer(TSGControl.instance.utils.getKeyFromValue(TSGControl.instance.controlMap, p.getUniqueId()))
								.getName()));
				return;
			}
			if (m.startsWith("/")) {
				e.getPlayer().sendMessage(TSGControl.instance.messages.PLAYER_EXECUTE_COMMAND_UNDER_CONTROL);
				return;
			}
			p.sendMessage(TSGControl.instance.utils.staffMessageFormat(m, p.getUniqueId()));
			Bukkit.getPlayer(TSGControl.instance.utils.getKeyFromValue(TSGControl.instance.controlMap, e.getPlayer().getUniqueId()))
					.sendMessage(TSGControl.instance.utils.playerMessageFormat(m, p.getUniqueId()));
		}
	}
}
