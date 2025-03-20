package me.hawkutils.utils;

import java.lang.reflect.Field;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class InventoryAPI {

	public static boolean hasSpace(Player p, ItemStack it) {
		Inventory inv = p.getInventory();
		int space = 0;
		int stack = 0;
		for (ItemStack item : inv.getContents()) {
			if (item == null || item.getType() == Material.AIR)
				space++;
			else if (it != null && item.isSimilar(it) && item.getAmount() < item.getMaxStackSize())
				stack++;
		}
		return space > 0 || stack > 0 ? true : false;
	}

	public static int getAmount(Player p, ItemStack it) {
		if (it == null)
			return 0;
		int amount = 0;
		Inventory inv = p.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (item == null || item.getType() == Material.AIR || !item.isSimilar(it))
				continue;
			amount += item.getAmount();
		}
		return amount;
	}

	public static void updateTitlePacket(Player p, String title) {
		InventoryView openInventory = p.getOpenInventory();
		if (openInventory == null) {
			return; // Verifica se o jogador tem um inventário aberto
		}
		try {
			// Converte o jogador e o inventário para o NMS
			CraftInventoryView craftInventoryView = (CraftInventoryView) openInventory;
			Container container = craftInventoryView.getHandle();

			// Usa reflection para acessar o campo "windowId" do Container
			Field windowIdField = Container.class.getDeclaredField("windowId");
			windowIdField.setAccessible(true);
			int windowId = (int) windowIdField.get(container);

			// Cria o pacote PacketPlayOutOpenWindow com o novo título
			PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(windowId, // ID da janela atual
					"minecraft:chest", // Tipo de inventário (não altera o inventário)
					new ChatMessage(title), // Novo título
					openInventory.getTopInventory().getSize() // Tamanho do inventário
			);

			// Envia o pacote para o jogador
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);

			// Atualiza o inventário do jogador (sem reiniciar o inventário)
			p.updateInventory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeItem(Player player, ItemStack itemStack, int quantidade) {
		PlayerInventory inventario = player.getInventory();
		ItemStack[] itens = inventario.getContents();
		for (int i = 0; i < itens.length; i++) {
			ItemStack itemNoInventario = itens[i];
			if (itemNoInventario != null && itemNoInventario.isSimilar(itemStack)) {
				if (itemNoInventario.getAmount() >= quantidade) {
					itemNoInventario.setAmount(itemNoInventario.getAmount() - quantidade);
					inventario.setItem(i, itemNoInventario);
					player.updateInventory();
					return;
				} else {
					quantidade -= itemNoInventario.getAmount();
					inventario.clear(i);
				}
			}
		}
	}

}
