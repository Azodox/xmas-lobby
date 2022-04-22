package fr.olten.xmas.roulette.keys;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.util.RGBLike;

import java.awt.*;

public enum KeyRarity {
    COMMON(Component.text("Commun").color(NamedTextColor.DARK_GRAY)),
    UNCOMMON(Component.text("Peu commun").color(NamedTextColor.GRAY)),
    RARE(Component.text("Rare").color(NamedTextColor.BLUE)),
    EPIC(Component.text("Épique").color(NamedTextColor.GOLD)),
    LEGENDARY(Component.text("Légendaire").color(TextColor.fromHexString("#ffb700")).decorate(TextDecoration.BOLD)),
    MYTHICAL(Component.text("MYTHIQUE").color(TextColor.fromHexString("#dc78fa")).decorate(TextDecoration.BOLD));

    private final Component component;

    KeyRarity(Component component) {
        this.component = component;
    }

    public Component getComponentValue() {
        return component;
    }
}
