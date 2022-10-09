package com.bedrockcloud;

import com.bedrockcloud.commands.HubCommand;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import com.bedrockcloud.cloudbridge.network.packets.ProxyPlayerJoinPacket;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import com.bedrockcloud.cloudbridge.network.packets.CloudPlayerChangeServerPacket;
import com.bedrockcloud.cloudbridge.network.packets.ProxyPlayerQuitPacket;
import com.bedrockcloud.cloudbridge.network.packets.ProxyServerDisconnectPacket;
import dev.waterdog.waterdogpe.utils.config.YamlConfig;
import dev.waterdog.waterdogpe.utils.types.IReconnectHandler;
import com.bedrockcloud.handler.ReconnectHandler;
import dev.waterdog.waterdogpe.utils.types.IJoinHandler;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.PreTransferEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectEvent;
import com.bedrockcloud.handler.JoinHandler;
import com.bedrockcloud.cloudbridge.CloudBridge;
import dev.waterdog.waterdogpe.plugin.Plugin;

import java.io.File;
import java.util.Locale;

public class BedrockCore extends Plugin
{
    private static BedrockCore instance;
    private CloudBridge cloudBridge;
    public JoinHandler joinHandler;
    
    public void onStartup() {
        BedrockCore.instance = this;
    }
    
    public void onEnable() {
        this.cloudBridge = new CloudBridge();
        this.getProxy().getEventManager().subscribe(PlayerDisconnectEvent.class, this::onPlayerDisconnectListener);
        this.getProxy().getEventManager().subscribe(PreTransferEvent.class, this::listen);
        this.getProxy().getEventManager().subscribe(PlayerLoginEvent.class, this::onPlayerJoin);
        getInstance().getProxy().setJoinHandler((IJoinHandler)(this.joinHandler = new JoinHandler()));
        getInstance().getProxy().setReconnectHandler((IReconnectHandler)new ReconnectHandler(this.joinHandler));
        this.getProxy().getCommandMap().registerCommand(new HubCommand(this.joinHandler));

        final ServerInfo server = ProxyServer.getInstance().getServerInfo("lobby1");
        if (server != null){
            ProxyServer.getInstance().getServerInfoMap().remove("lobby1");
            ProxyServer.getInstance().getConfiguration().getPriorities().remove("lobby1");
        }
    }

    public JoinHandler getJoinHandler() {
        return joinHandler;
    }

    public void onDisable() {
        final ProxyServerDisconnectPacket packet = new ProxyServerDisconnectPacket();
        packet.addValue("serverName", this.cloudBridge.getServerName());
        packet.pushPacket();
    }
    
    public void onPlayerDisconnectListener(PlayerDisconnectEvent event) {
        final ProxyPlayerQuitPacket newpacket = new ProxyPlayerQuitPacket();
        newpacket.playerName = event.getPlayer().getName().toLowerCase(Locale.ROOT).replace(" ", "_");
        try {
            newpacket.leftServer = event.getPlayer().getServerInfo().getServerName();
        } catch (NullPointerException e) {
            newpacket.leftServer = "NOT FOUND";
        }
        newpacket.pushPacket();
    }
    
    private void listen(final PreTransferEvent event) {
        final ServerInfo server = event.getTargetServer();
        if (server == JoinHandler.EMPTY_SERVER_INFO) {
            event.setCancelled(true);
            return;
        }
        final ServerInfo oldServer = event.getPlayer().getServerInfo();
        if (oldServer != null) {
            final CloudPlayerChangeServerPacket packet = new CloudPlayerChangeServerPacket();
            packet.playerName = event.getPlayer().getName().toLowerCase(Locale.ROOT).replace(" ", "_");
            packet.server = server.getServerName();
            packet.pushPacket();
        }
    }
    
    public void onPlayerJoin(final PlayerLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final ProxyPlayerJoinPacket packet = new ProxyPlayerJoinPacket();
        packet.playerName = player.getName().toLowerCase(Locale.ROOT).replace(" ", "_");
        packet.joinedServer = "Loading...";
        packet.address = String.valueOf(player.getAddress());
        packet.xuid = player.getXuid();
        packet.uuid = String.valueOf(player.getUniqueId());
        packet.pushPacket();
    }
    
    public static BedrockCore getInstance() {
        return BedrockCore.instance;
    }
    
    public CloudBridge getCloudBridge() {
        return this.cloudBridge;
    }

    public String getCloudPath() {
        return getProxyConfig().getString("cloud-path");
    }

    public YamlConfig getProxyConfig() {
        File configFile = new File(getProxy().getDataPath().toString() + "/config.yml");
        return new YamlConfig(configFile);
    }
}
