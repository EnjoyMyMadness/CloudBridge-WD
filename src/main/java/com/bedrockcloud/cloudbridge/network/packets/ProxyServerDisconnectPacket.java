package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class ProxyServerDisconnectPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "ProxyServerDisconnectPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        ProxyServer.getInstance().shutdown();
    }
}
