package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.cloudbridge.network.DataPacket;

public class CloudPlayerChangeServerPacket extends DataPacket
{
    public String playerName;
    public String server;
    
    @Override
    public String getPacketName() {
        return "CloudPlayerChangeServerPacket";
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("server", this.server);
        return super.encode();
    }
}
