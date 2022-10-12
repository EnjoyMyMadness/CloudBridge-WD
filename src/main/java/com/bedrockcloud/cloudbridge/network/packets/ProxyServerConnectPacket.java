package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.cloudbridge.CloudBridge;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class ProxyServerConnectPacket extends DataPacket
{
    @Override
    public String getPacketName() {
        return "ProxyServerConnectPacket";
    }
    
    @Override
    public String encode() {
        this.addValue("socketPort", CloudBridge.getSocketPort());
        this.addValue("proxyPid", Math.toIntExact(ProcessHandle.current().pid()));
        return super.encode();
    }
}
