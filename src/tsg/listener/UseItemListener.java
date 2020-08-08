package tsg.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseItemListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onItemUse(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			e.setCancelled(true);
		}
	}
}
