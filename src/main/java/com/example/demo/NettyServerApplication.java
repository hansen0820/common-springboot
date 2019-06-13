package com.example.demo;

import com.example.demo.listener.NettyServerListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner{

	@Resource
	private NettyServerListener nettyServerListener;

	public static void main(String[] args) {
		SpringApplication.run(NettyServerApplication.class, args);
	}

	@Override
	public void run(String... strings){
		nettyServerListener.start();
	}

}
