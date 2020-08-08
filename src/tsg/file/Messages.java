package tsg.file;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import tsg.main.TSGControl;

public class Messages {

	YamlConfiguration messages;

	public String PREFIX;

	public String PLAYER_NOT_FOUND;
	public String INCORRECT_USAGE;
	public String START_CONTROL_TITLE;
	public String STAFF_MESSAGES_FORMAT;
	public String PLAYER_MESSAGES_FORMAT;
	public String PLAYER_EXECUTE_COMMAND_UNDER_CONTROL;
	public String PLAYER_NOT_ONLINE;
	public String STAFF_NOT_ONLINE;
	public String STAFF_ALREADY_IN_CONTROL;
	public String PLAYER_ALREADY_IN_CONTROL;
	public String NOT_IN_A_CONTROL_SESSION;
	public String ON_RELOAD;
	public String ON_SET_STAFF_POSITION;
	public String ON_SET_PLAYER_POSITION;
	public String NO_PERMISSION;
	public String NOT_PLAYER;

	public List<String> HELP;
	public List<String> MESSAGES_ON_STAFF_QUIT;
	public List<String> STAFF_CONTROL_STARTED;
	public List<String> PLAYER_CONTROL_STARTED;
	public List<String> STAFF_CONTROL_FINISHED;
	public List<String> PLAYER_CONTROL_FINISHED;

	public void setup() {
		messages = TSGControl.instance.config;
		HELP = new ArrayList<String>();
		MESSAGES_ON_STAFF_QUIT = new ArrayList<String>();
		PREFIX = messages.getString("Messages.prefix").replaceAll("&", "§");
		PLAYER_NOT_FOUND = traslator(messages.getString("Messages.player-not-found"));
		INCORRECT_USAGE = traslator(messages.getString("Messages.incorrect-usage"));
		START_CONTROL_TITLE = traslator(messages.getString("Messages.player-control-finished"));
		STAFF_MESSAGES_FORMAT = traslator(messages.getString("Messages.staff-messages-format"));
		PLAYER_MESSAGES_FORMAT = traslator(messages.getString("Messages.player-messages-format"));
		PLAYER_EXECUTE_COMMAND_UNDER_CONTROL = traslator(
				messages.getString("Messages.player-execute-command-under-control"));
		PLAYER_NOT_ONLINE = traslator(messages.getString("Messages.player-not-online"));
		STAFF_NOT_ONLINE = traslator(messages.getString("Messages.staff-not-online"));
		STAFF_ALREADY_IN_CONTROL = traslator(messages.getString("Messages.staff-alredy-in-control"));
		PLAYER_ALREADY_IN_CONTROL = traslator(messages.getString("Messages.player-alredy-in-control"));
		NOT_IN_A_CONTROL_SESSION = traslator(messages.getString("Messages.not-in-a-control-session"));
		ON_RELOAD = traslator(messages.getString("Messages.on-reload"));
		ON_SET_STAFF_POSITION = traslator(messages.getString("Messages.on-set-staff-position"));
		ON_SET_PLAYER_POSITION = traslator(messages.getString("Messages.on-set-player-position"));
		NOT_PLAYER = traslator(messages.getString("Messages.executor-is-not-player"));
		NO_PERMISSION = traslator(messages.getString("Messages.no-permission"));
		HELP = messages.getStringList("Messages.help");
		MESSAGES_ON_STAFF_QUIT = messages.getStringList("Messages.messages-on-staff-quit");
		STAFF_CONTROL_STARTED = messages.getStringList("Messages.staff-control-started");
		PLAYER_CONTROL_STARTED = messages.getStringList("Messages.player-control-started");
		STAFF_CONTROL_FINISHED = messages.getStringList("Messages.staff-control-started");
		PLAYER_CONTROL_FINISHED = messages.getStringList("Messages.staff-control-finished");
	}

	public String traslator(String msg) {
		return msg.replaceAll("&", "§").replaceAll("%prefix%", PREFIX);
	}
}
