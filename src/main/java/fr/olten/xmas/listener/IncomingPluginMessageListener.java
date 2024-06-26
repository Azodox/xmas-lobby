package fr.olten.xmas.listener;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.olten.xmas.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class IncomingPluginMessageListener implements PluginMessageListener {

    private final Lobby lobby;
    public IncomingPluginMessageListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if(channel.equals("xmas:lobbysurvie")){
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subChannel = in.readUTF();

            switch (subChannel) {
                case "Incoming" -> {
                    var uuid = UUID.fromString(in.readUTF());
                    lobby.getPlayerManager().joiningPlayerFromSurvival(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
                }

                case "UnableToConnectTo" -> {
                    var serverName = in.readUTF();
                    var uuid = UUID.fromString(in.readUTF());
                    if(serverName.equals(lobby.getConfig().getString("serversName.survival"))) {
                        lobby.getPlayerManager().unableToConnectTo(Preconditions.checkNotNull(Bukkit.getPlayer(uuid), "Player not found"));
                    }
                }
            }
        }
    }
}
