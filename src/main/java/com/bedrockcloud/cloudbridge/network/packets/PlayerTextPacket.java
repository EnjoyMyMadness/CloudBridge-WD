package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class PlayerTextPacket extends DataPacket {
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_POPUP = 2;
    public static final int TYPE_TIP = 3;
    public static final int TYPE_TOAST = 4;

    @Override
    public String getPacketName() {
        return "PlayerTextPacket";
    }

    @Override
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
        final String playerName = jsonObject.get("playerName").toString();
        final String typeS = jsonObject.get("type").toString();
        final int type = Integer.parseInt(typeS);
        final String value2 = jsonObject.get("value").toString();
        final String msg = value2.replace("&", "§");
        final String value3 = msg.replace("\u00c2", "");
        final ProxiedPlayer proxiedPlayer = ProxyServer.getInstance().getPlayer(playerName);
        if (proxiedPlayer != null) {
            if (type == 0) {
                proxiedPlayer.sendMessage(value3);
            } else if (type == 1) {
                proxiedPlayer.sendTitle(value3);
            } else if (type == 2) {
                proxiedPlayer.sendPopup(value3, " BedrockCloud");
            } else if (type == 3) {
                proxiedPlayer.sendTip(value3);
            } else if (type == 4) {
            }
        } else if (playerName.equals("all.players")) {
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers().values()) {
                if (type == 0) {
                    p.sendMessage(value3);
                } else if (type == 1) {
                    p.sendTitle(value3);
                } else if (type == 2) {
                    p.sendPopup(value3, " BedrockCloud");
                } else if (type == 3) {
                    p.sendTip(value3);
                } else if (type == 4) {
                }
            }
        } else if (playerName.equals("team.members")) {
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers().values()) {
                if (p.hasPermission("cloud.notify")) {
                    if (type == 0) {
                        p.sendMessage(value3);
                    } else if (type == 1) {
                        p.sendTitle(value3);
                    } else if (type == 2) {
                        p.sendPopup(value3, " BedrockCloud");
                    } else if (type == 3) {
                        p.sendTip(value3);
                    } else if (type == 4) {
                    }
                }
            }
        }
    }
}