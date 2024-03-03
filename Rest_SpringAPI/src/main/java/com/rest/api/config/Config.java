package com.rest.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blazesoft.server.local.NdLocalServerException;
import com.rest.api.server.Server;

@Configuration
public class Config {
	@Value("${server.filepath}")
	private String _SERVER_CONFIG;

	@Bean
	public Server server(String[] args) throws NdLocalServerException {

		System.out.println("Enter in Server");
		// Create the server
		String serverConfig = _SERVER_CONFIG;
		Server server = (Server) Server.createServer(serverConfig);
		System.out.println(server);
		return server;
	}

}
