package tsg.command;

import java.util.ArrayList; 
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tsg.main.TSGControl;

public class CommandListener extends Command {

	List<String> commandAliases;

	public CommandListener(String name) {
		super(name);
		commandAliases = new ArrayList<String>();
		fillCommandAliases();
		this.description = "Control command!";
		this.setAliases(commandAliases);
	}

	@Override
	public boolean execute(CommandSender se, String s, String[] ar) {
		if (!(se instanceof Player)) {
			se.sendMessage(TSGControl.instance.messages.NOT_PLAYER);
			return true;
		}
		Player p = (Player) se;
		if (ar.length == 0) {
			if (!p.hasPermission("tsgcontrol.help")) {
				p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
				return true;
			}
			for (String h : TSGControl.instance.messages.HELP) {
				p.sendMessage(h.replaceAll("&", "§").replaceAll("%prefix%", TSGControl.instance.messages.PREFIX));
			}
		} else if (ar.length == 1) {
			if (!p.hasPermission("tsgcontrol.help")) {
				p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
				return true;
			}
			if (ar[0].equalsIgnoreCase("help")) {
				for (String h : TSGControl.instance.messages.HELP) {
					p.sendMessage(h.replaceAll("&", "§").replaceAll("%prefix%", TSGControl.instance.messages.PREFIX));
				}
			} else if (ar[0].equalsIgnoreCase("reload")) {
				if (!p.hasPermission("tsgcontrol.reload")) {
					p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
					return true;
				}
				TSGControl.instance.reload();
				p.sendMessage(TSGControl.instance.messages.ON_RELOAD);
			} else if (ar[0].equalsIgnoreCase("end")) {
				if (!p.hasPermission("tsgcontrol.control.end")) {
					p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
					return true;
				}
				TSGControl.instance.utils.endControl(p.getUniqueId());
			} else if (ar[0].equalsIgnoreCase("setstaffpos")) {
				if (!p.hasPermission("tsgcontrol.setstaffpos")) {
					p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
					return true;
				}
				TSGControl.instance.config.set("Settings.stafflocation.world", p.getWorld().getName());
				TSGControl.instance.config.set("Settings.stafflocation.x", p.getLocation().getX());
				TSGControl.instance.config.set("Settings.stafflocation.y", p.getLocation().getY());
				TSGControl.instance.config.set("Settings.stafflocation.z", p.getLocation().getZ());
				TSGControl.instance.saveConfig();
			} else if (ar[0].equalsIgnoreCase("setplayerpos")) {
				if (!p.hasPermission("tsgcontrol.setplayerpos")) {
					p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
					return true;
				}
				TSGControl.instance.config.set("Settings.playerlocation.world", p.getWorld().getName());
				TSGControl.instance.config.set("Settings.playerlocation.x", p.getLocation().getX());
				TSGControl.instance.config.set("Settings.playerlocation.y", p.getLocation().getY());
				TSGControl.instance.config.set("Settings.playerlocation.z", p.getLocation().getZ());
				TSGControl.instance.saveConfig();
			}
		} else if (ar.length == 2) {
			if (!p.hasPermission("tsgcontrol.control.start")) {
				p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
				return true;
			}
			if (ar[0].equalsIgnoreCase("control")) {
				try {
					TSGControl.instance.utils.startControl(p.getUniqueId(),
							TSGControl.instance.utils.getPlayerUUID(ar[1]));
				} catch (NullPointerException e) {
					p.sendMessage(TSGControl.instance.messages.PLAYER_NOT_FOUND.replaceAll("%player%", ar[1]));
				}
			} else if (ar[0].equalsIgnoreCase("end")) {
				if (!p.hasPermission("tsgcontrol.control.end.other")) {
					p.sendMessage(TSGControl.instance.messages.NO_PERMISSION);
					return true;
				}
				try {
					TSGControl.instance.utils.endControl(TSGControl.instance.utils.getPlayerUUID(ar[1]));
				} catch (NullPointerException e) {
					p.sendMessage(TSGControl.instance.messages.PLAYER_NOT_FOUND.replaceAll("%player%", ar[1]));
				}
			}
		}
		return false;
	}

	public void fillCommandAliases() {
		if (TSGControl.instance.config.isList("Command.Aliases")) {
			for (String alias : TSGControl.instance.config.getStringList("Command.Aliases")) {
				commandAliases.add(alias);
			}
		} else {
			TSGControl.instance.log.severe("Error commands alias not found");
		}
	}

}
