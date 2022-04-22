package fr.olten.xmas.rewards;

import fr.olten.xmas.roulette.keys.KeyTypes;

import java.util.Set;

public class KeyReward extends Reward {

    private KeyTypes keytype;

    public KeyReward(RewardType type, KeyTypes keyType) {
        super(type);
        this.keytype = keyType;
    }

    public Set<Reward> getRewards() {
        return this.keytype.getRewards();
    }
}
