package com.example.splendormobilegame.config;
import android.content.Context;
import com.example.splendormobilegame.config.exceptions.InvalidConfigException;
import java.io.InputStream;
import java.util.Properties;

public class Config implements IConfig {
    private String serverIp;
    private int connectionTimeoutMs;
    private int readTimeoutMs;
    private int automaticReconnectionMs;


    private Context context;
    public Config(Context context)
    {
        this.context = context;
    }
    //loading file config.properties
    private void loadFile() throws InvalidConfigException {
        Properties properties = new Properties();
        try {
            InputStream inputStream = context.getAssets().open("config.properties");
            properties.load(inputStream);
        } catch (Exception e) {
            throw new InvalidConfigException("Couldn't load config.properties file");
        }
    // Access the properties
        this.serverIp = properties.getProperty("ws.server.ip");
        this.automaticReconnectionMs = Integer.parseInt(properties.getProperty("ws.automatic.reconnection.ms"));
        this.connectionTimeoutMs = Integer.parseInt(properties.getProperty("ws.connection.timeout.ms"));
        this.readTimeoutMs = Integer.parseInt(properties.getProperty("ws.read.timeout.ms"));

    }
    //get server ip value form config.properites
    @Override
    public String getServerIp() throws InvalidConfigException{
        loadFile();
        return this.serverIp;
    }

    @Override
    public int getConnectionTimeoutMs() throws InvalidConfigException {
        return this.connectionTimeoutMs;
    }

    @Override
    public int getReadTimeoutMs() throws InvalidConfigException {
        return this.readTimeoutMs;
    }

    @Override
    public int getAutomaticReconnectionMs() throws InvalidConfigException {
        return this.automaticReconnectionMs;
    }
}