package me.hawkutils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Glass {

    private List<Item> glassItems = new ArrayList<>();

    public Glass(List<String> itemsList, int menuSize) {
        Random random = new Random(); // Criar uma instância de Random uma vez
        for (String line : itemsList) {
            String[] args = line.split(":");
            String itemId = args[0];
            String slots = args[1];
            
            if (slots.equalsIgnoreCase("all")) {
                addItemsToAllSlots(itemId, menuSize, random);
            } else {
                addItemsToSpecificSlots(itemId, slots, random);
            }
        }
    }

    // Adicionar itens a todos os slots
    private void addItemsToAllSlots(String itemId, int menuSize, Random random) {
        for (int i = 0; i < 9 * menuSize; i++) {
            addItem(itemId, i, random);
        }
    }

    // Adicionar itens a slots específicos
    private void addItemsToSpecificSlots(String itemId, String slots, Random random) {
        if (slots.contains(",")) {
            for (String slot : slots.split(",")) {
                addItem(itemId, Integer.parseInt(slot) - 1, random);
            }
        } else {
            addItem(itemId, Integer.parseInt(slots) - 1, random);
        }
    }

    // Método auxiliar para adicionar um item
    private void addItem(String itemId, int slot, Random random) {
        Item item = new Item(API.getItemStack(itemId));
        item.setDisplayName("§" + random.nextInt(9) + "●");
        item.setSlot(slot);
        glassItems.add(item);
    }

    public List<Item> getGlassItems() {
        return this.glassItems;
    }
}
