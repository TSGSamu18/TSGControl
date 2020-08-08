package tsg.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import tsg.main.TSGControl;

public class QuitListener implements Listener {

	boolean messagePlayerOnStaffQuit;
	boolean finishControlOnPlayerQuit;
	
	public void setup() {
		messagePlayerOnStaffQuit = TSGControl.instance.config.getBoolean("Settings.message-player-on-staff-quit");
		finishControlOnPlayerQuit = TSGControl.instance.config.getBoolean("Settings.finish-control-on-player-quit");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (TSGControl.instance.controlMap.containsKey(p.getUniqueId())) {
			if (!messagePlayerOnStaffQuit) {
				return;
			}
			for (String m : TSGControl.instance.messages.MESSAGES_ON_STAFF_QUIT) {
				Bukkit.getPlayer(TSGControl.instance.controlMap.get(p.getUniqueId())).sendMessage(
						m.replaceAll("&", "§").replaceAll("%prefix%", TSGControl.instance.messages.PREFIX));
			}
		} else if (TSGControl.instance.controlMap.containsValue(p.getUniqueId())) {
			if (!finishControlOnPlayerQuit) {
				return;
			}
			TSGControl.instance.utils.endControl(
					TSGControl.instance.utils.getKeyFromValue(TSGControl.instance.controlMap, p.getUniqueId()));
		}
	}
}
