package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.event.Event;
import dev.waterdog.waterdogpe.event.defaults.InitialServerDeterminationEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.BedrockCore;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class UnregisterServerPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "UnregisterServerPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);

        if (server != null) {
            if (!server.getServerName().equals("Lobby")) {
                ProxyServer.getInstance().getServerInfoMap().remove(serverName);
            } else {
                ProxyServer.getInstance().getServerInfoMap().remove(serverName);
                BedrockCore.getInstance().joinHandler.removeDefault(server);
                ProxyServer.getInstance().getConfiguration().getPriorities().remove(serverName);
            }
        }

        for (final ProxiedPlayer player : server.getPlayers()) {
            final ServerInfo initialServer = BedrockCore.getInstance().getProxy().getJoinHandler().determineServer(player);
            if (initialServer == null) {
                player.disconnect(
                        "§eStimoCloud" +
                                "\n" +
                                "§cNo free Cloud-Server found."
                );
                if (player.getServerInfo().getServerName().equals("Lobby") && serverName.equals("Lobby")){
                    player.disconnect(
                            "§eStimoCloud" +
                                    "\n" +
                                    "§cLobbyServer was stopped."
                    );
                }
            } else {
                final InitialServerDeterminationEvent serverEvent = new InitialServerDeterminationEvent(player, initialServer);
                BedrockCore.getInstance().getProxy().getEventManager().callEvent((Event)serverEvent);
                player.connect(initialServer);
            }
        }
    }
}