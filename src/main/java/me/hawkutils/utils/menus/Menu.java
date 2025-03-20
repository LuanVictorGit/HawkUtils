package me.hawkutils.utils.menus;

import org.bukkit.Bukkit;


import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import me.hawkutils.Core;
import me.hawkutils.utils.InventoryAPI;
import me.hawkutils.utils.managers.Manager;

@Getter
@Setter
public class Menu implements MenuExecutor{

	private String title,
	titleToUpdate;;
	private Inventory inventory;
	private boolean isRemoved = false;
	private Boolean cancelClick = false,
	removeOnClose = true;
	private Listener listener;
	private int size;
	
	public Menu(String title, int size) {
		this.title = title;
		this.size = size;
		this.inventory = Bukkit.createInventory(null, 9*size, title.replace("&", "§"));
		this.listener = new Listener() {
			
			@EventHandler
			public void click(InventoryClickEvent e) {
				if (e.getInventory() == null || !e.getInventory().equals(inventory)) return;
				onClick(e);
			}
			
			@EventHandler
			public void close(InventoryCloseEvent e) {
				if (e.getInventory() == null || !e.getInventory().equals(inventory)) return;
				onClose(e);
			}
			
			@EventHandler
			public void open(InventoryOpenEvent e) {
				if (e.getInventory() == null || !e.getInventory().equals(inventory)) return;
				onOpen(e);
			}
			
		};
		Bukkit.getPluginManager().registerEvents(listener, Core.getInstance());
		Manager.get().getMenus().add(this);
	}
	
	public Menu open(Player p) {
		Bukkit.getScheduler().runTask(Core.getInstance(), ()->{
			p.openInventory(inventory);
			p.updateInventory();
		});
		return this;
	}
	
	public void remove() {
		Manager.get().getMenus().remove(this);
		HandlerList.unregisterAll(listener);
		Bukkit.getOnlinePlayers().forEach(p -> {
			if (p.getOpenInventory() == null) return;
			Inventory inv = p.getOpenInventory().getTopInventory();
			if (!inv.equals(inventory)) return;
			p.closeInventory();
		});
	}

	public void setTitleInventory(Player p, String title) {
		this.titleToUpdate = title;
		InventoryAPI.updateTitlePacket(p, titleToUpdate);
	}
	
	public Menu setCancelClick(Boolean cancelClick) {
		this.cancelClick = cancelClick;
		return this;
	}

	public Menu removeOnClose(Boolean removeOnClose) {
		this.removeOnClose = removeOnClose;
		return this;
	}
	
	@Override
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!e.getInventory().equals(inventory)) return;
		if (cancelClick) e.setCancelled(true);
		if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
			p.playSound(p.getLocation(), Sound.WOOD_CLICK, 0.5f, 10f);
		}
	}

	@Override
	public void onOpen(InventoryOpenEvent e) {
		Player p = (Player) e.getPlayer();
		if (!e.getInventory().equals(inventory)) return;
		p.playSound(p.getLocation(), Sound.CHEST_OPEN, 0.5f, 10);
	}

	@Override
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (!e.getInventory().equals(inventory)) return;
		if (removeOnClose) {
			remove();
			isRemoved = true;
		}
		p.playSound(p.getLocation(), Sound.CHEST_CLOSE, 0.5f, 10);
	}
}
