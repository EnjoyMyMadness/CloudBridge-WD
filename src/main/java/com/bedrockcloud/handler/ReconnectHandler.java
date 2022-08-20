package com.bedrockcloud.handler;

import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.utils.types.IReconnectHandler;

public class ReconnectHandler implements IReconnectHandler
{
    private final JoinHandler joinHandler;
    
    public ReconnectHandler(final JoinHandler joinHandler) {
        this.joinHandler = joinHandler;
    }
    
    public ServerInfo getFallbackServer(final ProxiedPlayer player, final ServerInfo serverInfo, final String reason) {
        return this.joinHandler.determineServer(player, serverInfo);
    }
}
