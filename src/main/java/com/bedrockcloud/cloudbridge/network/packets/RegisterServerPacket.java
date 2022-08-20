package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.BedrockCore;
import com.bedrockcloud.manager.ServerManager;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class RegisterServerPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "RegisterServerPacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String serverName = jsonObject.get("serverName").toString();
        final String port = jsonObject.get("serverPort").toString();
        ServerManager.unregisterServer(serverName);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServerManager.registerServer(serverName, "127.0.0.1", Integer.parseInt(port));
        if (serverName.contains("Lobby")) {
            BedrockCore.getInstance().joinHandler.addDefault(ProxyServer.getInstance().getServerInfo(serverName));
        }
    }
}
