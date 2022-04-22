package fr.olten.xmas.rewards;

import fr.olten.xmas.Lobby;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.rank.Rank;
import net.valneas.account.rank.RankUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.Set;


public class RewardHandler {

    private static Lobby main;
    public RewardHandler(Lobby main) { RewardHandler.main = main;}

    // Receive specific reward.
    public static void receiveReward(Player player, Reward reward) {
        switch (reward.getType()) {
            case MONEY -> receiveMoney(player, ((NumericReward) reward).getValueModifier());
            case XP -> receiveXp(player, ((NumericReward) reward).getValueModifier());
            case LEVEL -> receiveLevel(player, ((NumericReward) reward).getValueModifier());
            case ITEM -> receiveItem(player, ((ObjetReward) reward).getItem(), (int) ((ObjetReward) reward).getValueModifier());
            case POINTS -> receivePoints(player, ((NumericReward) reward).getValueModifier());
            case PERMISSION -> receivePermission(player, ((AccountReward) reward).getString());
            case COMMAND -> executeCommand(player, ((AccountReward) reward).getString());
            case RANK -> receiveRank(player, ((AccountReward) reward).getString());
            case KEYREWARD -> receiveKeyReward(player, ((KeyReward) reward));
            //case MAJOR_RANK -> main.getRewardHandler().changeMajorRank(player, ((AccountReward) reward).getString());
        }

        // Call callback in case there is one.
        reward.callback();
    }

    // Receive multiple rewards.
    public static void receiveRewards(Player player, Set<Reward> rewards) {
        rewards.stream().forEach((reward) -> {
            receiveReward(player, reward);
        });
    }

    // Receive KeyReward as reward.
    public static void receiveKeyReward(Player player, KeyReward reward) {
        Reward r = reward.getRewards().stream().skip(new Random().nextInt(reward.getRewards().size())).findFirst().orElse(null);
        if(r == null) return;

        switch(r.getType()) {
            case MONEY -> receiveMoney(player, ((NumericReward) r).getValueModifier());
            case XP -> receiveXp(player, ((NumericReward) r).getValueModifier());
            case LEVEL -> receiveLevel(player, ((NumericReward) r).getValueModifier());
            case ITEM -> receiveItem(player, ((ObjetReward) r).getItem(), (int) ((ObjetReward) r).getValueModifier());
            case POINTS -> receivePoints(player, ((NumericReward) r).getValueModifier());
            case PERMISSION -> receivePermission(player, ((AccountReward) r).getString());
            case COMMAND -> executeCommand(player, ((AccountReward) r).getString());
            case RANK -> receiveRank(player, ((AccountReward) r).getString());
        }

        // Call callback in case there is one.
        r.callback();
    }

    // Receive Money as reward.
    private static void receiveMoney(Player player, double amount) {
        player.sendMessage("You received " + amount + " money");
    }

    // Receive Item as reward.
    private static void receiveItem(Player player, ItemStack itemStack, int amount) {
        //TODO : Make it so that the player receives the item in the xmas server.

        // If amount is smaller than 0 , we don't reward the player.
        if(amount < 0) {
            Bukkit.getLogger().severe("Item : " + amount + " is under 0, not rewarded.");
            return;
        }
        itemStack.setAmount(amount);
        player.getInventory().addItem(itemStack);
    }

    // Receive Command as reward.
    private static void executeCommand(Player player, String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("/", "")
                                                                 .replace("{player_name}", player.getName())
                                                                 .replace("{default_world}", "world")
                                                                 .replace("{player_uuid}", player.getUniqueId().toString()));
    }

    // Receive Xp as reward.
    private static void receiveXp(Player player, double amount) {
        if(amount > 0) {
            var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
            if(provider != null){
                var accountSystem = provider.getProvider();
                final AccountManager accountManager = new AccountManager(accountSystem, player);
                accountManager.set("xp",(int)accountManager.get("xp") + amount);
            }
            return;
        }
        Bukkit.getLogger().severe("XP : " + amount + " is under 0, not rewarded.");
    }

    // Receive Level as reward.
    private static void receiveLevel(Player player, double amount) {
        if(amount > 0) {
            var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
            if(provider != null){
                var accountSystem = provider.getProvider();
                final AccountManager accountManager = new AccountManager(accountSystem, player);
                accountManager.set("level",(int)accountManager.get("level") + amount);
            }
            return;
        }
        Bukkit.getLogger().severe("Level : " + amount + " is under 0, not rewarded.");
    }

    // Receive Permission as reward.
    private static void receivePermission(Player player, String permission) {
        if(permission != null) {
            var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
            if(provider != null){
                var accountSystem = provider.getProvider();
                accountSystem.getPermissionDispatcher().set(player, permission);
            }
            return;
        }
        Bukkit.getLogger().severe("The given permission is null, not rewarded.");
    }

    // Receive Points as reward.
    private static void receivePoints(Player player, double points) {
        if(points > 0) {
            var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
            if(provider != null){
                var accountSystem = provider.getProvider();
                final AccountManager accountManager = new AccountManager(accountSystem, player);
                accountManager.set("points",(int)accountManager.get("points") + points);
            }
            return;
        }
        Bukkit.getLogger().severe("Points : " + points + " is under 0, not rewarded.");
    }

    // Receive Rank as reward.
    private static void receiveRank(Player player, String rank) {
        if(rank != null) {
            var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
            if(provider != null){
                var accountSystem = provider.getProvider();
                final AccountManager accountManager = new AccountManager(accountSystem, player);
                final Rank rankManager = accountManager.newRankManager();
                if(rankManager.getRanks().contains(RankUnit.getByName(rank))) {
                    rankManager.addRank(RankUnit.getByName(rank));
                    return;
                }
                Bukkit.getLogger().severe("Given rank is not recognized, not rewarded.");
            }
            return;
        }
        Bukkit.getLogger().severe("Given rank is null, not rewarded.");
    }
}