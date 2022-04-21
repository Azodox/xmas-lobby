package fr.olten.xmas.rewards;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;

public class Reward implements Cancellable {
    private final RewardType type;
    private Callback rewardCallback;

    // Create a simple reward.
    public Reward(RewardType type) {
        this.type = type;
        this.rewardCallback = null;
    }

    // Create a reward with a callback.
    public Reward(RewardType type, Callback rewardCallback) {
        this.type = type;
        this.rewardCallback = rewardCallback;
    }

    public RewardType getType() { return type; }

    @Override
    public boolean isCancelled() { return false; }

    @Override
    public void setCancelled(boolean cancel) {}

    public Callback getRewardCallback() { return rewardCallback; }

    public void setCallback(Callback rewardCallback) { this.rewardCallback = rewardCallback; }

    // Execute the reward callback.
    public void callback() {
        if (rewardCallback != null) {
            // Execute method given as callback.
            rewardCallback.call();
            // Notify that the call back is executed.
            Bukkit.getLogger().info("Executing callback for reward of type" + type.toString());
        }
    }

    public enum RewardType {
        MONEY,ITEM,XP,LEVEL,POINTS,PERMISSION,COMMAND,RANK
    }

}
