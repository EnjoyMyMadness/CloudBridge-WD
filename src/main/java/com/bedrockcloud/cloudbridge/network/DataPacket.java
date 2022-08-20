package com.bedrockcloud.cloudbridge.network;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.json.simple.JSONValue;
import com.bedrockcloud.cloudbridge.config.Config;
import com.bedrockcloud.cloudbridge.network.client.ClientRequest;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class DataPacket
{
    public Map<String, Object> data;
    public String packetName;
    
    public DataPacket() {
        this.data = new HashMap<String, Object>();
    }
    
    public String getPacketName() {
        return this.packetName;
    }
    
    public void addValue(final String key, final String value) {
        this.data.put(key, value);
    }
    
    public void addValue(final String key, final int value) {
        this.data.put(key, value);
    }
    
    public void handle(final JSONObject jsonObject, final ClientRequest clientRequest) {
    }
    
    public String encode() {
        this.addValue("packetName", this.getPacketName());
        final Config config = new Config("./cloud.yml", 2);
        this.addValue("serverName", config.get("name", "PROXY_SERVER_NAME"));
        return JSONValue.toJSONString(this.data);
    }
    
    public void pushPacket() {
        Socket s = null;
        try {
            s = new Socket("127.0.0.1", 32323);
            if (s.isClosed()) {
                return;
            }
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            bufferedWriter.write(this.encode());
            bufferedWriter.newLine();
            bufferedWriter.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
