package tsg.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class HookProtocolLib {

	public void hook() {
		ProtocolManager lib = ProtocolLibrary.getProtocolManager();
		lib.addPacketListener(new PacketAdapter(TSGControl.instance, ListenerPriority.HIGHEST, PacketType.Play.Client.USE_ITEM) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
				if (e.getPacketType() == PacketType.Play.Client.USE_ITEM) {
					Player p = e.getPlayer();
					if (TSGControl.instance.controlMap.containsValue(p.getUniqueId())) {
						e.setCancelled(true);
					}
				}
			}
		});
		lib.addPacketListener(new PacketAdapter(TSGControl.instance, ListenerPriority.HIGHEST, PacketType.Play.Client.CHAT) {
			@Override
			public void onPacketReceiving(PacketEvent e) {
				if (e.getPacketType() == PacketType.Play.Client.CHAT) {
					Player p = e.getPlayer();
					PacketContainer packet = e.getPacket();
					String m = packet.getStrings().read(0);
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
						if (Bukkit.getPlayer(TSGControl.instance.utils.getKeyFromValue(TSGControl.instance.controlMap, e.getPlayer().getUniqueId())) == null) {
							p.sendMessage(TSGControl.instance.messages.STAFF_NOT_ONLINE.replaceAll("%staff%", Bukkit
									.getOfflinePlayer(TSGControl.instance.utils.getKeyFromValue(TSGControl.instance.controlMap, p.getUniqueId())).getName()));
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
		});
		TSGControl.instance.log.info("---------------------------------");
		TSGControl.instance.log.info("§e[§a+§e]§aHooked in ProtocolLib!");
		TSGControl.instance.log.info("---------------------------------");
	}
}
