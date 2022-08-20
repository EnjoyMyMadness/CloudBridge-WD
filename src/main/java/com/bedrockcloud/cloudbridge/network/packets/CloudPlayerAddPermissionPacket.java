package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class CloudPlayerAddPermissionPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "CloudPlayerAddPermissionPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String permission = jsonObject.get("permission").toString();
        ProxyServer.getInstance().getPlayer(playerName).addPermission(permission);
    }
}
