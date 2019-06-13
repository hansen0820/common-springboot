package com.example.demo.listener;

import com.example.demo.Adapter.ServerChannelHandlerAdapter;
import com.example.demo.bean.NettyConfig;
import com.example.demo.bean.NettyConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * 服务启动监听器
 *
 * @author hs
 */
@Component
public class NettyServerListener {
    /**
     * NettyServerListener 日志输出器
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyServerListener.class);

    /**
     * 创建bootstrap
     */
    ServerBootstrap serverBootstrap = new ServerBootstrap();

    /**
     * BOSS
     */
    EventLoopGroup boss = new NioEventLoopGroup();

    /**
     * Worker
     */
    EventLoopGroup work = new NioEventLoopGroup();

    /**
     * 通道适配器
     */
    @Resource
    private ServerChannelHandlerAdapter channelHandlerAdapter;

    /**
     * NETTY服务器配置类
     */
    @Resource
    private NettyConfig nettyConfig;

    @Resource
    private NettyConstant nettyConstant;

    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        logger.info("关闭服务器....");
        // 优雅退出
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }

    /**
     * 开启及服务线程
     */
    public void start() {
        // 从配置文件中(application.yml)获取服务端监听端口号
        int port = nettyConfig.getPort();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO));
        try {
            //设置事件处理
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(nettyConstant.getMaxFrameLength()
                            , 0, 2, 0, 2));
                    pipeline.addLast(new LengthFieldPrepender(2));
                    //pipeline.addLast(new ObjectCodec());

                    pipeline.addLast(channelHandlerAdapter);
                }
            });
            logger.info("netty服务器在[{}]端口启动监听", port);
            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info("[出现异常] 释放资源");
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

}

