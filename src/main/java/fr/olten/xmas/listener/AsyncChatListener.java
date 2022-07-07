package fr.olten.xmas.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Azodox_ (Luke)
 * 7/7/2022.
 */

public class AsyncChatListener implements Listener {

    private final AccountSystem accountSystem;

    public AsyncChatListener(AccountSystem accountSystem) {
        this.accountSystem = accountSystem;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event){
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            var accountManager = new AccountManager(accountSystem, source);
            var rank = accountManager.newRankManager().getMajorRank();
            return rank.getPrefix().append(sourceDisplayName.color(rank.getColor()))
                    .append(Component.text(" : ").color(NamedTextColor.GRAY))
                    .append(message);
        });
    }
}
