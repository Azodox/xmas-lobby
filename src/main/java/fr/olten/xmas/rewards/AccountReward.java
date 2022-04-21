package fr.olten.xmas.rewards;

public class AccountReward extends Reward implements IObtainableString{
    private String accountRewardString;

    public AccountReward(RewardType type, String accountRewardString) {
        super(type);
        this.accountRewardString = accountRewardString;
    }

    @Override
    public void obtain() {
        //TODO
    }

    @Override
    public String getString() {
        return accountRewardString;
    }

    @Override
    public void setString(String s) {
        this.accountRewardString = s;
    }
}
