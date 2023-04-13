package com.example.splendormobilegame.config;

import com.example.splendormobilegame.config.exceptions.InvalidConfigException;

public interface IConfig{

    public String getServerIp() throws InvalidConfigException;
    public int getConnectionTimeoutMs() throws InvalidConfigException;
    public int getReadTimeoutMs() throws InvalidConfigException;
    public int getAutomaticReconnectionMs() throws InvalidConfigException;

}