package fr.olten.xmas.achievement;

import net.kyori.adventure.text.Component;
import net.valneas.account.AccountSystem;
import org.bukkit.Bukkit;

import java.util.UUID;

public abstract class SimpleAchievement implements Achievement {

    private final Component name;
    private final Component description;
    private final double earnedPoints;

    public SimpleAchievement(Component name, Component description, double earnedPoints) {
        this.name = name;
        this.description = description;
        this.earnedPoints = earnedPoints;
    }

    @Override
    public Component getName() {
        return this.name;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public double getEarnedPoints() {
        return this.earnedPoints;
    }

    protected void complete(UUID uuid){
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);

        if(provider != null){
            var accountSystem = provider.getProvider();
        }
    }
}
