package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class PlayerKickPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "PlayerKickPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String reason = jsonObject.get("reason").toString();
        final String playerName = jsonObject.get("playerName").toString();
        if (ProxyServer.getInstance().getPlayer(playerName) != null) {
            ProxyServer.getInstance().getScheduler().scheduleTask(() -> ProxyServer.getInstance().getPlayer(playerName).disconnect(reason.replace("&", "§")), false);
        }
    }
}
