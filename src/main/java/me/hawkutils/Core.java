package me.hawkutils;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.hawkutils.utils.Console;
import me.hawkutils.utils.managers.Manager;

@Getter
public class Core extends JavaPlugin {

	@Getter
	private static Core instance;
	private Manager manager;
	
	@Override
	public void onEnable() {
		instance = this;
		manager = new Manager();
		Console.log("§6HawkUtils iniciado com sucesso! [Author lHawk_] §ev" + getDescription().getVersion());
	}
	
	@Override
	public void onDisable() {
		if (manager != null) {
			for (int i = 0; i < manager.getMenus().size(); i++) {
				manager.getMenus().get(i).remove();
			}
		}
		Console.log("§cHawkUtils desligado com sucesso! [Author lHawk_] v" + getDescription().getVersion());
	}
	
}
