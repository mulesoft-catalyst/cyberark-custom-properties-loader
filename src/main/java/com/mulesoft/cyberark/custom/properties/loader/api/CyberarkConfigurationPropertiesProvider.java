/*
 * (c) 2003-2021 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.cyberark.custom.properties.loader.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.regex.Pattern;

import org.mule.runtime.config.api.dsl.model.properties.ConfigurationPropertiesProvider;
import org.mule.runtime.config.api.dsl.model.properties.ConfigurationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Replace secrets from the Cyberark Key Service
 *
 * @since 1.0
 */
public class CyberarkConfigurationPropertiesProvider implements ConfigurationPropertiesProvider {

	private final static Logger LOGGER = LoggerFactory.getLogger(CyberarkConfigurationPropertiesProvider.class);
	private final static String CYBERARK_PROPERTIES_PREFIX = "cyberark::";
	private final static Pattern CYBERARK_SECRETS_PATTERN = Pattern
			.compile("\\$\\{" + CYBERARK_PROPERTIES_PREFIX + "[^}]*}");

	private final CyberarkConnectionProperties cyberarkConnProps;

	public CyberarkConfigurationPropertiesProvider(CyberarkConnectionProperties cyberarkConnProps) {
		this.cyberarkConnProps = cyberarkConnProps;
	}

	@Override
	public Optional<ConfigurationProperty> getConfigurationProperty(String configurationAttributeKey) {
		LOGGER.info("configurationAttributeKey=" + configurationAttributeKey);
		String propertyKey = configurationAttributeKey;

		if (propertyKey.startsWith(CYBERARK_PROPERTIES_PREFIX)) {
			final String propertyActualKey = propertyKey.substring(CYBERARK_PROPERTIES_PREFIX.length());
			LOGGER.info("propertyActualKey=" + propertyActualKey);
			try {
				final String value = getSecretFromVault(propertyActualKey);
				if (value != null) {
					return Optional.of(new ConfigurationProperty() {

						@Override
						public Object getSource() {
							return "Cyberark";
						}

						@Override
						public Object getRawValue() {
							return value;
						}

						@Override
						public String getKey() {
							return propertyActualKey;
						}
					});
				}
			} catch (Exception e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	@Override
	public String getDescription() {
		return "Cyberark Properties Loader";
	}

	private String getSecretFromVault(String key) {
		String secretValue = "";

		try {
			LOGGER.info("cyberarkConnProps.getCyberarkHost() = " + cyberarkConnProps.getCyberarkHost());
			LOGGER.info("cyberarkConnProps.getCyberarkPort() = " + cyberarkConnProps.getCyberarkPort());
			LOGGER.info("cyberarkConnProps.getCyberarkPath()= " + cyberarkConnProps.getCyberarkPath() );
			URL url = new URL(cyberarkConnProps.getCyberarkHost() + ":" + cyberarkConnProps.getCyberarkPort()
					+ cyberarkConnProps.getCyberarkPath()); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				secretValue = output.trim();
				secretValue = secretValue.substring(1,secretValue.length());
				secretValue = secretValue.substring(0, (secretValue.length() - 1));				
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			LOGGER.info("Problem during processing of getSecretValue(String key) " + e.getMessage());
		} catch (IOException e) {
			LOGGER.info("Problem during processing of getSecretValue(String key) " + e.getMessage());
		}
		return secretValue;
	}
}
