package com.bedrockcloud.player;

import com.bedrockcloud.cloudbridge.network.packets.ProxyPlayerQuitPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.InitialServerDeterminationEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.network.session.LoginData;
import dev.waterdog.waterdogpe.network.upstream.ConnectedUpstreamHandler;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.utils.types.TextContainer;
import dev.waterdog.waterdogpe.utils.types.TranslationContainer;

import java.util.Locale;

public class CustomPlayer extends ProxiedPlayer {
    public CustomPlayer(ProxyServer proxy, BedrockServerSession session, LoginData loginData) {
        super(proxy, session, loginData);
    }

    @Override
    public void disconnect() {
        super.disconnect();
        final ProxyPlayerQuitPacket newpacket = new ProxyPlayerQuitPacket();
        newpacket.playerName = this.getName().toLowerCase(Locale.ROOT).replace(" ", "_");
        try {
            newpacket.leftServer = this.getServerInfo().getServerName();
        } catch (NullPointerException e) {
            newpacket.leftServer = "NOT FOUND";
        }
        newpacket.pushPacket();
    }
}
