package com.bedrockcloud.cloudbridge.network;

import java.net.Socket;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import dev.waterdog.waterdogpe.ProxyServer;

import java.io.IOException;
import java.net.ServerSocket;

public class NetworkManager extends Thread {
    public ServerSocket serverSocket;

    public NetworkManager(final int Port) {
        try {
            this.serverSocket = new ServerSocket(Port);
        } catch (IOException e) {
            ProxyServer.getInstance().shutdown();
        }
        this.start();
    }

    @Override
    public void run() {
        this.starts();
        super.run();
    }

    public void starts() {
        while (true) {
            try {
                final Socket socket = this.serverSocket.accept();
                final ClientRequest clientRequest = new ClientRequest(socket);
            } catch (IOException e) {
                ProxyServer.getInstance().getLogger().error("", e);
            }
        }
    }
}
