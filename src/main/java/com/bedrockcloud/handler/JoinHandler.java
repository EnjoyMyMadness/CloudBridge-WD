package com.bedrockcloud.handler;

import java.net.InetSocketAddress;

import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import java.util.List;
import com.bedrockcloud.cloudbridge.network.packets.CloudPlayerChangeServerPacket;
import java.util.Comparator;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import java.util.ArrayList;
import dev.waterdog.waterdogpe.utils.types.IJoinHandler;

public class JoinHandler implements IJoinHandler
{
    public final ArrayList<ServerInfo> defaults;
    public static ServerInfo EMPTY_SERVER_INFO;
    
    public JoinHandler() {
        this.defaults = new ArrayList<ServerInfo>();
    }
    
    public ServerInfo determineServer(final ProxiedPlayer player) {
        player.getProxy().getScheduler().scheduleAsync(() -> this.searchDefault(player, null));
        return JoinHandler.EMPTY_SERVER_INFO;
    }
    
    public ServerInfo determineServer(final ProxiedPlayer player, final ServerInfo oldServer) {
        player.getProxy().getScheduler().scheduleAsync(() -> this.searchDefault(player, oldServer));
        return JoinHandler.EMPTY_SERVER_INFO;
    }
    
    private void searchDefault(final ProxiedPlayer player, final ServerInfo oldServer) {
        final List<ServerInfo> localCopy = new ArrayList<ServerInfo>(this.defaults);
        localCopy.remove(oldServer);
        if (localCopy.size() == 0) {
            player.disconnect(
                    "§6BedrockCloud" +
                    "\n" +
                    "§cNo free Lobby Server found."
            );
            return;
        }
        localCopy.sort(Comparator.comparingInt(s -> {
            try {
                return Integer.parseInt(s.getServerName().replaceAll("[^\\d]", ""));
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
        }));
        int smallestInt = Integer.MAX_VALUE;
        ServerInfo smallest = null;
        for (final ServerInfo serverInfo : localCopy) {
            try {
                final int playerCount = serverInfo.getPlayers().size();
                if (playerCount >= smallestInt) {
                    continue;
                }
                smallestInt = playerCount;
                smallest = serverInfo;
            } catch (Throwable ignored) {}//This catch is ignored by default
        }
        if (smallest == null) {
            player.disconnect(
                    "§6BedrockCloud" +
                            "\n" +
                            "§cNo free Lobby Server found."
            );
        } else {
            smallest.addPlayer(player);
            player.sendMessage("§l§aYou will be transferred to §r§e" + smallest.getServerName() + "§8.");
            player.connect(smallest);
            final CloudPlayerChangeServerPacket packet = new CloudPlayerChangeServerPacket();
            packet.playerName = player.getName();
            packet.server = String.valueOf(smallest.getServerName());
            packet.pushPacket();
        }
    }
    
    public void addDefault(final ServerInfo serverInfo) {
        this.defaults.add(serverInfo);
    }
    
    public void removeDefault(final ServerInfo serverInfo) {
        this.defaults.remove(serverInfo);
    }
    
    public boolean isDefault(final ServerInfo serverInfo) {
        return this.defaults.contains(serverInfo);
    }
    
    static {
        EMPTY_SERVER_INFO = (ServerInfo)new BedrockServerInfo((String)null, (InetSocketAddress)null, (InetSocketAddress)null);
    }
}
