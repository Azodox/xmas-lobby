package fr.olten.xmas.roulette.keys;

import fr.olten.xmas.rewards.Reward;

import java.util.Set;

public enum KeyTypes {
    OLTEN_KEY(KeyRarity.LEGENDARY),
    VALNEAS_KEY(KeyRarity.EPIC),
    ELEMENTARY_KEY(KeyRarity.EPIC),
    ELGARDIAH_KEY(KeyRarity.RARE),
    RAYNALD_KEY(KeyRarity.MYTHICAL),
    LUKE_KEY(KeyRarity.UNCOMMON),
    MICHEL_KEY(KeyRarity.LEGENDARY),
    MATT_KEY(KeyRarity.UNCOMMON),
    XMAS_KEY(KeyRarity.COMMON);

    private final KeyRarity rarity;
    private Set<Reward> rewards;

    KeyTypes(KeyRarity rarity) {
        this.rarity = rarity;
    }

    KeyTypes(KeyRarity rarity, Set<Reward> rewards) {
        this.rarity = rarity;
        this.rewards = rewards;
    }

    public KeyRarity getRarity() {
        return rarity;
    }

    public Set<Reward> getRewards() {
        return rewards;
    }

    public void setRewards(Set<Reward> rewards) {
        this.rewards = rewards;
    }

    public void addReward(Reward reward) {
        this.rewards.add(reward);
    }

    public void removeReward(Reward reward) {
        this.rewards.remove(reward);
    }
}