package com.bedrockcloud.cloudbridge;

import com.bedrockcloud.cloudbridge.network.packets.*;
import com.bedrockcloud.cloudbridge.config.Config;

import java.util.Random;
import com.bedrockcloud.cloudbridge.network.handler.PacketHandler;
import com.bedrockcloud.cloudbridge.network.NetworkManager;

public class CloudBridge
{
    private static NetworkManager networkManager;
    private static PacketHandler packetHandler;
    private static int port;
    private String serverName;
    
    public CloudBridge() {
        final Random r = new Random();
        final int low = 30000;
        final int high = 40000;
        CloudBridge.port = r.nextInt(high - low) + low;
        CloudBridge.packetHandler = new PacketHandler();
        registerPacket();
        CloudBridge.networkManager = new NetworkManager(CloudBridge.port);
        final ProxyServerConnectPacket packet = new ProxyServerConnectPacket();
        packet.pushPacket();
        final Config config = new Config("./cloud.yml", 2);
        this.serverName = (String)config.get("name");
    }
    
    public static void registerPacket() {
        getPacketHandler().registerPacket("ProxyServerConnectPacket", ProxyServerConnectPacket.class);
        getPacketHandler().registerPacket("ProxyServerDisconnectPacket", ProxyServerDisconnectPacket.class);
        getPacketHandler().registerPacket("RegisterServerPacket", RegisterServerPacket.class);
        getPacketHandler().registerPacket("UnregisterServerPacket", UnregisterServerPacket.class);
        getPacketHandler().registerPacket("ProxyPlayerJoinPacket", ProxyPlayerJoinPacket.class);
        getPacketHandler().registerPacket("ProxyPlayerQuitPacket", ProxyPlayerQuitPacket.class);
        getPacketHandler().registerPacket("PlayerTextPacket", PlayerTextPacket.class);
        getPacketHandler().registerPacket("CloudPlayerAddPermissionPacket", CloudPlayerAddPermissionPacket.class);
        getPacketHandler().registerPacket("CloudNotifyMessagePacket", CloudNotifyMessagePacket.class);
        getPacketHandler().registerPacket("PlayerMovePacket", PlayerMovePacket.class);
        getPacketHandler().registerPacket("StartServerPacket", StartServerPacket.class);
        getPacketHandler().registerPacket("PlayerKickPacket", PlayerKickPacket.class);
        getPacketHandler().registerPacket("CloudPlayerChangeServerPacket", CloudPlayerChangeServerPacket.class);
        getPacketHandler().registerPacket("SendToHubPacket", SendToHubPacket.class);
    }
    
    public static PacketHandler getPacketHandler() {
        return CloudBridge.packetHandler;
    }
    
    public static NetworkManager getNetworkManager() {
        return CloudBridge.networkManager;
    }
    
    public static int getSocketPort() {
        return CloudBridge.port;
    }
    
    public String getServerName() {
        return this.serverName;
    }
}
