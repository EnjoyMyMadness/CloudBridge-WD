package com.bedrockcloud.manager;

import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import java.net.InetSocketAddress;
import dev.waterdog.waterdogpe.ProxyServer;

public class ServerManager
{
    public static void registerServer(final String servername, final String ip, final int port) {
        ProxyServer.getInstance().registerServerInfo((ServerInfo)new BedrockServerInfo(servername, new InetSocketAddress(ip, port), (InetSocketAddress)null));
        ProxyServer.getInstance().getLogger().info("§8[§2+§8] §r" + servername);
    }
    
    public static void unregisterServer(final String servername) {
        if (ProxyServer.getInstance().getServerInfo(servername) != null) {
            ProxyServer.getInstance().removeServerInfo(servername);
            ProxyServer.getInstance().getLogger().info("§8[§4-§8] §r" + servername);
        }
    }
}
