package me.hawkutils.utils;

import org.bukkit.Bukkit;

import me.hawkutils.Core;

public class Console {

	public static void log(String... messages) {
		if (messages == null) return;
		for(String message : messages) {
			Bukkit.getConsoleSender().sendMessage(message.replace("&", "§"));
		}
	}
	
	public static void executeCommand(String cmd) {
		Bukkit.getScheduler().runTask(Core.getInstance(), ()->Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
	}
	
}
