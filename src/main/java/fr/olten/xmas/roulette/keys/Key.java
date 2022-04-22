package fr.olten.xmas.roulette.keys;

import org.bukkit.inventory.ItemStack;

public class Key {
    private ItemStack item;
    private KeyTypes keyType;
    private String customLore;

    public Key(ItemStack item, KeyTypes keyType, String customLore) {
        this.item = item;
        this.keyType = keyType;
        this.customLore = customLore;
    }

    // Get the itemstack related to the key.
    public ItemStack getItem() {
        return item;
    }

    // Get the type of the key.
    public KeyTypes getKeyType() {
        return keyType;
    }

    // Get the lore of the item.
    public String getCustomLore() {
        return customLore;
    }

    // Set the lore of the item.
    public void setCustomLore(String customLore) {
        this.customLore = customLore;
    }

    // Set the itemstack related to the key.
    public void setItem(ItemStack item) {
        this.item = item;
    }

    // Set the type of the key.
    public void setKeyType(KeyTypes keyType) {
        this.keyType = keyType;
    }
}
