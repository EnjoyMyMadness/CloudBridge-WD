package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.BedrockCore;
import com.bedrockcloud.cloudbridge.config.Config;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

import java.io.File;

public class CloudNotifyMessagePacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "CloudNotifyMessagePacket";
    }
    
    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String message = jsonObject.get("message").toString();
        final String msg = message.replace("&", "§");
        final String fixuni = msg.replace("\u00c2", "");
        for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers().values()) {
            if ((new File(BedrockCore.getInstance().getCloudPath() + "local/notify/" + p.getName() + ".json").exists())) {
                final Config config = new Config(BedrockCore.getInstance().getCloudPath() + "local/notify/" + p.getName() + ".json", Config.JSON);
                if (config.getBoolean("notify", false)) {
                    p.sendMessage(fixuni);
                }
            }
        }
    }
}
