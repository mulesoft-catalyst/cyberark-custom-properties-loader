package com.mulesoft.cyberark.custom.properties.loader.api;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.SimpleSessionManager;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.config.ClientHttpRequestFactoryFactory;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.ClientOptions;
import org.springframework.vault.support.SslConfiguration;
import org.springframework.vault.support.VaultResponseSupport;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class CyberarkConnectionProperties { 

	private static final Logger LOGGER = LoggerFactory.getLogger(CyberarkConnectionProperties.class);
	private String cyberarkHost;
	private String cyberarkPort;
	private String cyberarkPath;
	

	
	public String getCyberarkHost() {
		return cyberarkHost;
	}
	public void setCyberarkHost(String cyberarkHost) {
		this.cyberarkHost = cyberarkHost;
	}
	
	public String getCyberarkPath() {
		return cyberarkPath;
	}
	public void setCyberarkPath(String cyberarkPath) {
		this.cyberarkPath = cyberarkPath;
	}
	
	public String getCyberarkPort() {
		return cyberarkPort;
	}
	public void setCyberarkPort(String cyberarkPort) {
		this.cyberarkPort = cyberarkPort;
	}
	
	public CyberarkConnectionProperties(String cyberarkHost, String cyberarkPort,
			String cyberarkPath) {
		
		this.cyberarkHost = cyberarkHost;
		this.cyberarkPort = cyberarkPort;
		this.cyberarkPath = cyberarkPath;
		
	}

	
}
