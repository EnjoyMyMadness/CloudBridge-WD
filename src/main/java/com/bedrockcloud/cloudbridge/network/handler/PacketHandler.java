package com.bedrockcloud.cloudbridge.network.handler;

import com.bedrockcloud.cloudbridge.network.DataPacket;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler
{
    public Map<String, Class> registeredPackets;
    
    public PacketHandler() {
        this.registeredPackets = new HashMap<String, Class>();
    }
    
    public void registerPacket(final String name, final Class packet) {
        this.registeredPackets.put(name, packet);
    }
    
    public void unregisterPacket(final String name) {
        this.registeredPackets.remove(name);
    }
    
    public boolean isPacketRegistered(final String name) {
        return this.registeredPackets.get(name) != null;
    }
    
    public Map<String, Class> getRegisteredPackets() {
        return this.registeredPackets;
    }
    
    public Class getPacketByName(final String packetName) {
        return this.registeredPackets.get(packetName);
    }
    
    public String getPacketNameByRequest(final String request) {
        final Object obj = JSONValue.parse(request);
        if (obj != null) {
            final JSONObject jsonObject = (JSONObject)obj;
            if (jsonObject.get("packetName") != null) {
                return jsonObject.get("packetName").toString();
            }
        }
        ProxyServer.getInstance().getLogger().warning("Handling of packet cancelled because the packet is unknown!");
        return "Unknown Packet";
    }
    
    public JSONObject handleJsonObject(final String packetName, final String input) {
        if (this.isPacketRegistered(packetName)) {
            final Object obj = JSONValue.parse(input);
            final JSONObject jsonObject = (JSONObject)obj;
            return jsonObject;
        }
        ProxyServer.getInstance().getLogger().warning("Denied unknown packet.");
        return new JSONObject();
    }
    
    public void handleCloudPacket(final JSONObject jsonObject, final ClientRequest clientRequest) {
        if (clientRequest.getSocket().getInetAddress().toString().equals("/127.0.0.1")) {
            if (jsonObject.get("packetName") != null) {
                final String packetName = jsonObject.get("packetName").toString();
                final Class c = this.getPacketByName(packetName);
                try {
                    final DataPacket packet = (DataPacket) c.newInstance();
                    packet.handle(jsonObject, clientRequest);
                } catch (InstantiationException | IllegalAccessException ex2) {
                    final ReflectiveOperationException ex;
                    ex2.printStackTrace();
                }
            } else {
                ProxyServer.getInstance().getLogger().warning("Denied unauthorized server.");
            }
        }
    }
}
