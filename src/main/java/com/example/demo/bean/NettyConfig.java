package com.example.demo.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取yml配置文件中的信息
 * Created by hs
 */
@Component
@ConfigurationProperties(prefix = "netty")
public class NettyConfig {
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
