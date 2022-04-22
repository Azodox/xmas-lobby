package fr.olten.xmas.rewards;

import org.bukkit.inventory.ItemStack;

public class ObjetReward extends Reward implements IObtainableNumeric {

    private ItemStack item;
    private double itemAmount;

    public ObjetReward(RewardType type, ItemStack stack, double amount) {
        super(type);
        item = stack;
        itemAmount = amount;
    }

    // Get the reward item.
    public ItemStack getItem() {
        return item;
    }

    // Set the reward item.
    public void setItem(ItemStack stack) {
        item = stack;
    }

    @Override
    public void obtain() { }

    @Override
    public double getValueModifier() {
        return itemAmount;
    }

    @Override
    public void setValueModifier(double i) {
        itemAmount = i;
    }
}
