package fr.olten.xmas.listener.rank;

import fr.olten.xmas.manager.TeamNameTagManager;
import net.valneas.account.AccountManager;
import net.valneas.account.events.rank.MajorRankChangedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
public class RankChangedListener implements Listener {

    @EventHandler
    public void onRankChange(MajorRankChangedEvent event) {
        TeamNameTagManager.update((AccountManager) event.getAccount());
    }
}
