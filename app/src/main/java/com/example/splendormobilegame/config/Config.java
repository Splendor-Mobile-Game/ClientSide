package com.example.splendormobilegame.config;
import android.content.Context;
import com.example.splendormobilegame.config.exceptions.InvalidConfigException;
import java.io.InputStream;
import java.util.Properties;

public class Config implements IConfig {
    private String serverip;
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
        this.serverip = properties.getProperty("server.ip");
    }
    //get server ip value form config.properites
    @Override
    public String getServerIp() throws InvalidConfigException{
        loadFile();
        return this.serverip;
    }
}