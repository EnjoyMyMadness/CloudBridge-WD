package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class PlayerMovePacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "PlayerMovePacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("toServer").toString();
        final String playerName = jsonObject.get("playerName").toString();
        if (ProxyServer.getInstance().getServerInfo(serverName) == null) {
            ProxyServer.getInstance().getLogger().info("This service don't exists");
        } else if (ProxyServer.getInstance().getPlayer(playerName) != null) {
            ProxyServer.getInstance().getPlayer(playerName).connect(ProxyServer.getInstance().getServerInfo(serverName));
        }
    }
}
