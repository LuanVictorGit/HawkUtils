package me.hawkutils.utils.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface MenuExecutor {

	public void onClick(InventoryClickEvent e);
	
	public void onOpen(InventoryOpenEvent e);
	
	public void onClose(InventoryCloseEvent e);
	
}
