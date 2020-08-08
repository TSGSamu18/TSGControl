package tsg.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import tsg.main.TSGControl;

public class InventoryListener implements Listener {

	@EventHandler
	public void onClickListener(InventoryClickEvent e) {
		Inventory inventory = e.getInventory();
		if (inventory.getName() == null) {
			return;
		}
		if (e.getInventory().getTitle().equals(
				ChatColor.translateAlternateColorCodes('&', TSGControl.instance.finishinventory.INVENTORY_TITLE))) {
			Player p = (Player) e.getWhoClicked();
			ItemStack is = e.getCurrentItem();
			e.setCancelled(true);
			if (e.getSlotType().equals(SlotType.OUTSIDE)) {
				p.closeInventory();
				return;
			}
			if (e.getClick().equals(ClickType.SHIFT_RIGHT)) {
				p.closeInventory();
				p.updateInventory();
				return;
			}
			if (e.getRawSlot() >= e.getInventory().getSize() || is == null || is.getType() == Material.AIR) {
				return;
			}
			if (TSGControl.instance.finishinventory.commandMap.containsKey(is)) {
				for (String cmd : TSGControl.instance.finishinventory.commandMap.get(is)) {
					if (cmd.startsWith("%console%")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%player%", Bukkit
								.getOfflinePlayer(TSGControl.instance.controlMap.get(p.getUniqueId())).getName()
								.replaceAll("%staff%", e.getWhoClicked().getName())));
					} else {
						p.chat(cmd.replaceAll("%player%", Bukkit
								.getOfflinePlayer(TSGControl.instance.controlMap.get(p.getUniqueId())).getName())
								.replaceAll("%staff%", e.getWhoClicked().getName()));
					}
					TSGControl.instance.controlMap.remove(p.getUniqueId());
				}
				p.closeInventory();
			}
			return;
		}
	}
	
}
