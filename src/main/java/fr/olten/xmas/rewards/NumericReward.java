package fr.olten.xmas.rewards;


public class NumericReward extends Reward implements IObtainableNumeric {

    private double valueModifier;

    public NumericReward(RewardType type, double valueModifier) {
        super(type);
        this.valueModifier = valueModifier;
    }

    public NumericReward(RewardType type, Callback callback, double valueModifier) {
        super(type,callback);
        this.valueModifier = valueModifier;
    }

    @Override
    public void obtain() {
       //TODO
    }

    @Override
    public double getValueModifier() {

        return this.valueModifier;
    }

    @Override
    public void setValueModifier(double value) {
        this.valueModifier = value;
    }
}
