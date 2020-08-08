package tsg.main;

import java.lang.reflect.Field;    
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

import tsg.command.CommandListener;
import tsg.file.FileManager;
import tsg.file.Messages;
import tsg.inventory.FinishControlInventory;
import tsg.listener.ChatListener;
import tsg.listener.InventoryListener;
import tsg.listener.QuitListener;
import tsg.listener.UseItemListener;
 
public class TSGControl extends JavaPlugin {

	public static TSGControl instance;
	public Logger log = this.getLogger();
	public YamlConfiguration config;

	// @Params Staff, Player
	public HashMap<UUID, UUID> controlMap;

	public Utils utils;
	public Messages messages;
	public FileManager fileManager;
	public FinishControlInventory finishinventory;
	public QuitListener quitListener;
	public InventoryListener inventoryListener;
	HookProtocolLib hookProtocolLib;

	boolean uselib = false;

	@Override
	public void onDisable() {
		if (uselib) {
			ProtocolLibrary.getProtocolManager().removePacketListeners(this);
		}
		utils.saveList(this.fileManager.save);
	}

	public boolean hookPL() {
		if (!TSGControl.instance.config.getBoolean("Settings.use-protocol-lib")) {
			return false;
		}
		if (TSGControl.instance.getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
			TSGControl.instance.log.severe("--------------------");
			TSGControl.instance.log.severe("ProcolLib not found!");
			TSGControl.instance.log.severe("--------------------");
			return false;
		}
		hookProtocolLib = new HookProtocolLib();
		return true;
	}
	
	@Override
	public void onEnable() {
		instance = this;
		config = (YamlConfiguration) this.getConfig();
		this.initializeClass();
		utils.setup();
		fileManager.setup();
		finishinventory.setup();
		messages.setup();
		uselib = hookPL();
		this.registerEvents();
		this.registerCommand(this.config.getString("Command.Main"),
				new CommandListener(this.config.getString("Command.Main")));
		utils.loadList(this.fileManager.save);
	}

	public void registerCommand(String fallback, Command command) {
		try {
			Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
			commandMap.register(fallback, command);
		} catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
			this.log.severe("Error while registering commands:" + e.toString());
		}
	}

	public void registerEvents() {
		inventoryListener = new InventoryListener();
		quitListener = new QuitListener();
		quitListener.setup();
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(inventoryListener, this);
		manager.registerEvents(quitListener, this);
		if (!uselib) {
			manager.registerEvents(new ChatListener(), this);
			manager.registerEvents(new UseItemListener(), this);
		}
	}

	public void initializeClass() {
		controlMap = new HashMap<UUID, UUID>();
		utils = new Utils();
		messages = new Messages();
		fileManager = new FileManager();
		finishinventory = new FinishControlInventory();
	}

	public void reload() {
		fileManager.setup();
		this.reloadConfig();
		finishinventory.setup();
		messages.setup();
		utils.setup();
		quitListener.setup();
		uselib = hookPL();
	}
}
