package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.cloudbridge.network.DataPacket;

public class ProxyPlayerQuitPacket extends DataPacket
{
    public String playerName;
    public String leftServer;
    
    @Override
    public String getPacketName() {
        return "ProxyPlayerQuitPacket";
    }
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("leftServer", this.leftServer);
        return super.encode();
    }
}
