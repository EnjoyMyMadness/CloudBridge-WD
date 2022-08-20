package com.bedrockcloud.cloudbridge.network.client;

import com.bedrockcloud.cloudbridge.CloudBridge;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientRequest extends Thread
{
    private final Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    
    public ClientRequest(final Socket socket) {
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.start();
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    @Override
    public void run() {
        while (!this.socket.isClosed()) {
            String line = null;
            try {
                line = this.dataInputStream.readLine();
                if (line == null) {
                    break;
                }
                CloudBridge.getPacketHandler().handleCloudPacket(CloudBridge.getPacketHandler().handleJsonObject(CloudBridge.getPacketHandler().getPacketNameByRequest(line), line), this);
            } catch (IOException e) {
                for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers().values()){
                    p.disconnect("§cProxy Restart");
                }
                ProxyServer.getInstance().shutdown();
                System.exit(0);
            }
        }
    }
}
