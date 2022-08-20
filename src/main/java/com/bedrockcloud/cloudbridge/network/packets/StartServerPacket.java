package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.cloudbridge.network.DataPacket;

public class StartServerPacket extends DataPacket
{
    public String groupName;
    public int count;
    
    @Override
    public String getPacketName() {
        return "StartServerPacket";
    }
    
    @Override
    public String encode() {
        this.addValue("groupName", this.groupName);
        this.addValue("count", this.count);
        return super.encode();
    }
}
