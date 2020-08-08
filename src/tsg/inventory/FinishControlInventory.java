package tsg.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import tsg.main.TSGControl;

public class FinishControlInventory {

	public HashMap<ItemStack, List<String>> commandMap;

	public String INVENTORY_TITLE;

	public void setup() {
		INVENTORY_TITLE = TSGControl.instance.config.getString("inventory.title");
	}

	public Inventory getFinishControlInventory() {
		commandMap = new HashMap<ItemStack, List<String>>();
		int size = TSGControl.instance.config.getInt("inventory.size");
		Inventory inv = Bukkit.createInventory((InventoryHolder) null, size,
				ChatColor.translateAlternateColorCodes('&', INVENTORY_TITLE));
		ItemStack is;
		for (String s : TSGControl.instance.config.getConfigurationSection("inventory").getKeys(false)) {
			is = new ItemStack(Material
					.matchMaterial(TSGControl.instance.config.getString("inventory." + s + ".material").toUpperCase()));
			is.setDurability((short) TSGControl.instance.config.getInt("inventory." + s + ".data"));
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName(TSGControl.instance.config.getString("inventory." + s + ".name"));
			List<String> lore = new ArrayList<String>();
			for (String l : TSGControl.instance.config.getStringList("inventory." + s + ".lore")) {
				lore.add(l.replace("&", "§"));
			}
			meta.setLore(lore);
			if (TSGControl.instance.config.getBoolean("inventory." + s + ".glow")) {
				meta.addEnchant(Enchantment.getByName("KNOCKBACK"), 1, false);
			}
			inv.setItem(TSGControl.instance.config.getInt("inventory." + s + ".slot"), is);
			if (TSGControl.instance.config.getBoolean("inventory." + s + ".isActive")) {
				commandMap.put(is, TSGControl.instance.config.getStringList("inventory." + s + ".commands"));
			}
		}
		return inv;
	}
}
